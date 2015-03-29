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
package org.nla.tarotdroid.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class RandomHelper {

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
	 * 
	 */
	private PlayerList list;
	
	/**
	 * Uninstantiable. 
	 */
	public RandomHelper(final PlayerList list) {
		this.list = list;
	}

	/**
	 * Returns a random player from the player list.
	 * @return a random player from the player list.
	 */
	public PersistableBusinessObject getRandomPlayerFromList() {
		return this.list.get(RandomHelper.RANDOM.nextInt(this.list.size()) + 1);
	}

	/**
	 * Returns a random player from the player list, different from the specified player.
	 * @return a random player from the player list, different from the specified player.
	 */
	public PersistableBusinessObject getRandomPlayerFromList(final Player playerToExclude) {
		if (playerToExclude != null) {
			List<Player> playersToExclude = new ArrayList<Player>(1);
			playersToExclude.add(playerToExclude);
			return this.getRandomPlayerFromList(playersToExclude);
		}
		else {
			return this.getRandomPlayerFromList();
		}
	}

	/**
	 * Returns a random player from the player list, different from all specified players.
	 * @return a random player from the player list, different from all specified players.
	 */
	public PersistableBusinessObject getRandomPlayerFromList(final List<Player> playersToExclude) {
		PersistableBusinessObject randomPlayer = null;
		while (randomPlayer == null || playersToExclude.contains(randomPlayer)) {
			randomPlayer = this.getRandomPlayerFromList();
		}
		return randomPlayer;
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
