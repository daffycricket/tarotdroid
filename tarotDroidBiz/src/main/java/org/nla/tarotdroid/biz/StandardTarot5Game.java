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

import org.nla.tarotdroid.biz.computers.StandardTarot5GameScoresComputer;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *	A tarot game.
 */
public class StandardTarot5Game extends StandardBaseGame {
	
	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
    private static final long serialVersionUID = -49778392893641985L;

    /**
	 * The called player.
	 */
	private Player calledPlayer;

	/**
	 * The color called by the leader player.
	 */
	@CloudField(targetType="String")
	private King calledKing;
	
	/**
	 * Default Constructor.	
	 */
	public StandardTarot5Game() {
		super();
	}

	/**
	 * Returns the called player.
	 * @return the called player.
	 */
	public Player getCalledPlayer() {
		return this.calledPlayer;
	}

	/**
	 * Sets the called player.
	 * @param calledPlayer the called player to set.
	 */
	public void setCalledPlayer(final Player calledPlayer) {
//		if (calledPlayer != null && !this.players.contains(calledPlayer)) {
//			throw new IllegalArgumentException(calledPlayer + " not playing");
//		}
		this.calledPlayer = calledPlayer;
	}

	/**
	 * Returns the called color.
	 * @return the called color.
	 */
	public King getCalledKing() {
		return this.calledKing;
	}

	/**
	 * Sets the called color.
	 * @param calledKing the called color to set.
	 */
	public void setCalledKing(final King calledKing) {
		if (calledKing == null) {
			throw new IllegalArgumentException("calledKing is null");
		}
		this.calledKing = calledKing;
	}

	/**
	 * Indicates whether the leading player called himself.
	 * @return true if the leading player called himself; false otherwise.
	 */
	public boolean isLeaderAlone() {
		return this.calledPlayer == null || this.calledPlayer == this.leadingPlayer;
	}

	/**
	 * Returns a list of players corresponding to the defense team.
	 * @return a list of players corresponding to the defense team.
	 */
	@Override
	public PlayerList getDefenseTeam() {
		PlayerList defenseTeam = new PlayerList();
		for (Player player : this.players) {
			if (player != this.leadingPlayer && player != this.calledPlayer) {
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
		if (players.size() != 5) {
			throw new IllegalArgumentException("players must be exactly five =" + players.size());
		}
		super.setPlayers(players);
	}
	
	/**
	 * Computes and returns the game scores, if they haven't yet been computed. Otherwise, returns the scores previously computed.
	 * @return the game scores.
	 */
	@Override
	public GameScores getGameScores() {
		StandardTarot5GameScoresComputer gameScoresComputer = new StandardTarot5GameScoresComputer(this, this.gameSetParameters);
		gameScoresComputer.computeScore();
		return gameScoresComputer.getGameScores();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer toReturn = new StringBuffer();
		toReturn.append("Index=");
		toReturn.append(this.getIndex());
		toReturn.append("|");
		toReturn.append("LeaderPlayer=");
		toReturn.append(this.getLeadingPlayer());
		toReturn.append("|");
		toReturn.append("Bet=");
		toReturn.append(this.getBet());
		toReturn.append("|");
		toReturn.append("CalledPlayer=");
		toReturn.append(this.getCalledPlayer());
		toReturn.append("|");
		toReturn.append("CalledKing=");
		toReturn.append(this.getCalledKing());
		toReturn.append("|");
		toReturn.append("Oudlers=");
		toReturn.append(this.getNumberOfOudlers());
		toReturn.append("|");
		toReturn.append("AttackPoints=");
		toReturn.append(this.getPoints());
		toReturn.append("|");
		toReturn.append("IsWon=");
		toReturn.append(this.isGameWon());
		return toReturn.toString();
	}
}