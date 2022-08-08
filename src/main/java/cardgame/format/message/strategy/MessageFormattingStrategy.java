package cardgame.format.message.strategy;

import java.util.List;

import cardgame.domain.Player;

public interface MessageFormattingStrategy {
	
	String setupPlayerGameStatusMessage(Player player);
	
	void printSummary(List<Player> players);

}
