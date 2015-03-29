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

import static com.google.common.base.Preconditions.checkArgument;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PassedGame;
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
public class PassedGameRow extends BaseGameRow {

	/**
	 * @param gameSet
	 * @param context
	 * @param dialog
	 * @param attrs
	 * @param game
	 * @param weight
	 */
	protected PassedGameRow(final Context context, final ProgressDialog dialog, final AttributeSet attrs, final BaseGame game, final float weight, final GameSet gameSet) {
		super(context, dialog, attrs, weight, gameSet);
		checkArgument(game instanceof PassedGame, "Incorrect game type: " + game.getClass());
		this.setOrientation(HORIZONTAL);
		this.game = game;
		this.initializeViews();
		this.setOnLongClickListener(this);
	}

	/**
	 * @param gameSet
	 * @param context
	 * @param dialog
	 * @param game
	 * @param weight
	 */
	protected PassedGameRow(final Context context, final ProgressDialog dialog, final BaseGame game, final float weight, final GameSet gameSet) {
		this(context, dialog, null, game, weight, gameSet);
	}

	/**
	 * @return the game
	 */
	protected PassedGame getGame() {
		return (PassedGame)this.game;
	}
	
	/**
	 * Initializes the views.
	 */
	protected void initializeViews() {
		// game bet label
		this.addView(ScoreCellFactory.buildPassedGameDescription(
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
				playerScore.append("-");
				color = Color.GRAY;					
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