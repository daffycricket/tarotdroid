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
package org.nla.tarotdroid.lib.ui.controls;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot5Game;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class StandardTarot5GameRow extends BaseGameRow {

	/**
	 * @param context
	 * @param dialog
	 * @param attrs
	 * @param game
	 * @param weight
	 * @param gameSet
	 */
	protected StandardTarot5GameRow(final Context context, final ProgressDialog dialog, final AttributeSet attrs, final StandardBaseGame game, final float weight, final GameSet gameSet) {
		super(context, dialog, attrs, weight, gameSet);
		this.game = game;
		this.initializeViews();
		this.setOnLongClickListener(this);
	}

	/**
	 * @param context
	 * @param dialog
	 * @param game
	 * @param weight
	 * @param gameSet
	 */
	protected StandardTarot5GameRow(final Context context, final ProgressDialog dialog, final StandardBaseGame game, final float weight, final GameSet gameSet) {
		this(context, dialog, null, game, weight, gameSet);
	}

	/**
	 * @return the game
	 */
	public StandardTarot5Game getGame() {
		return (StandardTarot5Game)this.game;
	}
	
	/**
	 * Initializes the views.
	 */
	protected void initializeViews() {
		// game bet label
		this.addView(ScoreCellFactory.buildStandardGameDescription(
				this.getContext(), 
				this.getGame().getBet().getBetType(), 
				(int)this.getGame().getDifferentialPoints(), 
				this.game.getIndex()
		));
		
		// each individual player score
		for (Player player : this.getGameSet().getPlayers()) {
			int playerScore = 0;

			// dead player
			if (!game.getPlayers().contains(player)) {
				this.addView(ScoreCellFactory.buildDeadPlayerCell(this.getContext()));
				continue;
			}
			
			// in game player
			else {
				// get score to display, depending on the user settings
				if (AppContext.getApplication().getAppParams().isDisplayGlobalScoresForEachGame()) {
					playerScore = this.getGameSet().getGameSetScores().getIndividualResultsAtGameOfIndex(game.getIndex(), player);
				}
				else {
					playerScore = game.getGameScores().getIndividualResult(player);
				}
				

				
				// called player
				if (
						player == this.getGame().getCalledPlayer() ||			// called player 
						(
							this.getGame().isLeaderAlone() && 					// leading player is called player
							this.getGame().getLeadingPlayer() == player
						)
					) {
					this.addView(ScoreCellFactory.buildCalledPlayerCell(
							this.getContext(), 
							playerScore, 
							this.getGame().getCalledKing().getKingType())
					);
					continue;
				}
				 
				// leading player
				else if (player == this.getGame().getLeadingPlayer()) {
					this.addView(ScoreCellFactory.buildLeaderPlayerCell(
							this.getContext(), 
							playerScore)
					);
					continue;
				}

				// defense player
				else {
					this.addView(ScoreCellFactory.buildDefensePlayerCell(
							this.getContext(), 
							playerScore)
					);
					continue;
				}
			}
		}
	}
}
