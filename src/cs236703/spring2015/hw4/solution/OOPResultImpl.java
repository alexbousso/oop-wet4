package cs236703.spring2015.hw4.solution;

import cs236703.spring2015.hw4.provided.OOPResult;

public class OOPResultImpl implements OOPResult {

	private OOPTestResult result;
	private String message;
	
	public OOPResultImpl(String message, OOPTestResult result) {
		this.message = message;
		this.result = result;
	}
	
	@Override
	public OOPTestResult getResultType() {
		return result;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OOPResultImpl)) {
			return false;
		}
		return ((OOPResultImpl)obj).getResultType() == (this.getResultType()) &&
				((OOPResultImpl)obj).getMessage().equals(this.getMessage());
	}
}
