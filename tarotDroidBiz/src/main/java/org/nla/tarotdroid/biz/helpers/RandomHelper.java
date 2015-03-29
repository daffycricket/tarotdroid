/*
	This file is part of the Android application TarotDroid.
 	
	TarotDroid is free software: you can redistribute it and/or modify
 	it under the terms of the GNU General Public License as published by
 	the Free Software Foundation, either version 3 of the License, or
 	(at your option) any later version.
 	
 	TarotDroid is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 	GNU General Public License for more details.
 	
 	You should have received a copy of the GNU General Public License
 	along with TarotDroid. If not, see <http://www.gnu.org/licenses/>.
*/
package org.nla.tarotdroid.biz.helpers;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Random;

import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.enums.GameStyleType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public final class RandomHelper {

	/**
	 * Random object generator.
	 */
	private static final Random RANDOM = new Random();
	
	/**
	 * Bet list.
	 */
	private static final Bet[] BETS = new Bet[]{Bet.GARDE, Bet.GARDESANS, Bet.GARDECONTRE};

	/**
	 * King list.
	 */
	private static final King[] KINGS = new King[]{King.CLUB, King.DIAMOND, King.HEART, King.SPADE };
	
	/**
	 * Player names array.
	 */
	private static String[] playerNames = new String[]{ "Arthur", "NicoL", "NicoR", "Gui", "PM", "Cyril", "FloM", "FloV", "Damien", "Cyril", "Alex", "Carole", "JK", "Mika", "Ludas", "Jimmy", "Vince", "Valentin" }; 
	
	/**
	 * Default constructor. 
	 */
	private RandomHelper() {
	}
	
	public static int nextInt(int maxInt) {
		return RANDOM.nextInt(maxInt);
	}
	
	/**
	 * 
	 * @return
	 */
	public static PlayerList createRandomPlayerList(final int nbPlayers) {
		PlayerList toReturn = new PlayerList();
		
		while (toReturn.size() < nbPlayers) {
			Player player = new Player(playerNames[RandomHelper.RANDOM.nextInt(playerNames.length-1)]);
			if (!toReturn.contains(player)) {
				toReturn.add(player);
			}
		}			 
		
		return toReturn;
	}
	
	
	/**
	 * Creates a list of random game sets.
	 * @return
	 */
	public static List<GameSet> createRandomGameSets(int gameSetCount) {
		List<GameSet> gameSets = newArrayList();
		for (int i = 0; i < gameSetCount; ++i) {
			switch(RandomHelper.RANDOM.nextInt(3)) {
				case 0:
					gameSets.add(createRandomStandardTarot3GameSet());
					break;
				case 1:
					gameSets.add(createRandomStandardTarot4GameSet());
					break;
				case 2:
				default:
					gameSets.add(createRandomStandardTarot5GameSet());
					break;
			}
		}
		return gameSets;
	}
	
	
	/**
	 * Creates a random game set. 
	 * @return
	 */
	public static GameSet createRandomStandardTarotGameSet() {
		switch(RandomHelper.RANDOM.nextInt(3)) {
			case 0:
				return createRandomStandardTarot3GameSet();
			case 1:
				return createRandomStandardTarot4GameSet();
			case 2:
			default:
				return createRandomStandardTarot5GameSet();
		}
	}
	
	/**
	 * Creates a random game, depending on the game set type.
	 * @param gameSet
	 * @return
	 */
	public static StandardBaseGame createRandomStandardTarotGame(final GameSet gameSet) {
		switch(gameSet.getGameStyleType()) {
			case Tarot3:
				return createRandomStandardTarot3Game(gameSet.getPlayers());
			case Tarot4:
				return createRandomStandardTarot4Game(gameSet.getPlayers());
			case Tarot5:
			default:
				return createRandomStandardTarot5Game(gameSet.getPlayers());				
		}
	}

	/**
	 * Creates a 3 player-style game with random players.
	 * @return
	 */
	public static GameSet createRandomStandardTarot3GameSet() { 
		GameSet gameSet = new GameSet();
		gameSet.setGameStyleType(GameStyleType.Tarot3);
		gameSet.setGameSetParameters(new GameSetParameters());
		PlayerList players = createRandomPlayerList(3);
		gameSet.setPlayers(players);
		return gameSet;
	}

	/**
	 * Creates a 3 player-style game with specified players.
	 * @return
	 */
	public static GameSet createRandomStandardTarot3GameSet(final PlayerList playerList) { 
		GameSet gameSet = new GameSet();
		gameSet.setGameStyleType(GameStyleType.Tarot3);
		gameSet.setGameSetParameters(new GameSetParameters());
		gameSet.setPlayers(playerList);
		return gameSet;
	}
	
	/**
	 * Creates a 4 player-style game with random players.
	 * @return
	 */
	public static GameSet createRandomStandardTarot4GameSet() { 
		GameSet gameSet = new GameSet();
		gameSet.setGameStyleType(GameStyleType.Tarot4);
		gameSet.setGameSetParameters(new GameSetParameters());
		PlayerList players = createRandomPlayerList(4);
		gameSet.setPlayers(players);
		return gameSet;
	}
	
	/**
	 * Creates a 4 player-style game with specified players.
	 * @return
	 */
	public static GameSet createRandomStandardTarot4GameSet(final PlayerList playerList) { 
		GameSet gameSet = new GameSet();
		gameSet.setGameStyleType(GameStyleType.Tarot4);
		gameSet.setGameSetParameters(new GameSetParameters());
		gameSet.setPlayers(playerList);
		return gameSet;
	}

	/**
	 * Creates a 5 player-style game with random players.
	 * @return
	 */
	public static GameSet createRandomStandardTarot5GameSet() { 
		GameSet gameSet = new GameSet();
		gameSet.setGameStyleType(GameStyleType.Tarot5);
		gameSet.setGameSetParameters(new GameSetParameters());
		PlayerList players = createRandomPlayerList(5);
		gameSet.setPlayers(players);
		return gameSet;
	}
	
	/**
	 * Creates a 5 player-style game with specified  players.
	 * @return
	 */
	public static GameSet createRandomStandardTarot5GameSet(final PlayerList playerList) { 
		GameSet gameSet = new GameSet();
		gameSet.setGameStyleType(GameStyleType.Tarot5);
		gameSet.setGameSetParameters(new GameSetParameters());
		gameSet.setPlayers(playerList);
		return gameSet;
	}

	/**
	 * Creates a random 3 player-style standard game.
	 * @param players
	 * @return
	 */
	public static StandardTarot3Game createRandomStandardTarot3Game(final PlayerList players) {
		StandardTarot3Game toReturn = new StandardTarot3Game();
		toReturn.setPlayers(players);
		toReturn.setBet(getRandomBet());
		toReturn.setLeadingPlayer(getRandomPlayerFromList(players));
		toReturn.setNumberOfOudlers(getRandomOudlerCount());
		toReturn.setPoints(getRandomPoints());
		return toReturn;
	}
	
	/**
	 * Creates a random penalty game.
	 * @param players
	 * @return
	 */
	public static PenaltyGame createRandomPenaltyGame(final PlayerList players) {
		PenaltyGame toReturn = new PenaltyGame();
		toReturn.setPlayers(players);
		toReturn.setPenaltedPlayer(getRandomPlayerFromList(players));
		toReturn.setPenaltyPoints((players.getPlayers().size() - 1) * 100);
		return toReturn;
	}

	/**
	 * Creates a random passed game.
	 * @param players
	 * @return
	 */
	public static PassedGame createRandomPassedGame(final PlayerList players) {
		PassedGame toReturn = new PassedGame();
		toReturn.setPlayers(players);
		return toReturn;
	}
	
	/**
	 * Creates a random 3 player-style game.
	 * @param players
	 * @return
	 */
	public static StandardTarot4Game createRandomStandardTarot4Game(final PlayerList players) {
		StandardTarot4Game toReturn = new StandardTarot4Game();
		toReturn.setPlayers(players);
		toReturn.setBet(getRandomBet());
		toReturn.setLeadingPlayer(getRandomPlayerFromList(players));
		toReturn.setNumberOfOudlers(getRandomOudlerCount());
		toReturn.setPoints(getRandomPoints());
		return toReturn;
	}
	
	/**
	 * Creates a random 3 player-style game.
	 * @param players
	 * @return
	 */
	public static StandardTarot5Game createRandomStandardTarot5Game(final PlayerList players) {
		StandardTarot5Game toReturn = new StandardTarot5Game();
		toReturn.setPlayers(players);
		toReturn.setBet(getRandomBet());
		toReturn.setLeadingPlayer(getRandomPlayerFromList(players));
		toReturn.setCalledPlayer(getRandomPlayerFromList(players));
		toReturn.setCalledKing(getRandomKing());
		toReturn.setNumberOfOudlers(getRandomOudlerCount());
		toReturn.setPoints(getRandomPoints());
		return toReturn;
	}
	
	/**
	 * Returns a random player from the player list.
	 * @return a random player from the player list.
	 */
	public static Player getRandomPlayerFromList(final PlayerList players) {
		return players.get(RandomHelper.RANDOM.nextInt(players.size()) + 1);
	}
	
	/**
	 * Returns a random Bet.
	 * @return a random Bet.
	 */
	public static Bet getRandomBet() {
		return RandomHelper.BETS[RandomHelper.RANDOM.nextInt(RandomHelper.BETS.length)];
	}

	/**
	 * Returns a random King.
	 * @return a random King.
	 */
	public static King getRandomKing() {
		return RandomHelper.KINGS[RandomHelper.RANDOM.nextInt(RandomHelper.KINGS.length)];
	}

	/**
	 * Returns a random integer btw 0 and 91.
	 * @return a random integer btw 0 and 91.
	 */
	public static int getRandomPoints() {
		return RandomHelper.RANDOM.nextInt(91);
	}
	
	/**
	 * Returns a random integer btw 0 and 91.
	 * @return a random integer btw 0 and 91.
	 */
	public static int getRandomOudlerCount() {
		return RandomHelper.RANDOM.nextInt(3) + 1;
	}
}
