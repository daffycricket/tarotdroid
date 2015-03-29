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

import org.nla.tarotdroid.biz.computers.BelgianTarot5GameScoresComputer;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class BelgianTarot5Game extends BelgianBaseGame {
	
	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
	private static final long serialVersionUID = 1475686131106533274L;

	/**
	 * Fourth player.
	 */
	private Player fourth;
	
	/**
	 * Fifth player.
	 */
	private Player fifth;

	/**
	 * @return the fourth
	 */
	public Player getFourth() {
		return this.fourth;
	}

	/**
	 * @param fourth the fourth to set
	 */
	public void setFourth(final Player fourth) {
		this.fourth = fourth;
	}

	/**
	 * @return the fifth
	 */
	public Player getFifth() {
		return this.fifth;
	}

	/**
	 * @param fifth the fifth to set
	 */
	public void setFifth(final Player fifth) {
		this.fifth = fifth;
	}
	
	/**
	 * Computes and returns the game scores, if they haven't yet been computed. Otherwise, returns the scores previously computed.
	 * @return the game scores.
	 */
	public GameScores getGameScores() {
		BelgianTarot5GameScoresComputer gameScoresComputer = new BelgianTarot5GameScoresComputer(this, this.gameSetParameters);
		gameScoresComputer.computeScore();
		return gameScoresComputer.getGameScores();
	}
}
