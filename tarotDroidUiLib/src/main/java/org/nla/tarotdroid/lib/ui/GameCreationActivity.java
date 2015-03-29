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
import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.constants.ActivityParams;
import org.nla.tarotdroid.lib.ui.constants.ResultCodes;
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
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.dal.DalException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

/**
 * Activity to create a game.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class GameCreationActivity extends SherlockActivity
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
	 * Plus button for attack score.
	 */
	protected Button btnPlusAttackPoints;
	
	/**
	 * Minus button for attack score.
	 */
	protected Button btnMinusAttackPoints;

	/**
	 * Defense points selector.
	 */
	private SeekBar barDefensePoints;
	
	/**
	 * Defense team score as a textview.
	 */
	protected TextView txtDefensePoints;
	
	/**
	 * Plus button for defense score.
	 */
	protected Button btnPlusDefensePoints;
	
	/**
	 * Minus button for defense score.
	 */
	protected Button btnMinusDefensePoints;

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
	 * Misery panel.
	 */
	private RelativeLayout panelMisery;

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
	 * Dead and dealer panel.
	 */
	private LinearLayout panelDeadAndDealer;

	/**
	 * Main parameters title.
	 */
	private TextView txtTitleMainParameters;

	/**
	 * Main parameters panel.
	 */
	private LinearLayout panelMainParameters;

	/**
	 * Annoucement panel title.
	 */
	private TextView txtTitleAnnouncements;

	/**
	 * Annoucement panel.
	 */
	private LinearLayout panelAnnouncements;
	
	/**
	 * Global penalty points as a textview.
	 */
	protected TextView txtGlobalPenaltyPoints;
	
	/**
	 * Plus button for global penalty points.
	 */
	protected Button btnPlusGlobalPenaltyPoints;
	
	/**
	 * Minus button for global penalty points.
	 */
	protected Button btnMinusGlobalPenaltyPoints;

	/**
	 * Individual player penalty points seek bar.
	 */
	private SeekBar barPlayerPenaltyPoints;
	
	/**
	 * Individual player penalty points as a textview.
	 */
	protected TextView txtPlayerPenaltyPoints;
	
	/**
	 * Change event Listener for the attack seek bar.
	 */
	private OnSeekBarChangeListener attackPointsChangeListener = new OnSeekBarChangeListener() {
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
		 */
		@Override
		public void onStopTrackingTouch(final SeekBar seekBar) {
		}
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
		 */
		@Override
		public void onStartTrackingTouch(final SeekBar seekBar) {
		}
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
		 */
		@Override
		public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
			GameCreationActivity.this.attackScore = progress;
			GameCreationActivity.this.updatePointsViews();
		}
	};
	
	/**
	 * Change event Listener for the defense seek bar.
	 */
	private OnSeekBarChangeListener defensePointsChangeListener = new OnSeekBarChangeListener() {
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
		 */
		@Override
		public void onStopTrackingTouch(final SeekBar seekBar) {
		}
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
		 */
		@Override
		public void onStartTrackingTouch(final SeekBar seekBar) {
		}
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
		 */
		@Override
		public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
			GameCreationActivity.this.attackScore = 91 - progress;
			GameCreationActivity.this.updatePointsViews();
		}
	};
	
	/**
	 * Change event Listener for the player penalty points seek bar.
	 */
	private OnSeekBarChangeListener playerPenaltyPointsChangeListener = new OnSeekBarChangeListener() {
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
		 */
		@Override
		public void onStopTrackingTouch(final SeekBar seekBar) {
		}
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
		 */
		@Override
		public void onStartTrackingTouch(final SeekBar seekBar) {
		}
		
		/* (non-Javadoc)
		 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
		 */
		@Override
		public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
			GameCreationActivity.this.playerPenaltyPoints = progress;
			GameCreationActivity.this.updatePenaltyPointsViews();
		}
	};

	
	/**
	 * Panels Click listener to use when no dead player is selected.
	 */
	private OnClickListener onNoDeadPlayerSelectedClickListener = new OnClickListener() {
		
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(final View layoutView) {
			Toast.makeText(
					layoutView.getContext(),
					GameCreationActivity.this.getString(R.string.msgSelectDeadPlayer), 
					Toast.LENGTH_LONG
			).show();
		}
	};
	
	/**
	 * Handles increase attack score.
	 */ 
	private OnClickListener onIncreaseAttackScoreClickListener = new OnClickListener() {
		
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(final View v) {
			GameCreationActivity.this.attackScore += 1;
			GameCreationActivity.this.updatePointsViews();
		}
	};
	
	/**
	 * Handles decrease attack score.
	 */ 
	private OnClickListener onDecreaseAttackScoreClickListener = new OnClickListener() {
		
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(final View v) {
			GameCreationActivity.this.attackScore -= 1;
			GameCreationActivity.this.updatePointsViews();
		}
	};
	
	/**
	 * Handles increase global penalty points.
	 */ 
	private OnClickListener onIncreasePenaltyPointsClickListener = new OnClickListener() {
		
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(final View v) {
			GameCreationActivity.this.playerPenaltyPoints += 1;
			GameCreationActivity.this.updatePenaltyPointsViews();
		}
	};
	
	/**
	 * Handles decrease global penalty points.
	 */ 
	private OnClickListener onDecreasePenaltyPointsClickListener = new OnClickListener() {
		
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(final View v) {
			GameCreationActivity.this.playerPenaltyPoints -= 1;
			GameCreationActivity.this.updatePenaltyPointsViews();
		}
	};
	
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

	/**
	 * All players.
	 */
	private List<Player> allPlayers;

	/**
	 * Only players in game, that is all players minus dead players.
	 */
	private List<Player> inGamePlayers;
	
	/**
	 * Attack score.
	 */
	private int attackScore;
	
	/**
	 * Player penalty points.
	 */
	private int playerPenaltyPoints;

	/**
	 * Player multiplication rate for global penalty points.
	 */
	private int playerMultiplicationRate;
	
	/**
	 * The game type.
	 */
	private GameType gameType;
	
//	/**
//	 * The current game set. 
//	 */
//	private GameSet gameSet;
	
	/**
	 * The potential game to edit.
	 */
	private BaseGame game;
	
	/**
	 * Indicates whether the activity is in edit mode.
	 */
	private boolean isInEditMode;
	
	/**
	 * Returns the game set on which activity has to work.
	 * @return
	 */
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}
	
	/* (non-Javadoc)
	 * @see greendroid.app.GDActivity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
//			// check params
//			checkArgument(this.getIntent().getExtras().containsKey(ActivityParams.PARAM_GAMESET_ID), "Game set id must be provided");
//			this.getGameSet() = AppContext.getApplication().getDalService().getGameSetById(this.getIntent().getExtras().getLong(ActivityParams.PARAM_GAMESET_ID));

//			// check params
//			Bundle args = this.getIntent().getExtras();
//			if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
//				this.getGameSet() = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
//			}
//			else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
//				//this.getGameSet() = UIHelper.deserializeGameSet(args.getString(ActivityParams.PARAM_GAMESET_SERIALIZED));
//				this.getGameSet() = (GameSet)args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
//			}
//			else {
//				throw new IllegalArgumentException("Game set id or serialized game set must be provided");
//			}
			
			// set keep screen on 
			UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());
			
			// internal members initialization
			this.identifyGameType();
			
			this.auditEvent();
			List<Player> players = this.getGameSet().getPlayers().getPlayers();
			this.allPlayers = newArrayList(players);
			this.inGamePlayers = newArrayList(players);
			this.isInEditMode = false;
			
			// layout initialization
			switch(this.gameType) {
				case Standard:
					this.setContentView(R.layout.standard_game_creation);
					break;
				case Belgian:
					this.setContentView(R.layout.belgian_game_creation);
					break;
				case Penalty:
					this.setContentView(R.layout.penalty_game_creation);
					break;
				case Pass:
					this.setContentView(R.layout.passed_game_creation);
					break;
			}
			
			this.panelMainParameters = (LinearLayout) this.findViewById(R.id.panelMainParameters);
			this.txtTitleMainParameters = (TextView) this.findViewById(R.id.txtTitleMainParameters);	

			// get dead and dealer widgets
			this.panelDeadAndDealer = (LinearLayout) this.findViewById(R.id.panelDeadAndDealer);
			this.panelDead = (RelativeLayout) this.findViewById(R.id.panelDead);
			this.selectorDead = (Selector<Player>) this.findViewById(R.id.galleryDead);
			this.selectorDead.setObjects(this.allPlayers);
			this.selectorDealer = (Selector<Player>) this.findViewById(R.id.galleryDealer);
			this.selectorDealer.setObjects(this.allPlayers);
			
			// initialize views
			this.initializeCommonViews();
			switch(this.gameType) {
				case Standard:
					this.intializeStandardViews();
					this.initializeDeadAndDealerPanelForStandardCase();
					break;
				case Belgian:
					this.intializeBelgianViews();
					this.initializeDeadAndDealerPanelForBelgianCase();
					break;
				case Penalty:
					this.intializePenaltyViews();
					this.initializeDeadAndDealerPanelForPenaltyCase();
					break;
				case Pass:
					this.initializeDeadAndDealerPanelForPassCase();
					break;
			}
			
			// if the activity is in edit mode, get the game and display its values 
			if (this.getIntent().getExtras().containsKey(ActivityParams.PARAM_GAME_INDEX)) {
				this.game = this.getGameSet().getGameOfIndex(this.getIntent().getExtras().getInt(ActivityParams.PARAM_GAME_INDEX));
				this.isInEditMode = true;
				
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
					//this.intialize();
					//this.displayBelgianGame();
				}
			}
			
			// sets the title
			this.setTitle();
		}
		catch (Exception e) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.gameCreationActivityError, e, this);
		}
	}
	
	/**
	 * Initializes the common views.
	 */
	private void initializeCommonViews() {
		TextView txtTitleDeadAndDealer = (TextView)this.findViewById(R.id.txtTitleDeadAndDealer);
		if (this.isDisplayDeadPlayerPanel()) {
			txtTitleDeadAndDealer.setText(R.string.lblDeadAndDealerSection);
		}
		else {
			txtTitleDeadAndDealer.setText(R.string.lblDealerSection);
		}
	}

	/**
	 * Sets the title according to the game style.
	 */
	private void setTitle() {
		if (this.isInEditMode) {
			this.setTitle(R.string.lblUpdateGameTitle);
		}
		else {
			switch(this.gameType) {
				case Standard :
					this.setTitle(R.string.lblNewStandardGameTitle);
					break;
				case Belgian :
					this.setTitle(R.string.lblNewBelgianGameTitle);
					break;
				case Penalty:
					this.setTitle(R.string.lblNewPenaltyGameTitle);
					break;
				case Pass:
					this.setTitle(R.string.lblNewPassedGameTitle);
					break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		AuditHelper.auditSession(this);
	}
	
	/**
	 *	Traces creation event. 
	 */
	private void auditEvent() {
		Map<AuditHelper.ParameterTypes, Object> parameters = newHashMap();
		parameters.put(AuditHelper.ParameterTypes.gameType, this.gameType);
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayGameCreationV2Page, parameters);
	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem miSaveGame = menu.add(R.string.lblValidateGame);
		miSaveGame.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		miSaveGame.setIcon(R.drawable.ic_compose);
		miSaveGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            
			/* (non-Javadoc)
			 * @see com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(com.actionbarsherlock.view.MenuItem)
			 */
			@Override
            public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				if (!GameCreationActivity.this.isFormValid()) {
					Toast.makeText(
							GameCreationActivity.this, 
							GameCreationActivity.this.getString(R.string.msgValidationKo), 
							Toast.LENGTH_SHORT
					).show();
				}
				else {
					if (GameCreationActivity.this.isInEditMode) {
						GameCreationActivity.this.updateGame(GameCreationActivity.this.game);
						new PersistGameTask(GameCreationActivity.this).execute(GameCreationActivity.this.game);
					}
					else {
						new PersistGameTask(GameCreationActivity.this).execute(GameCreationActivity.this.createGame());
					}
				}
            	return true;
            }
        });

		MenuItem miInfo = menu.add(R.string.lblHelpItem);
		miInfo.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		miInfo.setIcon(R.drawable.gd_action_bar_info);
		miInfo.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            
			/* (non-Javadoc)
			 * @see com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener#onMenuItemClick(com.actionbarsherlock.view.MenuItem)
			 */
			@Override
            public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				
				// identify content to display
				String content = null;
				switch (GameCreationActivity.this.getGameSet().getGameStyleType()) {
					case Tarot3:						
						switch (gameType) {
							case Belgian:
								content = String.format(
										GameCreationActivity.this.getText(R.string.msgHelpNewBelgian3Game).toString(),
										GameCreationActivity.this.getGameSet().getGameSetParameters().getBelgianBaseStepPoints()
                                );
							break;
							
							case Standard:
								content = GameCreationActivity.this.getText(R.string.msgHelpNewStd3Game).toString();
							default:
							break;
						}
					break;

					case Tarot5:
						switch (gameType) {
							case Belgian:
								content = String.format(
										GameCreationActivity.this.getText(R.string.msgHelpNewBelgian5Game).toString(),
										GameCreationActivity.this.getGameSet().getGameSetParameters().getBelgianBaseStepPoints(),
										GameCreationActivity.this.getGameSet().getGameSetParameters().getBelgianBaseStepPoints() * 2
                                );
							break;
						
							case Standard:
								content = GameCreationActivity.this.getText(R.string.msgHelpNewStd5Game).toString();
							default:
							break;
						}
					break;

					case Tarot4:
					default:
						switch (gameType) {
							case Belgian:
								content = String.format(
										GameCreationActivity.this.getText(R.string.msgHelpNewBelgian4Game).toString(),
										GameCreationActivity.this.getGameSet().getGameSetParameters().getBelgianBaseStepPoints(),
										GameCreationActivity.this.getGameSet().getGameSetParameters().getBelgianBaseStepPoints() * 2
                                );
							break;
					
							case Standard:
								content = GameCreationActivity.this.getText(R.string.msgHelpNewStd4Game).toString();
							default:
							break;
						}
					break;
				}

				// identify title to display
				String title = null;
				switch (GameCreationActivity.this.getGameSet().getGameStyleType()) {
					case Tarot3:						
						switch (gameType) {
							case Belgian:
								title = GameCreationActivity.this.getString(R.string.titleHelpNewBelgian3Game);
							break;
							
							case Standard:
								title = GameCreationActivity.this.getString(R.string.titleHelpNewStd3Game);
							default:
							break;
						}
					break;
	
					case Tarot5:
						switch (gameType) {
							case Belgian:
								title = GameCreationActivity.this.getString(R.string.titleHelpNewBelgian5Game);
							break;
						
							case Standard:
								title = GameCreationActivity.this.getString(R.string.titleHelpNewStd5Game);
							default:
							break;
						}
					break;
	
					case Tarot4:
					default:
						switch (gameType) {
							case Belgian:
								title = GameCreationActivity.this.getString(R.string.titleHelpNewBelgian4Game);
							break;
					
							case Standard:
								title = GameCreationActivity.this.getString(R.string.titleHelpNewStd4Game);
							default:
							break;
						}
					break;
				}
				
				if (title == null) {
					switch (gameType) {
						case Pass :
							title = GameCreationActivity.this.getString(R.string.titleHelpNewPassedGame);
							content = GameCreationActivity.this.getString(R.string.msgHelpNewPassedGame);
							break;
						case Penalty :
							title = GameCreationActivity.this.getString(R.string.titleHelpNewPenaltyGame);;
							content = GameCreationActivity.this.getString(R.string.msgHelpNewPenaltyGame);
							break;
					}
				}
				
				// display help only if messages are set
				if (title != null && content != null) {
					UIHelper.showSimpleRichTextDialog(
							GameCreationActivity.this, 
							content, 
							title
					);
				}
				
            	return true;
            }
        });
		
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * Sets the dealer player in the selector.
	 */
	private void setDealerPlayer() {
    	if (this.getGameSet().getGameCount() > 0) {
    		PersistableBusinessObject formerDealerPlayer = this.getGameSet().getLastGame().getDealer();
    		if (formerDealerPlayer != null) {
    			this.selectorDealer.setSelected(this.getGameSet().getPlayers().getNextPlayer(formerDealerPlayer));
    		}
    	}
    	else {
			this.selectorDealer.setSelected(this.getGameSet().getPlayers().get(1));
			this.panelDeadAndDealer.setVisibility(View.VISIBLE);
    	}
	}
	
	/**
	 * Tries to set the dead player in the selector using the previous games in the game set.
	 */
	private boolean trySetDeadPlayer() {
    	if (this.getGameSet().getGameCount() > 0) {
    		PersistableBusinessObject formerDeadPlayer = this.getGameSet().getLastGame().getDeadPlayer();
    		if (formerDeadPlayer != null) {
    			this.selectorDead.setSelected(this.getGameSet().getPlayers().getNextPlayer(formerDeadPlayer));
    			return true;
    		}
    	}
    	return false;
	}

	/**
	 * Figures out the game type using the given data passed through the intent.
	 */
	private void identifyGameType() {
		
		if (this.getIntent().getExtras() != null && !this.getIntent().getExtras().containsKey(ActivityParams.PARAM_TYPE_OF_GAME)) {
			throw new IllegalArgumentException("type of game must be provided");
		}
		
		this.gameType = GameType.valueOf(this.getIntent().getExtras().getString(ActivityParams.PARAM_TYPE_OF_GAME));
	}
	
	/**
	 * Indicates whether the dead player panel must be displayed.
	 * @return true if the dead player panel must be displayed.
	 */
	private boolean isDisplayDeadPlayerPanel() {
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5 && this.getGameSet().getPlayers().size() == 5) {
			return false;
		}
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot4 && this.getGameSet().getPlayers().size() == 4) {
			return false;
		}
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot3 && this.getGameSet().getPlayers().size() == 3) {
			return false;
		}
		return true;
	}
    
    /**
     * Indicates whether the form is valid.
     * @return
     */
    private boolean isFormValid() {
    	switch(this.gameType) {
			case Standard:
				return this.isStandardFormValid();
			case Belgian:
				return this.isBelgianFormValid();
			case Penalty:
				return this.isPenaltyFormValid();
			case Pass:
				return this.isPassedFormValid();
			default:
				throw new RuntimeException("Incorrect game type");
    	}
	}
    
	/**
     * Creates and returns a game created depending on the type of game and on the form widget values.
     * @return
     */
    private BaseGame createGame() {
    	switch(this.gameType) {
    		case Standard:
    			return this.createStandardGame();
    		case Belgian:
    			return this.createBelgianGame();
    		case Penalty:
    			return this.createPenaltyGame();
    		case Pass:
    			return this.createPassedGame();
    		default:
    			throw new RuntimeException("Incorrect game style type");
    	}
    }
    
	/**
     * Updates the game with the form widget values.
     * @return
     */
    private void updateGame(final BaseGame game) {
    	if (game instanceof StandardTarot5Game) {
    		this.updateStandard5Game((StandardTarot5Game)game);
    	}
    	else if (game instanceof StandardBaseGame) {
    		this.updateStandardGame((StandardBaseGame)game);
    	}
    	else if (game instanceof BelgianTarot3Game) {
    		this.updateBelgian3Game((BelgianTarot3Game)game);
    	}
    	else if (game instanceof BelgianTarot4Game) {
    		this.updateBelgian4Game((BelgianTarot4Game)game);
    	}
    	else if (game instanceof BelgianTarot5Game) {
    		this.updateBelgian5Game((BelgianTarot5Game)game);
    	}
    	else if (game instanceof PenaltyGame) {
    		this.updatePenaltyGame((PenaltyGame)game);
    	}
    	else if (game instanceof PassedGame) {
    		this.updatePassedGame((PassedGame)game);
    	}
    }
    
    /**
     * Initializes the widgets specific to penalty games.
     */
    private void intializePenaltyViews() {
    	// widgets
    	//this.selectorPenalted = (Selector<Player>) findViewById(R.id.selectorPenalted);
		this.txtGlobalPenaltyPoints = (TextView)this.findViewById(R.id.txtGlobalPenaltyPoints);
		this.btnPlusGlobalPenaltyPoints = (Button)this.findViewById(R.id.btnPlusGlobalPenaltyPoints);
		this.btnMinusGlobalPenaltyPoints = (Button)this.findViewById(R.id.btnMinusGlobalPenaltyPoints);
		this.barPlayerPenaltyPoints = (SeekBar) findViewById(R.id.barPlayerPenaltyPoints);
		this.txtPlayerPenaltyPoints = (TextView)this.findViewById(R.id.txtPlayerPenaltyPoints);

		
		// widget contents
		//this.selectorPenalted.setObjects(this.inGamePlayers);
		
		// event handlers
		this.btnPlusGlobalPenaltyPoints.setOnClickListener(this.onIncreasePenaltyPointsClickListener);
		this.btnMinusGlobalPenaltyPoints.setOnClickListener(this.onDecreasePenaltyPointsClickListener);
		this.barPlayerPenaltyPoints.setOnSeekBarChangeListener(this.playerPenaltyPointsChangeListener);
		
		// set bar properties
		this.barPlayerPenaltyPoints.setMax(100);
		this.playerPenaltyPoints = 0;
		
		// synchronizes buttons/edit text with score
		this.updatePenaltyPointsViews();
    }
    
	/**
	 * Initializes the dead player and dealer player panel and all subsequent views for a penalty game.
	 */
	private void initializeDeadAndDealerPanelForPenaltyCase() {
		// if no dead player, case is easy...
		if (!this.isDisplayDeadPlayerPanel()) {
			this.panelDead.setVisibility(View.GONE);
			this.setDealerPlayer();
			return;
		}

		// if not, it becomes more complicated...
		else {
			// load dead player selector with all players
			this.selectorDead.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
			this.selectorDealer.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
			this.selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

				/* (non-Javadoc)
				 * @see Selector.OnObjectSelectedListener#onItemSelected(Player)
				 */
				@Override
				public void onItemSelected(final Player selected) {

					// create new ingame player list
					inGamePlayers = newArrayList(GameCreationActivity.this.getGameSet().getPlayers().getPlayers());
					inGamePlayers.remove(selectorDead.getSelected());
					
					// load player selector with this new player list
					//selectorPenalted.setObjects(inGamePlayers);
					
					// display panels
					panelMainParameters.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panelMainParameters, true));
					txtTitleMainParameters.setOnClickListener(null);
				}				

				/* (non-Javadoc)
				 * @see Selector.OnObjectSelectedListener#onNothingSelected()
				 */
				@Override
				public void onNothingSelected() {
					// hide panels
					panelMainParameters.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panelMainParameters, true));
					txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
				}
			});
			
			// if no dead player was previously selected, hide dealer and all other panels
			if (!this.trySetDeadPlayer()) {
				this.panelDeadAndDealer.setVisibility(View.VISIBLE);
				this.panelMainParameters.setVisibility(View.GONE);
				this.txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
			}
			this.setDealerPlayer();
		}
	}
	
	/**
	 * Initializes the dead player and dealer player panel and all subsequent views for a passed game.
	 */
	private void initializeDeadAndDealerPanelForPassCase() {
		// if no dead player, case is easy...
		if (!this.isDisplayDeadPlayerPanel()) {
			this.panelDead.setVisibility(View.GONE);
			this.setDealerPlayer();
			return;
		}

		// if not, it becomes more complicated...
		else {
			// load dead player selector with all players
			this.selectorDead.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
			this.selectorDealer.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
			this.selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

				/* (non-Javadoc)
				 * @see Selector.OnObjectSelectedListener#onItemSelected(Player)
				 */
				@Override
				public void onItemSelected(final Player selected) {

					// create new ingame player list
					inGamePlayers = newArrayList(GameCreationActivity.this.getGameSet().getPlayers().getPlayers());
					inGamePlayers.remove(selectorDead.getSelected());
				}				

				/* (non-Javadoc)
				 * @see Selector.OnObjectSelectedListener#onNothingSelected()
				 */
				@Override
				public void onNothingSelected() {
				}
			});
			
			// if no dead player was previously selected, hide dealer and all other panels
			if (!this.trySetDeadPlayer()) {
				this.panelDeadAndDealer.setVisibility(View.VISIBLE);
			}
			this.setDealerPlayer();
		}
	}
	
    /**
     * Indicates whether the penalty form is valid : penalty > 0 and penalty modulo (defense player count) = 0.
     * @return
     */
    private boolean isPenaltyFormValid() {
    	if (!this.selectorDealer.isSelected()) {
    		return false;
    	}
    	
    	if (this.playerPenaltyPoints == 0) {
    		return false;
    	}
    	
//    	if (!this.selectorPenalted.isSelected()) {
//    		return false;
//    	}
    	
    	return true;
    }

    /**
     * Indicates whether the passed form is valid.
     * @return
     */
    private boolean isPassedFormValid() {
    	if (!this.selectorDealer.isSelected()) {
    		return false;
    	}
    	
    	return true;
    }
    
	/**
     * Creates and returns a penalty game.
     * @return
     */
    private BaseGame createPenaltyGame() {
		PenaltyGame game = new PenaltyGame();
    	game.setPlayers(this.getGameSet().getPlayers());
    	game.setDeadPlayer(this.selectorDead.getSelected());
    	game.setDealer(this.selectorDealer.getSelected());
		game.setPenaltedPlayer(this.selectorDealer.getSelected());
		game.setPenaltyPoints(this.playerPenaltyPoints * this.playerMultiplicationRate);
		return game;
    }
    
	/**
     * Creates and returns a penalty game.
     * @return
     */
    private void updatePenaltyGame(final PenaltyGame game) {
    	game.setPlayers(new PlayerList(this.inGamePlayers));
    	game.setDeadPlayer(this.selectorDead.getSelected());
    	game.setDealer(this.selectorDealer.getSelected());
		game.setPenaltedPlayer(this.selectorDealer.getSelected());
		game.setPenaltyPoints(this.playerPenaltyPoints * this.playerMultiplicationRate);
    }

	/**
     * Creates and returns a passed game.
     * @return
     */
    private void updatePassedGame(final PassedGame game) {
    	game.setPlayers(new PlayerList(this.inGamePlayers));
    	game.setDeadPlayer(this.selectorDead.getSelected());
    	game.setDealer(this.selectorDealer.getSelected());
    }
    
	/**
     * Creates and returns a passed game.
     * @return
     */
    private PassedGame createPassedGame() {
    	PassedGame game = new PassedGame();
    	game.setPlayers(new PlayerList(this.inGamePlayers));
    	game.setDeadPlayer(this.selectorDead.getSelected());
    	game.setDealer(this.selectorDealer.getSelected());
		return game;
    }
    
    /**
     * Initializes the widgets specific to belgian games.
     */
    @SuppressWarnings("unchecked")
	private void intializeBelgianViews() {
    	this.selectorFirst = (Selector<Player>) findViewById(R.id.galleryFirst);
    	this.selectorSecond = (Selector<Player>) findViewById(R.id.gallerySecond);
    	this.selectorThird = (Selector<Player>) findViewById(R.id.galleryThird);
    	this.panelFourth = (RelativeLayout) findViewById(R.id.panelFourth);
    	this.selectorFourth = (Selector<Player>) findViewById(R.id.galleryFourth);
    	this.panelFifth = (RelativeLayout) findViewById(R.id.panelFifth);
    	this.selectorFifth = (Selector<Player>) findViewById(R.id.galleryFifth);
    	
    	this.selectorFirst.setObjects(this.inGamePlayers);
    	this.selectorSecond.setObjects(this.inGamePlayers);
    	this.selectorThird.setObjects(this.inGamePlayers);
    	this.selectorFourth.setObjects(this.inGamePlayers);
    	this.selectorFifth.setObjects(this.inGamePlayers);
    	
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot4) {
			this.panelFourth.setVisibility(View.VISIBLE);
		}
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			this.panelFourth.setVisibility(View.VISIBLE);
			this.panelFifth.setVisibility(View.VISIBLE);
		}
    }
    
	/**
	 * Initializes the dead player and dealer player panel and all subsequent views for a belgian game.
	 */
	private void initializeDeadAndDealerPanelForBelgianCase() {
		// if no dead player, case is easy...
		if (!this.isDisplayDeadPlayerPanel()) {
			this.panelDead.setVisibility(View.GONE);
			this.setDealerPlayer();
			return;
		}

		// if not, it becomes more complicated...
		else {
			// load dead player selector with all players
			this.selectorDead.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
			this.selectorDealer.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
			
			this.selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

				/* (non-Javadoc)
				 * @see Selector.OnObjectSelectedListener#onItemSelected(Player)
				 */
				@Override
				public void onItemSelected(final Player selected) {

					// create new ingame player list
					inGamePlayers = newArrayList(GameCreationActivity.this.getGameSet().getPlayers().getPlayers());
					inGamePlayers.remove(selectorDead.getSelected());
					
					// load player selector with this new player list
					selectorFirst.setObjects(inGamePlayers);
					selectorSecond.setObjects(inGamePlayers);
					selectorThird.setObjects(inGamePlayers);
					selectorFourth.setObjects(inGamePlayers);
					selectorFifth.setObjects(inGamePlayers);
					
					// display panels
					panelMainParameters.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panelMainParameters, true));
					txtTitleMainParameters.setOnClickListener(null);
				}				

				/* (non-Javadoc)
				 * @see Selector.OnObjectSelectedListener#onNothingSelected()
				 */
				@Override
				public void onNothingSelected() {
					// hide panels
					panelMainParameters.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panelMainParameters, true));
					txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
				}
			});
			
			
			// if no dead player was previously selected, hide dealer and all other panels
			if (!this.trySetDeadPlayer()) {
				this.panelDeadAndDealer.setVisibility(View.VISIBLE);
				this.panelMainParameters.setVisibility(View.GONE);
				this.txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
			}
			this.setDealerPlayer();
		}
	}
    
    /**
     * Indicates whether the belgian form is valid.
     * @return
     */
    private boolean isBelgianFormValid() {
    	switch(this.getGameSet().getGameStyleType()) {
			case Tarot3:
				return this.isBelgian3GameValid();
			case Tarot4:
				return this.isBelgian4GameValid();
			case Tarot5:
				return this.isBelgian5GameValid();
			default:
				throw new RuntimeException("Incorrect game style type");
    	}
	}
    
    /**
     * Tells whether the belgian 5 form is valid.
     * @return
     */
	private boolean isBelgian5GameValid() {
		boolean isValid = this.isBelgian4GameValid();

    	// fifth
		isValid = isValid && this.selectorFifth.isSelected();
		
		// 5 != 1
		isValid = isValid && this.selectorFifth.getSelected() != this.selectorFirst.getSelected();
		
		// 5 != 2
		isValid = isValid && this.selectorFifth.getSelected() != this.selectorSecond.getSelected();

		// 5 != 3
		isValid = isValid && this.selectorFifth.getSelected() != this.selectorThird.getSelected();
		
		// 5 != 4
		isValid = isValid && this.selectorFifth.getSelected() != this.selectorFourth.getSelected();		
		
		return isValid;
	}

    /**
     * Tells whether the belgian 4 form is valid.
     * @return
     */
	private boolean isBelgian4GameValid() {
		boolean isValid = this.isBelgian3GameValid();
		
    	// fourth
		isValid = isValid && this.selectorFourth.isSelected();
		
		// 4 != 1
		isValid = isValid && this.selectorFourth.getSelected() != this.selectorFirst.getSelected();
		
		// 4 != 2
		isValid = isValid && this.selectorFourth.getSelected() != this.selectorSecond.getSelected();

		// 4 != 3
		isValid = isValid && this.selectorFourth.getSelected() != this.selectorThird.getSelected();
		
		return isValid;
	}

    /**
     * Tells whether the belgian 3 form is valid.
     * @return
     */
	private boolean isBelgian3GameValid() {
		boolean isValid = true;
    	
    	// first
		isValid = isValid && this.selectorFirst.isSelected();

		// second
		isValid = isValid && this.selectorSecond.isSelected();
		
		// third
		isValid = isValid && this.selectorThird.isSelected();
		
		// 1 != 2
		isValid = isValid && this.selectorFirst.getSelected() != this.selectorSecond.getSelected();
		
		// 1 != 3
		isValid = isValid && this.selectorFirst.getSelected() != this.selectorThird.getSelected();

		// 2 != 3
		isValid = isValid && this.selectorSecond.getSelected() != this.selectorThird.getSelected();
		
		return isValid;
	}
	
	/**
     * Creates and returns a belgian game created depending on the type of game and on the form widget values.
     * @return
     */
    private BaseGame createBelgianGame() {
    	switch(this.getGameSet().getGameStyleType()) {
    		case Tarot3:
    			return this.createBelgian3Game();
    		case Tarot4:
    			return this.createBelgian4Game();
    		case Tarot5:
    			return this.createBelgian5Game();
    		default:
    			throw new RuntimeException("Incorrect game style type");
    	}
    }
    
    /**
     * Creates and return a belgian 5 game.
     * @return
     */
    private BelgianTarot5Game createBelgian5Game() {
    	BelgianTarot5Game game = new BelgianTarot5Game();
		this.setCommonBelgianProperties(game);
		
		// first
		game.setFirst(this.selectorFirst.getSelected());
		
		// second
		game.setSecond(this.selectorSecond.getSelected());
		
		// third
		game.setThird(this.selectorThird.getSelected());

		// fourth
		game.setFourth(this.selectorFourth.getSelected());
		
		// fifth
		game.setFifth(this.selectorFifth.getSelected());

		return game;
	}

    /**
     * Creates and return a belgian 4 game.
     * @return
     */
	private BelgianTarot4Game createBelgian4Game() {
		BelgianTarot4Game game = new BelgianTarot4Game();
		this.setCommonBelgianProperties(game);
		
		// first
		game.setFirst(this.selectorFirst.getSelected());
		
		// second
		game.setSecond(this.selectorSecond.getSelected());
		
		// third
		game.setThird(this.selectorThird.getSelected());

		// fourth
		game.setFourth(this.selectorFourth.getSelected());

		return game;
	}

    /**
     * Creates and return a belgian 3 game.
     * @return
     */
	private BelgianTarot3Game createBelgian3Game() {
		BelgianTarot3Game game = new BelgianTarot3Game();
		this.setCommonBelgianProperties(game);
		
		// first
		game.setFirst(this.selectorFirst.getSelected());
		
		// second
		game.setSecond(this.selectorSecond.getSelected());
		
		// third
		game.setThird(this.selectorThird.getSelected());
		return game;
	}
	
    /**
     * Updates and returns a belgian 5 game.
     * @return
     */
    private void updateBelgian5Game(final BelgianTarot5Game game) {
		this.setCommonBelgianProperties(game);
		
		// first
		game.setFirst(this.selectorFirst.getSelected());
		
		// second
		game.setSecond(this.selectorSecond.getSelected());
		
		// third
		game.setThird(this.selectorThird.getSelected());

		// fourth
		game.setFourth(this.selectorFourth.getSelected());
		
		// fifth
		game.setFifth(this.selectorFifth.getSelected());
	}

    /**
     * Updates and returns a belgian 4 game.
     * @return
     */
	private void updateBelgian4Game(final BelgianTarot4Game game) {
		this.setCommonBelgianProperties(game);
		
		// first
		game.setFirst(this.selectorFirst.getSelected());
		
		// second
		game.setSecond(this.selectorSecond.getSelected());
		
		// third
		game.setThird(this.selectorThird.getSelected());

		// fourth
		game.setFourth(this.selectorFourth.getSelected());
	}

    /**
     * Updates and returns a belgian 3 game.
     * @return
     */
	private void updateBelgian3Game(final BelgianTarot3Game game) {
		this.setCommonBelgianProperties(game);
		
		// first
		game.setFirst(this.selectorFirst.getSelected());
		
		// second
		game.setSecond(this.selectorSecond.getSelected());
		
		// third
		game.setThird(this.selectorThird.getSelected());
	}
	
    /**
     * Sets the common properties of a belgian game.
     * @param game
     */
    private void setCommonBelgianProperties(final BaseGame game) {
    	if (game == null) {
    		throw new IllegalArgumentException("game is null");
    	}
    	
        // game players
    	game.setPlayers(new PlayerList(this.inGamePlayers));

        // dead player
    	if (this.selectorDead.isSelected()) {
    		game.setDeadPlayer(this.selectorDead.getSelected());	
    	}
		
		// dealer player
    	game.setDealer(this.selectorDealer.getSelected());
    }
    
    /**
     * Initializes the widgets specific to belgian games.
     */
	@SuppressWarnings("unchecked")
	private void intializeStandardViews() {
		// Main Parameters widgets	
		this.selectorBet = (Selector<Bet>) findViewById(R.id.galleryBet);
		this.selectorLeader = (Selector<Player>) findViewById(R.id.galleryLeader);
		this.panelCalled = (RelativeLayout) findViewById(R.id.panelCalled);
		this.selectorCalled = (Selector<Player>) findViewById(R.id.galleryCalled);
		this.panelKing = (RelativeLayout) findViewById(R.id.panelKing);
		this.selectorKing = (Selector<King>) findViewById(R.id.galleryKing);
		this.selectorOudlers = (Selector<Integer>) findViewById(R.id.galleryOudlers);
		this.barAttackPoints = (SeekBar) findViewById(R.id.barAttackPoints);
		this.txtAttackPoints = (TextView)this.findViewById(R.id.txtAttackPoints);
		this.btnPlusAttackPoints = (Button)this.findViewById(R.id.btnPlusAttackPoints);
		this.btnMinusAttackPoints = (Button)this.findViewById(R.id.btnMinusAttackPoints);
		this.barDefensePoints = (SeekBar) findViewById(R.id.barDefensePoints);
		this.txtDefensePoints = (TextView)this.findViewById(R.id.txtDefensePoints);
		this.btnPlusDefensePoints = (Button)this.findViewById(R.id.btnPlusDefensePoints);
		this.btnMinusDefensePoints = (Button)this.findViewById(R.id.btnMinusDefensePoints);

		// Annoucements widgets
		this.panelAnnouncements = (LinearLayout) findViewById(R.id.panelAnnouncements);
		this.txtTitleAnnouncements = (TextView) findViewById(R.id.txtTitleAnnouncements);		
		this.selectorHandful = (Selector<Team>) findViewById(R.id.galleryHandful);
		this.selectorDoubleHandful = (Selector<Team>) findViewById(R.id.galleryDoubleHandful);
		this.selectorTripleHandful = (Selector<Team>) findViewById(R.id.galleryTribleHandful);
		this.panelMisery = (RelativeLayout) findViewById(R.id.panelMisery);
		this.selectorMisery = (Selector<Player>) findViewById(R.id.galleryMisery);
		this.selectorKidAtTheEnd = (Selector<Team>) findViewById(R.id.galleryKidAtTheEnd);
		this.selectorSlam = (Selector<Chelem>) findViewById(R.id.gallerySlam);
	
		// loading widget contents
		List<Bet> availableBets = newArrayList();
		if (AppContext.getApplication().getAppParams().isPriseAuthorized()) {
			if (AppContext.getApplication().getAppParams().isPetiteAuthorized()) {
				availableBets.add(Bet.PETITE);
			}
			availableBets.add(Bet.PRISE);
		}
		availableBets.add(Bet.GARDE);
		availableBets.add(Bet.GARDESANS);
		availableBets.add(Bet.GARDECONTRE);
		
		this.selectorBet.setObjects(availableBets);
		this.selectorLeader.setObjects(this.inGamePlayers);
		this.selectorCalled.setObjects(this.inGamePlayers);
		this.selectorKing.setObjects(newArrayList(King.CLUB, King.DIAMOND, King.HEART, King.SPADE));
		this.selectorOudlers.setObjects(newArrayList(0, 1, 2, 3));
		this.barAttackPoints.setProgress(0);
		this.barDefensePoints.setProgress(91);
		this.selectorHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM, Team.BOTH_TEAMS));
		this.selectorDoubleHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorTripleHandful.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorKidAtTheEnd.setObjects(newArrayList(Team.LEADING_TEAM, Team.DEFENSE_TEAM));
		this.selectorMisery.setObjects(this.inGamePlayers);
		this.selectorSlam.setObjects(newArrayList(Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED, Chelem.CHELEM_ANOUNCED_AND_FAILED, Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED));
		
		// event handers
		this.barAttackPoints.setOnSeekBarChangeListener(this.attackPointsChangeListener);
		this.barDefensePoints.setOnSeekBarChangeListener(this.defensePointsChangeListener);
		this.btnPlusAttackPoints.setOnClickListener(this.onIncreaseAttackScoreClickListener);
		this.btnMinusAttackPoints.setOnClickListener(this.onDecreaseAttackScoreClickListener);
		this.btnPlusDefensePoints.setOnClickListener(this.onDecreaseAttackScoreClickListener);
		this.btnMinusDefensePoints.setOnClickListener(this.onIncreaseAttackScoreClickListener);
		
		// set tarot 5 widgets visibilities
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			this.panelCalled.setVisibility(View.VISIBLE);
			this.panelKing.setVisibility(View.VISIBLE);
			this.panelMisery.setVisibility(View.VISIBLE);
		}
		
		// set misery visibility at 3/4 player if parameters say so
		if (AppContext.getApplication().getAppParams().isMiseryAuthorized()) {
			this.panelMisery.setVisibility(View.VISIBLE);
		}
		
		// synchronizes buttons/edit text with score
		this.attackScore = 0;
		this.updatePointsViews();
	}

	/**
	 * Initializes the dead player and dealer player panel and all subsequent views for a standard game.
	 */
	private void initializeDeadAndDealerPanelForStandardCase() {
		// if no dead player, case is easy...
		if (!this.isDisplayDeadPlayerPanel()) {
			this.panelDead.setVisibility(View.GONE);
			this.setDealerPlayer();
			return;
		}

		// if not, it becomes more complicated...
		else {
			// load dead player selector with all players
			this.selectorDead.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
			this.selectorDealer.setObjects(newArrayList(this.getGameSet().getPlayers().getPlayers()));
			
			// load/hide the rest of the views when a dead player is selected
			this.selectorDead.setObjectSelectedListener(new Selector.OnObjectSelectedListener<Player>() {

				/* (non-Javadoc)
				 * @see Selector.OnObjectSelectedListener#onItemSelected(Player)
				 */
				@Override
				public void onItemSelected(final Player selected) {
					// create new ingame player list
					inGamePlayers = newArrayList(GameCreationActivity.this.getGameSet().getPlayers().getPlayers());
					inGamePlayers.remove(selectorDead.getSelected());
					
					// load player selector with this new player list
					selectorLeader.setObjects(inGamePlayers);
					selectorCalled.setObjects(inGamePlayers);
					selectorMisery.setObjects(inGamePlayers);
					
					// display panels
					panelMainParameters.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panelMainParameters, true));
					panelAnnouncements.startAnimation(new ScaleAnimToShow(1.0f, 1.0f, 1.0f, 0.0f, 500, panelAnnouncements, true));
					txtTitleMainParameters.setOnClickListener(null);
					txtTitleAnnouncements.setOnClickListener(null);					
				}

				/* (non-Javadoc)
				 * @see Selector.OnObjectSelectedListener#onNothingSelected()
				 */
				@Override
				public void onNothingSelected() {
					// hide panels
					panelMainParameters.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panelMainParameters, true));
					panelAnnouncements.startAnimation(new ScaleAnimToHide(1.0f, 1.0f, 1.0f, 0.0f, 500, panelAnnouncements, true));
					txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
					txtTitleAnnouncements.setOnClickListener(onNoDeadPlayerSelectedClickListener);
				}
			});
			
			// if no dead player was previously selected, hide dealer and all other panels
			if (!this.trySetDeadPlayer()) {
				this.panelDeadAndDealer.setVisibility(View.VISIBLE);
				this.panelMainParameters.setVisibility(View.GONE);
				this.txtTitleMainParameters.setOnClickListener(onNoDeadPlayerSelectedClickListener);
			}
			this.setDealerPlayer();
		}
	}
	
    /**
     * Update the different point views.
     */
	private void updatePointsViews() {
        // sets attack and defense default points values
        this.txtAttackPoints.setText(new Integer(this.attackScore).toString());
        this.barAttackPoints.setProgress(this.attackScore);
        this.txtDefensePoints.setText(new Integer(91 - this.attackScore).toString());
        this.barDefensePoints.setProgress(91 - this.attackScore);
        
        // disable/enable -/+ buttons depending on the score 
        if (this.attackScore == 91) {
        	this.btnPlusAttackPoints.setEnabled(false);
        	this.btnMinusDefensePoints.setEnabled(false);
        	this.btnMinusAttackPoints.setEnabled(true);
        	this.btnPlusDefensePoints.setEnabled(true);
        }
        else if (this.attackScore == 0) {
        	this.btnMinusAttackPoints.setEnabled(false);
        	this.btnPlusDefensePoints.setEnabled(false);
        	this.btnPlusAttackPoints.setEnabled(true);
        	this.btnMinusDefensePoints.setEnabled(true);
        }
        else {
        	this.btnMinusAttackPoints.setEnabled(true);
        	this.btnPlusDefensePoints.setEnabled(true);
        	this.btnPlusAttackPoints.setEnabled(true);
        	this.btnMinusDefensePoints.setEnabled(true);        	
        }
    }
	
	/**
	 * Computes the global penalty points depending on the playerpenalty points.
	 * @return
	 */
	private int computeGlobalPenaltyPoints() {
		return this.playerPenaltyPoints * this.playerMultiplicationRate;
	}
	
	/**
	 * Update the penalty point views.
	 */
	private void updatePenaltyPointsViews() {
		// set zoom rate
		this.playerMultiplicationRate = this.getGameSet().getPlayers().size() - 1;
		
        // set player point values
        this.txtPlayerPenaltyPoints.setText(new Integer(this.playerPenaltyPoints).toString());
        this.barPlayerPenaltyPoints.setProgress(this.playerPenaltyPoints);

		// set global point values
        this.txtGlobalPenaltyPoints.setText(new Integer(this.computeGlobalPenaltyPoints()).toString());
        
        // disable/enable -/+ buttons depending on the player penalty points 
        if (this.playerPenaltyPoints == 100) {
        	this.btnPlusGlobalPenaltyPoints.setEnabled(false);
        	this.btnMinusGlobalPenaltyPoints.setEnabled(true);
        }
        else if (this.playerPenaltyPoints == 0) {
        	this.btnMinusGlobalPenaltyPoints.setEnabled(false);
        	this.btnPlusGlobalPenaltyPoints.setEnabled(true);
        }
        else {
        	this.btnMinusGlobalPenaltyPoints.setEnabled(true);
        	this.btnPlusGlobalPenaltyPoints.setEnabled(true);
        }
	}
	
    /**
     * Indicates whether the standard form is valid.
     * @return
     */
    private boolean isStandardFormValid() {
    	switch(this.getGameSet().getGameStyleType()) {
			case Tarot3:
				return this.isStandard3GameValid();
			case Tarot4:
				return this.isStandard4GameValid();
			case Tarot5:
				return this.isStandard5GameValid();
			default:
				throw new RuntimeException("Incorrect game style type");
    	}
	}
    
	/**
     * Creates and returns a standard game created depending on the type of game and on the form widget values.
     * @return
     */
    private BaseGame createStandardGame() {
    	switch(this.getGameSet().getGameStyleType()) {
    		case Tarot3:
    			return this.createStandard3Game();
    		case Tarot4:
    			return this.createStandard4Game();
    		case Tarot5:
    			return this.createStandard5Game();
    		default:
    			throw new RuntimeException("Incorrect game style type");
    	}
    }
    
    /**
     * Tells whether the form values are correct for a tarot 3 game.
     * @return
     */
	private boolean isStandard3GameValid() {
		return this.areCommonStandardPropertiesValid();
	}

    /**
     * Tells whether the form values are correct for a tarot 4 game.
     * @return
     */
	private boolean isStandard4GameValid() {
		return this.areCommonStandardPropertiesValid();
	}
    
    /**
     * Tells whether the form values are correct for a tarot 5 game.
     * @return
     */
    private boolean isStandard5GameValid() {
		boolean isValid = this.areCommonStandardPropertiesValid();
		
		// king
		isValid = isValid && this.selectorKing.isSelected();
		
		// called playerd
		isValid = isValid && this.selectorCalled.isSelected();
		
		return isValid;
	}
    
    /**
     * Indicates whether common properties are valid.
     * @return
     */
    private boolean areCommonStandardPropertiesValid() {
		boolean isValid = true;
    	
    	// dealer player
		isValid = isValid && this.selectorDealer.isSelected();

		// bet
		isValid = isValid && this.selectorBet.isSelected();
		
		// oudlers
		isValid = isValid && this.selectorOudlers.isSelected();
		
		// leader player
		isValid = isValid && this.selectorLeader.isSelected();
		
		return isValid;
    }

    /**
     * Creates and returns a StandardTarot4Game.
     * @return
     */    
    private StandardTarot3Game createStandard3Game() {
		// game to return
		StandardTarot3Game toReturn = new StandardTarot3Game();
		
		// sets the common properties
		this.setCommonStandardProperties(toReturn);
		
		return toReturn;
    }

    /**
     * Creates and returns a StandardTarot4Game.
     * @return
     */    
    private StandardTarot4Game createStandard4Game() {
		// game to return
		StandardTarot4Game toReturn = new StandardTarot4Game();
		
		// sets the common properties
		this.setCommonStandardProperties(toReturn);
		
		return toReturn;
    }
    
    /**
     * Creates and returns a StandardTarot5Game.
     * @return
     */ 
    private StandardTarot5Game createStandard5Game() {
		// game to return
		StandardTarot5Game toReturn = new StandardTarot5Game();
		
		// sets the common properties
		this.setCommonStandardProperties(toReturn);

		// king
		toReturn.setCalledKing(this.selectorKing.getSelected());
	
		// called player
		toReturn.setCalledPlayer(this.selectorCalled.getSelected());
				

		return toReturn;
	}
    
    /**
     * Updates the properties of a standard 3 or 4 games.
     * @param game
     */
    private void updateStandardGame(final StandardBaseGame game) {
    	// sets the common properties
    	this.setCommonStandardProperties(game);
    }

    /**
     * Updates the properties of a standard 5 games.
     * @param game
     */
    private void updateStandard5Game(final StandardTarot5Game game) {
    	// sets the common properties
    	this.setCommonStandardProperties(game);
		// king
    	game.setCalledKing(this.selectorKing.getSelected());
	
		// called player
    	game.setCalledPlayer(this.selectorCalled.getSelected());
    }
    
    /**
     * Sets the common properties of a StandardBaseGame.
     * @param game
     */
    private void setCommonStandardProperties(final StandardBaseGame game) {
    	if (game == null) {
    		throw new IllegalArgumentException("game is null");
    	}
    	
        // game players
    	game.setPlayers(new PlayerList(this.inGamePlayers));

        // dead player
//    	if (this.selectorDead.isSelected()) {
//    		game.setDeadPlayer(this.selectorDead.getSelected());	
//    	}
    	game.setDeadPlayer(this.selectorDead.getSelected());
		
		// dealer player
    	game.setDealer(this.selectorDealer.getSelected());

		// bet
    	game.setBet(this.selectorBet.getSelected());
		
		// oudlers
    	game.setNumberOfOudlers(this.selectorOudlers.getSelected());
		
		// leader player
    	game.setLeadingPlayer(this.selectorLeader.getSelected());
    	
		// attack score 
		game.setPoints((double)this.barAttackPoints.getProgress());
		
		// handful
		game.setTeamWithPoignee(this.selectorHandful.getSelected());
		
		// double handful
		game.setTeamWithDoublePoignee(this.selectorDoubleHandful.getSelected());
		
		// triple handful
		game.setTeamWithTriplePoignee(this.selectorTripleHandful.getSelected());
		
		// misery
		if (this.selectorMisery.isSelected()) {
			PlayerList playersWithMisery = new PlayerList();
			playersWithMisery.add(this.selectorMisery.getSelected());
			game.setPlayersWithMisery(playersWithMisery);
		}
		else {
			game.setPlayersWithMisery(null);
		}
		
		// kid at the end
		game.setTeamWithKidAtTheEnd(this.selectorKidAtTheEnd.getSelected());
		
		// slam
		game.setChelem(this.selectorSlam.getSelected());
    }
    
	/**
	 * Displays the dead and dealer panels.
	 */
	@SuppressWarnings("unchecked")
	private void displayDeadAndDealer() {
		
		// initialize dealer player widgets
		this.panelDealer = (RelativeLayout) this.findViewById(R.id.panelDealer);
		if (this.game.getDealer() != null) {
			this.panelDealer.setVisibility(View.VISIBLE);
			this.selectorDealer = (Selector<Player>) this.findViewById(R.id.galleryDealer);
			this.selectorDealer.setObjects(this.getGameSet().getPlayers().getPlayers());
			this.selectorDealer.setSelected(this.game.getDealer());
		}
		else {
			this.panelDealer.setVisibility(View.GONE); 
		}
		
		// initialize dead player widgets
		this.panelDead = (RelativeLayout) this.findViewById(R.id.panelDead);
		if (this.game.getDeadPlayer() != null) {
			this.panelDead.setVisibility(View.VISIBLE);
			this.selectorDead = (Selector<Player>) this.findViewById(R.id.galleryDead);
			this.selectorDead.setObjects(this.getGameSet().getPlayers().getPlayers());
			this.selectorDead.setSelected(this.game.getDeadPlayer());
		}
		else {
			this.panelDead.setVisibility(View.GONE);
		}
		
		// if neither dealer nor dead players are set, don't display the section
		if (this.game.getDealer() == null && this.game.getDeadPlayer() == null) {
			this.panelDeadAndDealerSection = (LinearLayout) this.findViewById(R.id.panelDeadAndDealerSection);
			this.panelDeadAndDealerSection.setVisibility(View.GONE);
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
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorThird.setSelected(belgianGame.getThird());
    	}
    	else if (this.game instanceof BelgianTarot4Game) {
    		BelgianTarot4Game belgianGame = (BelgianTarot4Game)this.game;
        	this.selectorFirst.setSelected(belgianGame.getFirst());
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorThird.setSelected(belgianGame.getThird());
        	this.selectorFourth.setSelected(belgianGame.getFourth());
    	}
    	else if (this.game instanceof BelgianTarot5Game) {
    		BelgianTarot5Game belgianGame = (BelgianTarot5Game)this.game;
        	this.selectorFirst.setSelected(belgianGame.getFirst());
        	this.selectorSecond.setSelected(belgianGame.getSecond());
        	this.selectorThird.setSelected(belgianGame.getThird());
        	this.selectorFourth.setSelected(belgianGame.getFourth());
        	this.selectorFifth.setSelected(belgianGame.getFifth());
    	}
    }
	
	/**
     * Displays the standard game.
     */
    private void displayStandardGame() {
    	StandardBaseGame stdGame = (StandardBaseGame)this.game;
		
    	// common properties
		this.selectorBet.setSelected(stdGame.getBet());
		this.selectorLeader.setSelected(stdGame.getLeadingPlayer());
		this.selectorOudlers.setSelected(stdGame.getNumberOfOudlers());
		this.barAttackPoints.setProgress((int)stdGame.getPoints());
		this.txtAttackPoints.setText(new Integer((int)stdGame.getPoints()).toString());
		this.barDefensePoints.setProgress(91 - (int)stdGame.getPoints());
		this.txtDefensePoints.setText(new Integer(91 - (int)stdGame.getPoints()).toString());
		
		// announcements
		this.panelAnnouncements.setVisibility(View.VISIBLE);
		this.selectorHandful.setSelected(stdGame.getTeamWithPoignee());
		this.selectorDoubleHandful.setSelected(stdGame.getTeamWithDoublePoignee());
		this.selectorTripleHandful.setSelected(stdGame.getTeamWithTriplePoignee());
		this.selectorKidAtTheEnd.setSelected(stdGame.getTeamWithKidAtTheEnd());
		this.selectorSlam.setSelected(stdGame.getChelem());

		// 5 player specifics
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			StandardTarot5Game std5Game = (StandardTarot5Game)this.game;
			this.panelCalled.setVisibility(View.VISIBLE);
			this.panelKing.setVisibility(View.VISIBLE);
			this.panelMisery.setVisibility(View.VISIBLE);
			this.selectorCalled.setSelected(std5Game.getCalledPlayer());
			this.selectorKing.setSelected(std5Game.getCalledKing());
			this.selectorMisery.setSelected(std5Game.getPlayerWithMisery());
		}
		
		// display misery if game 3/4 player has a misery set
		if (stdGame.getPlayerWithMisery() != null) {
			this.panelMisery.setVisibility(View.VISIBLE);
			this.selectorMisery.setSelected(stdGame.getPlayerWithMisery());
		}
    }
    
	/**
     * Displays the penalty game.
     * @return
     */
    private void displayPenaltyGame() {
		PenaltyGame penaltyGame = (PenaltyGame)this.game;
    	//this.selectorPenalted.setSelected(penaltyGame.getPenaltedPlayer());
    	//this.barPlayerPenaltyPoints.setProgress(penaltyGame.getPenaltyPoints());
    	this.playerPenaltyPoints = penaltyGame.getPenaltyPoints() / this.playerMultiplicationRate;
    	this.updatePenaltyPointsViews();
    }
    
    /**
     * An enumeration of all game types.
     */
    public enum GameType {
    	/**
    	 * Standard.
    	 */
    	Standard,
    	
    	/**
    	 * Belgian.
    	 */
    	Belgian, 

    	/**
    	 * Penalty.
    	 */
    	Penalty,
    	
    	/**
    	 * Passed
    	 */
    	Pass
    }
    
	/**
	 *	An AsyncTask aimed to create and store the game.
	 */
	private class PersistGameTask extends AsyncTask<BaseGame, Void, Void> {

		/**
		 * The context.
		 */
		private Context context;

		/**
		 * A progress dialog shown during the game creation and storage.
		 */
		private final ProgressDialog dialog;

		/**
		 * Flag indicating whether an error occured in the background. 
		 */
		private boolean backgroundErrorHappened;

		/**
		 * Potential exception that happened in the background.
		 */
		private Exception backgroundException;

		/**
		 * Constructor using a context.
		 * @param context the android context.
		 */
		protected PersistGameTask(final Context context) {
			checkArgument(context != null, "context is null");
			this.context = context;
			this.dialog = new ProgressDialog(this.context);
			this.backgroundErrorHappened = false;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			String message = isInEditMode ? this.context.getResources().getString(R.string.msgGameUpdate) : this.context.getResources().getString(R.string.msgGameCreation);
			this.dialog.setMessage(message);
			this.dialog.show();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(final BaseGame... games) {
			try {
				BaseGame game = games[0];

				// modify the game and the subsequent games  
				if (isInEditMode) {
					List<BaseGame> removedGames = GameCreationActivity.this.getGameSet().removeGameAndAllSubsequentGames(game);
					for(int i = removedGames.size(); i > 0; --i) {
						BaseGame toReInsert = removedGames.get(i-1);
						GameCreationActivity.this.getGameSet().addGame(toReInsert);
					}
				}
				// add the game
				else {
					GameCreationActivity.this.getGameSet().addGame(game);
				}

				// persist the game only if the gameset was previously persisted
				if (GameCreationActivity.this.getGameSet().isPersisted()) {
					
					// update the game
					if (isInEditMode) {
						AppContext.getApplication().getDalService().updateGame(game, GameCreationActivity.this.getGameSet());
					}
					// create the game
					else {
						AppContext.getApplication().getDalService().saveGame(game, GameCreationActivity.this.getGameSet());
					}
					GameCreationActivity.this.getGameSet().setFacebookPostTs(null);
				}
			}
			catch (DalException e) {
				this.backgroundException = e;
				this.backgroundErrorHappened = true;
			}
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final Void unused) {
			// hide busy idicator
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			// display toast if error happened
			if (this.backgroundErrorHappened) {
				AuditHelper.auditError(AuditHelper.ErrorTypes.persistGameTaskError, this.backgroundException, GameCreationActivity.this);
			}
			else {
				String message = this.context.getResources().getString(R.string.msgGameAdded);
				if (isInEditMode) {
					message = this.context.getResources().getString(R.string.msgGameUpdated);
				}

				// display toast 
				Toast.makeText(
						this.context,
						message,
						Toast.LENGTH_SHORT
				).show();
			}
					
//			// go back to main previous activity
//			if (!gameSet.isPersisted()) {
//				Intent resultIntent = new Intent();
//				resultIntent.putExtra(ActivityParams.PARAM_GAMESET_SERIALIZED, gameSet);
//				setResult(ResultCodes.AddGame_Ok, resultIntent);				
//			}
//			else {
				setResult(ResultCodes.AddGame_Ok);
//			}
		    finish();
		}
	}
    
	/**
	 * An animation class to hide smoothly a section. 
	 */
	private class ScaleAnimToHide extends ScaleAnimation
	{
		private View mView;

		private LayoutParams mLayoutParams;

		private int mMarginBottomFromY, mMarginBottomToY;

		private boolean mVanishAfter = false;

		public ScaleAnimToHide(float fromX, float toX, float fromY, float toY, int duration, View view,boolean vanishAfter)
		{
			super(fromX, toX, fromY, toY);
			setDuration(duration);
			mView = view;
			mVanishAfter = vanishAfter;
			mLayoutParams = (LayoutParams) view.getLayoutParams();
			int height = mView.getHeight();
			mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
			mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t)
		{
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f)
			{
				int newMarginBottom = mMarginBottomFromY + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
				mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
				mView.getParent().requestLayout();
			}
			else if (mVanishAfter)
			{
				mView.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * An animation class to display smoothly a section. 
	 */
	private class ScaleAnimToShow extends ScaleAnimation
	{
		private View mView;

		private LayoutParams mLayoutParams;

		private int mMarginBottomFromY, mMarginBottomToY;

		public ScaleAnimToShow(float toX, float fromX, float toY, float fromY, int duration, View view,boolean vanishAfter)
		{
			super(fromX, toX, fromY, toY);
			setDuration(duration);
			mView = view;
			mLayoutParams = (LayoutParams) view.getLayoutParams();
			mView.setVisibility(View.VISIBLE);
			int height = mView.getHeight();
			mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin + height;
			mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) + height;

			mMarginBottomFromY = 0;
			mMarginBottomToY = height;
		}

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t)
		{
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f)
			{
				int newMarginBottom = (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime) - mMarginBottomToY;
				mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,mLayoutParams.rightMargin, newMarginBottom);
				mView.getParent().requestLayout();
			}
		}
	}

}