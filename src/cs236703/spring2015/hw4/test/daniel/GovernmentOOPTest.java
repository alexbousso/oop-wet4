package cs236703.spring2015.hw4.test.daniel;

import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.Set;

import cs236703.spring2015.hw4.solution.OOPAfter;
import cs236703.spring2015.hw4.solution.OOPBefore;
import cs236703.spring2015.hw4.solution.OOPSetup;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

@OOPTestClass(true)
public class GovernmentOOPTest {
	Set<MemberOfKnesset> allMembers;
	SimpleDateFormat df;

	/** Test setup **/
	@OOPSetup
	public void doSetup() {
		allMembers = new LinkedHashSet<MemberOfKnesset>();
		df = new SimpleDateFormat("dd/MM/yyyy");
	}

	/** Befores and afters **/
	@OOPBefore({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM", "testAddBills",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting" })
	public void addMembersOfKnesset() {
		try {
			allMembers.add(new MemberOfKnesset("Oren Hazan", df
					.parse("28/10/1981"), 1504));
			allMembers.add(new MemberOfKnesset("Yuli Edelstein", df
					.parse("05/08/1958"), 6905));
			allMembers.add(new MemberOfKnesset("Tzipi Hotovely", df
					.parse("02/12/1978"), 9866));
			allMembers.add(new MemberOfKnesset("Haim Yalin", df
					.parse("12/08/1958"), 11750));
			allMembers.add(new MemberOfKnesset("Eiman Udeh", df
					.parse("01/01/1975"), 1504));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OOPBefore({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting" })
	public void addMinisters() {
		try {
			allMembers.add(new Minister("Miri Regev", df.parse("26/05/1965"),
					20565, "Zmora", "Culture and Sport Ministry"));
			allMembers.add(new Minister("Gilad Erdan", df.parse("30/09/1970"),
					21695, "Loov", "Public Security Ministry"));
			allMembers.add(new Minister("Moshe Kahlon", df.parse("19/11/1960"),
					38695, "Bar", "Finance Ministry"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OOPBefore({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting" })
	public void addPrimeMinister() {
		try {
			allMembers.add(new PrimeMinister("Benjamin Netanyahu", df
					.parse("21/10/1949"), 156923, "Michael",
					"Prime Minister's Office", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OOPAfter({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting" })
	public void removePrimeMinister() {
		for (MemberOfKnesset k : allMembers) {
			if (k.isPrimeMinister()) {
				allMembers.remove(k);
			}
		}
	}

	@OOPAfter({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting" })
	public void removeMinisters() {
		for (MemberOfKnesset k : allMembers) {
			if (k.isMinister() && !(k.isPrimeMinister())) {
				allMembers.remove(k);
			}
		}
	}

	@OOPAfter({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM", "testAddBills",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting" })
	public void removeMembersOfKnesset() {
		for (MemberOfKnesset k : allMembers) {
			if (!k.isMinister()) {
				allMembers.remove(k);
			}
		}
	}

	/** Actual testing methods **/

	@OOPTest(order = 1)
	public void testCountingAllNonMinisters() {
		int count = 0;
		for (MemberOfKnesset k : allMembers) {
			if (!k.isMinister()) {
				count++;
			}
		}

		OOPUnitCore.assertEquals(5, count);
	}

	@OOPTest(order = 2)
	public void testCountingAllMembersExceptPM() {
		int count = 0;
		for (MemberOfKnesset k : allMembers) {
			if (!k.isPrimeMinister()) {
				count++;
			}
		}

		OOPUnitCore.assertEquals(8, count);
	}

	@OOPTest(order = 3)
	public void testAddBills() {
		for (MemberOfKnesset k : allMembers) {
			k.addBill("Bill about something");
		}

		for (MemberOfKnesset k : allMembers) {
			OOPUnitCore.assertEquals(1, k.getBillCount());
		}
	}

	@OOPTest(order = 4)
	public void testAddAssistantsWithoutExpecting() {
		for (MemberOfKnesset k : allMembers) {
			k.setNameOfAssistant("Shelly");
		}

		// Now an exception should be thrown and NOT be expected
	}

	@OOPTest(order = 5, test_throws = true, exc = IllegalStateException.class)
	public void testaddAssistantsWithExpecting() {
		for (MemberOfKnesset k : allMembers) {
			k.setNameOfAssistant("Shelly");
		}

		// Now an exception should be thrown and be expected
	}

	@OOPTest(order = 6, test_throws = true, exc = NullPointerException.class)
	public void testaddAssistantsWithWrongExpecting() {
		for (MemberOfKnesset k : allMembers) {
			k.setNameOfAssistant("Shelly");
		}

		// Now an exception should be thrown and NOT be expected
	}

}
