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

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.lib.ui.constants.UIConstants;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class BelgianTarotGameRow extends BaseGameRow {

	/**
	 * @param context
	 * @param dialog
	 * @param attrs
	 * @param game
	 * @param weight
	 * @param gameSet
	 */
	protected BelgianTarotGameRow(final Context context, final ProgressDialog dialog, final AttributeSet attrs, final BaseGame game, final float weight, final GameSet gameSet) {
		super(context, dialog, attrs, weight, gameSet);
		if (!(game instanceof BelgianTarot3Game || game instanceof BelgianTarot4Game || game instanceof BelgianTarot5Game)) {
			throw new IllegalArgumentException("Incorrect game type: " + game.getClass());
		}
		this.setOrientation(HORIZONTAL);
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
	protected BelgianTarotGameRow(final Context context, final ProgressDialog dialog, final BaseGame game, final float weight, final GameSet gameSet) {
		this(context, dialog, null, game, weight, gameSet);
	}

	/**
	 * @return the game
	 */
	protected BaseGame getGame() {
		return this.game;
	}
	
	/**
	 * Initializes the views.
	 */
	protected void initializeViews() {
		// game bet label
		this.addView(ScoreCellFactory.buildBelgianGameDescription(
				this.getContext(), 
				this.game.getIndex()
		));
		
		// each individual player score
		for (Player player : this.getGameSet().getPlayers()) {
			StringBuffer playerScore = new StringBuffer();
			int color;

			// dead player ?
			if (!game.getPlayers().contains(player)) {
				playerScore.append("#");
				color = Color.GRAY;
			}
			
			// in game player
			else {

				// get score to display, depending on the user settings
				String scoreToDisplay = "0";
				// => global scores at the end of the game
				if (AppContext.getApplication().getAppParams().isDisplayGlobalScoresForEachGame()) {
					scoreToDisplay = Integer.toString(this.getGameSet().getGameSetScores().getIndividualResultsAtGameOfIndex(game.getIndex(), player));
				}
				// => individual scores of the game
				else {
					scoreToDisplay = Integer.toString(game.getGameScores().getIndividualResult(player));
				}
				
				playerScore.append(scoreToDisplay);
				color = this.getResources().getColor(R.color.DefensePlayer);
			}
			
			// game score text control creation 
			TextView txtIndividualGameScore = new TextView(this.getContext());
			txtIndividualGameScore.setGravity(Gravity.CENTER);
    		txtIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
    		txtIndividualGameScore.setWidth(UIConstants.PLAYER_VIEW_WIDTH);
    		txtIndividualGameScore.setBackgroundColor(color);
    		txtIndividualGameScore.setTextColor(Color.BLACK);
    		txtIndividualGameScore.setTypeface(null, Typeface.BOLD);
			txtIndividualGameScore.setText(playerScore);
			this.addView(txtIndividualGameScore);
		}
	}
}
