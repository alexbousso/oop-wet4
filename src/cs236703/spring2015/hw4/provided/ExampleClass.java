package cs236703.spring2015.hw4.provided;

import cs236703.spring2015.hw4.solution.OOPBefore;
import cs236703.spring2015.hw4.solution.OOPUnitCore;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;

public @OOPTestClass(true) class ExampleClass {
	
	private int field = 0;
	
	public @OOPBefore({"test1"}) void beforeFirstTest() 
	{
		this.field = 123;
	}
	
	public @OOPTest(order=1) void test1() throws OOPAssertionError
	{
		//this must run before the other test. must not throw an exception to succeed
		OOPUnitCore.assertEquals(123, this.field);
	}
	
	public @OOPTest(order=2) void test2() throws OOPAssertionError
	{
		OOPUnitCore.assertEquals(321, this.field);
	}

}
