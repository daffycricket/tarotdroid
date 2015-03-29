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
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.constants.UIConstants;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public final class ScoreCellFactory {
	
    /**
     * Default constructor.
     */
    private ScoreCellFactory() {        
    }
    
	/**
	 *Returns a View describing a standard game.
	 * @param context
	 * @param bet
	 * @param points
	 * @param gameIndex
	 * @return a View describing a standard game.
	 */
	protected static View buildStandardGameDescription(final Context context, final BetType bet, final int points, final int gameIndex) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
		
		txtBet.setText(
				String.format(
						context.getResources().getString(R.string.lblStandardGameSynthesis), 
						Integer.toString(gameIndex),
						UIHelper.getShortBetTranslation(bet),
						(points >= 0 ? "+" + points : Integer.toString(points))
				)
		);
		return txtBet;
	}
	
	/**
	 * Returns a View describing a belgian game.
	 * @param context
	 * @param bet
	 * @param points
	 * @param gameIndex
	 * @return a View describing a belgian game.
	 */
	protected static View buildBelgianGameDescription(final Context context, final int gameIndex) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
		txtBet.setText(
				String.format(
						context.getResources().getString(R.string.lblBelgianGameSynthesis), 
						Integer.toString(gameIndex),
						context.getResources().getString(R.string.belgeDescription)
				)
		);
		
		return txtBet;
	}
	
	/**
	 * Returns a View describing the next game.
	 * @param context
	 * @return a View describing the next game.
	 */
	protected static View buildNextGameDescription(final Context context) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
		txtBet.setText("Next");
		
		return txtBet;
	}
	
	/**
	 * Returns a View describing the penalty.
	 * @param context
	 * @return a View describing the penalty.
	 */
	protected static View buildPenaltyGameDescription(final Context context, final int gameIndex) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
//		txtBet.setText(context.getResources().getString(R.string.penaltyDescription));
		txtBet.setText(
			String.format(
				context.getResources().getString(R.string.lblPenaltyGameSynthesis), 
				Integer.toString(gameIndex),
				context.getResources().getString(R.string.shortPenaltyDescription)
			)
		);
		
		return txtBet;
	}
	
	/**
	 * Returns a View describing the passed game.
	 * @param context
	 * @return a View describing the passed game.
	 */
	protected static View buildPassedGameDescription(final Context context, final int gameIndex) {
		TextView txtBet = new TextView(context);
		txtBet.setGravity(Gravity.LEFT);
		txtBet.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtBet.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtBet.setBackgroundColor(Color.TRANSPARENT);
		txtBet.setSingleLine();
		txtBet.setTypeface(null, Typeface.BOLD);
		txtBet.setTextColor(Color.WHITE);
		txtBet.setEllipsize(TruncateAt.END);
//		txtBet.setText(context.getResources().getString(R.string.passDescription));
		txtBet.setText(
			String.format(
				context.getResources().getString(R.string.lblPassedGameSynthesis), 
				Integer.toString(gameIndex),
				context.getResources().getString(R.string.passDescription)
			)
		);
		
		return txtBet;
	}
	
	/**
	 * Builds and returns a View for a leader player.
	 * @param points the points to display.
	 * @return a View for a leader player.
	 */
	protected static View buildLeaderPlayerCell(final Context context, final int points) {
		final int leadingPlayerColor = context.getResources().getColor(R.color.LeadingPlayer);
		
		// text view
		TextView txtIndividualGameScore = ScoreCellFactory.buildScoreCellView(context, leadingPlayerColor, Integer.toString(points), UIConstants.TXT_SCORE_LAYOUT_PARAMS);

		// image view
		ImageView imgLeader = new ImageView(context);
		imgLeader.setBackgroundColor(leadingPlayerColor);
		imgLeader.setLayoutParams(UIConstants.CALLED_COLOR_LAYOUT_PARAMS);
		imgLeader.setScaleType(ImageView.ScaleType.FIT_XY);
		imgLeader.setImageResource(R.drawable.icon_leader);

		// horizontal layout
		LinearLayout layoutIndividualGameScore = ScoreCellFactory.buildLayout(context, leadingPlayerColor);
		layoutIndividualGameScore.addView(txtIndividualGameScore);
		layoutIndividualGameScore.addView(imgLeader);
		return layoutIndividualGameScore;
	}
	
	/**
	 * Builds and returns a View for a defense player.
	 * @param points
	 * @param isSuccessful
	 * @return a View for a leader player.
	 */
	protected static View buildDefensePlayerCell(final Context context, final int points) {
		return ScoreCellFactory.buildScoreCellView(context, context.getResources().getColor(R.color.DefensePlayer), Integer.toString(points));
	}
	
	/**
	 * Builds and returns a View for a called player.
	 * @param points
	 * @param calledColor
	 * @return a View for a called player.
	 */
	protected static View buildCalledPlayerCell(final Context context, final int points, final KingType calledKing) {
		final int leadingPlayerColor = context.getResources().getColor(R.color.LeadingPlayer);
		
		// text view
		TextView txtIndividualGameScore = ScoreCellFactory.buildScoreCellView(context, leadingPlayerColor, new Integer(points).toString(), UIConstants.TXT_SCORE_LAYOUT_PARAMS);

		// image view
		ImageView imgCalledColor = new ImageView(context);
		imgCalledColor.setBackgroundColor(leadingPlayerColor);
		imgCalledColor.setLayoutParams(UIConstants.CALLED_COLOR_LAYOUT_PARAMS);
		imgCalledColor.setScaleType(ImageView.ScaleType.FIT_XY);
		switch(calledKing) {
		case Clubs:
			imgCalledColor.setImageResource(R.drawable.icon_club);
			break;
		case Diamonds:
			imgCalledColor.setImageResource(R.drawable.icon_diamond_old);
			break;					
		case Hearts:
			imgCalledColor.setImageResource(R.drawable.icon_heart_old);
			break;
		case Spades:
			imgCalledColor.setImageResource(R.drawable.icon_spade);
			break;
		default :
		    break;
	}

		// horizontal layout
		LinearLayout layoutIndividualGameScore = ScoreCellFactory.buildLayout(context, leadingPlayerColor);
		layoutIndividualGameScore.addView(txtIndividualGameScore);
		layoutIndividualGameScore.addView(imgCalledColor);
		return layoutIndividualGameScore;
	}
	
	/**
	 * Builds and returns a View for a dead player.
	 * @param points
	 * @param isSuccessful
	 * @return a View for a leader player.
	 */
	protected static View buildDeadPlayerCell(final Context context) {
		return ScoreCellFactory.buildScoreCellView(context, Color.GRAY, "#");
	}
	
	/**
	 * @param context
	 * @param backGroundColor
	 * @param points
	 * @return
	 */
	private static TextView buildScoreCellView(final Context context, final int backGroundColor, final String points, final LinearLayout.LayoutParams layoutParams) {
		TextView txtIndividualGameScore = new TextView(context);
		txtIndividualGameScore.setGravity(Gravity.CENTER);
		txtIndividualGameScore.setLayoutParams(layoutParams);
		txtIndividualGameScore.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtIndividualGameScore.setBackgroundColor(backGroundColor);
		txtIndividualGameScore.setTextColor(Color.BLACK);
		txtIndividualGameScore.setTypeface(null, Typeface.BOLD);
		txtIndividualGameScore.setText(points);
		txtIndividualGameScore.setSingleLine();
		txtIndividualGameScore.setEllipsize(TruncateAt.END);
		return txtIndividualGameScore;
	}
	
	/**
	 * @param context
	 * @return
	 */
	public static TextView buildEmptyCellView(final Context context) {
		TextView txtIndividualGameScore = new TextView(context);
		txtIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtIndividualGameScore.setBackgroundColor(Color.TRANSPARENT);
		return txtIndividualGameScore;
	}
	
	/**
	 * @param context
	 * @return
	 */
	public static TextView buildNextDealerCellView(final Context context) {
		TextView txtIndividualGameScore = new TextView(context);
		txtIndividualGameScore.setGravity(Gravity.CENTER);
		txtIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		txtIndividualGameScore.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
		txtIndividualGameScore.setBackgroundColor(Color.TRANSPARENT);
		txtIndividualGameScore.setTextColor(Color.WHITE);
		txtIndividualGameScore.setTypeface(null, Typeface.BOLD);
		txtIndividualGameScore.setText("DEALER");
		txtIndividualGameScore.setSingleLine();
		txtIndividualGameScore.setEllipsize(TruncateAt.END);		
		return txtIndividualGameScore;
	}
	
	/**
	 * @param context
	 * @param backGroundColor
	 * @param points
	 * @return
	 */
	private static TextView buildScoreCellView(final Context context, final int backGroundColor, final String points) {
		return ScoreCellFactory.buildScoreCellView(context, backGroundColor, points, UIConstants.TABGAMESET_LAYOUT_PARAMS);
	}
	
	/**
	 * @param context
	 * @param backGroundColor
	 * @return
	 */
	private static LinearLayout buildLayout(final Context context, final int backGroundColor) {
		LinearLayout layoutIndividualGameScore = new LinearLayout(context);
		layoutIndividualGameScore.setOrientation(LinearLayout.HORIZONTAL);
		layoutIndividualGameScore.setGravity(Gravity.CENTER);
		layoutIndividualGameScore.setLayoutParams(UIConstants.TABGAMESET_LAYOUT_PARAMS);
		layoutIndividualGameScore.setBackgroundColor(backGroundColor);
		layoutIndividualGameScore.setWeightSum(1);
		return layoutIndividualGameScore;
	}
}
