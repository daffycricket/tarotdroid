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

import static com.google.common.base.Preconditions.checkArgument;

import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class PenaltyGameGameScoresComputer extends BaseGameScoresComputer {
	
	/**
	 * The game on which to base the calculations. 
	 */
	private PenaltyGame game;
	
	/**
	 * 
	 * @param game
	 * @param gameSetParameters
	 */
	public PenaltyGameGameScoresComputer(final PenaltyGame game/*, final GameSetParameters gameSetParameters*/) {
		super();
		checkArgument(game != null, "game is null");
		this.game = game;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.computers.BaseGameScoresComputer#computeScore()
	 */
	@Override
	public void computeScore() {
		checkArgument(this.game.getPenaltedPlayer() != null, "penalted player is not set.");
		
		int nbPlayers = this.game.getPlayers().getPlayers().size() - 1;
		this.scores.addScore(this.game.getPenaltedPlayer(), -1 * this.game.getPenaltyPoints());
		for (Player player : this.game.getPlayers().getPlayers()) {
			if (player != this.game.getPenaltedPlayer()) {
				this.scores.addScore(player, 1 * (this.game.getPenaltyPoints() / nbPlayers));
			}
		}
	}
}
