package cs236703.spring2015.hw4.test.daniel;

import cs236703.spring2015.hw4.solution.OOPAfter;
import cs236703.spring2015.hw4.solution.OOPBefore;
import cs236703.spring2015.hw4.solution.OOPSetup;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

@OOPTestClass(true)
public class ParentOOPTest {
	String name;
	String lastModifier;
	int age;
	
	@OOPSetup
	public void doParentSetup() {
		name = "Parent";
		age = 60;
	}
	
	@OOPBefore({"testInheritanceOrder", "testInheritanceOrder3"})
	public void doBeforeParent() {
		lastModifier = "Parent before";
	}
	
	@OOPAfter({"testInheritanceOrder", "testInheritanceOrder3"})
	public void doAfterParent() {
		lastModifier = "Parent after";
	}
	
	@OOPTest(order = 1)
	public void testInit() {
		OOPUnitCore.assertEquals("Parent", name);
		OOPUnitCore.assertEquals(60, age);
	}
	
	@OOPTest(order = 2)
	public void testInheritanceOrder() {
		// Before this test, only doBeforeParent should have been executed
		OOPUnitCore.assertEquals("Parent before", lastModifier);
	}
	
	@OOPTest(order = 3)
	public void testInheritanceOrder2() {
		// This is executed after the previous test, so we can test that doAfterParent was run
		OOPUnitCore.assertEquals("Parent after", lastModifier);
	}
	
}
