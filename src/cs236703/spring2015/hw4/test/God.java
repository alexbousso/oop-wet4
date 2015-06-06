package cs236703.spring2015.hw4.test;

import cs236703.spring2015.hw4.solution.OOPAfter;
import cs236703.spring2015.hw4.solution.OOPBefore;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

@OOPTestClass(value = true)
public class God extends SuperCow {
	
	@OOPBefore({"god1"})
	public void beforeGod1() {
		OOPUnitCore.assertEquals("before in SuperCow", this.god);
		this.god = "before in God";
	}
	
	@OOPTest(order = 5)
	public void god1() {
		OOPUnitCore.assertEquals("before in God", this.god);
		this.god = "test";
		this.moo1 = "god doesn't moo";
	}
	
	@OOPAfter({"god1"})
	public void afterGod1() {
		OOPUnitCore.assertEquals("test", this.god);
		this.god = "after in God";
	}
	
	@OOPTest(order = 6)
	public void god2() {
		OOPUnitCore.assertEquals("after in SuperCow", this.god);
	}
	
	@OOPTest(order = 2)
	public void god3() {
		OOPUnitCore.assertEquals("no moo", moo1);
	}
	
	@OOPTest(order = 4, test_throws = true, exc = Exception.class)
	public void god4() throws Exception {
		throw new Exception("I belive in Super Cow!");
	}
	
	@OOPTest(order = 100, test_throws = true, exc = IllegalArgumentException.class)
	public void god5() {
		
	}
	
	@Override
	public void cow1() {}
	
	@Override
	public void cow2() {}
	
	@Override
	public void cow3() {}
	
	@Override
	public void cow4() {}
}



















