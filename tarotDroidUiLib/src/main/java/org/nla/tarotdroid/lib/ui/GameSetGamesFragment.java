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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.lib.ui.controls.PlayersRow;
import org.nla.tarotdroid.lib.ui.controls.RowFactory;
import org.nla.tarotdroid.lib.ui.controls.ScoresRow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Framgment that shows the table of games in a game set.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class GameSetGamesFragment extends SherlockFragment {

    /**
     * Tab View.
     */
    private View mViewTabGameSet;
    
	/**
	 * Players layout.
	 */
	private LinearLayout playersLayout;

	/**
	 * Game set layout.
	 */
	private LinearLayout gamesLayout;

	/**
	 * Player scores table row.
	 */
	private ScoresRow playerScoresRow;
	
	/**
	 * Players row.
	 */
	private PlayersRow playersRow;
	
//	/**
//	 * The current game set.
//	 */
//	private GameSet gameSet;
	
	/**
	 * Creates a new instance of GameSetGamesFragment.
	 * @param gameSet
	 * @return A new instance of GameSetGamesFragment.
	 */
	public static GameSetGamesFragment newInstance(/*GameSet gameSet*/) {
//		checkArgument(gameSet != null, "gameSet is null");
		GameSetGamesFragment fragment = new GameSetGamesFragment();
		
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
        	
//        	this.gameSet = this.getGameSet();
			
    		// create views
        	this.mViewTabGameSet = inflater.inflate(R.layout.tablegameset, null);            
            this.playersLayout = (LinearLayout)this.mViewTabGameSet.findViewById(R.id.playersLayout);
            this.gamesLayout = (LinearLayout)this.mViewTabGameSet.findViewById(R.id.gamesLayout);
			this.playerScoresRow = new ScoresRow(this.getActivity(), this.getGameSet());
		}
        catch (Exception e) {
        	AuditHelper.auditError(ErrorTypes.tabGameSetActivityError, e, this.getActivity());
		}
		
		return this.mViewTabGameSet;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		this.refreshPlayersRows();
		this.refreshGameRows();
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
     * Removes and recreates all game rows on the tab view.
     */
    protected void refreshGameRows() {
    	this.gamesLayout.removeAllViews();
    	
    	// display things only if there's at least one game
    	if (this.getGameSet().getGameCount() > 0) {

    		// build an ordered list of views to display 
    		List<View> rowsToDisplay = new ArrayList<View>(this.getGameSet().getGameCount() + 1);
    		for (BaseGame game : this.getGameSet()) {
    			rowsToDisplay.add(RowFactory.buildGameRow(TabGameSetActivity.getInstance(), TabGameSetActivity.getInstance().progressDialog, game, 1, this.getGameSet()));
    		}
//    		// include including next dealer view to list if previous dealer is known
//    		if (AppContext.getApplication().getBizService().getGameSetParameters().isDisplayNextDealer() && AppContext.getApplication().getBizService().getGameSet().getLastGame().getDealer() != null) {
//    			rowsToDisplay.add(RowFactory.buildNextGameRow(this.getActivity(), this.progressDialog, 1));
//    		}
    		
    		// display rows in reverse order ?
    		if (AppContext.getApplication().getAppParams().isDisplayGamesInReverseOrder()) {
    			Collections.reverse(rowsToDisplay);
    		}
    		
    		// display each view in the layout
    		for (View rowToDisplay : rowsToDisplay) {
    			this.gamesLayout.addView(rowToDisplay);
    		}
    	}
    	
    	// always update title and score, in case of deletion for instance
    	this.updatePlayerScores();
    	this.setTitle();
    }
    
    /** 
     * Refreshes the players rows.
     */
    protected void refreshPlayersRows() {
		this.playerScoresRow = new ScoresRow(this.getActivity(), this.getGameSet());
		this.playersRow = this.buildPlayerRow();
        this.playersLayout.removeAllViews();
		this.playersLayout.addView(this.playersRow);
        this.playersLayout.addView(this.playerScoresRow);
    }

    /**
     * Builds a player row depending on the game set 
     * @return
     */
    protected PlayersRow buildPlayerRow() {
    	int gameCount = this.getGameSet().getGameCount();
		
    	// display next dealer only if params say so and there's more than one game
    	PersistableBusinessObject nextDealer = null;
    	if (AppContext.getApplication().getAppParams().isDisplayNextDealer() && gameCount > 0) {
			PersistableBusinessObject formerDealer = this.getGameSet().getLastGame().getDealer();
			if (formerDealer != null) {
				nextDealer = this.getGameSet().getPlayers().getNextPlayer(formerDealer);
			}
		}
		
		return new PlayersRow(this.getActivity(), nextDealer);
    }
    
    /**
     * Updates the player scores.
     */
    protected void updatePlayerScores() {
    	// updates score only if a game has already been played 
    	if (this.getGameSet().getGameSetScores().getResultsAtLastGame() != null) {
    		this.playerScoresRow.updatePlayerScore(this.getGameSet().getGameSetScores().getResultsAtLastGame());
    	}
    	// if no game is present in the set, reset all scores to zero
    	else {
    		this.playerScoresRow.resetAllPlayerScores();
    	}
    }
    
    /**
     * Sets a specific title.
     */
    private void setTitle() {
    	String title;
    	// no game in GameSet
    	if (this.getGameSet().getGameCount() == 0) {
    		title = this.getString(R.string.lblTabGameSetActivityNoGameTitle);
    	}
    	// 1 game in GameSet
    	else if (this.getGameSet().getGameCount() == 1) {
    		title = this.getString(R.string.lblTabGameSetActivityOneGameTitle);
    	}
    	// several games in GameSet
    	else {
    		title = String.format(this.getString(R.string.lblTabGameSetActivitySeveralGamesTitle), this.getGameSet().getGameCount());
    	}
    	getActivity().setTitle(title);
    }
}
