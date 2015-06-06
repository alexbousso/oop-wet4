package cs236703.spring2015.hw4.provided;

public class OOPAssertionError extends AssertionError {
	
	private static final long serialVersionUID = 1L;
	private Object expected;
	private Object actual;
	
	public OOPAssertionError(Object expected, Object actual) {
		this.expected = expected;
		this.actual = actual;
	}
	
	@Override
	public String getMessage() {
		return "expected: <" + expected + "> but was: <" + actual + ">";
	}

}
