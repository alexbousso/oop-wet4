package cs236703.spring2015.hw4.test.daniel;

import static org.junit.Assert.assertEquals;

import java.io.Console;
import java.util.Map;

import org.junit.Test;

import cs236703.spring2015.hw4.provided.OOPResult;
import cs236703.spring2015.hw4.solution.OOPTestSummary;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

public class CompleteTest {

	@Test
	public void test() {
		OOPTestSummary result = OOPUnitCore.runClass(GovernmentOOPTest.class);
		assertEquals(8, result.getNumSuccesses());
		assertEquals(5, result.getNumErrors());
		assertEquals(1, result.getNumFailures());
		
		OOPTestSummary resultParent = OOPUnitCore.runClass(ParentOOPTest.class);
		assertEquals(3, resultParent.getNumSuccesses());
		assertEquals(0, resultParent.getNumErrors());
		assertEquals(0, resultParent.getNumFailures());
		
		// Running this test suite will execute all previous tests from parent as well
		OOPTestSummary resultChild = OOPUnitCore.runClass(ChildOOPTest.class);
		assertEquals(5, resultChild.getNumSuccesses());
		assertEquals(0, resultChild.getNumErrors());
		assertEquals(0, resultChild.getNumFailures());
	}
}
