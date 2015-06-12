package cs236703.spring2015.hw4.test.daniel;

import cs236703.spring2015.hw4.solution.OOPAfter;
import cs236703.spring2015.hw4.solution.OOPBefore;
import cs236703.spring2015.hw4.solution.OOPSetup;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

@OOPTestClass(true)
public class ChildOOPTest extends ParentOOPTest {
	
	@OOPSetup
	public void doParentSetup() {
		name = "Child";
		age = 12;
	}
	
	@OOPBefore({"testInheritanceOrder3"})
	public void doBeforeChild() {
		lastModifier = "Child before";
	}
	
	@OOPAfter({"testInheritanceOrder3"})
	public void doAfterChild() {
		lastModifier = "Child after";
	}
	
	@OOPTest(order = 1)
	public void testInit() {
		System.out.print("testInit");
		OOPUnitCore.assertEquals("Child", name);
		OOPUnitCore.assertEquals(12, age);
	}
	
	@OOPTest(order = 4)
	public void testInheritanceOrder3() {
		// Before this test, doBeforeParent and doBeforeChild should have been executed
		OOPUnitCore.assertEquals("Child before", lastModifier);
	}
	
	@OOPTest(order = 5)
	public void testInheritanceOrder4() {
		// This is executed after the previous test, so we can test this was the order of execution:
		// doBeforeParent -> doBeforeChild -> test -> doAfterChild -> doAfterParent
		OOPUnitCore.assertEquals("Parent after", lastModifier);
	}
	
}
