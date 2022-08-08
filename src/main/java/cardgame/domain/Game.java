package cardgame.domain;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import cardgame.constants.GameConstants;
import cardgame.format.message.strategy.MessageFormattingStrategy;
import cardgame.strategy.BlackJackStrategy;

public class Game {
	
	private final int numberOfPlayers;
	List<Card> shuffledCards;
	private List<Player> players = new ArrayList<>();
	private List<Player> intermediatePlayers;
	private final Deck deck;
	private Map<Integer, String> gameStatus = new ConcurrentHashMap<>();
	private final BlackJackStrategy blackJackStrategy;
	private final MessageFormattingStrategy messageFormattingStrategy;

	private boolean gameEnded = false;
	
	public Game(int numberOfPlayers, BlackJackStrategy blackJackStrategy, MessageFormattingStrategy messageFormattingStrategy) {
		this.numberOfPlayers = numberOfPlayers;
		this.deck = new Deck();
		this.blackJackStrategy = blackJackStrategy;
		this.messageFormattingStrategy = messageFormattingStrategy;
		shuffledCards = deck.shuffleAndGetCards();
		initPlayers(numberOfPlayers);
	}
	
	private void initPlayers(int numberOfPlayers) {
		numberOfPlayers = numberOfPlayers + 1;
		for(int i = 0; i < numberOfPlayers; i++) {
			if(i == numberOfPlayers -1) {
				Player player = new Player(i, PlayerType.DEALER);
				players.add(player);
				continue;
			}
			Player player = new Player(i, PlayerType.PLAYER);
			players.add(player);
		}
	}
	
	public List<Player> getPlayers() {
		return new ArrayList<>(players);
	}
	
	public void dealFirstCard() {
		ListIterator<Card> cardIterator = shuffledCards.listIterator();
		Player firstPlayer = null;
		int firstPlayerCardSize = 0;
		while(firstPlayerCardSize != 2) {
			
			for(int i = 0; i < players.size(); i++) {
				if(firstPlayerCardSize == 2) break;
				while(cardIterator.hasNext()) {
					Player player = players.get(i);
					Card card = cardIterator.next();
					player.addCard(card);
					blackJackStrategy.applyStrategy(player, gameStatus, null);
					
					if(i == 0) {
						firstPlayer = players.get(i);
						firstPlayerCardSize = firstPlayerCardSize + 1;
						
					}
					System.out.println(gameStatus.get(player.getPlayerIdentity()));
					cardIterator.remove();
					break;
				}

			}
			
		}
		intermediatePlayers = new ArrayList<>(getPlayers());
	}
	
	public String processPlayerAction(String playerAction) {
			ListIterator<Player> playersIterator = intermediatePlayers.listIterator();
			ListIterator<Card> cardIterator = shuffledCards.listIterator();
			Player player = null;
			String playerGameStatus = null;
			
			while(playersIterator.hasNext()) {
				player = playersIterator.next();
				
				if(player.getPlayerType() == PlayerType.DEALER) { 
				  processDealerActions();
				  break; 
				}
				
				if(playerAction.equalsIgnoreCase("stand")) {
					player.setPlayerStatus(PlayerStatus.STAND);
					playersIterator.remove();
					if(intermediatePlayers.size() == 1) {
						playerAction = "";
						processDealerActions();
						break;
					} else {
						playerAction = "";
						continue;
					}
				}
				while(cardIterator.hasNext()) {
					Card card = cardIterator.next();
						player.addCard(card);
						blackJackStrategy.applyStrategy(player, gameStatus, playerAction);
						playerGameStatus = gameStatus.get(player.getPlayerIdentity());
						cardIterator.remove();
						break;
				}
				
				if((player.getPlayerStatus() == PlayerStatus.BUSTED || player.getPlayerStatus() == PlayerStatus.WINS || player.getPlayerStatus() == PlayerStatus.STAND)) {
					playerAction = "";
					System.out.println(gameStatus.get(player.getPlayerIdentity()));
					playersIterator.remove();
					continue;
				} else {
					break;
				}
			}
			if(gameEnded == true) {
				playerGameStatus = "false";
			}
			return playerGameStatus;
	}
	
	private void processDealerActions() {
		Player dealer = players.stream().filter(p -> p.getPlayerType() == PlayerType.DEALER).findFirst().get();
		
		ListIterator<Card> cardIterator = shuffledCards.listIterator();
		while(dealer.getPlayerStatus() == PlayerStatus.GAME_IN_PROGRESS || dealer.getPlayerStatus() == PlayerStatus.HIT) {
			while(cardIterator.hasNext()) {
				Card dealerCard = cardIterator.next();
				dealer.addCard(dealerCard);
				
				
				
				String dealerAction = null;
				PlayerStatus dealerStatus = dealer.getPlayerStatus();
				if(dealerStatus != PlayerStatus.BUSTED || dealerStatus != PlayerStatus.WINS) {
					dealerAction = GameConstants.PLAYER_ACTION_HIT;
				} else {
					dealerAction = GameConstants.PLAYER_ACTION_STAND;
				}
				blackJackStrategy.applyStrategy(dealer, gameStatus, dealerAction);
				
				if(dealer.getScore() >=17 && dealer.getScore() < 21) {
					dealer.setPlayerStatus(PlayerStatus.STAND);
					if(gameStatus.get(dealer.getPlayerIdentity()).contains("hits")) {
						gameStatus.get(dealer.getPlayerIdentity()).replace("hits", "stands");
					}
					
				} else if(dealer.getScore() == 21) {
					if(gameStatus.get(dealer.getPlayerIdentity()).contains("hits")) {
						gameStatus.get(dealer.getPlayerIdentity()).replace("hits", "wins");
					}
					dealer.setPlayerStatus(PlayerStatus.WINS);
				}
				System.out.println(gameStatus.get(dealer.getPlayerIdentity()));
				cardIterator.remove();
				break;
			}
			if(dealer.getPlayerStatus() == PlayerStatus.STAND || dealer.getPlayerStatus() == PlayerStatus.WINS || dealer.getPlayerStatus() == PlayerStatus.BUSTED) {
				messageFormattingStrategy.printSummary(getPlayers());
				gameEnded = true;
				break;
			} else {
				continue;
			}
		}
	}
	
	private void clear() {
		players.clear();
		gameStatus.clear();
		deck.clear();
	}
	
	public void startGame() {
		System.out.println("Starting game with: " + (players.size() -1) + " player/s");
		System.out.println("Shuffling");
		dealFirstCard();
	}

	public boolean isGameEnded() {
		return gameEnded;
	}

	public Map<Integer, String> getGameStatus() {
		return Collections.unmodifiableMap(gameStatus);
	}

}
