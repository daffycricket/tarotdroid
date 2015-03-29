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
package org.nla.tarotdroid.biz.computers;

import org.nla.tarotdroid.biz.GameScores;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public abstract class BaseGameScoresComputer {
	
	/**
	 * The GameScore into which to store the score of each player.
	 */
	protected GameScores scores;
	
	/**
	 * The base points earned/lost at the end of the game.
	 */
	protected int basePoints;
	
	/**
	 * Default constructor.
	 */
	protected BaseGameScoresComputer() {
		this.scores = new GameScores();
	}
	
	/**
	 * Returns the base points.
	 * @return the base points.
	 */
	public int getBasePoints() {
		return this.basePoints;
	}
	
	/**
	 * Returns the score. To be called only after computerScore().
	 * @return the score.
	 */
	public GameScores getGameScores() {
		return this.scores;
	}
	
	/**
	 * Computes the score.
	 */
	public abstract void computeScore();
}
