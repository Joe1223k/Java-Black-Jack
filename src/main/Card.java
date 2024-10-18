package main;

public class Card {
	
	String value;
	String type;
	
	public Card(String value, String type) {
		this.value = value;
		this.type = type;
	}
	
	public String toString() {
		return value + "-" + type;
	}
	
	public int getValue() {
		// Ace, Jack, Queen, King
		if ("AJQK".contains(value)) {
			if (value == "A") {
				return 11;
			}
			return 10;
		}
		// 2 to 10
		return Integer.parseInt(value);
		
	}
	
	public boolean isAce() {
		return value == "A";
	}
	
	public String getImagePath() {
		return "/" + toString() + ".png";
	}

}
