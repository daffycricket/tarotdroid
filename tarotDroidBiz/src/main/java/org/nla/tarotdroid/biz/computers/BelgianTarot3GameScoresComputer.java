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

import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.GameSetParameters;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class BelgianTarot3GameScoresComputer extends BaseGameScoresComputer {
	
	/**
	 * The base step points used to compute the score.
	 */
	private int baseStepPoints;
	
	/**
	 * The game on which to base the calculations. 
	 */
	private BelgianTarot3Game game;
	
	/**
	 * 
	 * @param game
	 * @param gameSetParameters
	 */
	public BelgianTarot3GameScoresComputer(final BelgianTarot3Game game, final GameSetParameters gameSetParameters) {
		super();
		if (gameSetParameters == null) {
			throw new IllegalArgumentException("gameSetParameters=" + gameSetParameters);
		}
		if (game == null) {
			throw new IllegalArgumentException("game=" + game);
		}
		this.game = game;
		this.baseStepPoints = gameSetParameters.getBelgianBaseStepPoints();
	}
	
	/**
	 * 
	 * @param game
	 */
	public BelgianTarot3GameScoresComputer(final BelgianTarot3Game game) {
		this(game, new GameSetParameters());
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.computers.BaseGameScoresComputer#computeScore()
	 */
	@Override
	public void computeScore() {
		if (this.game.getFirst() == null) {
			throw new RuntimeException("First player is not set."); 
		}
		if (this.game.getSecond() == null) {
			throw new RuntimeException("Second player is not set."); 
		}
		if (this.game.getThird() == null) {
			throw new RuntimeException("Third player is not set."); 
		}
		
		// first player gets base points
		// second player gets 0 points
		// third player gets -base points
		this.scores.addScore(this.game.getFirst(), this.baseStepPoints);	
		this.scores.addScore(this.game.getSecond(), 0);
		this.scores.addScore(this.game.getThird(), -this.baseStepPoints);
	}
}
