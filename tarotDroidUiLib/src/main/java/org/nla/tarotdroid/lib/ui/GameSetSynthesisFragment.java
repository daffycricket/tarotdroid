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
package org.nla.tarotdroid.lib.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.MapPlayersScores;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.constants.RequestCodes;
import org.nla.tarotdroid.lib.ui.constants.UIConstants;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.widget.ProfilePictureView;

/**
 * Fragment that shows a synthesis of a fragment.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class GameSetSynthesisFragment extends SherlockFragment {

    /**
     * Map between the player rank and their corresponding LinearLayout on the synthesis view.
     */
    private Map<Integer, LinearLayout> statsRows;
    
	/**
     * Player stats layout.
     */
    private LinearLayout playerStatsLayout;
    
//	/**
//	 * The current game set.
//	 */
//	private GameSet gameSet;
	
    /**
     * Creates a new instance of GameSetSynthesisFragment.
     * @param gameSetId
     * @return
     */
	public static GameSetSynthesisFragment newInstance(/*GameSet gameSet*/) {
//		checkArgument(gameSet != null, "gameSet is null");
		GameSetSynthesisFragment fragment = new GameSetSynthesisFragment();
		
//		Bundle args = new Bundle();
//		if (!gameSet.isPersisted()) {
//			//args.putString(ActivityParams.PARAM_GAMESET_SERIALIZED, UIHelper.serializeGameSet(gameSet));
//			args.putSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED, gameSet);
//		}
//		else {
//			args.putLong(ActivityParams.PARAM_GAMESET_ID, gameSet.getId());
//		}
//		fragment.setArguments(args);
		
		return fragment;
	}
	
	/**
	 * Returns the game set on which activity has to work.
	 * @return
	 */
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}
	
//	/**
//	 * Sets the internal game set.
//	 * @param gameSet
//	 */
//	public void setGameSet(GameSet gameSet) {
//		this.gameSet = gameSet;
//	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		try {
			// check params
//    		checkArgument(this.getArguments().containsKey(ActivityParams.PARAM_GAMESET_ID), "Game set id must be provided");
//    		this.gameSet = AppContext.getApplication().getDalService().getGameSetById(this.getArguments().getLong(ActivityParams.PARAM_GAMESET_ID));
//			Bundle args = this.getArguments();
//			if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
//				this.gameSet = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
//			}
//			else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
//				//this.gameSet = UIHelper.deserializeGameSet(args.getString(ActivityParams.PARAM_GAMESET_SERIALIZED));
//				this.gameSet = (GameSet)args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
//			}
//			else {
//				throw new IllegalArgumentException("Game set id or serialized game set must be provided");
//			}


			GameStyleType gameStyleType = this.getGameSet().getGameStyleType();
			
			if (gameStyleType == GameStyleType.Tarot5) {
				view = inflater.inflate(R.layout.tablegameset_synthesis5, null);
			}
			else {
				view = inflater.inflate(R.layout.tablegameset_synthesis, null);
			}
			
			if (this.playerStatsLayout == null) {
			    this.playerStatsLayout = (LinearLayout)view.findViewById(R.id.statsLayout);
			}
			
			this.statsRows = new HashMap<Integer, LinearLayout>();        
			this.statsRows.put(1, (LinearLayout)this.playerStatsLayout.findViewById(R.id.rowStatsPlayer1));
			this.statsRows.put(2, (LinearLayout)this.playerStatsLayout.findViewById(R.id.rowStatsPlayer2));
			this.statsRows.put(3, (LinearLayout)this.playerStatsLayout.findViewById(R.id.rowStatsPlayer3));
			this.statsRows.put(4, (LinearLayout)this.playerStatsLayout.findViewById(R.id.rowStatsPlayer4));
			this.statsRows.put(5, (LinearLayout)this.playerStatsLayout.findViewById(R.id.rowStatsPlayer5));
			if (gameStyleType == GameStyleType.Tarot5) {
			    this.statsRows.put(6, (LinearLayout)this.playerStatsLayout.findViewById(R.id.rowStatsPlayer6));
			}
			
			
			// remove useless lines
			int nbPlayers = this.getGameSet().getPlayers().size();
			
			// tarot à 5
			if (gameStyleType == GameStyleType.Tarot5) {
			    
				// 5 players
				if (nbPlayers == 5) {
			        this.playerStatsLayout.removeView(this.playerStatsLayout.findViewById(R.id.imgSeparator6));
			        this.playerStatsLayout.removeView(this.playerStatsLayout.findViewById(R.id.rowStatsPlayer6));
			        this.playerStatsLayout.setWeightSum(5);
			    }
			    
				// 5 players + 1 additionnal player
				else if (nbPlayers == 6) {
			    }
			}
			
			// tarot à 3 ou 4
			else {
				
				// 3 players
			    if (nbPlayers == 3) {
			        this.playerStatsLayout.removeView(this.playerStatsLayout.findViewById(R.id.imgSeparator5));
			        this.playerStatsLayout.removeView(this.playerStatsLayout.findViewById(R.id.rowStatsPlayer5));
			        this.playerStatsLayout.removeView(this.playerStatsLayout.findViewById(R.id.imgSeparator4));
			        this.playerStatsLayout.removeView(this.playerStatsLayout.findViewById(R.id.rowStatsPlayer4));
			        this.playerStatsLayout.setWeightSum(3);
			    }
			    
			    // 4 players
			    else if (nbPlayers == 4) {
			        this.playerStatsLayout.removeView(this.playerStatsLayout.findViewById(R.id.imgSeparator5));
			        this.playerStatsLayout.removeView(this.playerStatsLayout.findViewById(R.id.rowStatsPlayer5));
			        this.playerStatsLayout.setWeightSum(4);
			    }
			    
			    // 4 players + 1 additionnal player
			    else if (nbPlayers == 5) {
			         // do nothing
			    }
			}
		}
		catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.tabGameSetActivityError, e, this.getActivity());
		}
        
        return view;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		this.refreshPlayerStatsRows();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
    public void onSaveInstanceState(Bundle outState) {
    	// HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site) 
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }

	/**
     * Refreshes the stats rows on the synthesis view.
     */
    protected void refreshPlayerStatsRows() {
        // sort players
        List<Player> sortedPlayers = this.getGameSet().getPlayers().getPlayers();
        Collections.sort(sortedPlayers, new PlayerScoreComparator());
        
        // get general data sources
        IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(this.getGameSet(), "guava");
        MapPlayersScores lastScores = this.getGameSet().getGameSetScores().getResultsAtLastGame();
        Map<Player, Integer> leadingCount = gameSetStatisticsComputer.getLeadingCount();
        Map<Player, Integer> leadingSuccesses = gameSetStatisticsComputer.getLeadingSuccessCount();
        Map<Player, Integer> calledCount = gameSetStatisticsComputer.getCalledCount();
        int minScoreEver = gameSetStatisticsComputer.getMinScoreEver();
        int maxScoreEver = gameSetStatisticsComputer.getMaxScoreEver();
        
        // format statistics lines
        for (int rank = 0; rank < sortedPlayers.size(); ++rank) {
            // get player specific data sources
            Player player = sortedPlayers.get(rank);
            int lastScore = lastScores == null ? 0 : lastScores.get(player);
            int successfulLeadingGamesCount = leadingSuccesses.get(player) == null ? 0 : leadingSuccesses.get(player).intValue();
            int leadingGamesCount = leadingCount.get(player) == null ? 0 : leadingCount.get(player);
            int successRate = (int)(((double)successfulLeadingGamesCount) / ((double)leadingGamesCount) * 100.0);
            int minScoreForPlayer = gameSetStatisticsComputer.getMinScoreEverForPlayer(player);
            int maxScoreForPlayer = gameSetStatisticsComputer.getMaxScoreEverForPlayer(player);
            
            // get line widgets
            LinearLayout statRow = this.statsRows.get(rank + 1);
            //TextView statPlayerName = (TextView)statRow.findViewById(R.id.statPlayerName);
            TextView statScore = (TextView)statRow.findViewById(R.id.statScore);
            TextView statLeadingGamesCount = (TextView)statRow.findViewById(R.id.statLeadingGamesCount);
            TextView statSuccessfulGamesCount = (TextView)statRow.findViewById(R.id.statSuccessfulGamesCount);
            TextView statMinScore = (TextView)statRow.findViewById(R.id.statMinScore);
            TextView statMaxScore = (TextView)statRow.findViewById(R.id.statMaxScore);
            
            
//    		Bitmap playerImage = null;
//    		if (player.getPictureUri() != null && !player.getPictureUri().equals("")) {
//    			playerImage = UIHelper.getPlayerImage(this.getActivity(), player);
//    		}
//    		
//            
//            // assign values to widgets
//            if (player.getFacebookId() != null) {
//    			// player facebook image
//    			ProfilePictureView pictureView = new ProfilePictureView(this.getActivity());
//    			pictureView.setProfileId(player.getFacebookId());
//    			pictureView.setPresetSize(ProfilePictureView.SMALL);
//    			//pictureView.setOnClickListener(playerClickListener);
//    			pictureView.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
//    			statRow.removeViewAt(0);
//            	statRow.addView(pictureView, 0);
//            }
//            else {
//            	// WARNING: The properties below reference the style ScoreTextStyle, but we can't set a style at runtime.
//            	TextView playerName = new TextView(this.getActivity());
//            	playerName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));
//            	playerName.setGravity(Gravity.CENTER);
//            	playerName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
//            	playerName.setEllipsize(TruncateAt.END);
//            	playerName.setSingleLine();
//            	playerName.setTextColor(Color.WHITE);
//            	playerName.setTypeface(null, Typeface.BOLD);
//            	playerName.setText(player.getName());
//            	//playerName.setBackgroundResource(R.drawable.border_player_white);
//            	statRow.removeViewAt(0);
//            	statRow.addView(playerName, 0);
//            }
            
            OnClickListener playerClickListener = new PlayerClickListener(player);
            
    		// facebook picture
    		if (player.getFacebookId() != null) {
    			ProfilePictureView pictureView = new ProfilePictureView(this.getActivity());
    			pictureView.setProfileId(player.getFacebookId());
    			pictureView.setPresetSize(ProfilePictureView.SMALL);
    			pictureView.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);

	    		pictureView.setOnClickListener(playerClickListener);
	    		//this.addView(pictureView);
            	statRow.removeViewAt(0);
            	statRow.addView(pictureView, 0);
    		}
    		
    		// contact picture
    		else if (player.getPictureUri() != null && player.getPictureUri().toString().contains("content://com.android.contacts/contacts")) {
    			Bitmap playerImage = UIHelper.getContactPicture(this.getActivity(), Uri.parse(player.getPictureUri()).getLastPathSegment());
	    		ImageView imgPlayer = new ImageView(this.getActivity());
	    		imgPlayer.setImageBitmap(playerImage);
	    		imgPlayer.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
	    		
	    		imgPlayer.setOnClickListener(playerClickListener);
	    		//this.addView(imgPlayer);
            	statRow.removeViewAt(0);
            	statRow.addView(imgPlayer, 0);
    		}
    		
    		// no picture, only name
    		else {
	    		TextView txtPlayer = new TextView(this.getActivity());
	    		txtPlayer.setText(player.getName());
	    		txtPlayer.setGravity(Gravity.CENTER);
	    		txtPlayer.setLayoutParams(UIConstants.PLAYERS_LAYOUT_PARAMS);
	    		txtPlayer.setMinWidth(UIConstants.PLAYER_VIEW_WIDTH);
	    		txtPlayer.setHeight(UIConstants.PLAYER_VIEW_HEIGHT);
	    		txtPlayer.setBackgroundColor(Color.TRANSPARENT);
	    		txtPlayer.setTypeface(null, Typeface.BOLD);
	    		txtPlayer.setTextColor(Color.WHITE);
	    		txtPlayer.setSingleLine();
	    		txtPlayer.setEllipsize(TruncateAt.END);
	    		
	    		txtPlayer.setOnClickListener(playerClickListener);
	    		//this.addView(txtPlayer);
            	statRow.removeViewAt(0);
            	statRow.addView(txtPlayer, 0);
    		}
            
            
            statScore.setText(Integer.toString(lastScore));
            statLeadingGamesCount.setText(Integer.toString(leadingGamesCount));
            statSuccessfulGamesCount.setText(Integer.toString(successfulLeadingGamesCount) + " (" + Integer.toString(successRate) + "%)");
            
            // display called game count if necessary 
            if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
                TextView statCalledGamesCount = (TextView)statRow.findViewById(R.id.statCalledGamesCount);
                statCalledGamesCount.setText(Integer.toString(calledCount.get(player) == null ? 0 : calledCount.get(player)));
            }
            
            statMinScore.setText(Integer.toString(minScoreForPlayer));
            statMaxScore.setText(Integer.toString(maxScoreForPlayer));
            statMinScore.setTextColor(Color.WHITE);
            statMaxScore.setTextColor(Color.WHITE);
            
            // color min score if lowest
            if (minScoreEver == minScoreForPlayer) {
                statMinScore.setTextColor(Color.YELLOW);
            }
            
            // color max score if highest
            if (maxScoreEver == maxScoreForPlayer) {
                statMaxScore.setTextColor(Color.GREEN);
            }
        }
    }
    
    /**
     * Comparator of players based upon their scores: player1 < player2 if score of player1 > score of player2. 
     * @author Nicolas LAURENT daffycricket<a>yahoo.fr
     */
    private class PlayerScoreComparator implements Comparator<Player> {

        @Override
        public int compare(final Player arg0, final Player arg1) {
            MapPlayersScores lastScores = getGameSet().getGameSetScores().getResultsAtLastGame();
            if (lastScores == null) {
                return 0;
            }
            else {
                return lastScores.get((PersistableBusinessObject)arg1) - lastScores.get((PersistableBusinessObject)arg0);
            }
        }
    }
    
	/**
	 * Internal player click listener.
	 */
	private class PlayerClickListener implements OnClickListener {

		private Player player;
		
		protected PlayerClickListener(Player player) {
			this.player = player;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(TabGameSetActivity.getInstance(), PlayerStatisticsActivity.class);
			intent.putExtra("player", this.player.getName());
			TabGameSetActivity.getInstance().startActivityForResult(intent, RequestCodes.DISPLAY_PLAYER);
		}
	}
}
