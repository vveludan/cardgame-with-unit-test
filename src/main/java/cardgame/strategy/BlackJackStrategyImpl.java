package cardgame.strategy;

import java.util.Map;

import cardgame.domain.Player;
import cardgame.domain.PlayerStatus;
import cardgame.format.message.strategy.MessageFormattingStrategy;

public class BlackJackStrategyImpl implements BlackJackStrategy {
	
	private final MessageFormattingStrategy messageFormattingStrategy;
	
	public BlackJackStrategyImpl(MessageFormattingStrategy messageFormattingStrategy) {
		this.messageFormattingStrategy = messageFormattingStrategy;
	}
	
	public void applyStrategy(Player player, Map<Integer, String> gameStatus, String playerInput) {
		if(playerInput == null) {
			player.setPlayerStatus(PlayerStatus.GAME_IN_PROGRESS);
			gameStatus.put(player.getPlayerIdentity(), messageFormattingStrategy.setupPlayerGameStatusMessage(player));
			return;
		}
		int score = player.getScore();
		if(score == 21) {
			player.setPlayerStatus(PlayerStatus.WINS);
			gameStatus.put(player.getPlayerIdentity(), messageFormattingStrategy.setupPlayerGameStatusMessage(player));
		} else if(score > 21) {
			player.setPlayerStatus(PlayerStatus.BUSTED);
			gameStatus.put(player.getPlayerIdentity(), messageFormattingStrategy.setupPlayerGameStatusMessage(player));
		} else {
			if("hit".equalsIgnoreCase(playerInput)) {
				player.setPlayerStatus(PlayerStatus.HIT);
			} else if("stand".equalsIgnoreCase(playerInput)) {
				player.setPlayerStatus(PlayerStatus.STAND);
			} else if("".equalsIgnoreCase(playerInput)) {
				player.setPlayerStatus(PlayerStatus.GAME_IN_PROGRESS);
			} else {
				throw new IllegalArgumentException("Invalid Player Input received");
			}
			gameStatus.put(player.getPlayerIdentity(), messageFormattingStrategy.setupPlayerGameStatusMessage(player));
		}
	}
	
}
