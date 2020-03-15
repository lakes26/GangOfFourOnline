package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import connections.Packet;

public class Gamestate implements Serializable{

	private static final long serialVersionUID = 6996747481516062547L;
	public Player[] players;
	private int[] playerScores;
	private int currentPlayerID;
	private int[] prevHand = {0,0,0};
	private boolean clockwise = true;
	private int lastPlayedID = -1;
	private boolean gameOver = false;

	public Gamestate(Player[] players) {
		this.players = players;
		this.currentPlayerID = new Random().nextInt(4);
		this.playerScores = new int[]{0,0,0,0};
	}
	
	public void startRound() {
		for(int i = 0; i < 4; i++) {
			players[i].clearHand();
		}
		Deck deck = new Deck();
		deck.shuffle();
		for(int i = 0; i < deck.getDeckSize(); i++) {
			players[i % 4].addCard(deck.draw());
		}
	}
	
	public void startGame() {
		this.currentPlayerID = new Random().nextInt(4);
		this.playerScores = new int[]{0,0,0,0};
		clockwise = true;
		startRound();
	}
	
	public void changeCurrentPlayer() {
		if(clockwise) {
			currentPlayerID = (currentPlayerID + 1) % 4;
		} else {
			if(currentPlayerID == 0)
				currentPlayerID = 3;
			else
				currentPlayerID--;
		}
	}
	
	public boolean isRoundOver() {
		for(int i = 0; i < 4; i++) {
			if(this.players[i].getNumCards() == 0)
				return true;
		}
		return false;
	}
	
	public void incrementScores() {
		int numCards;
		for(int i = 0; i < 4; i++) {
			numCards = this.players[i].getNumCards();
			if(numCards <= 7)
				this.playerScores[i] += numCards;
			else if(numCards <= 10)
				this.playerScores[i] += (2 * numCards);
			else if(numCards <= 13)
				this.playerScores[i] += (3 * numCards);
			else if(numCards <= 15)
				this.playerScores[i] += (4 * numCards);
			else
				this.playerScores[i] += (5 * numCards);
		}
	}
	
	public boolean isGameOver() {
		for(int i = 0; i < 4; i++) {
			if(this.playerScores[i] >= 100)
				return true;
		}
		return false;
	}
	
	public int calculateWinner() {
		int bestScore = 100;
		int bestIndex = 0;
		for(int i = 0; i < 4; i++) {
			if(this.playerScores[i] < bestScore) {
				bestScore = this.playerScores[i];
				bestIndex = i;
			} else if(this.playerScores[i] == bestScore) {
				if(new Random().nextInt(2) == 1)
					bestIndex = i;
			}
		}
		return bestIndex;
	}
	
	
	private boolean isValueSame(Card[] cards) {
		int length = cards.length;
		boolean same = true;
		for(int i = 0; i < length - 1; i++) {
			if(cards[i].getValue() != cards[i+1].getValue())
				same = false;
		}
		return same;
	}
	
	//assumes 5 cards
	private boolean isStraight(Card[] cards) {
		boolean isStraight = true;
		for(int i = 0; i < 4; i++) {
			if(cards[i].getValue() + 1 != cards[i+1].getValue())
				isStraight = false;
		}
		return isStraight;
	}
	
	private boolean isFlush(Card[] cards) {
		boolean isFlush = true;
		for(int i = 0; i < 4; i++) {
			if(cards[i].getColor() != cards[i+1].getColor())
				isFlush = false;
		}
		return isFlush;
	}
	
	private boolean isFullHouse(Card[] cards) {
		if(isValueSame(Arrays.copyOfRange(cards, 0, 3)) && isValueSame(Arrays.copyOfRange(cards, 3, 5))
				|| isValueSame(Arrays.copyOfRange(cards, 0, 2)) && isValueSame(Arrays.copyOfRange(cards, 2, 5))) {
			return true;
		} else {
			return false;
		}
	}
	
	public void sortCards(Card[] cards, String key) {
		if(key.compareToIgnoreCase("color") == 0) {
			Arrays.sort(cards, new Comparator<Card>() {
				@Override
				public int compare(Card o1, Card o2) {
					int colcmp =  Integer.compare(o1.getColor(),o2.getColor());
					if(colcmp == 0)
						return Integer.compare(o1.getValue(),o2.getValue());
					else
						return colcmp;
				}
		    });
		} else if(key.compareToIgnoreCase("value") == 0){
			Arrays.sort(cards, new Comparator<Card>() {
				@Override
				public int compare(Card o1, Card o2) {
					int valcmp =  Integer.compare(o1.getValue(),o2.getValue());
					if(valcmp == 0)
						return Integer.compare(o1.getColor(),o2.getColor());
					else
						return valcmp;
				}
		    });
		}
	}
	

	/* 				
	 * Single = 1	
	 * Pair = 2			
	 * Triple = 3	
	 * Straight = 4	
	 * Flush = 5	
	 * Full House = 6	
	 * Straight Flush = 7
	 * Gang = 8 
	 * 
	 * returns formated as[Type, Value(rank), Color Tie Break]
	 */
	
	public int[] getHandStrength(ArrayList<Card> cardslist) {
		int numCards = cardslist.size();
		Card[] cards = cardslist.toArray(new Card[0]);
		int[] return_array = {-1,-1,-1};
		
		if(numCards == 1) {
			
			return_array[0] = 1;
			return_array[1] = cards[0].getValue();
			return_array[2] = cards[0].getColor();
		}
		else if(numCards == 2) {
			if(isValueSame(cards)) {
				sortCards(cards, "color");
				return_array[0] = 2;
				return_array[1] = cards[0].getValue();
				return_array[2] = cards[0].getColor() + 3*cards[1].getColor();
			}
		}
		else if(numCards == 3) {
			if(isValueSame(cards)) {
				sortCards(cards, "color");
				return_array[0] = 3;
				return_array[1] = cards[0].getValue();
				return_array[2] = cards[0].getColor() + 2*cards[1].getColor() + 4*cards[2].getColor();
			}
		}
		else if(numCards >= 4 && isValueSame(cards)) {
				return_array[0] = 8;
				return_array[0] = numCards;
				return_array[2] = cards[0].getValue();
		}
		else if(numCards == 5) {
			sortCards(cards, "value");
			
			if(isStraight(cards) && isFlush(cards)) {
				return_array[0] = 7;
				return_array[1] = cards[4].getValue();
				return_array[2] = cards[4].getColor();
			}
			else if(isStraight(cards)) {
				return_array[0] = 4;
				return_array[1] = cards[4].getValue();
				return_array[2] = cards[4].getColor();
			}
			else if(isFlush(cards)) {
				return_array[0] = 5;
				return_array[1] = cards[4].getValue();
				return_array[2] = cards[4].getColor();
			}
			else if(isFullHouse(cards)) {
				return_array[0] = 6;
				if(cards[0].getValue() == cards[2].getValue()) {
					return_array[1] = 12 * cards[0].getValue() + cards[4].getValue();
					return_array[2] = 10* cards[0].getColor() + 20*cards[1].getColor() + 40*cards[2].getColor() + cards[3].getColor() + 3* cards[4].getColor();
				} else {
					return_array[1] = cards[0].getValue() + 12 * cards[4].getValue();
					return_array[2] = cards[0].getColor() + 3 *cards[1].getColor() + 10 * cards[2].getColor() + 20* cards[3].getColor() + 40* cards[4].getColor();
				}
			}
		}
		return return_array;
	}

	public boolean isStronger(int[] handStrength, int[] prevHand) {
		boolean isStronger = false;
		for(int i = 0; i < 3; i++) {
			if(handStrength[i] > prevHand[i]) {
				isStronger = true;
				break;
			} else if(handStrength[i] < prevHand[i]) {
				isStronger = false;
				break;
			} else if(i == 2) {
				isStronger = false;
			}
		}
		return isStronger;
	}
	
	public boolean playCards(int playerID, ArrayList<Card> cards) {
		int[] currentHand = this.getHandStrength(cards);
		if(isStronger(currentHand, this.prevHand)) {
			for(int i = 0; i < cards.size(); i++) {
				this.players[playerID].removeCard(cards.get(i));
				this.prevHand = currentHand;
			}
			return true;
		} else {
			return false;
		}
	}

	public void parsePacket(Packet packet) {
		
		if(packet.getSortRequest() == 1)
			this.players[packet.getPlayerID()].sortHand("value");
		else if(packet.getSortRequest() == 2)
			this.players[packet.getPlayerID()].sortHand("color");
		
		else if(packet.getPlayerID() == this.currentPlayerID) {
			
			boolean canPass = true;
			if(this.currentPlayerID == this.lastPlayedID || this.lastPlayedID == -1) {
				for(int i = 0; i < 3; i++) {
					this.prevHand[i] = 0;
				}
				canPass = false;
			}
			
			if(packet.getCardsPlayed().size() == 0 && canPass) {
				this.changeCurrentPlayer();
			} else if(playCards(packet.getPlayerID(), packet.getCardsPlayed())) {
				this.lastPlayedID = this.currentPlayerID;
				if(this.isRoundOver()) {
					this.clockwise = !this.clockwise;
					this.incrementScores();
					if(this.isGameOver()) {
						this.gameOver  = true;
					} else {
						this.startRound();
					}
				}
				this.changeCurrentPlayer();
			}
		}
	}
	
	public boolean getGameOver() {
		return this.gameOver;
	}
	
	public int[] getPrevHand() {
		return prevHand;
	}

	public void setPrevHand(int[] prevHand) {
		this.prevHand = prevHand;
	}
}
 