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

import org.nla.tarotdroid.biz.computers.BaseGameScoresComputer;
import org.nla.tarotdroid.biz.computers.StandardTarot4GameScoresComputer;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *	A tarot game.
 */
public class StandardTarot4Game extends StandardBaseGame {
		
	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
    private static final long serialVersionUID = -4148414733574923549L;

    /**
	 * Default Constructor.	
	 */
	public StandardTarot4Game() {
		super();
	}

	/**
	 * Returns a list of players corresponding to the defense team.
	 * @return a list of players corresponding to the defense team.
	 */
	@Override
	public PlayerList getDefenseTeam() {
		PlayerList defenseTeam = new PlayerList();
		for (Player player : this.players) {
			if (player != this.leadingPlayer) {
				defenseTeam.add(player);
			}
		}
		return defenseTeam;
	}
	
	/**
	 * Sets the list of players in the game.
	 * @param players the list of players in the game to set.
	 */
	@Override
	public void setPlayers(final PlayerList players) {
		if (players.size() != 4) {
			throw new IllegalArgumentException("players must be exactly four =" + players.size());
		}
		super.setPlayers(players);
	}
	
	/**
	 * Computes and returns the game scores, if they haven't yet been computed. Otherwise, returns the scores previously computed.
	 * @return the game scores.
	 */
	@Override
	public GameScores getGameScores() {
		BaseGameScoresComputer gameScoresComputer = new StandardTarot4GameScoresComputer(this, this.gameSetParameters);
		gameScoresComputer.computeScore();
		return gameScoresComputer.getGameScores();
	}
}