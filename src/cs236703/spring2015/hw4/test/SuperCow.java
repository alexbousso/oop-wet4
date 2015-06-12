package cs236703.spring2015.hw4.test;

import cs236703.spring2015.hw4.solution.OOPAfter;
import cs236703.spring2015.hw4.solution.OOPBefore;
import cs236703.spring2015.hw4.solution.OOPSetup;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

@OOPTestClass(true)
public class SuperCow {
	protected String moo1 = "no moo";
	protected String moo2 = "no moo";
	protected String moo3 = "initial value";
	protected String god = "initial value";
	
	@OOPSetup
	public void setupCow() {
		this.moo3 = "setup";
		this.god = "setup";
//		System.out.print("Setup->");
	}
	
	@OOPBefore({"god1"})
	public void beforeCowGod1() {
		OOPUnitCore.assertEquals("initial value", this.god);
		this.god = "before in SuperCow";
	}
	
	@OOPAfter({"god1"})
	public void afterCowGod1() {
		OOPUnitCore.assertEquals("after in God", this.god);
		this.god = "after in SuperCow";
	}
	
	@OOPBefore({"cow1"})
	public void beforeCow1() {
//		System.out.print("beforeCow1->");
		this.moo1 = "moo1";
	}
	
	@OOPBefore({"cow2"})
	public void beforeCow2() {
		this.moo2 = "moo2";
	}
	
	@OOPBefore({"cow3"})
	public void beforeCow3() throws Exception {
		this.moo3 = "before";
		throw new Exception("Moooooooooo");
	}
	
	@OOPTest(order = 10)
	public void cow1() {
//		System.out.print("cow1->");
		OOPUnitCore.assertEquals("moo1", this.moo1);
		OOPUnitCore.assertEquals("no moo", this.moo2);
		this.moo1 = "moooo";
	}
	
	@OOPTest(order = 20)
	public void cow2() {
//		System.out.print("cow2->");
		OOPUnitCore.assertEquals("meow", this.moo1);
		OOPUnitCore.assertEquals("moo2", this.moo2);
		this.moo2 = "moooooooooo";
	}
	
	@OOPTest(order = 30)
	public void cow3() {
//		System.out.print("cow3->");
		this.moo3 = "in test";
	}
	
	@OOPTest(order = 31)
	public void cow4() {
//		System.out.print("cow4->\n");
		// Checking if backup after 'BeforeTest' fails works
		OOPUnitCore.assertEquals("setup", this.moo3);
	}
	
	@OOPAfter({"cow1"})
	public void afterCow1() {
//		System.out.print("afterCow1->");
		OOPUnitCore.assertEquals("moooo", this.moo1);
		this.moo1 = "meow";
	}
	
	@OOPAfter({"cow2"})
	public void afterCow2() {
		OOPUnitCore.assertEquals("moooooooooo", this.moo2);
		this.moo2 = "I just mooed!";
	}
}
