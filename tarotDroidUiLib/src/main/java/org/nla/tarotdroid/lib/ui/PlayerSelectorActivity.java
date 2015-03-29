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
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.constants.ActivityParams;
import org.nla.tarotdroid.lib.ui.controls.PlayerSelectorRow;
import org.nla.tarotdroid.lib.ui.tasks.StartNewGameSetTask;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.dal.DalException;
import org.nla.tarotdroid.lib.ui.constants.PreferenceConstants;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
 
public class PlayerSelectorActivity extends SherlockActivity {
  
	/**
	 * Player row count.
	 */
	private static final String PLAYER_ROW_COUNT = "player_row_count";
	
	/**
	 * Player name constant.
	 */
	private static final String PLAYER_NAME = "player_name";
	
	/**
	 * Optional player name constant.
	 */
	private static final String OPTIONAL_PLAYER_NAME = "optional_player_name";

	/**
	 * The layout for compulsory players.
	 */
	private LinearLayout layoutCompulsoryPlayers;
	
	/**
	 * The progress dialog.
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * The game style type.
	 */
	private GameStyleType gameStyleType;
	
	/**
	 * The number of added rows.
	 */
	private int rowCount;
	
	/**
	 * The list of compulsory player rows.
	 */
	private List<PlayerSelectorRow> playerSelectorRows;
	
	/**
	 * The optional player row.
	 */
	private PlayerSelectorRow optionalPlayerSelectorRow;
	
	/**
	 * Facebook ui lifecyle manager.
	 */
	private UiLifecycleHelper uiHelper;
	
	/**
	 * The game set to create.
	 */
	private GameSet gameSet;
	
	/**
	 * Facebook session state change callback.
	 */
    private Session.StatusCallback facebookSessionStatusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
        }
    };
	
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	try {
			this.setContentView(R.layout.player_selector);
			
			// create game set stub
			this.gameSet = new GameSet();
			this.gameSet.setGameSetParameters(AppContext.getApplication().initializeGameSetParameters());
			
			// facebook lifecycle objects
	        this.uiHelper = new UiLifecycleHelper(this, facebookSessionStatusCallback);
	        this.uiHelper.onCreate(savedInstanceState);
			
			this.identifyGameSetType();
			this.auditEvent();
			
			// display warning message if more than 5 games are already stored and and app is not limited 
			boolean gameSetNotToBeStored = AppContext.getApplication().isAppLimited() && AppContext.getApplication().getDalService().getGameSetCount() >= 5;   
			if (gameSetNotToBeStored) {
				
				UIHelper.showSimpleRichTextDialog(
                        this,
                        this.getText(R.string.msgGameSetNotStored).toString(),
                        this.getString(R.string.titleGameSetNotStored)
                );
			}			
			
			this.rowCount = 0;
			this.playerSelectorRows = newArrayList();
			this.progressDialog = new ProgressDialog(this);
			this.layoutCompulsoryPlayers = (LinearLayout)this.findViewById(R.id.layoutCompulsoryPlayers);
			this.optionalPlayerSelectorRow = (PlayerSelectorRow)this.findViewById(R.id.optionalPlayerSelectorRow);
			this.optionalPlayerSelectorRow.setPlayerIndex(10);
			
			// facebook session creation
			Session session = Session.getActiveSession();
			if (session == null) {
			    if (savedInstanceState != null) {
			        session = Session.restoreSession(this, null, null, savedInstanceState);
			    }
			    if (session == null || session.isClosed()) {
			        session = new Session(this);
			    }
			    Session.setActiveSession(session);
			}
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
			    session.openForRead(new Session.OpenRequest(this));
			}
			
			this.initializeViews();
		}
        catch (Exception e) {
        	AuditHelper.auditError(AuditHelper.ErrorTypes.playerSelectorActivityError, e, this);
		}
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
    	super.onResume();
    	this.uiHelper.onResume();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	this.uiHelper.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	this.uiHelper.onDestroy();
    }
    
    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putInt(PLAYER_ROW_COUNT, this.rowCount);
    	for (int i = 0; i < this.rowCount; ++i) {
    		PlayerSelectorRow playerSelectorRow = playerSelectorRows.get(i);
			outState.putString(i + PLAYER_NAME, playerSelectorRow.getPlayerName());
    	}
    	
    	if (this.optionalPlayerSelectorRow.getPlayerName() != null && !this.optionalPlayerSelectorRow.getPlayerName().equals("")) {
    		outState.putString(OPTIONAL_PLAYER_NAME, this.optionalPlayerSelectorRow.getPlayerName());
    	}
    	this.uiHelper.onSaveInstanceState(outState);
    };
    
    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onRestoreInstanceState(android.os.Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
        	int formerRowCount = savedInstanceState.getInt(PLAYER_ROW_COUNT);
        	for (int i = 0; i < formerRowCount; ++i) {
        		PlayerSelectorRow playerSelectorRow = this.playerSelectorRows.get(i);
    			playerSelectorRow.setPlayerName(savedInstanceState.getString(i + PLAYER_NAME));
    		}
        }
        
    	if (savedInstanceState.containsKey(OPTIONAL_PLAYER_NAME)) {
    		this.optionalPlayerSelectorRow.setPlayerName(savedInstanceState.getString(OPTIONAL_PLAYER_NAME));
    	}
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
    	super.onStart();
    	AuditHelper.auditSession(this);
    }
    
    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onStop()
     */
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) { 
	    super.onActivityResult(requestCode, resultCode, data);
	    this.uiHelper.onActivityResult(requestCode, resultCode, data);
	}
    
    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
		MenuItem miStartGameSet = menu.add(R.string.lblStartItem);
		miStartGameSet.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		miStartGameSet.setIcon(R.drawable.ic_compose);
		miStartGameSet.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
    			// form isnt valid
    			if (!isFormValid()) {
    				Toast.makeText(
    						PlayerSelectorActivity.this, 
    						AppContext.getApplication().getResources().getString(R.string.msgValidationKo), 
    						Toast.LENGTH_SHORT
    						).show();
    			}
    			// form is valid
    			else {
    				// set game properties
    				setPlayersAndGameStyleType();

    				// create game set and navigate towards tab view
    				new StartNewGameSetTask(
    						PlayerSelectorActivity.this, 
    						PlayerSelectorActivity.this.progressDialog,
    						PlayerSelectorActivity.this.gameSet
    				).execute();
    			}
				return true;
			}
		});
		
		return super.onCreateOptionsMenu(menu);
    }
    
	/**
	 *	Traces creation event. 
	 */
	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayGameSetCreationPage);
	}
	
	/**
	 * Figures out the game type using the given data passed through the intent.
	 */
	private void identifyGameSetType() {
		if (this.getIntent().getExtras() != null && !this.getIntent().getExtras().containsKey(ActivityParams.PARAM_TYPE_OF_GAMESET)) {
			throw new IllegalArgumentException("type of gameset must be provided");
		}
		
		this.gameStyleType = GameStyleType.valueOf(this.getIntent().getExtras().getString(ActivityParams.PARAM_TYPE_OF_GAMESET));
	}
	
	/**
	 * Adds a compulsory player selector row.
	 */
	private void addCompulsoryPlayerRow() {
		PlayerSelectorRow playerSelectorRow = new PlayerSelectorRow(this, this.rowCount);
		this.layoutCompulsoryPlayers.addView(playerSelectorRow);
		this.playerSelectorRows.add(playerSelectorRow);
		this.rowCount += 1;
	}
	
	/**
	 * Initializes the views.
	 */
	private void initializeViews() {
    	switch(this.gameStyleType) {
			case Tarot3:
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				break;
			case Tarot4:
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				break;
			case Tarot5:
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				this.addCompulsoryPlayerRow();
				break;
    	}
	}
	
    /**
     * Sets the players and the game style type. Must be overridden in subclasses.
     */
    protected void setPlayersAndGameStyleType() {
    	
    	// sets the players
    	switch(this.gameStyleType) {
			case Tarot3:
				this.setPlayers3();
				break;
			case Tarot4:
				this.setPlayers4();
				break;
			case Tarot5:
				this.setPlayers5();
				break;
    	}

		// sets the game style type
		this.gameSet.setGameStyleType(this.gameStyleType);
    }
    
    /**
     * Finds a Player by his name in the repository or build a new Player with his name. 
     * @param playerName
     * @return
     */
    private Player getPlayerByName(String playerName) {
    	Player toReturn = null;
    	try {
			toReturn = AppContext.getApplication().getDalService().getPlayerByName(playerName);
		}
    	catch (DalException e) {
    		toReturn = null;
		}
    	if (toReturn == null) {
    		toReturn = new Player(playerName);
    	}
    	
    	return toReturn;
    }
    
    /**
     * Sets the players for a 3 player style game set.
     */
    private void setPlayers3() {
    	// get the player names
    	String player1Name = this.playerSelectorRows.get(0).getPlayerName();
    	String player2Name = this.playerSelectorRows.get(1).getPlayerName();
    	String player3Name = this.playerSelectorRows.get(2).getPlayerName();
    	String player4Name = this.optionalPlayerSelectorRow.getPlayerName();

		// create the player list
		PlayerList playerList = new PlayerList();

		// add first 3 players
		Player player1 = this.getPlayerByName(player1Name);
		Player player2 = this.getPlayerByName(player2Name);
		Player player3 = this.getPlayerByName(player3Name);
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		
		// add potential 4th player
		if (player4Name != null && player4Name.length() != 0) {
			Player player4 = this.getPlayerByName(player4Name);
			playerList.add(player4);
		}
		
		// store player names in preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PreferenceConstants.PrefPlayer1Name, player1Name);
		editor.putString(PreferenceConstants.PrefPlayer2Name, player2Name);
		editor.putString(PreferenceConstants.PrefPlayer3Name, player3Name);
		editor.commit();
		
		// set players in game set
		this.gameSet.setPlayers(playerList);
    }

    /**
     * Sets the players for a 4 player style game set.
     */
    private void setPlayers4() {
    	// get the player names
    	String player1Name = this.playerSelectorRows.get(0).getPlayerName();
    	String player2Name = this.playerSelectorRows.get(1).getPlayerName();
    	String player3Name = this.playerSelectorRows.get(2).getPlayerName();
    	String player4Name = this.playerSelectorRows.get(3).getPlayerName();
    	String player5Name = this.optionalPlayerSelectorRow.getPlayerName();
    	
		// create the player list
		PlayerList playerList = new PlayerList();

		// add first 4 players
		Player player1 = this.getPlayerByName(player1Name);
		Player player2 = this.getPlayerByName(player2Name);
		Player player3 = this.getPlayerByName(player3Name);
		Player player4 = this.getPlayerByName(player4Name);

		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		
		// add potential 5th player
		if (player5Name != null && player5Name.length() != 0) {
			Player player5 = this.getPlayerByName(player5Name);
			playerList.add(player5);
		}
		
		// store player names in preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PreferenceConstants.PrefPlayer1Name, player1Name);
		editor.putString(PreferenceConstants.PrefPlayer2Name, player2Name);
		editor.putString(PreferenceConstants.PrefPlayer3Name, player3Name);
		editor.putString(PreferenceConstants.PrefPlayer4Name, player4Name);
		editor.commit();
		
		// set players in game set
		this.gameSet.setPlayers(playerList);
    }
    
    /**
     * Sets the players for a 5 player style game set.
     */
    private void setPlayers5() {
    	// get the player names
    	String player1Name = this.playerSelectorRows.get(0).getPlayerName();
    	String player2Name = this.playerSelectorRows.get(1).getPlayerName();
    	String player3Name = this.playerSelectorRows.get(2).getPlayerName();
    	String player4Name = this.playerSelectorRows.get(3).getPlayerName();
    	String player5Name = this.playerSelectorRows.get(4).getPlayerName();
    	String player6Name = this.optionalPlayerSelectorRow.getPlayerName();
    	
		// create the player list
		PlayerList playerList = new PlayerList();

		// add first 5 players
		Player player1 = this.getPlayerByName(player1Name);
		Player player2 = this.getPlayerByName(player2Name);
		Player player3 = this.getPlayerByName(player3Name);
		Player player4 = this.getPlayerByName(player4Name);
		Player player5 = this.getPlayerByName(player5Name);
		playerList.add(player1);
		playerList.add(player2);
		playerList.add(player3);
		playerList.add(player4);
		playerList.add(player5);
		
		// add potential 6th player
		if (player6Name != null && player6Name.length() != 0) {
			Player player6 = this.getPlayerByName(player6Name);
			playerList.add(player6);
		}
		
		// store player names in preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(PreferenceConstants.PrefPlayer1Name, player1Name);
		editor.putString(PreferenceConstants.PrefPlayer2Name, player2Name);
		editor.putString(PreferenceConstants.PrefPlayer3Name, player3Name);
		editor.putString(PreferenceConstants.PrefPlayer4Name, player4Name);
		editor.putString(PreferenceConstants.PrefPlayer5Name, player4Name);
		editor.commit();
		
		// set players in game set
		this.gameSet.setPlayers(playerList);
    }
    
    /**
     * Indicates whether the form is valid.
     * @return
     */
    private boolean isFormValid() {
    	switch(this.gameStyleType) {
    		case Tarot3:
    			return this.isForm3Valid();
    		case Tarot4:
    			return this.isForm4Valid();
    		case Tarot5:
    			return this.isForm5Valid();
    		default:
    			return true;
    	}
    }
    
    /**
     * Indicates whether input data is valid for a 3 player style game set.
     */
    protected boolean isForm3Valid() {
    	String player1Name = this.playerSelectorRows.get(0).getPlayerName();
    	String player2Name = this.playerSelectorRows.get(1).getPlayerName();
    	String player3Name = this.playerSelectorRows.get(2).getPlayerName();
    	
    	return
    		!player1Name.trim().equals("")
    		&& !player2Name.trim().equals("")
    		&& !player3Name.trim().equals("")
    		&& !player1Name.equalsIgnoreCase(player2Name)
    		&& !player1Name.equalsIgnoreCase(player3Name)
    		&& !player2Name.equalsIgnoreCase(player3Name);
    }

    /**
     * Indicates whether input data is valid for a 4 player style game set.
     */
    protected boolean isForm4Valid() {
    	String player1Name = this.playerSelectorRows.get(0).getPlayerName();
    	String player2Name = this.playerSelectorRows.get(1).getPlayerName();
    	String player3Name = this.playerSelectorRows.get(2).getPlayerName();
    	String player4Name = this.playerSelectorRows.get(3).getPlayerName();
    	
    	return 
    		!player1Name.trim().equals("")
    		&& !player2Name.trim().equals("")
    		&& !player3Name.trim().equals("")
    		&& !player4Name.trim().equals("")
    		&& !player1Name.equalsIgnoreCase(player2Name)
    		&& !player1Name.equalsIgnoreCase(player3Name)
    		&& !player1Name.equalsIgnoreCase(player4Name)
    		&& !player2Name.equalsIgnoreCase(player3Name)
    		&& !player2Name.equalsIgnoreCase(player4Name)
    		&& !player3Name.equalsIgnoreCase(player4Name);
    }

    /**
     * Indicates whether input data is valid for a 5 player style game set.
     */
    protected boolean isForm5Valid() {
    	String player1Name = this.playerSelectorRows.get(0).getPlayerName();
    	String player2Name = this.playerSelectorRows.get(1).getPlayerName();
    	String player3Name = this.playerSelectorRows.get(2).getPlayerName();
    	String player4Name = this.playerSelectorRows.get(3).getPlayerName();
    	String player5Name = this.playerSelectorRows.get(4).getPlayerName();

    	return 
        		!player1Name.trim().equals("")
        		&& !player2Name.trim().equals("")
        		&& !player3Name.trim().equals("")
        		&& !player4Name.trim().equals("")
        		&& !player5Name.trim().equals("")
        		&& !player1Name.equalsIgnoreCase(player2Name)
        		&& !player1Name.equalsIgnoreCase(player3Name)
        		&& !player1Name.equalsIgnoreCase(player4Name)
        		&& !player1Name.equalsIgnoreCase(player5Name)
        		&& !player2Name.equalsIgnoreCase(player3Name)
        		&& !player2Name.equalsIgnoreCase(player4Name)
        		&& !player2Name.equalsIgnoreCase(player5Name)
        		&& !player3Name.equalsIgnoreCase(player4Name)
    			&& !player3Name.equalsIgnoreCase(player5Name)
    			&& !player4Name.equalsIgnoreCase(player5Name);
    }
}