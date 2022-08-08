package cardgame.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Player {
	
	private int score;
	private final PlayerType playerType;
	private final int playerIdentity;
	private List<Card> cards = new ArrayList<>();
	private PlayerStatus playerStatus;
	
	
	public Player(int playerIdentiry, PlayerType playerType) {
		this.playerType = playerType;
		this.playerIdentity = playerIdentiry;
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	public List<Card> getCards() {
		return Collections.unmodifiableList(cards);
	}
	
	public int getScore() {
		boolean hasAceCard = cards.stream().anyMatch(card -> card.getCardType() == CardType.ACE);
		score = cards.stream().collect(Collectors.summingInt(Card::getCardValue));
		if(hasAceCard) {
			int scoreWithOutAce = score -1;
			if(scoreWithOutAce >= 5 && scoreWithOutAce <=10) score = scoreWithOutAce + 11;
		}
		return score;
	}
	
	public int getPlayerIdentity() {
		return playerIdentity;
	}
	
	public void setPlayerStatus(PlayerStatus playerStatus) {
		this.playerStatus = playerStatus;
	}
	
	public PlayerStatus getPlayerStatus() {
		return this.playerStatus;
	}
	
	public PlayerType getPlayerType() {
		return this.playerType;
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(playerIdentity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return playerIdentity == other.playerIdentity;
	}

	public String toString() {
		String player = null;
		if(playerType == PlayerType.DEALER) {
			if(getScore() > 21) {
				player = "dealer is busted";
			} else {
				player = "dealer has " + getScore();
			}
			
		} else {
			if(getScore() > 21) {
				player = "Scoring Player " + playerIdentity + " busted ";
			} else {
				player = "Scoring Player " + playerIdentity + " has " + getScore();
			}
			
		}
		return player;
	}

}
