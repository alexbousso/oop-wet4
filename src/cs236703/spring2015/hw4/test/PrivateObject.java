package cs236703.spring2015.hw4.test;

import cs236703.spring2015.hw4.solution.OOPSetup;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

@OOPTestClass(true)
public class PrivateObject {
	private int number;
	private String word;
	private Object obj;
	private BadObject badObj;
	
	private PrivateObject() {}
	
	@OOPSetup
	public void doSetup() {
		number = 42;
		word = "SuperCow";
		obj = word;
		badObj = new BadObject();
	}
	
	@OOPTest(order = 1)
	public void test() {
		throw new IllegalArgumentException("Mooooo is private");
	}
	
	@OOPTest(order = 2)
	public void test2() {
		OOPUnitCore.assertEquals(42, number);
		OOPUnitCore.assertEquals("SuperCow", word);
	}
}
