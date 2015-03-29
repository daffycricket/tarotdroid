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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;

import org.nla.tarotdroid.lib.ui.constants.ActivityParams;
import org.nla.tarotdroid.lib.ui.controls.Selector;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianBaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.biz.enums.GameStyleType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * A fragment to read a game..
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class GameReadFragment extends SherlockFragment
{
	/**
	 * Dead player panel.
	 */
	private RelativeLayout panelDead;
	
	/**
	 * Dead player selector.
	 */
	private Selector<Player> selectorDead;
	
	/**
	 * Dead player panel.
	 */
	private RelativeLayout panelDealer;
	
	/**
	 * Dealer player selector.
	 */
	private Selector<Player> selectorDealer;
	
	/**
	 * Bet selector.
	 */
	private Selector<Bet> selectorBet;
	
	/**
	 * Leader player selector.
	 */
	private Selector<Player> selectorLeader;
	
	/**
	 * Called player panel.
	 */
	private RelativeLayout panelCalled;
	
	/**
	 * Called player selector.
	 */
	private Selector<Player> selectorCalled;

	/**
	 * King panel.
	 */
	private RelativeLayout panelKing;

	/**
	 * King selector.
	 */
	private Selector<King> selectorKing;
	
	/**
	 * Oudler selector.
	 */
	private Selector<Integer> selectorOudlers;
	
	/**
	 * Attack points selector.
	 */
	private SeekBar barAttackPoints;
	
	/**
	 * Attack team score as a textview.
	 */
	protected TextView txtAttackPoints;

	/**
	 * Defense points selector.
	 */
	private SeekBar barDefensePoints;
	
	/**
	 * Defense team score as a textview.
	 */
	protected TextView txtDefensePoints;

	/**
	 * Handful points selector.
	 */
	private Selector<Team> selectorHandful;

	/**
	 * Double Handful points selector.
	 */
	private Selector<Team> selectorDoubleHandful;

	/**
	 * Triple Handful points selector.
	 */
	private Selector<Team> selectorTripleHandful;
	
	/**
	 * Misery player selector.
	 */
	private Selector<Player> selectorMisery;
	
	/**
	 * Kid at the end team selector.
	 */
	private Selector<Team> selectorKidAtTheEnd;
	
	/**
	 * Slam selector.
	 */
	private Selector<Chelem> selectorSlam;

	/**
	 * Dead and dealer panel.
	 */
	private LinearLayout panelDeadAndDealerSection;

	/**
	 * Annoucement panel.
	 */
	private LinearLayout panelAnnoucementSection;
	
	/**
	 * Annoucement panel.
	 */
	private LinearLayout panelAnnouncements;
	
	/**
	 * Handful panel.
	 */
	private RelativeLayout panelHandful;

	/**
	 * Double Handful panel.
	 */
	private RelativeLayout panelDoubleHandful;

	/**
	 * Triple Handful panel.
	 */
	private RelativeLayout panelTripleHandful;
	
	/**
	 * Misery panel.
	 */
	private RelativeLayout panelMisery;

	/**
	 * Kid at the end panel.
	 */
	private RelativeLayout panelKidAtTheEnd;
	
	/**
	 * Slam panel.
	 */
	private RelativeLayout panelSlam;
	
	/**
	 * First player selector.
	 */
	private Selector<Player> selectorFirst;

	/**
	 * Second player selector.
	 */
	private Selector<Player> selectorSecond;

	/**
	 * Third player selector.
	 */
	private Selector<Player> selectorThird;

	/**
	 * Fourth player panel.
	 */
	private RelativeLayout panelFourth;
	
	/**
	 * Fourth player selector.
	 */
	private Selector<Player> selectorFourth;

	/**
	 * Fifth player panel.
	 */
	private RelativeLayout panelFifth;

	/**
	 * Fifth player selector.
	 */
	private Selector<Player> selectorFifth;
	
//	/**
//	 * Penalted player selector.
//	 */
//	private Selector<Player> selectorPenalted;
		
	/**
	 * Global penalty points as a textview.
	 */
	protected TextView txtGlobalPenaltyPoints;
	
//	/**
//	 * Individual player penalty points seek bar.
//	 */
//	private SeekBar barPlayerPenaltyPoints;
	
//	/**
//	 * The game set to display.
//	 */
//	private GameSet gameSet;
	
	/**
	 * The game to display.
	 */
	private BaseGame game;
	
	/**
	 * The main layout
	 */
	private FrameLayout frameLayout;
	
	/**
	 * Constructs a new instance of GameReadFragment. 
	 * @param gameIndex
	 * @param gameSet
	 * @return
	 */
	public static GameReadFragment newInstance(int gameIndex, GameSet gameSet) {
		checkArgument(gameSet != null, "gameSet is null");
		GameReadFragment fragment = new GameReadFragment();
		
		Bundle args = new Bundle();
		args.putInt(ActivityParams.PARAM_GAME_INDEX, gameIndex);
//		if (!gameSet.isPersisted()) {
//			//args.putString(ActivityParams.PARAM_GAMESET_SERIALIZED, UIHelper.serializeGameSet(gameSet));
//			args.putSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED, gameSet);
//		}
//		else {
//			args.putLong(ActivityParams.PARAM_GAMESET_ID, gameSet.getId());
//		}
		fragment.setArguments(args);
		
		return fragment;
	}
	
	/**
	 * Returns the game set on which activity has to work.
	 * @return
	 */
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// check params
		Bundle args = this.getArguments();
//		if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
//			this.gameSet = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
//		}
//		else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
//			//this.gameSet = UIHelper.deserializeGameSet(args.getString(ActivityParams.PARAM_GAMESET_SERIALIZED));
//			this.gameSet = (GameSet)args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
//		}
//		else {
//			throw new IllegalArgumentException("Game set id or serialized game set must be provided");
//		}
		
		checkArgument(args.containsKey(ActivityParams.PARAM_GAME_INDEX), "Game index must be provided");
		this.game = this.getGameSet().getGameOfIndex(args.getInt(ActivityParams.PARAM_GAME_INDEX));		
		
		// set layout
		if (this.game instanceof StandardBaseGame) {
			this.frameLayout = (FrameLayout)inflater.inflate(R.layout.standard_game_read, container, false);
		}
		else if (this.game instanceof BelgianBaseGame) {
			this.frameLayout = (FrameLayout)inflater.inflate(R.layout.belgian_game_read, container, false);
		}
		else if (this.game instanceof PenaltyGame) {
			this.frameLayout = (FrameLayout)inflater.inflate(R.layout.penalty_game_read, container, false);
		}
		else if (this.game instanceof PassedGame) {
			this.frameLayout = (FrameLayout)inflater.inflate(R.layout.passed_game_read, container, false);
		}

		
		// display game
		this.displayDeadAndDealer();
		if (this.game instanceof StandardBaseGame) {
			this.intializeStandardViews();
			this.displayStandardGame();
		}
		else if (this.game instanceof BelgianBaseGame) {
			this.intializeBelgianViews();
			this.displayBelgianGame();
		}
		else if (this.game instanceof PenaltyGame) {
			this.intializePenaltyViews();
			this.displayPenaltyGame();			
		}
		else if (this.game instanceof PassedGame) {
			// I guess nothing to do....			
		}
		
		this.smoothlyHideText();
		return this.frameLayout;
	}
	
	/**
	 * Displays game index in game set and then smoothly removes the text.
	 */
	private void smoothlyHideText() {
	    final TextView onTop = (TextView) this.frameLayout.findViewById(R.id.txtOnTop);
	    onTop.setText(this.game.getIndex() + "/" + this.game.getHighestGameIndex());
	    
		Animation animation1 = new AlphaAnimation(1.0f, 0.0f);
	    animation1.setDuration(1500);
	    
	  //animation1 AnimationListener
	    animation1.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {
	            // start animation2 when animation1 ends (continue)
	        	onTop.setVisibility(View.GONE);
	        }

	        @Override
	        public void onAnimationRepeat(Animation arg0) {
	        }

	        @Override
	        public void onAnimationStart(Animation arg0) {
	        }
	    });

	    onTop.startAnimation(animation1);
	}
		
	/**
	 * Displays the dead and dealer panels.
	 */
	@SuppressWarnings("unchecked")
	private void displayDeadAndDealer() {
		
		// initialize dealer player widgets
		this.panelDealer = (RelativeLayout) this.frameLayout.findViewById(R.id.panelDealer);
		if (this.game.getDealer() != null) {
			this.panelDealer.setVisibility(View.VISIBLE);
			this.selectorDealer = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryDealer);
			this.selectorDealer.setObjects(this.getGameSet().getPlayers().getPlayers());
			this.selectorDealer.setSelected(this.game.getDealer());
			this.selectorDealer.setReadOnly(true);
		}
		else {
			this.panelDealer.setVisibility(View.GONE); 
		}
		
		// initialize dead player widgets
		this.panelDead = (RelativeLayout) this.frameLayout.findViewById(R.id.panelDead);
		if (this.game.getDeadPlayer() != null) {
			this.panelDead.setVisibility(View.VISIBLE);
			this.selectorDead = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryDead);
			this.selectorDead.setObjects(this.getGameSet().getPlayers().getPlayers());
			this.selectorDead.setSelected(this.game.getDeadPlayer());
			this.selectorDead.setReadOnly(true);
		}
		else {
			this.panelDead.setVisibility(View.GONE);
		}
		
		// if neither dealer nor dead players are set, don't display the section
		if (this.game.getDealer() == null && this.game.getDeadPlayer() == null) {
			this.panelDeadAndDealerSection = (LinearLayout) this.frameLayout.findViewById(R.id.panelDeadAndDealerSection);
			this.panelDeadAndDealerSection.setVisibility(View.GONE);
		}
	}
    	
	/**
     * Displays the standard game.
     */
    private void displayStandardGame() {
    	StandardBaseGame stdGame = (StandardBaseGame)this.game;
		
    	// common properties
		this.selectorBet.setSelected(stdGame.getBet());
		this.selectorBet.setReadOnly(true);
		this.selectorLeader.setSelected(stdGame.getLeadingPlayer());
		this.selectorLeader.setReadOnly(true);
		this.selectorOudlers.setSelected(stdGame.getNumberOfOudlers());
		this.selectorOudlers.setReadOnly(true);
		this.barAttackPoints.setProgress((int)stdGame.getPoints());
		this.txtAttackPoints.setText(new Integer((int)stdGame.getPoints()).toString());
		this.barDefensePoints.setProgress(91 - (int)stdGame.getPoints());
		this.txtDefensePoints.setText(new Integer(91 - (int)stdGame.getPoints()).toString());
		
		// announcements
		if (this.isDisplayAnnouncementPanel()) {
			this.displayAnnouncementSubPanels();
			this.panelAnnouncements.setVisibility(View.VISIBLE);
			this.selectorHandful.setSelected(stdGame.getTeamWithPoignee());
			this.selectorHandful.setReadOnly(true);
			this.selectorDoubleHandful.setSelected(stdGame.getTeamWithDoublePoignee());
			this.selectorDoubleHandful.setReadOnly(true);
			this.selectorTripleHandful.setSelected(stdGame.getTeamWithTriplePoignee());
			this.selectorTripleHandful.setReadOnly(true);
			this.selectorKidAtTheEnd.setSelected(stdGame.getTeamWithKidAtTheEnd());
			this.selectorKidAtTheEnd.setReadOnly(true);
			this.selectorSlam.setSelected(stdGame.getChelem());
			this.selectorSlam.setReadOnly(true);
			this.selectorMisery.setSelected(stdGame.getPlayerWithMisery());
			this.selectorMisery.setReadOnly(true);
		}
		else {
			this.panelAnnoucementSection.setVisibility(View.GONE);
			this.panelAnnouncements.setVisibility(View.GONE);
		}

		// 5 player specifics
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			StandardTarot5Game std5Game = (StandardTarot5Game)this.game;
			this.panelCalled.setVisibility(View.VISIBLE);
			this.panelKing.setVisibility(View.VISIBLE);
			
			this.selectorCalled.setSelected(std5Game.getCalledPlayer());
			this.selectorCalled.setReadOnly(true);
			this.selectorKing.setSelected(std5Game.getCalledKing());
			this.selectorKing.setReadOnly(true);
			
		}
    }
    
    /**
     * Initializes the widgets specific to belgian games.
     */
	@SuppressWarnings("unchecked")
	private void intializeStandardViews() {

		// Main Parameters widget recuperation	
		this.selectorBet = (Selector<Bet>) this.frameLayout.findViewById(R.id.galleryBet);
		this.selectorLeader = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryLeader);
		this.panelCalled = (RelativeLayout) this.frameLayout.findViewById(R.id.panelCalled);
		this.selectorCalled = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryCalled);
		this.panelKing = (RelativeLayout) this.frameLayout.findViewById(R.id.panelKing);
		this.selectorKing = (Selector<King>) this.frameLayout.findViewById(R.id.galleryKing);
		this.selectorOudlers = (Selector<Integer>) this.frameLayout.findViewById(R.id.galleryOudlers);
		this.barAttackPoints = (SeekBar) this.frameLayout.findViewById(R.id.barAttackPoints);
		this.txtAttackPoints = (TextView)this.frameLayout.findViewById(R.id.txtAttackPoints);
		this.barDefensePoints = (SeekBar) this.frameLayout.findViewById(R.id.barDefensePoints);
		this.txtDefensePoints = (TextView) this.frameLayout.findViewById(R.id.txtDefensePoints);
		
		// Annoucements widgets recuperation
		this.panelAnnouncements = (LinearLayout) this.frameLayout.findViewById(R.id.panelAnnouncements);
		this.panelAnnoucementSection = (LinearLayout) this.frameLayout.findViewById(R.id.panelAnnoucementSection);
		this.panelHandful = (RelativeLayout) this.frameLayout.findViewById(R.id.panelHandful);
		this.selectorHandful = (Selector<Team>) this.frameLayout.findViewById(R.id.galleryHandful);
		this.panelDoubleHandful = (RelativeLayout) this.frameLayout.findViewById(R.id.panelDoubleHandful);
		this.selectorDoubleHandful = (Selector<Team>) this.frameLayout.findViewById(R.id.galleryDoubleHandful);
		this.panelTripleHandful = (RelativeLayout) this.frameLayout.findViewById(R.id.panelTripleHandful);
		this.selectorTripleHandful = (Selector<Team>) this.frameLayout.findViewById(R.id.galleryTribleHandful);
		this.panelMisery = (RelativeLayout) this.frameLayout.findViewById(R.id.panelMisery);
		this.selectorMisery = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryMisery);
		this.panelKidAtTheEnd = (RelativeLayout) this.frameLayout.findViewById(R.id.panelKidAtTheEnd);
		this.selectorKidAtTheEnd = (Selector<Team>) this.frameLayout.findViewById(R.id.galleryKidAtTheEnd);
		this.panelSlam = (RelativeLayout) this.frameLayout.findViewById(R.id.panelSlam);
		this.selectorSlam = (Selector<Chelem>) this.frameLayout.findViewById(R.id.gallerySlam);
	
		// widget content loading
		this.selectorBet.setObjects(newArrayList(Bet.PETITE, Bet.PRISE, Bet.GARDE, Bet.GARDESANS, Bet.GARDECONTRE));
		this.selectorLeader.setObjects(this.game.getPlayers().getPlayers());
		this.selectorOudlers.setObjects(newArrayList(0, 1, 2, 3));
		this.barAttackPoints.setProgress(0);
		this.barAttackPoints.setEnabled(false);
		this.barDefensePoints.setProgress(91);
		this.barDefensePoints.setEnabled(false);
		this.selectorHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM, Team.BOTH_TEAMS));
		this.selectorDoubleHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorTripleHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorKidAtTheEnd.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorSlam.setObjects(newArrayList(Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED, Chelem.CHELEM_ANOUNCED_AND_FAILED, Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED));
		this.panelMisery.setVisibility(View.VISIBLE);
		this.selectorMisery.setObjects(this.game.getPlayers().getPlayers());
		
		// tarot 5 specific widget content loading
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			this.panelCalled.setVisibility(View.VISIBLE);
			this.selectorCalled.setObjects(this.game.getPlayers().getPlayers());
			this.panelKing.setVisibility(View.VISIBLE);
			this.selectorKing.setObjects(newArrayList(King.CLUB, King.DIAMOND, King.HEART, King.SPADE));
		}
		else {
			this.panelCalled.setVisibility(View.GONE);
			this.panelKing.setVisibility(View.GONE);
		}
	}

    /**
     * Indicates whether the announcement panel is to be displayed.
     * @return
     */
    private boolean isDisplayAnnouncementPanel() {	
    	StandardBaseGame stdGame = (StandardBaseGame)this.game;
		boolean toReturn = 
				stdGame.getTeamWithPoignee() != null || 
				stdGame.getTeamWithDoublePoignee() != null || 
				stdGame.getTeamWithTriplePoignee() != null || 
				stdGame.getTeamWithKidAtTheEnd() != null || 
				stdGame.getChelem() != null ||
				stdGame.getPlayerWithMisery() != null;
		
		return toReturn;
    }
    
    /**
     * Displays the annoucement sub panels.
     * @return
     */
    private void displayAnnouncementSubPanels() {
    	StandardBaseGame stdGame = (StandardBaseGame)this.game;
    	
    	if (stdGame.getTeamWithPoignee() == null) {
    		this.panelHandful.setVisibility(View.GONE);
    	}
    	if (stdGame.getTeamWithDoublePoignee() == null) {
    		this.panelDoubleHandful.setVisibility(View.GONE);
    	}
    	if (stdGame.getTeamWithTriplePoignee() == null) {
    		this.panelTripleHandful.setVisibility(View.GONE);
    	}
    	if (stdGame.getTeamWithKidAtTheEnd() == null) {
    		this.panelKidAtTheEnd.setVisibility(View.GONE);
    	}
    	if (stdGame.getChelem() == null) {
    		this.panelSlam.setVisibility(View.GONE);
    	}
    	if (stdGame.getPlayerWithMisery() == null) {
    		this.panelMisery.setVisibility(View.GONE);
    	}    	
    }

	/**
     * Displays the belgian game.
     * @return
     */
    private void displayBelgianGame() {
    	if (this.game instanceof BelgianTarot3Game) {
    		BelgianTarot3Game belgianGame = (BelgianTarot3Game)this.game;
        	this.selectorFirst.setSelected(belgianGame.getFirst());
        	this.selectorFirst.setReadOnly(true);
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorSecond.setReadOnly(true);
        	this.selectorThird.setSelected(belgianGame.getThird());
        	this.selectorThird.setReadOnly(true);
    	}
    	else if (this.game instanceof BelgianTarot4Game) {
    		BelgianTarot4Game belgianGame = (BelgianTarot4Game)this.game;
        	this.selectorFirst.setSelected(belgianGame.getFirst());
        	this.selectorFirst.setReadOnly(true);
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorSecond.setReadOnly(true);
        	this.selectorThird.setSelected(belgianGame.getThird());
        	this.selectorThird.setReadOnly(true);
        	this.selectorFourth.setSelected(belgianGame.getFourth());
        	this.selectorFourth.setReadOnly(true);
    	}
    	else if (this.game instanceof BelgianTarot5Game) {
    		BelgianTarot5Game belgianGame = (BelgianTarot5Game)this.game;
        	this.selectorFirst.setSelected(belgianGame.getFirst());
        	this.selectorFirst.setReadOnly(true);
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorSecond.setReadOnly(true);
        	this.selectorThird.setSelected(belgianGame.getThird());
        	this.selectorThird.setReadOnly(true);
        	this.selectorFourth.setSelected(belgianGame.getFourth());
        	this.selectorFourth.setReadOnly(true);
        	this.selectorFifth.setSelected(belgianGame.getFifth());
        	this.selectorFifth.setReadOnly(true);
    	}
    }
    
    /**
     * Initializes the widgets specific to belgian games.
     */
    @SuppressWarnings("unchecked")
	private void intializeBelgianViews() {
    	
    	// Main Parameters widget recuperation	
    	this.selectorFirst = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryFirst);
    	this.selectorSecond = (Selector<Player>) this.frameLayout.findViewById(R.id.gallerySecond);
    	this.selectorThird = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryThird);
    	this.panelFourth = (RelativeLayout) this.frameLayout.findViewById(R.id.panelFourth);
    	this.selectorFourth = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryFourth);
    	this.panelFifth = (RelativeLayout) this.frameLayout.findViewById(R.id.panelFifth);
    	this.selectorFifth = (Selector<Player>) this.frameLayout.findViewById(R.id.galleryFifth);
    	
    	// widget content loading
    	this.selectorFirst.setObjects(this.game.getPlayers().getPlayers());
    	this.selectorSecond.setObjects(this.game.getPlayers().getPlayers());
    	this.selectorThird.setObjects(this.game.getPlayers().getPlayers());
		this.panelFourth.setVisibility(View.GONE);
		this.panelFifth.setVisibility(View.GONE);
    	
    	if (this.game instanceof BelgianTarot4Game) {
    		this.panelFourth.setVisibility(View.VISIBLE);
        	this.selectorFourth.setObjects(this.game.getPlayers().getPlayers());
    	}
    	else if (this.game instanceof BelgianTarot5Game) {
    		this.panelFourth.setVisibility(View.VISIBLE);
    		this.selectorFourth.setObjects(this.game.getPlayers().getPlayers());
    		this.panelFifth.setVisibility(View.VISIBLE);
        	this.selectorFifth.setObjects(this.game.getPlayers().getPlayers());
    	}
    }
    
	/**
     * Displays the penalty game.
     * @return
     */
    private void displayPenaltyGame() {
		PenaltyGame penaltyGame = (PenaltyGame)this.game;
//		this.selectorPenalted.setSelected(penaltyGame.getPenaltedPlayer());
		this.txtGlobalPenaltyPoints.setText(new Integer(penaltyGame.getPenaltyPoints()).toString());
    }
    
    /**
     * Initializes the widgets specific to penalty games.
     */
	private void intializePenaltyViews() {
    	// widget recuperation
//    	this.selectorPenalted = (Selector<Player>) this.frameLayout.findViewById(R.id.selectorPenalted);
    	this.txtGlobalPenaltyPoints = (TextView) this.frameLayout.findViewById(R.id.txtGlobalPenaltyPoints);

    	// widget content loading
//    	this.selectorPenalted.setObjects(this.game.getPlayers().getPlayers());    	
//    	this.selectorPenalted.setReadOnly(true);
    }
}