package cs236703.spring2015.hw4.test.daniel;

import java.util.Date;

import javax.activity.InvalidActivityException;

public class PrimeMinister extends Minister {
	private boolean isBibi;
	
	public PrimeMinister() {
		this.isBibi = true;
	}
	
	public PrimeMinister(String name, Date birthday, int numVotes, 
			String nameOfAssistant, String ministryName, boolean isBibi) {
		super(name, birthday, numVotes, nameOfAssistant, ministryName);
		this.isBibi = isBibi;
	}
	
	public PrimeMinister(String name) {
		super(name, new Date(), 0, "", "");
		this.isBibi = true;
	}
	
	@Override
	public boolean isPrimeMinister() {
		return true;
	}
	
	public void makePeace() {
		if(isBibi) {
			throw new IllegalArgumentException("There's no partner for peace");
		}
	}
	
	public void maintainTiesWithObama() throws InvalidActivityException {
		if(isBibi) {
			throw new InvalidActivityException("Obama is stupid and an antisemite");
		}
	}
	
	public void getElectedFairly() {
		if(isBibi) {
			throw new IllegalArgumentException("The Arabs are flocking to the voting polls!");
		}
	}
}
