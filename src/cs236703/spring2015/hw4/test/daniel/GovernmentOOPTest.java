package cs236703.spring2015.hw4.test.daniel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import cs236703.spring2015.hw4.solution.OOPAfter;
import cs236703.spring2015.hw4.solution.OOPBefore;
import cs236703.spring2015.hw4.solution.OOPSetup;
import cs236703.spring2015.hw4.solution.OOPTest;
import cs236703.spring2015.hw4.solution.OOPTestClass;
import cs236703.spring2015.hw4.solution.OOPUnitCore;

@OOPTestClass(true)
public class GovernmentOOPTest {
	HashSet<MemberOfKnesset> allMembers;
	SimpleDateFormat df;
	String govName;

	/** Test setup **/
	@OOPSetup
	public void doSetup() {
		allMembers = new HashSet<MemberOfKnesset>();
		df = new SimpleDateFormat("dd/MM/yyyy");
		govName = "20th Government";
	}

	/** Befores and afters **/
	@OOPBefore({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM", "testAddBills",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting", "testAssertGood",
			"testBadAssert" })
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
			"testaddAssistantsWithWrongExpecting", "testAssertGood",
			"testBadAssert" })
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
			"testaddAssistantsWithWrongExpecting", "testAssertGood",
			"testBadAssert" })
	public void addPrimeMinister() {
		try {
			allMembers.add(new PrimeMinister("Benjamin Netanyahu", df
					.parse("21/10/1949"), 156923, "Michael",
					"Prime Minister's Office", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OOPBefore({ "testBadBefore", "testBadBeforeAndAfter" })
	public void badBefore() throws Exception {
		govName = "Bla Bla Bla";

		throw new Exception("badBefore just destroyed the object!");
	}
	
	@OOPBefore({"testBeforeAfterInheritance"})
	public void inheritanceBefore() {
		govName = "Set by: Government before";
	}
	
	@OOPAfter("testBeforeAfterInheritance")
	public void inheritanceAfter() {
		govName = "Set by: Government after";
	}

	@OOPAfter({ "testBadAfter", "testBadBeforeAndAfter" })
	public void badAfter() throws Exception {
		df = null;

		throw new Exception("badAfter just destroyed the object!");
	}

	@OOPAfter({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting", "testAssertGood",
			"testBadAssert" })
	public void removePrimeMinister() {
		// An iterator must be used here explicitly to avoid
		// ConcurrentModificationException
		Iterator<MemberOfKnesset> iter = allMembers.iterator();

		while (iter.hasNext()) {
			MemberOfKnesset m = iter.next();

			if (m.isPrimeMinister()) {
				iter.remove();
			}
		}
	}

	@OOPAfter({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting", "testAssertGood",
			"testBadAssert" })
	public void removeMinisters() {
		// An iterator must be used here explicitly to avoid
		// ConcurrentModificationException
		Iterator<MemberOfKnesset> iter = allMembers.iterator();

		while (iter.hasNext()) {
			MemberOfKnesset m = iter.next();

			if (m.isMinister() && !m.isPrimeMinister()) {
				iter.remove();
			}
		}
	}

	@OOPAfter({ "testCountingAllNonMinisters",
			"testCountingAllMembersExceptPM", "testAddBills",
			"testAddAssistantsWithoutExpecting",
			"testaddAssistantsWithExpecting",
			"testaddAssistantsWithWrongExpecting", "testAssertGood",
			"testBadAssert" })
	public void removeMembersOfKnesset() {
		// An iterator must be used here explicitly to avoid
		// ConcurrentModificationException
		Iterator<MemberOfKnesset> iter = allMembers.iterator();

		while (iter.hasNext()) {
			MemberOfKnesset m = iter.next();

			if (!m.isMinister()) {
				iter.remove();
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

	@OOPTest(order = 7)
	public void testBadBefore() {
		// Do nothing
	}

	@OOPTest(order = 8)
	public void testBadBeforeWasFixed() {
		OOPUnitCore.assertEquals(govName, "20th Government");
		OOPUnitCore.assertEquals("dd/MM/yyyy", df.toPattern());
	}

	@OOPTest(order = 9)
	public void testBadAfter() {
		// Do nothing
	}

	@OOPTest(order = 10)
	public void testBadAfterWasFixed() {
		OOPUnitCore.assertEquals(govName, "20th Government");
		OOPUnitCore.assertEquals("dd/MM/yyyy", df.toPattern());
	}

	@OOPTest(order = 11)
	public void testBadBeforeAndAfter() {
		// Do nothing
	}

	@OOPTest(order = 12)
	public void testBadBeforeAndAfterWasFixed() {
		// Do nothing() {
		OOPUnitCore.assertEquals(govName, "20th Government");
		OOPUnitCore.assertEquals("dd/MM/yyyy", df.toPattern());
	}

	@OOPTest(order = 13)
	public void testAssertGood() {
		int countMembersOfKnesset = 0, countMinisters = 0, countPMs = 0;

		for (MemberOfKnesset k : allMembers) {
			countMembersOfKnesset++;
			if (k.isMinister())
				countMinisters++;
			if (k.isPrimeMinister())
				countPMs++;
		}

		OOPUnitCore.assertEquals(9, countMembersOfKnesset);
		OOPUnitCore.assertEquals(4, countMinisters);
		OOPUnitCore.assertEquals(1, countPMs);
	}

	@OOPTest(order = 14)
	public void testBadAssert() {
		int countMembersOfKnesset = 0, countMinisters = 0, countPMs = 0;

		for (MemberOfKnesset k : allMembers) {
			countMembersOfKnesset++;
			if (k.isMinister())
				countMinisters++;
			if (k.isPrimeMinister())
				countPMs++;
		}

		OOPUnitCore.assertEquals(9, countMembersOfKnesset);
		OOPUnitCore.assertEquals(4, countMinisters);
		OOPUnitCore.assertEquals(2, countPMs); // Should fall here
	}
}
