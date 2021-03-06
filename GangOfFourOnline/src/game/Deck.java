package game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
	public ArrayList<Card> cards;
	private int deckSize;
	
	public Deck() {
		this.cards = new ArrayList<>();
		Card card;
		
		cards.add(new Card(4, 1, 1));
		for(int i = 1; i < 11; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < 2; k++) {
					card = new Card(j, i, k);
					cards.add(card);
				}
				if(i == 10) {
					if(j < 2) 
						card = new Card(j, 11, 1);
					else
						card = new Card(j, 12, 1);
					cards.add(card);
				}
			}
		}
		this.deckSize = this.cards.size();
	}
	
	public int getDeckSize() {
		return deckSize;
	}

	public void shuffle() {
		Collections.shuffle(this.cards, new Random());
	}
	
	public Card draw() {
		Card card = this.cards.get(0);
		this.cards.remove(card);
		return card;
	}
	
	public void print() {
		for(int i = 0; i < this.cards.size(); i++) {
			System.out.print(cards.get(i).toString());
			if(i % 5 == 4) {
				System.out.println();
			}
		}
	}
}
