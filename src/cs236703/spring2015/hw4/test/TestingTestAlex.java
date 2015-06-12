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
		
		assertEquals(7, result.getNumSuccesses());
		assertEquals(1, result.getNumFailures());
		assertEquals(1, result.getNumErrors());
	}
	
	@Test
	public void testOrder() {
		OOPTestSummary result = OOPUnitCore.runClass(Son.class);
		assertEquals(5, result.getNumSuccesses());
		assertEquals(0, result.getNumFailures());
		assertEquals(0, result.getNumErrors());
	}
	
	@Test
	public void testPrivateModifiers() {
		OOPTestSummary result = OOPUnitCore.runClass(PrivateObject.class);
		assertEquals(1, result.getNumSuccesses());
		assertEquals(0, result.getNumFailures());
		assertEquals(1, result.getNumErrors());
	}
}
