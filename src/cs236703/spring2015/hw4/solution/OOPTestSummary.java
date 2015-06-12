package cs236703.spring2015.hw4.solution;

import java.util.Map;

import org.omg.CORBA.Environment;

import cs236703.spring2015.hw4.provided.OOPResult;
import cs236703.spring2015.hw4.provided.OOPResult.OOPTestResult;

public class OOPTestSummary {
	
	private Map<String, OOPResult> testMap;
	
	public OOPTestSummary(Map<String, OOPResult> testMap) {
		this.testMap = testMap;
	}
	
	private int getNumOfType(OOPTestResult type) {
		int count = 0;
		
		for (OOPResult res : testMap.values()) {
			if (res.getResultType() == type) {
				count++;
			}
		}
		
		return count;
	}
	
	public int getNumSuccesses() {
		return getNumOfType(OOPTestResult.SUCCESS);
	}
	
	public int getNumFailures() {
		return getNumOfType(OOPTestResult.FAILURE);
	}
	
	public int getNumErrors() {
		return getNumOfType(OOPTestResult.ERROR);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String key : testMap.keySet()) {
			sb.append(key)
				.append(" - ")
				.append(testMap.get(key).getResultType().toString())
				.append(", ")
				.append(testMap.get(key).getMessage())
				.append(System.lineSeparator());
		}
		
		return sb.toString();
	}
}
