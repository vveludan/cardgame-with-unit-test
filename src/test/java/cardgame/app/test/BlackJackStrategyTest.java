package cardgame.app.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cardgame.domain.Game;
import cardgame.domain.Player;
import cardgame.domain.PlayerStatus;
import cardgame.domain.PlayerType;
import cardgame.format.message.strategy.MessageFormattingStrategy;
import cardgame.format.message.strategy.MessageFormattingStrategyImpl;
import cardgame.strategy.BlackJackStrategy;
import cardgame.strategy.BlackJackStrategyImpl;

public class BlackJackStrategyTest {

	MessageFormattingStrategy formattingStrategy = new MessageFormattingStrategyImpl();
	BlackJackStrategy blackJackStrategy = new BlackJackStrategyImpl(formattingStrategy) ;

	/*
	 * @BeforeEach void setUp() { formattingStrategy = new
	 * MessageFormattingStrategyImpl(); blackJackStrategy = new
	 * BlackJackStrategyImpl(formattingStrategy);
	 * 
	 * }
	 */
	@Test
	public void testBlackJackApp() {
		Game game = new Game(1, blackJackStrategy, formattingStrategy);
		game.startGame();
		String result = null;
		List<Player> players = game.getPlayers();
		Player dealer = players.stream().filter(p -> p.getPlayerType() == PlayerType.DEALER).findFirst().get();
		Player player = players.stream().filter(p -> p.getPlayerType() == PlayerType.PLAYER).findFirst().get();
		while (player.getScore() <= 16) {
			result = game.processPlayerAction("hit");
			if (result.equals("false")) {
				break;
			}
			if (player.getScore() >= 17 && !result.equals("false")) {
				result = game.processPlayerAction("stand");
			} else {
				result = game.processPlayerAction("hit");
			}
		}

		
		if (player.getScore() >= 17  && (result != null && !result.equals("false"))) {
			result = game.processPlayerAction("stand");
		} else if(player.getScore() >= 17 && result == null) {
			result = game.processPlayerAction("stand");
		}
		  
		PlayerStatus playerStatus = player.getPlayerStatus();
		PlayerStatus dealerStatus = dealer.getPlayerStatus();
		int playerScore = player.getScore();
		int dealerScore = dealer.getScore();

		if (playerStatus == PlayerStatus.BUSTED) {
			assertTrue(player.getScore() > 21, "Player is Busted");
		} 
		
		if(dealerStatus == PlayerStatus.BUSTED) {
			assertTrue(dealer.getScore() > 21, "Dealer is Busted");
		}
		
		
		if(dealerScore > playerScore && dealerScore <= 21) {
			assertTrue(dealerStatus == PlayerStatus.WINS && playerStatus == PlayerStatus.STAND, "dealer wins over player");
		}

		if(dealerScore > playerScore && dealerScore > 21 && playerScore < 21) {
			assertTrue(dealerStatus == PlayerStatus.BUSTED && playerStatus == PlayerStatus.WINS, "player wins and dealer busted");
		}

		if(dealerScore == playerScore && (dealerStatus != PlayerStatus.BUSTED && playerStatus != PlayerStatus.BUSTED)) {
			assertTrue(dealerStatus == PlayerStatus.TIES && playerStatus == PlayerStatus.TIES, "dealer ties with player");
		}
		
		if(dealerScore == playerScore && (dealerStatus == PlayerStatus.BUSTED && playerStatus == PlayerStatus.BUSTED)) {
			assertTrue(dealerStatus == PlayerStatus.BUSTED && playerStatus == PlayerStatus.BUSTED, "dealer and player are busted");
		}


		if(dealerScore < playerScore && !(player.getScore() > 21)) {
			assertTrue(dealerStatus == PlayerStatus.STAND && playerStatus == PlayerStatus.WINS, "dealer loses to player");
		}

		if(dealerScore < playerScore && player.getScore() > 21 && dealerScore <= 21) {
			assertTrue(dealerStatus == PlayerStatus.WINS && playerStatus == PlayerStatus.BUSTED, "dealer wins over player, player busted");
		}
	}
	


}
