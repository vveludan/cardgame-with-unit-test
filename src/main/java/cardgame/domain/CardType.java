package cardgame.domain;

import java.util.stream.Stream;

public enum CardType {
	
	ACE(1),
	JACK(10),
	QUEEN(10),
	KING(10),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	TEN(10);
	
	private int value;
	
	CardType(int value) {
		this.value = value;
	}
	
	int getValue() {
		return value;
	}
	
	public static int getValue(CardType cardType) {
		int result = Stream.of(CardType.values()).filter(type -> type == cardType).map(card -> card.getValue()).findAny().orElse(-1);
		if(result == -1) {
			throw new IllegalArgumentException("Invalid CardType: "+ cardType + " received");
		}
		return result;
	}
	

}
