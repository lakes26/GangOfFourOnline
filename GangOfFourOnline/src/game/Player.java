package game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Player implements Serializable{

	private static final long serialVersionUID = -4429830816163804850L;
	
	public ArrayList<Card> hand;
	private int playerID;
	private int numCards = 0;
	
	public Player(int playerID) {
		this.playerID = playerID;
		this.hand = new ArrayList<Card>();
	}
	
	public void addCard(Card card) {
		hand.add(card);
		numCards++;
	}
	
	public void removeCard(Card card) {
		hand.remove(card);
		numCards--;
	}
	
	public void sortHand(String key) {
		Card[] cards = this.hand.toArray(new Card[0]);
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
		this.hand = new ArrayList<Card>(Arrays.asList(cards));
	}
	
	public void clearHand() {
		this.hand = new ArrayList<Card>();
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public int getNumCards() {
		return numCards;
	}

	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}
}
