package cs236703.spring2015.hw4.provided;

import static org.junit.Assert.*;


import org.junit.Test;

import cs236703.spring2015.hw4.solution.OOPUnitCore;
import cs236703.spring2015.hw4.solution.OOPTestSummary;

public class ExampleTest {

	@Test
	public void testForExample() {
		
		OOPTestSummary result = OOPUnitCore.runClass(ExampleClass.class);
		assertEquals(1, result.getNumSuccesses());
		assertEquals(1, result.getNumFailures());
		assertEquals(0, result.getNumErrors());
	}

}
