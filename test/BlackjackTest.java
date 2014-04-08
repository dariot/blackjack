import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

@SuppressWarnings("static-access")
public class BlackjackTest {

	@Test
	public void testGetCountWithoutHole() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("A");
		cards.add("3");
		boolean hole = false;
		assertEquals(tester.cardsToString(cards, hole), "A 3");
	}

	@Test
	public void testGetCountWithHole() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("A");
		cards.add("3");
		boolean hole = true;
		assertEquals(tester.cardsToString(cards, hole), "A *");
	}

	@Test
	public void testGetCount1() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("3");
		cards.add("Q");
		assertEquals(tester.getCount(cards), 13);
	}

	@Test
	public void testGetCount2() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("A");
		cards.add("3");
		cards.add("Q");
		assertEquals(tester.getCount(cards), 14);
	}

	@Test
	public void testGetCount3() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("A");
		cards.add("4");
		assertEquals(tester.getCount(cards), 15);
	}

	@Test
	public void testCheckBlacjackTrue() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("A");
		cards.add("Q");
		assertTrue(tester.checkBlackjack(cards));
	}

	@Test
	public void testCheckBlacjackFalse() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("3");
		cards.add("Q");
		assertFalse(tester.checkBlackjack(cards));
	}

	@Test
	public void testEqualCardsTrue() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("Q");
		cards.add("K");
		assertTrue(tester.equalCards(cards));
	}

	@Test
	public void testEqualCardsFalse() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("3");
		cards.add("K");
		assertFalse(tester.equalCards(cards));
	}

	@Test
	public void testEqualCardsTooManyCards() {
		Blackjack tester = new Blackjack();
		ArrayList<String> cards = new ArrayList<String>();
		cards.add("K");
		cards.add("K");
		cards.add("K");
		assertFalse(tester.equalCards(cards));
	}
}