package cs236703.spring2015.hw4.provided;

public interface OOPResult {
	
	public enum OOPTestResult {
		SUCCESS, FAILURE, ERROR;
	};
	
	public OOPTestResult getResultType();
	public String getMessage();
	public boolean equals(Object obj);

}
