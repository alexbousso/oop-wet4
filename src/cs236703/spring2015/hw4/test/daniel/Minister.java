package cs236703.spring2015.hw4.test.daniel;

import java.util.Date;

public class Minister extends MemberOfKnesset {
	String nameOfAssistant;
	String ministryName;
	
	public Minister() {
		nameOfAssistant = "";
		ministryName = "";
	}
	
	public Minister(String name, Date birthday, int numVotes, String nameOfAssistant, String ministryName) {
		super(name, birthday, numVotes);
		this.nameOfAssistant = nameOfAssistant;
		this.ministryName = ministryName;
	}
	
	public Minister(String name) {
		super(name, new Date(), 0);
		nameOfAssistant = "";
		ministryName = "";
	}
	
	public String getNameOfAssistant() {
		return nameOfAssistant;
	}

	@Override
	public void setNameOfAssistant(String nameOfAssistant) {
		this.nameOfAssistant = nameOfAssistant;
	}

	public String getMinistryName() {
		return ministryName;
	}

	public void setMinistryName(String ministryName) {
		this.ministryName = ministryName;
	}
	
	@Override
	public boolean isMinister() {
		return true;
	}
}
