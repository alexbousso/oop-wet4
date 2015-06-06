package cs236703.spring2015.hw4.test;

import static org.junit.Assert.*;

import org.junit.Test;

import cs236703.spring2015.hw4.solution.OOPTestSummary;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

public class TestingTestAlex {

	@Test
	public void testSuperCow() {
		OOPTestSummary result = OOPUnitCore.runClass(SuperCow.class);
		assertEquals(3, result.getNumSuccesses());
		assertEquals(0, result.getNumFailures());
		assertEquals(1, result.getNumErrors());
	}
	
	@Test
	public void testGod() {
		OOPTestSummary result = OOPUnitCore.runClass(God.class);
		assertEquals(4, result.getNumSuccesses());
		assertEquals(0, result.getNumFailures());
		assertEquals(1, result.getNumErrors());
	}
}
