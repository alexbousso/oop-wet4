package cs236703.spring2015.hw4.test;

import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

@OOPTestClass(true)
public class Son extends Dad{
	@OOPTest(order = 1)
	public void Son1() {
		MyList.add(3);
	}
	
	@OOPTest(order = 2)
	public void Son2() {
		MyList.add(4);
	}
	
	@OOPTest(order = 10)
	public void CheckListOrder() {
		System.out.println(MyList.toString());
		
		for (int i = 1; i <= 4; i++) {
			OOPUnitCore.assertEquals(MyList.get(i - 1).intValue(), i);
		}
	}
}
