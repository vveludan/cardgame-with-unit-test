package cardgame.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Deck {
	
	private Map<DeckType, List<Card>> deckMap = new EnumMap<>(DeckType.class);
	private List<Card> allCards = new ArrayList<>();
	
	public Deck() {
		populateDeck();
		populateAllCards();
	}
	
	public List<Card> shuffleAndGetCards() {
		Collections.shuffle(allCards);
		return new ArrayList<>(allCards);
	}
	
	private void populateAllCards() {
		allCards.addAll(deckMap.get(DeckType.HEART));
		allCards.addAll(deckMap.get(DeckType.DIAMOND));
		allCards.addAll(deckMap.get(DeckType.SPADE));
		allCards.addAll(deckMap.get(DeckType.CLUB));
	}
	
	private void populateDeck() {
		deckMap.put(DeckType.HEART, populateCards(DeckType.HEART));
		deckMap.put(DeckType.DIAMOND, populateCards(DeckType.DIAMOND));
		deckMap.put(DeckType.SPADE, populateCards(DeckType.SPADE));
		deckMap.put(DeckType.CLUB, populateCards(DeckType.CLUB));
	}
	
	private List<Card> populateCards(DeckType deckType) { 
		return List.of(new Card(CardType.ACE, deckType), 
				new Card(CardType.JACK, deckType), 
				new Card(CardType.QUEEN, deckType), 
				new Card(CardType.KING, deckType), 
				new Card(CardType.TWO, deckType),
				new Card(CardType.THREE, deckType), 
				new Card(CardType.FOUR, deckType), 
				new Card(CardType.FIVE, deckType), 
				new Card(CardType.SIX, deckType), 
				new Card(CardType.SEVEN, deckType), 
				new Card(CardType.EIGHT, deckType),
				new Card(CardType.NINE, deckType), 
				new Card(CardType.TEN, deckType));
		
	}
	
	public static void main(String[] args) {
		Deck deck = new Deck();
		List<Card> allCards = deck.shuffleAndGetCards();
		allCards.forEach(card -> System.out.println(card.toString()));
		
	}
	
	public void clear() {
		allCards.clear();
		deckMap.clear();
	}

}
