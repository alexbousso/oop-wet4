package cs236703.spring2015.hw4.test.daniel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemberOfKnesset {
	private String name;
	private Date birthday;
	private int numOfVotes;
	private List<String> billsBroughtToVote;
	
	public MemberOfKnesset() {
		this.name = "";
		this.birthday = new Date();
		this.numOfVotes = 0;
		this.billsBroughtToVote = new ArrayList<>();
	}
	
	public MemberOfKnesset(String name, Date birthday, int numVotes) {
		this.name = name;
		this.birthday = birthday;
		this.numOfVotes = numVotes;
		this.billsBroughtToVote = new ArrayList<>();
	}
	
	public MemberOfKnesset(String name) {
		this.name = name;
		this.birthday = new Date();
		this.numOfVotes = 0;
		this.billsBroughtToVote = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getNumOfVotes() {
		return numOfVotes;
	}

	public void setNumOfVotes(int numOfVotes) {
		this.numOfVotes = numOfVotes;
	}
	
	public boolean isMinister() {
		return false;
	}
	
	public boolean isPrimeMinister() {
		return false;
	}
	
	public void addBill(String billName) {
		billsBroughtToVote.add(billName);
	}
	
	public int getBillCount() {
		return billsBroughtToVote.size();
	}
	
	public void clearBillList() {
		billsBroughtToVote.clear();
	}
	
	public void setNameOfAssistant(String nameOfAssistant) {
		throw new IllegalStateException("You're not a minister, no assistant for you");
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof MemberOfKnesset)) {
			return false;
		}
		
		return this.getName().equals(((MemberOfKnesset)o).getName());
	}
}
