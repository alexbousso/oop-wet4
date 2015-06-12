package cs236703.spring2015.hw4.test;

public class BadObject implements Cloneable {

	@Override
	public BadObject clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public BadObject() {}
	
	public BadObject(BadObject other) {
		throw new NullPointerException();
	}
}