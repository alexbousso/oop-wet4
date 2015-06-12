package cs236703.spring2015.hw4.test;

import java.util.ArrayList;
import java.util.List;

import cs236703.spring2015.hw4.solution.OOPSetup;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;

@OOPTestClass(true)
public class Dad {
	public List<Integer> MyList;
	
	@OOPSetup
	public void setupDad() {
		MyList = new ArrayList<>();
	}
	
	@OOPTest(order = 1)
	public void Dad1() {
		MyList.add(new Integer(1));
	}
	
	@OOPTest(order = 2)
	public void Dad2() {
		MyList.add(new Integer(2));
	}
}
