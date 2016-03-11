import java.util.Scanner;

public class Blackjack {

	public static void main(String[] args) {
		int chips = Integer.parseInt(args[0]); 
		
		// Check if the number of chips given by the user is valid or not
		if (chips <= 0) {
			System.out.println("Please enter an integer that is greater than or equal to 1.");
		}
		else {
			System.out.println("You have " + chips + " chips.");
			CardPile fourDecks = CardPile.makeFullDeck(4);
			
			// Play blackjack until there are fewer than or equal to 10 cards left in the deck
			while (fourDecks.getNumCards() > 10) {
				Scanner reader = new Scanner(System.in);
				System.out.println("How much would you like to bet?");
				int bet  = reader.nextInt();
				
				// If the bet is a negative integer, that means the user wants to leave the game
				if (bet < 0) {
					System.out.println("You chose to leave, the game ends.");
					break;
				}
				
				// Make sure the user only bets chips that he/she has
				if (bet > chips) {
					System.out.println("You don't have enough chips to bet.");
					break;
				}
				
				// Store in the temporary value oneRound the result of one round of blackjack
				Results oneRound = playRound(fourDecks);
				
				// Update the player's chip pile according to the rules of blackjack
				// Here the player is the same as the user
				if (oneRound == Results.DEALER_WINS) {
					chips -= bet;
					System.out.println("This is your chip count so far: " + chips + ".");
				}
				else if (oneRound == Results.PLAYER_WINS) {
					chips += bet;
					System.out.println("This is your chip count so far: " + chips + ".");
				}
				else if (oneRound == Results.BLACKJACK) {
					chips += bet * 1.5;
					System.out.println("This is your chip count so far: " + chips + ".");
				}
				// If the game ends in a tie, the total chip count remains unchanged
				else {
					System.out.println("This is your chip count so far: " + chips + ".");
				}
				
				// If the user runs out of chips, the game ends
				if (chips == 0) {
					System.out.println("You have run out of chips, the game ends.");
					break;
				}	
			}
			System.out.println("You have no more cards to play blackjack.");
		}	
	}
	
	// Method takes as input a Card object and returns its score in blackjack
	// Card with Ace should return 11
	public static int getScore(Card c) {
		Value v = c.getValue();
		if (v == Value.ACE) {
			return 11;
		}
		else if (v == Value.JACK || v == Value.QUEEN || v == Value.KING) {
			return 10;
		}
		else {
			// .ordinal: initial constant is assigned a value of 0, so add 1
			// declaration of enums starts at TWO, so add 1 again
			return v.ordinal() + 2;
		}
	}
	
	// Method takes as input a CardPile object and returns its total blackjack score
	public static int countValues(CardPile deck) {
		int bestScore = 0;
		// Count the number of Aces in the deck
		int countAces = 0;
		for (int i = 0; i < deck.getNumCards(); i++) {
			Card c = deck.get(i);
			// first only sum up the values of the cards that are not Aces
			if (getScore(c) != 11) {
				bestScore += getScore(c);
			}
			
			// if the card has a value of 11, it's necessarily an Ace
			else {
				countAces++;
			}
		}
		
		// If there is an Ace/Aces in the CardPile deck, determine its/their value(s)
		// considering the current score
		for (int i = 0; i < countAces; i++) {
			if (bestScore <= 10) {
				bestScore += 11;
			}
			else {
				bestScore += 1;
			}
		}
		return bestScore;
	}
	
	// Method takes as input a CardPile object and executes one round of blackjack
	// it returns an enum Results 
	public static Results playRound(CardPile deck) {
		// Create a CardPile Object for the player and for the dealer
		CardPile player = new CardPile();
		CardPile dealer = new CardPile();
		
		// You could possibly run out of cards if you change the main method
		// which would give an ArrayOutOfBoundsException
		try
		{
			// The player and the dealer are each given two cards at the beginning of the round
			player.addToBottom(deck.remove(0));
			player.addToBottom(deck.remove(0));
			// The player can see both of his/her own cards
			System.out.println("This is your hand: " + player.toString() + ".");
			// The Player should know their score
			System.out.println("Your score so far is " + countValues(player) + ".");
			
			// Create two Card Objects to retrieve the dealer's second card
			Card firstDCard = deck.remove(0);
			Card secondDCard = deck.remove(0);
			dealer.addToBottom(firstDCard);
			dealer.addToBottom(secondDCard);
			
			// The player can only see the dealer's second card
			System.out.println("This is the dealer's second card: " + secondDCard.toString() + ".");
			
			// End the round of blackjack if the player has been dealt blackjack
			// or if both the player and the dealer have been dealt blackjack
			if (countValues(player) == 21 && countValues(dealer) == 21) {
				System.out.println("You have both been dealt blackjack, this is a tie.");
				printResult(player, dealer);
				return Results.TIE;
			}
			else if (countValues(player) == 21) {
				System.out.println("You have been dealt blackjack, you win.");
				printResult(player, dealer);
				return Results.BLACKJACK;
			}
			// The player still gets to play if the dealer has been dealt blackjack
		
			// After the cards are dealt, the player chooses to hit or to stay
			Scanner keyboardReader = new Scanner (System.in);
			boolean userPlays = true;
			
			// Play blackjack until either the player or the dealer busts
			// or until the player decides to stay
			while (userPlays) {
				System.out.println("Would you like to hit or to stay?");
				String decision  = keyboardReader.next();
				
				// If the player/user decides to hit, he/she gets a new card
				if (decision.equals("hit")) {
					player.addToBottom(deck.remove(0));
					System.out.println("This is your hand so far: " + player.toString() + ".");
					System.out.println("This is your score so far: " + countValues(player) + ".");
					System.out.println("This is the dealer's second card: " + secondDCard.toString() + ".");
					
					// If the player busts, the game ends and he/she loses
					if (busting(player)) {
						userPlays = false;
						System.out.println("You bust, the dealer wins.");
						printResult(player, dealer);
						return Results.DEALER_WINS;
					}
				}
				// If the player decides to stay, the dealer plays
				else if (decision.equals("stay")) {
					System.out.println("You chose to stay, the dealer plays.");
					userPlays = false;
				}
			}			
			// We have exited the loop: either the player busts or chooses to stay
			if (countValues(player) <= 21) {
				// the dealer hits until their total is 18 or higher
				int dealerScore = countValues(dealer);
				while (dealerScore < 18) {
					dealer.addToBottom(deck.remove(0));
					dealerScore = countValues(dealer);
				}
				// If the dealer busts, the game ends and the user wins
				if (busting(dealer)) {
					printResult(player,dealer);
					System.out.println("The dealer busts, you win.");
					return Results.PLAYER_WINS;
				}
			}
			
			// If neither the player nor the dealer busts, 
			// the player whose score is closest to 21 but not over 21 wins
			printResult(player, dealer);
			return (closestTo21(player, dealer));
		}
		catch(IndexOutOfBoundsException e)
		{
			System.out.println("There are not enough cards to play this round of blackjack, we'll return a tie.");
			return Results.TIE;
		}
	}
	
	
	// Helper method: checks if the player or the dealer busts
	// takes as input a CardPile Object, returns a boolean
	private static boolean busting(CardPile player) {
		boolean playerBusts = true;
		int currentScore = countValues(player);
		if (currentScore > 21) {
			return playerBusts;
		}
		else {
			playerBusts = false;
			return playerBusts;
		}
	}
	
	// Helper method: determines whose score is closest to 21
	// takes as input two CardPile Objects, returns an enum
	private static Results closestTo21(CardPile player, CardPile dealer) {
		int playerScore = countValues(player);
		int dealerScore = countValues(dealer);
		if ((21-playerScore) < (21-dealerScore)) {
			System.out.println("Your score is closer to 21 than the dealer's score is, you win.");
			return Results.PLAYER_WINS;
		}
		else if ((21-playerScore) > (21-dealerScore)) {
			System.out.println("The dealer's score is closer to 21 than yours is, you lose.");
			return Results.DEALER_WINS;
		}
		else {
			System.out.println("Both of your scores are identical, it's a tie.");
			return Results.TIE;
		}
	}
	
	// Helper method: prints the results of one round of blackjack
	// takes as input two CardPile Objects: one for the player, the other one for the dealer
	private static void printResult(CardPile player, CardPile dealer) {
		System.out.print("This is your full hand: " + player.toString() + ". ");
		System.out.println("This is your final score: " + countValues(player) + ".");
		System.out.print("This is the dealer's full hand: " + dealer.toString() + ". ");
		System.out.println("This is the dealer's final score: " + countValues(dealer) + ". ");
	}

	private enum Results {
		DEALER_WINS,
		PLAYER_WINS,
		TIE,
		BLACKJACK
	}
}
