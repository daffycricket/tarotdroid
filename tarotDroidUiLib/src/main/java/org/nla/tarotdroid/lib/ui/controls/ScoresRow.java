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

import java.util.HashMap;
import java.util.Map;

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.MapPlayersScores;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.lib.ui.constants.UIConstants;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class ScoresRow extends LinearLayout {
	
	/**
	 * The text view of each player score.
	 */
	private Map<Player, TextView> mapPlayersScoresTextViews;
	
	/**
	 * The current game set.
	 */
	private GameSet gameSet;

	/**
	 * Constructor.
	 * @param context
	 * @param attrs
	 */
	public ScoresRow(final Context context, final AttributeSet attrs, final GameSet gameSet) {
		super(context, attrs);
		this.gameSet = gameSet;
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 8);
		this.setLayoutParams(params);
		this.setOrientation(HORIZONTAL);
		this.setWeightSum(this.gameSet.getPlayers().size() + 1);
		this.mapPlayersScoresTextViews = new HashMap<Player, TextView>();
		this.initializeViews();
	}
	
	/**
	 * Constructor.
	 * TODO Check use of param players
	 * @param context
	 */
	public ScoresRow(final Context context, final GameSet gameSet) {
		this(context, null, gameSet);
	}
	
    /**
     * Initializes graphical views.
     */
	protected void initializeViews() {
		// "Scores" label
		TextView lblScores = new TextView(this.getContext());
		lblScores.setText(R.string.lblScores);
		lblScores.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		lblScores.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
		lblScores.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		lblScores.setBackgroundColor(Color.TRANSPARENT);
		lblScores.setTextColor(Color.WHITE);
		lblScores.setSingleLine();
		lblScores.setEllipsize(TruncateAt.END);
		lblScores.setTypeface(null, Typeface.BOLD);
		this.addView(lblScores);
    	
    	// Individual scores text views
    	for (Player player : this.gameSet.getPlayers()) {
    		// player name
    		TextView txtPlayerScore = new TextView(this.getContext());
    		txtPlayerScore.setText("0");
    		txtPlayerScore.setGravity(Gravity.CENTER);
    		txtPlayerScore.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
    		txtPlayerScore.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
    		txtPlayerScore.setBackgroundColor(Color.GRAY);
    		txtPlayerScore.setTextColor(Color.BLACK);
    		txtPlayerScore.setSingleLine();
    		txtPlayerScore.setEllipsize(TruncateAt.END);
    		txtPlayerScore.setTypeface(null, Typeface.BOLD);
    		this.addView(txtPlayerScore);
    		this.mapPlayersScoresTextViews.put(player, txtPlayerScore);
    	}
	}
	
	/**
	 * Updates player score.
	 * @param mapPlayersScores
	 */
	public void updatePlayerScore(final MapPlayersScores mapPlayersScores) {
		checkArgument(mapPlayersScores != null, "mapPlayersScores is null");

    	// player names and scores 
    	for (PersistableBusinessObject player : this.gameSet.getPlayers()) {
    		// map the player score to his score text view
    		int rankColor = Color.GRAY;
    		switch (mapPlayersScores.getPlayerRank(player)) {
    			case 1:
    				rankColor = this.getResources().getColor(R.color.FirstScore);
    				break;
    			case 2:
    				rankColor = this.getResources().getColor(R.color.SecondScore);
    				break;
    			case 3:
    				rankColor = this.getResources().getColor(R.color.ThirdScore);
    				break;
    			case 4:
    				rankColor = this.getResources().getColor(R.color.FourthScore);
    				break;
    			case 5:
    				rankColor = this.getResources().getColor(R.color.FifthScore);
    				break;
    			case 6:
    				rankColor = this.getResources().getColor(R.color.SixthScore);
    				break;
    			default :
    			    rankColor = Color.WHITE;
    		}
    		this.mapPlayersScoresTextViews.get(player).setBackgroundColor(rankColor);
    		this.mapPlayersScoresTextViews.get(player).setText(Integer.toString(mapPlayersScores.get(player)));
    	}
	}

	/**
	 * Resets all player scores to zero.
	 */
	public void resetAllPlayerScores() {
    	for (PersistableBusinessObject player : this.gameSet.getPlayers()) {
    		int rankColor = Color.GRAY;
    		this.mapPlayersScoresTextViews.get(player).setBackgroundColor(rankColor);
    		this.mapPlayersScoresTextViews.get(player).setText("0");
    	}
	}
}