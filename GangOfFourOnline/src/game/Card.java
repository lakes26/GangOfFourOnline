package game;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Serializable{

	private static final long serialVersionUID = 8698664110790946497L;
	
	private int color;
	private int value;
	
	public Card(int color, int value) {
		this.color = color;
		this.value = value;
	}

	public int getColor() {
		return color;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		String cardcolor;
		String cardvalue = String.valueOf(value);
		
		if(color == 0) {
			cardcolor = "Green ";
		} else if(color == 1) {
			cardcolor = "Yellow ";
		} else if(color == 2) {
			cardcolor = "Red ";
		} else {
			cardcolor = "Multicolor ";
		}
		
		if(value == 11)
			cardvalue = "Phoenix";
		else if (value == 12)
			cardvalue = "Dragon";
		
		return "(" + cardcolor + cardvalue + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Card card = (Card) o;
		return color == card.color &&
				value == card.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(color, value);
	}
}
