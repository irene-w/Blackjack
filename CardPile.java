import java.util.ArrayList;
import java.util.Collections;

// Class to represent the notion of a pile of cards.
public class CardPile {
	// Store all the cards into an ArrayList<Card> 
	private ArrayList<Card> cards;
	
	// Store how many cards are non null, assuming no one inserts null by calling add(null)
	private int numCards;
		
	// Initialize the private properties of the CardPile object
	public CardPile() { 
		this.cards = new ArrayList<Card>();
		this.numCards = 0; 
	}
	
	// Method finds the smallest index i for which the value of cards[i] is equal to null
	// and places c in that position
	// then updates counter numCards
	public void addToBottom(Card c) {
		// The .add() method adds the Card c at the end of the list of cards
		this.cards.add(c);
		this.numCards++;
	}
	
	// Returns whether or not the card pile is empty 
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}
	
	// Returns the Card Object at the location of i
	//It is possible this will return null or throw an exception if the 
	// index i is less than 0 or greater than numCards - 1
	public Card get(int i) {
		// Use .get() method
		return this.cards.get(i);
	}
	
	// Method removes the element located at index i from the cards ArrayList,
	// fills in the "holes" so that subsequent cards are at an index 1 previous,
	// and returns the card's value
	public Card remove(int i) {
		// Store the card so that we can remove it
		Card cardRemoved = this.cards.get(i);
		// Method .remove() removes the elements at the specified position in this list
		// and shifts any subsequent elements to the left to fill in the "holes"
		this.cards.remove(i);
		
		// decrease numCards by 1
		this.numCards--;
		return cardRemoved;
	}
	
	// Method returns the index of where the card with specified Suit and Value 
	// appears for the first time in cards
	// if it's not in cards, return -1
	public int find(Suit s, Value v) {
		for (int i = 0; i < this.cards.size(); i++) {
			Card currentCard = this.cards.get(i);
			Suit currentSuit = currentCard.getSuit();
			Value currentValue = currentCard.getValue();
			if (currentSuit == s && currentValue == v) {
				return i;
			}
		}
		return -1;		
	}
	
	// Method returns a String representation of the CardPile
	public String toString() {
		String cardString = "";
		for (int i = 0; i < this.cards.size(); i++) {
			cardString += i + "." + cards.get(i) + " ";
		}
		return cardString;
	}
	
	// Method makes a shuffled deck filled with all the possible cards
	// returns a CardPile Object
	public static CardPile makeFullDeck() {
		// Create a CardPile Object that we will return
	    CardPile deck = new CardPile();
	    // Every possible suit
	    for (Suit s : Suit.values()) {
	      // Every possible value
	      for (Value v : Value.values()) {
	        // add to the bottom of the deck
	       deck.addToBottom(new Card(s,v)); 
	      }
	    }
		
		// Use method shuffle from the library Collections
		Collections.shuffle(deck.cards);
		return deck;
	}
	
	// Method produces a shuffled pile of cards in which each of the 52 cards are represented n times
	public static CardPile makeFullDeck(int n) {
		// n must be at least one: if not, return an empty CardPile
		if (n <= 0) {
			System.out.println("The input for n is invalid, please enter a positive integer for n that is at least 1.");
			CardPile emptyDeck = new CardPile();
			return emptyDeck;
		}
		
		// Create a CardPile Object that we will return
		CardPile shuffledDecks = new CardPile();
		
		// Create a CardPile Object to store one full shuffled deck
		CardPile deck = new CardPile();  
		
		// Create n different shuffled decks
		for (int i = 0; i < n; i++) {
			deck = CardPile.makeFullDeck();
			// Add to the bottom of the shuffledDecks
			for (int j = 0; j < deck.cards.size(); j++) {
				shuffledDecks.addToBottom(deck.get(j));
			}
		}
		return shuffledDecks;
	}
	
	// Method returns how many cards are non null.
	public int getNumCards() {
		return this.cards.size(); 
	}
}

