package cs236703.spring2015.hw4.test.daniel;

import static org.junit.Assert.*;

import org.junit.Test;

import cs236703.spring2015.hw4.provided.ExampleClass;
import cs236703.spring2015.hw4.solution.OOPTestSummary;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

public class GovernmentJUnitTest {

	@Test
	public void test() {
		OOPTestSummary result = OOPUnitCore.runClass(GovernmentOOPTest.class);
		assertEquals(4, result.getNumSuccesses());
		assertEquals(2, result.getNumErrors());
		assertEquals(0, result.getNumFailures());
	}
}
