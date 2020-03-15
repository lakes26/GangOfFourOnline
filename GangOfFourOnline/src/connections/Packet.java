package connections;

import java.util.ArrayList;

import game.Card;

public class Packet {
	
	private int playerID;
	private int sortRequest;
	private ArrayList<Card> cardsPlayed;
		
	public Packet(int id, int sortRequest, ArrayList<Card> cardsPlayed) {
		this.setPlayerID(id);
		this.cardsPlayed = cardsPlayed;
		this.sortRequest = sortRequest;
	}

	public int getPlayerID() {
		return playerID;
	}
	
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public ArrayList<Card> getCardsPlayed() {
		return cardsPlayed;
	}
	
	public void setCardsPlayed(ArrayList<Card> cardsPlayed) {
		this.cardsPlayed = cardsPlayed;
	}
	
	public int getSortRequest() {
		return sortRequest;
	}

	public void setSort(int sortRequest) {
		this.sortRequest = sortRequest;
	}
}
