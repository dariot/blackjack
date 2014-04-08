import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {

    static final int STARTING_CHIPS = 100;
    static final int STANDARD_NUMBER_DECKS = 6;
    static final Scanner in = new Scanner(System.in);

    static int chipCount = STARTING_CHIPS;
    static int bet = 0;
    static String input = "";
    static boolean dealerTurn = false;
    static boolean doubleDown = false;
    /* splitMode is true if the player has split the cards, false otherwise */
    static boolean splitMode = false;
    /* if the player splits aces the blackjack payout goes back to 1:1 */
    static boolean splittingAces = false;
    /* the standard casino blackjack deck */
    static Deck dealerDeck = new Deck(STANDARD_NUMBER_DECKS);
    static ArrayList<String> playerCards = new ArrayList<String>();
    static ArrayList<String> dealerCards = new ArrayList<String>();
    /* counter for the current playing deck (in case of splitting) */
    static int currentDeck = 0;
    /* list for all the decks of the player (2 in case of splitting) */
    static ArrayList<ArrayList<String>> playerDecks = new ArrayList<ArrayList<String>>();
    /* list for all the bets of the player (2 in case of splitting) */
    static ArrayList<Integer> bets = new ArrayList<Integer>();

    /**
     * Simulates the extraction of a card from the dealer's deck.
     * 
     * @return a string representing the random card.
     */
    public static String dealCard() {
    	return dealerDeck.getCard();
    }

    /**
     * Waits for the user input and accepts it only if it's a valid
     * number.
     * 
     * @return the integer representing the number of chips bet
     * by the player.
     */
    public static int validateBet() {
        int chips = 0;
        boolean error = true;
        /* keep waiting for user input until it is a valid number */
        while (error) {
            error = false;
            input = in.next();
            if (!input.matches("[0-9]+")) {
            	/* input is not a number */
                System.out.println("Please enter a valid number");
                error = true;
            } else {
                chips = Integer.parseInt(input);
                if (chips < 1) {
                	/* input is less than the minimum bet */
                    System.out.println("You have to bet at least one chip");
                    error = true;
                } else if (chips > chipCount) {
                	/* input is more than the player's budget */
                    System.out.println("You cannot bet more chips that what you currently have");
                    error = true;
                }
            }
        }
        return chips;
    }

    /**
     * Converts to string the given cards.
     * 
     * @param cards the <code>ArrayList</code> of cards.
     * @param hole <code>true</code> if we need to cover the second card (the
     * dealer has yet to begin its turn).
     * @return the string representing the cards passed as input.
     */
    public static String cardsToString(ArrayList<String> cards, boolean hole) {
        String output = "";
        for (int i = 0; i < cards.size(); i++) {
            if (hole && i == cards.size() - 1) {
                output += "* ";
            } else {
                output += cards.get(i) + " ";
            }
        }
        return output.trim();
    }

    /**
     * Prints the current state of the game: the current bet, the player's
     * cards, the dealer's cards and the player's current count.
     */
    public static void printCurrentState() {
        System.out.println("\n******");
        System.out.println("Your bet: " + bets.get(currentDeck));
        System.out.println("Your cards: " + cardsToString(playerDecks.get(currentDeck), false));
        System.out.println("Dealer cards: " + cardsToString(dealerCards, !dealerTurn));
        System.out.println("\nYour count: " + getPlayerCount());
        System.out.println("******");
    }

    /**
     * Converts the cards passed as input to the corresponding count.
     * An ace counts as the highest possible value (1 or 11) that doesn't
     * make the total count go over 21.
     * 
     * @param cards the <code>ArrayList</code> of input cards.
     * @return the <code>int</code> representing the count of the given cards.
     */
    public static int getCount(ArrayList<String> cards) {
        int count = 0;
        /* keep count of how many aces are there in the given set of cards */
        int aceCount = 0;
        for (String c : cards) {
            if (c.matches("[0-9]+")) {
                count += Integer.parseInt(c);
            } else if (c.matches("[JQK]")) {
                count += 10;
            } else if (c.matches("A")) {
            	aceCount++;
            }
        }
        /* for every ace count it as 1 or 11 as it's most convenient */
        for (int i = 0; i < aceCount; i++) {
        	if (count + 11 > 21) {
                count += 1;
            } else {
                count += 11;
            }
        }
        return count;
    }

    /**
     * Computes the player's current count.
     * 
     * @return the <code>int</code> representing the player's count.
     */
    public static int getPlayerCount() {
        return getCount(playerDecks.get(currentDeck));
    }

    /**
     * Computes the dealer's current count.
     * 
     * @return the <code>int</code> representing the dealer's count.
     */
    public static int getDealerCount() {
        return getCount(dealerCards);
    }

    /**
     * Check if the given cards form a blackjack.
     * 
     * @param cards the <code>ArrayList</code> of cards.
     * @return <code>true</code> if the count of the given cards is
     * 21, <code>false</code> otherwise.
     */
    public static boolean checkBlackjack(ArrayList<String> cards) {
        return getCount(cards) == 21;
    }

    /**
     * Check if the player's current cards form a blackjack.
     * 
     * @return <code>true</code> if the count of the player's cards is
     * 21, <code>false</code> otherwise.
     */
    public static boolean checkPlayerBlackjack() {
        return checkBlackjack(playerDecks.get(currentDeck));
    }

    /**
     * Check if the dealer's current cards form a blackjack.
     * 
     * @return <code>true</code> if the count of the dealer's cards is
     * 21, <code>false</code> otherwise.
     */
    public static boolean checkDealerBlackjack() {
        return checkBlackjack(dealerCards);
    }

    /**
     * Represents the dealer's turn: it follows the standard casino rules
     * of hitting until the count gets to 17 or more.
     */
    public static void dealerTurn() {
        int dealerCount;
        while (true) {
            dealerCount = getCount(dealerCards);
            if (dealerCount < 17) {
            	/* hit */
                System.out.println("\nThe dealer decided to hit");
                dealerCards.add(dealCard());
                printCurrentState();
                if (getDealerCount() > 21) {
                    return;
                }
            } else {
            	/* stand */
                System.out.println("\nThe dealer decided to stand");
                System.out.println("\nDealer count: " + getDealerCount());
                break;
            }
        }
    }

    /**
     * Deals a card to the player and checks for blackjack or bust.
     */
    public static void receiveCard() {
        playerDecks.get(currentDeck).add(dealCard());
        printCurrentState();
        if (checkPlayerBlackjack()) {
            return;
        }
        if (getPlayerCount() > 21) {
            return;
        }
    }

    /**
     * Compares the initial two cards and returns true if they have the same value.
     * 
     * @param cards the <code>ArrayList</code> containing the initial two cards.
     * @return <code>true</code> if the two cards have the same value, <code>false</code>
     * otherwise.
     */
    public static boolean equalCards(ArrayList<String> cards) {
        String one = cards.get(0);
        String two = cards.get(1);
        boolean result = cards.size() == 2 && ( (one.equals("A") && two.equals("A")) ||
            (one.matches("[JQK]") && two.matches("[JQK]")) ||
            one.equals(two) );
        return result;
    }

    /**
     * Lets the user play his/her hand.
     * Keeps waiting for the user input until the player stands or
     * he/she goes bust.
     */
    public static void playHand() {
        do {
            if (splitMode) {
                System.out.println("\nDECK " + (currentDeck + 1));
                printCurrentState();
            }
            /* wait for user input */
            do {
                System.out.println("\nEnter 'H' to hit, 'S' to stand, 'D' to double down");
                if (!splitMode && equalCards(playerCards)) {
                    System.out.println("Enter 'L' to split");
                }
                input = in.next();
            } while (!input.equalsIgnoreCase("h") && !input.equalsIgnoreCase("s") && !input.equalsIgnoreCase("d") && !input.equalsIgnoreCase("l"));
            if (input.equalsIgnoreCase("h")) {
            	/* hit */
                System.out.println("You decided to hit");
                receiveCard();
            } else if (input.equalsIgnoreCase("s")) {
            	/* stand */
                System.out.println("You decided to stand");
            } else if (input.equalsIgnoreCase("d")) {
                System.out.println("You decided to double down");
                doubleDown = true;
                bets.set(currentDeck, bets.get(currentDeck) * 2);
                receiveCard();
                break;
            } else if (!splitMode && equalCards(playerCards) && input.equalsIgnoreCase("l")) {
            	/* split */
                System.out.println("You decided to split");
                splitMode = true;
                ArrayList<String> firstDeck = new ArrayList<String>();
                if (playerCards.get(0) == "A") {
                    splittingAces = true;
                }
                firstDeck.add(playerCards.get(0));
                firstDeck.add(dealCard());
                ArrayList<String> secondDeck = new ArrayList<String>();
                secondDeck.add(playerCards.get(1));
                secondDeck.add(dealCard());
                playerDecks.set(0, firstDeck);
                playerDecks.add(secondDeck);
                bets.add(bet);
            }
        } while (!input.equalsIgnoreCase("s") && getPlayerCount() <= 21);
    }

    /**
     * Represents the ending of a hand: checking for results
     * and determining the winner.
     */
    public static void endHand() {
        int playerCount = getCount(playerDecks.get(currentDeck));
        int dealerCount = getCount(dealerCards);
        /* check for players who went bust */
        if (playerCount > 21) {
            System.out.println("\nYou went bust. The dealer wins.");
            chipCount -= bets.get(currentDeck);
            return;
        }
        if (dealerCount > 21) {
            System.out.println("\nThe dealer went bust. You win.");
            chipCount += bets.get(currentDeck);
            return;
        }
        /*
         * if no one has gone bust, compare the various counts in order
         * to determine the winner
         */
        if (playerCount > dealerCount) {
            System.out.println("\nYou win.");
            if (!splittingAces && checkPlayerBlackjack()) {
                System.out.println("Blackjack!");
                chipCount += bets.get(currentDeck) * 1.5;
            } else {
                chipCount += bets.get(currentDeck);
            }
        } else if (playerCount < dealerCount) {
            System.out.println("\nThe dealer wins.");
            chipCount -= bets.get(currentDeck);
        } else {
            System.out.println("\nIt's a tie.");
        }
    }

    /**
     * Represents the beginning of a hand: gets the player's bet, deals
     * the first two cards and starts the player's and dealer's hands in turn.
     */
    public static void beginHand() {
        System.out.println("\nYour chip count: " + chipCount);
        System.out.println("Please enter the number of chips you want to bet");
        bet = validateBet();
        bets.add(bet);
        /* first two cards for the dealer and the player */
        playerCards.add(dealCard());
        dealerCards.add(dealCard());
        playerCards.add(dealCard());
        playerDecks.add(playerCards);
        dealerCards.add(dealCard());
        printCurrentState();
        /* start playing a standard hand */
        for (int i = 0; i < playerDecks.size(); i++) {
            currentDeck = i;
            /* check for a player blackjack */
            if (!checkPlayerBlackjack()) {
            	playHand();
            }
        }
        /* check if the player has went bust */
        boolean playerNotBusted = false;
        for (int i = 0; i < playerDecks.size(); i++) {
            currentDeck = i;
            if (getPlayerCount() <= 21) {
                playerNotBusted = true;
                break;
            }
        }
        /* if the player does not go bust let the dealer play his own hand */
        if (playerNotBusted) {
            dealerTurn = true;
            printCurrentState();
            dealerTurn();
        }
    }

    /**
     * Clears the lists used for keeping the state during the game.
     */
    private static void clearLists() {
        bets.clear();
        playerDecks.clear();
        playerCards.clear();
        dealerCards.clear();
    }
    
    /**
     * Completely resets the state for a fresh start in a new hand.
     */
    private static void resetState() {
    	currentDeck = 0;
    	dealerTurn = false;
        doubleDown = false;
        splitMode = false;
    	clearLists();
    }

    /**
     * Prints a welcome message, some rules for the game and starts a new
     * hand or quits the game depending on user's input.
     * 
     * @param args an optional array of arguments.
     */
    public static void main(String[] args) {
        System.out.println("\n*** WELCOME TO BLACKJACK ***");
        System.out.println("The standard payout is 1:1, the blackjack payout is 3:2");
        System.out.println("Doubling down will increase your initial bet by 100%");
        /* wait for user input and begin new hand or quit the game accordingly */
        do {
            System.out.println("\nEnter 'P' to play a hand or 'Q' to leave the table and quit the game");
            input = in.next();
            if (input.equalsIgnoreCase("p")) {
                resetState();
                beginHand();
                /* end the current hand */
                for (int i = 0; i < playerDecks.size(); i++) {
                    currentDeck = i;
                    if (splitMode) {
                        System.out.println("\nDECK " + (currentDeck + 1));
                    }
                    endHand();
                }
                System.out.println("\nYour chip count: " + chipCount);
            }
        } while (!input.equalsIgnoreCase("q") && chipCount > 0);
        if (chipCount <= 0) {
        	System.out.println("You ran out of money. Better luck next time!");
        }
        in.close();
    }

}