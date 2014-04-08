import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class DeckTest {

	@Test
	public void testDeckWith0() {
		Deck d = new Deck(0);
		assertEquals(d.getLength(), 0);
	}
	
	@Test
	public void testDeckWith1() {
		Deck d = new Deck(1);
		assertEquals(d.getLength(), 52);
	}
	
	@Test
	public void testDeckWith6() {
		Deck d = new Deck(6);
		assertEquals(d.getLength(), 52 * 6);
	}
	
	@Test
	public void testGetCardWithEmptyDeck() {
		Deck d = new Deck(0);
		assertEquals(d.getCard(), "");
	}
	
	@Test
	public void testGetCardUntilDeckEnd() {
		Deck d = new Deck(1);
		for (int i = 0; i < d.getLength(); i++) {
			d.getCard();
		}
		assertFalse("".equals(d.getCard()));
	}

}
