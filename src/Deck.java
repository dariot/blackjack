
public class Deck {

	/*
	 * the array containing all the cards in the final deck (may be more than
	 * one standard deck)
	 */
	private String[] deck;
	/* current position of the iterator in the shuffled deck */
	private int pos = 0;

	/* array of the 52 cards in a standard deck */
	private String[] cards = { "A", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "J", "Q", "K", "A", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "J", "Q", "K", "A", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "J", "Q", "K", "A", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "J", "Q", "K" };

	/**
	 * Initializes a shuffled deck of <code>n</code> standard decks of 52 cards
	 * each.
	 * 
	 * @param n
	 *            the number of standard decks to use.
	 */
	public Deck(int n) {
		this.deck = new String[n * cards.length];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < cards.length; j++) {
				this.deck[(cards.length * i) + j] = cards[j];
			}
		}
		this.shuffle();
	}

	/**
	 * Fisher-Yates shuffle inspired by
	 * http://algs4.cs.princeton.edu/11model/Knuth.java.html
	 */
	private void shuffle() {
		int r = 0, n = this.getLength();
		for (int i = 0; i < n; i++) {
			r = i + (int) (Math.random() * (n - i));
			String swap = this.deck[r];
			this.deck[r] = deck[i];
			this.deck[i] = swap;
		}
	}

	/**
	 * Returns the length of the final deck.
	 * 
	 * @return the length of the final deck.
	 */
	public int getLength() {
		return this.deck.length;
	}

	/**
	 * Simulates the extraction of a card from the shuffled deck.
	 * 
	 * @return a string representing the selected card.
	 */
	public String getCard() {
		String card = "";
		if (this.getLength() > 0) {
			card = this.deck[pos];
			this.pos++;
			if (this.pos == this.getLength()) {
				this.pos = 0;
				this.shuffle();
			}
		}
		return card;
	}

}
