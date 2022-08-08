package cardgame.domain;

import java.util.Objects;

public class Card {
	
	private final CardType cardType;
	private final DeckType deckType;
	
	public Card(CardType cardType, DeckType deckType) {
		this.cardType = cardType;
		this.deckType = deckType;
	}
	
	public int getCardValue() {
		return cardType.getValue();
	}
	
	public CardType getCardType() {
		return cardType;
	}
	
	public DeckType getDeckType() {
		return deckType;
	}
	
	public String toString() {
		String cardValue = null;
		if(cardType == CardType.ACE) {
			cardValue = cardType.name();
		} else {
			cardValue = ""+getCardValue();
		}
		return cardValue + " " + deckType.toString(); 
	}

	@Override
	public int hashCode() {
		return Objects.hash(cardType, deckType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		return cardType == other.cardType && deckType == other.deckType;
	}





}
