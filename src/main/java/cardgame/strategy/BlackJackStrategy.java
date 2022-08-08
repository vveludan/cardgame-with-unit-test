package cardgame.strategy;

import java.util.Map;

import cardgame.domain.Player;

public interface BlackJackStrategy {
	
	void applyStrategy(Player player, Map<Integer, String> gameStatus, String playerInput);

}
