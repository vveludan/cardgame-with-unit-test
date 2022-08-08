package cardgame.format.message.strategy;

import java.util.List;

import cardgame.constants.GameConstants;
import cardgame.domain.Player;
import cardgame.domain.PlayerStatus;
import cardgame.domain.PlayerType;

public class MessageFormattingStrategyImpl implements MessageFormattingStrategy {

	@Override
	public String setupPlayerGameStatusMessage(Player player) {
		  int numberOfCards = player.getCards().size();
		  String card = numberOfCards == 1 ? GameConstants.CARD : GameConstants.CARDS;
		  StringBuffer buffer = null;
		  String hitOrStand = null;
		  if(isPlayer(player)) {
			  buffer = new StringBuffer(GameConstants.DEALING_TO_PLAYER);
			  hitOrStand = GameConstants.PLAYER_HIT_OR_STAND; 
		  } else {
			  buffer = new StringBuffer(GameConstants.DEALING_TO_DEALER);
			  if(numberOfCards == GameConstants.DEALER_NUMBER_OF_CARDS_FOR_HIT) { 
				  hitOrStand = GameConstants.DEALER_HITS;
			  } else if(numberOfCards == GameConstants.DEALER_NUMBER_OF_CARDS_FOR_STAND) {
				  hitOrStand = GameConstants.DEALER_STANDS; 
			  }
			  
		  }
		  
		  
		  formatPlayerGameStatusMessage(player, card, buffer, hitOrStand);
		  
		  return buffer.toString();
	}
	
	

	private void formatPlayerGameStatusMessage(Player player, String card, StringBuffer buffer, String hitOrStand) {
		if(player.getPlayerStatus() == PlayerStatus.BUSTED) { 
			  if(isPlayer(player)) {
				  buffer.append(player.getPlayerIdentity()).append(", ").append(card).append(": ").append(player.getCards().toString()).append(". ").append(GameConstants.BUSTED_OVER_TWENTY_ONE);
			  } else {
				  buffer.append(", ").append(card).append(": ").append(player.getCards().toString()).append(". ").append(GameConstants.BUSTED_OVER_TWENTY_ONE);
			  }
		  }  else if(player.getPlayerStatus() == PlayerStatus.WINS) {
			  if(isPlayer(player)) {
				  buffer.append(player.getPlayerIdentity()).append(", ").append(card).append(": ").append(player.getCards().toString()).append(". ").append(GameConstants.WINS_WITH_TWENTY_ONE);
			  } else {
				  buffer.append(", ").append(card).append(": ").append(player.getCards().toString()).append(". ").append(GameConstants.WINS_WITH_TWENTY_ONE);
			  }
		  } else if(player.getPlayerStatus() == PlayerStatus.HIT || player.getPlayerStatus() == PlayerStatus.STAND){
			  if(isPlayer(player)) {
				  buffer.append(player.getPlayerIdentity()).append(", ").append(card).append(": ").append(player.getCards().toString()).append(". ").append(hitOrStand);
			  } else {
				  buffer.append(", ").append(card).append(": ").append(player.getCards().toString()).append(". ").append(hitOrStand);
			  }
		  } else if(player.getPlayerStatus() == PlayerStatus.GAME_IN_PROGRESS) {
			  if(isPlayer(player)) {
				  if(player.getCards().size() > 1) {
					  buffer.append(player.getPlayerIdentity()).append(", ").append(card).append(": ").append(player.getCards().toString()).append(hitOrStand);
				  } else {
					  buffer.append(player.getPlayerIdentity()).append(", ").append(card).append(": ").append(player.getCards().toString());
				  }
				  
			  } else {
				  buffer.append(", ").append(card).append(": ").append("face down");
			  }
		  }
	}
	
	public void printSummary(List<Player> players) {
		StringBuffer buffer = null;
		Player dealer = players.stream().filter(player -> player.getPlayerType() == PlayerType.DEALER).findFirst().get();
		int dealerScore = dealer.getScore();
		for(int i=0; i < players.size();i++) {
			
			if(players.size() -1 == i) {
				break;
			}
			
			Player player = players.get(i);
			if(i == 0) {
				buffer = new StringBuffer(player.toString()).append(", ").append(dealer.toString()).append(". ");
			} else {
				buffer.append(player.toString()).append(", ").append(dealer.toString()).append(". ");
			}
			
			if(dealerScore > player.getScore() && dealerScore <= 21) {
				buffer.append("Dealer wins.\n");
				dealer.setPlayerStatus(PlayerStatus.WINS);
			} else if(dealerScore > player.getScore() && dealerScore > 21 && (player.getPlayerStatus() != PlayerStatus.BUSTED)) {
				buffer.append("Player ").append(player.getPlayerIdentity()).append(" wins over dealer\n");
				player.setPlayerStatus(PlayerStatus.WINS);
			}
			else if(dealerScore == player.getScore()) {
				buffer.append("Dealer ties with player.\n");
				player.setPlayerStatus(PlayerStatus.TIES);
				dealer.setPlayerStatus(PlayerStatus.TIES);
			} else if(dealerScore < player.getScore() && !(player.getScore() > 21) && (player.getPlayerStatus() == PlayerStatus.STAND && dealer.getPlayerStatus() == PlayerStatus.STAND)) {
				buffer.append("Player ").append(player.getPlayerIdentity()).append(" wins\n");
				player.setPlayerStatus(PlayerStatus.WINS);
			} else if(dealerScore < player.getScore() && player.getScore() > 21 && dealer.getPlayerStatus() != PlayerStatus.BUSTED) {
				dealer.setPlayerStatus(PlayerStatus.WINS);
				buffer.append("Dealer wins.\n");
			} else if(dealerScore > 21 && player.getScore() > 21) {
				dealer.setPlayerStatus(PlayerStatus.BUSTED);
				player.setPlayerStatus(PlayerStatus.BUSTED);
				buffer.append("both player and dealer are busted\n");
			}

			
		}
		System.out.println(buffer.toString());
	}
	

	
	private boolean isPlayer(Player player) {
		return player.getPlayerType() == PlayerType.PLAYER ? true : false;
	}


}
