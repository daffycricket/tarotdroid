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
package org.nla.tarotdroid.lib.app;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.Result;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.dal.IDalService;
import org.nla.tarotdroid.dal.sql.SqliteDalService;
import org.nla.tarotdroid.lib.helpers.BluetoothHelper;
import org.nla.tarotdroid.lib.model.TarotDroidUser;
import org.nla.tarotdroid.lib.ui.constants.PreferenceConstants;
import org.nla.tarotdroid.lib.ui.tasks.LoadDalTask;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public abstract class BaseApp extends Application implements ITarotDroidApp {
	
    /**
     * The DAL Service.
     */
    private IDalService dalService;
    
    /**
     * The application params.
     */
    private AppParams appParams;
    
    /**
     * The bluetooth helper.
     */
    private BluetoothHelper bluetoothHelper;    
    
    /**
     * Flag indicating whether the app is in debug mode.
     */
    private boolean appInDebugMode;
    
    /**
     * The version name.
     */
    private String versionName;
    
    /**
     * The package name.
     */
    private String packageName;
    
    /**
     * The task aimed to instantiate the dal and load elements from it. 
     */
    private LoadDalTask loadDalTask;
    
    /**
     * The last time the app was launched.
     */
    private long lastLaunchTimestamp;
    
    /**
     * The selected facebook user.
     */
    private GraphUser selectedUser;
    
    /**
     * The logged account.
     */
    private GraphUser graphUser;
    
    /**
     * The logged in user.
     */
    private TarotDroidUser tarotDroidUser;
    
    /**
     * The notifications ids for game sets being published.
     */
    private Map<String, Integer> notificationIds;

    /* (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.setApplication(this);
        this.initializeDalService();
        this.retrieveInfosFromManifest();
        this.initializeBiznessStrings();
        this.initializeUser();
        this.setLastLaunchTimestamp();
        this.notificationIds = newHashMap();
    }

	/* (non-Javadoc)
	 * @see ITarotDroidApp#getUuid()
	 */
	@Override
	public UUID getUuid() {
		return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	}

	/* (non-Javadoc)
	 * @see ITarotDroidApp#getServiceName()
	 */
	@Override
	public String getServiceName() {
		return "TarotDroidService";
	}
	
	/* (non-Javadoc)
	 * @see ITarotDroidApp#getCloudDns()
	 */
	public String getCloudDns() {
		if (this.isAppInDebugMode()) {
			return this.getString(R.string.dnsTarotDroidDevCloud);
		}
		else {
			return this.getString(R.string.dnsTarotDroidCloud);
		}
	}
	
	/* (non-Javadoc)
	 * @see ITarotDroidApp#getFacebookCloudUrl()
	 */
	public String getFacebookCloudUrl() {
		if (this.isAppInDebugMode()) {
			return this.getString(R.string.urlTarotDroidFacebookDevCloud);
		}
		else {
			return this.getString(R.string.urlTarotDroidFacebookCloud);
		}
	}
	
	/* (non-Javadoc)
	 * @see ITarotDroidApp#getFacebookAppUrl()
	 */
	public String getFacebookAppUrl() {
		if (this.isAppInDebugMode()) {
			return this.getString(R.string.urlTarotDroidFacebookDevApp);
		}
		else {
			return this.getString(R.string.urlTarotDroidFacebookApp);
		}
	}
	
	@Override
	public GraphUser getLoggedFacebookUser() {
		return this.graphUser;
	}

	/* (non-Javadoc)
	 * @see ITarotDroidApp#setLoggedAccount(android.accounts.Account)
	 */
	@Override
	public void setLoggedFacebookUser(GraphUser graphUser) {
		this.graphUser = graphUser;
	}
	
	/**
	 * Sets the last time the app was launched.
	 */
    private void setLastLaunchTimestamp() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	this.lastLaunchTimestamp = preferences.getLong(PreferenceConstants.PrefDateLastLaunch, 0);
    	
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(PreferenceConstants.PrefDateLastLaunch, System.currentTimeMillis());
        editor.commit();
    }
    
    /* (non-Javadoc)
     * @see ITarotDroidApp#getLastLaunchTimestamp()
     */
    @Override
    public long getLastLaunchTimestamp() {
        return this.lastLaunchTimestamp;
    }
    
    /**
     * Retrieves misc info from the manifest file.
     */
    private void retrieveInfosFromManifest() {
    	try {
			PackageManager manager = this.getPackageManager();
			PackageInfo packageInfo = manager.getPackageInfo(this.getPackageName(), 0);
			this.versionName = packageInfo.versionName;
			this.packageName = packageInfo.packageName;
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			this.appInDebugMode = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE;

		}
    	catch (NameNotFoundException e) {
    		this.appInDebugMode = false;
		}
	}
    
    /**
     * Initializes localized strings related to business objects.
     */
    private void initializeBiznessStrings() {
    	Bet.PETITE.setLabel(this.getString(R.string.petiteDescription));
		Bet.PRISE.setLabel(this.getString(R.string.priseDescription));
		Bet.GARDE.setLabel(this.getString(R.string.gardeDescription));
		Bet.GARDESANS.setLabel(this.getString(R.string.gardeSansDescription));
		Bet.GARDECONTRE.setLabel(this.getString(R.string.gardeContreDescription));
		
		King.HEART.setLabel(this.getString(R.string.lblHeartsColor));
		King.DIAMOND.setLabel(this.getString(R.string.lblDiamondsColor));
		King.SPADE.setLabel(this.getString(R.string.lblSpadesColor));
		King.CLUB.setLabel(this.getString(R.string.lblClubsColor));
		
		Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED.setLabel(this.getString(R.string.lblAnnouncedAndSucceededChelem));
		Chelem.CHELEM_ANOUNCED_AND_FAILED.setLabel(this.getString(R.string.lblAnnouncedAndFailedChelem));
		Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED.setLabel(this.getString(R.string.lblNotAnnouncedButSucceededChelem));
		
		Team.DEFENSE_TEAM.setLabel(this.getString(R.string.lblDefenseTeam));
		Team.LEADING_TEAM.setLabel(this.getResources().getString(R.string.lblLeadingTeam));
		Team.BOTH_TEAMS.setLabel(this.getResources().getString(R.string.lblBothTeams));
		
		Result.SUCCESS.setLabel(this.getString(R.string.lblSuccesses));
		Result.FAILURE.setLabel(this.getString(R.string.lblFailures));
    }
    
    /**
     * Initializes logged in user.
     */
    private void initializeUser() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	String prefTarotDroidUser = preferences.getString(PreferenceConstants.PrefTarotDroidUser, null);
    	if (prefTarotDroidUser != null) {
    		this.tarotDroidUser = new Gson().fromJson(prefTarotDroidUser, TarotDroidUser.class);
    	}
    }
    
    /* (non-Javadoc)
     * @see ITarotDroidApp#isAppInDebugMode()
     */
    @Override
    public boolean isAppInDebugMode() {
    	return this.appInDebugMode;
    }

    /* (non-Javadoc)
     * @see ITarotDroidApp#getAppVersion()
     */
    @Override
    public String getAppVersion() {
    	return this.versionName;
    } 

    /* (non-Javadoc)
     * @see ITarotDroidApp#getAppPackage()
     */
    @Override
    public String getAppPackage() {
    	return this.packageName;
    } 

    /* (non-Javadoc)
     * @see ITarotDroidApp#getDalService()
     */
    @Override
    public IDalService getDalService() {
    	return this.dalService;
    }
    
    /* (non-Javadoc)
     * @see ITarotDroidApp#setDalService(org.nla.tarotdroid.dal.IDalService)
     */
    @Override
    public void setDalService(final IDalService dalService) {
    	this.dalService = dalService;
    }
    
    /* (non-Javadoc)
     * @see ITarotDroidApp#getLoadDalTask()
     */
    @Override
    public LoadDalTask getLoadDalTask() {
    	return this.loadDalTask;
    }
    
    /**
	 * @return the appParams
	 */
    public AppParams getAppParams() {
		if (this.appParams == null) {
			this.initializeAppParams();
		}
    	return this.appParams;
	}

	/* (non-Javadoc)
     * @see ITarotDroidApp#getBluetoothHelper()
     */
    @Override
    public BluetoothHelper getBluetoothHelper() {
    	if (this.bluetoothHelper == null) {
    		this.bluetoothHelper = new BluetoothHelper(this); 
    	}
    	return this.bluetoothHelper;
    }
    
    /**
     * Loads the DAL.
     */
    private void initializeDalService() {
		this.loadDalTask = new LoadDalTask(this);
		this.loadDalTask.execute();
    }

    /* (non-Javadoc)
     * @see ITarotDroidApp#initializeGameSetParameters()
     */
    @Override
	public GameSetParameters initializeGameSetParameters() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		// initiailizes game set params
		GameSetParameters gameSetParameters = new GameSetParameters();
		gameSetParameters.setPetiteBasePoints(preferences.getInt(PreferenceConstants.PrefPetitePoints, 10));
		gameSetParameters.setPetiteRate(preferences.getInt(PreferenceConstants.PrefPetiteRate, 1));
		gameSetParameters.setPriseBasePoints(preferences.getInt(PreferenceConstants.PrefPrisePoints, 25));
		gameSetParameters.setPriseRate(preferences.getInt(PreferenceConstants.PrefPriseRate, 1));
		gameSetParameters.setGardeBasePoints(preferences.getInt(PreferenceConstants.PrefGardePoints, 50));
		gameSetParameters.setGardeRate(preferences.getInt(PreferenceConstants.PrefGardeRate, 2));
		gameSetParameters.setGardeSansBasePoints(preferences.getInt(PreferenceConstants.PrefGardeSansPoints, 100));
		gameSetParameters.setGardeSansRate(preferences.getInt(PreferenceConstants.PrefGardeSansRate, 4));
		gameSetParameters.setGardeContreBasePoints(preferences.getInt(PreferenceConstants.PrefGardeContrePoints, 150));
		gameSetParameters.setGardeContreRate(preferences.getInt(PreferenceConstants.PrefGardeContreRate, 6));
		gameSetParameters.setPoigneePoints(preferences.getInt(PreferenceConstants.PrefPoigneePoints, 20));
		gameSetParameters.setDoublePoigneePoints(preferences.getInt(PreferenceConstants.PrefDoublePoigneePoints, 30));
		gameSetParameters.setTriplePoigneePoints(preferences.getInt(PreferenceConstants.PrefTriplePoigneePoints, 40));
		gameSetParameters.setMiseryPoints(preferences.getInt(PreferenceConstants.PrefMiseryPoints, 10));
		gameSetParameters.setKidAtTheEndPoints(preferences.getInt(PreferenceConstants.PrefKidAtTheEndPoints, 10));
		gameSetParameters.setAnnouncedAndSucceededChelemPoints(preferences.getInt(PreferenceConstants.PrefAnnouncedAndSucceededChelemPoints, 400));
		gameSetParameters.setAnnouncedAndFailedChelemPoints(preferences.getInt(PreferenceConstants.PrefAnnouncedAndFailedChelemPoints, -200));
		gameSetParameters.setNotAnnouncedButSucceededChelemPoints(preferences.getInt(PreferenceConstants.PrefNotAnnouncedButSucceededChelemPoints, 200));
		gameSetParameters.setBelgianBaseStepPoints(preferences.getInt(PreferenceConstants.PrefBelgianStepPoints, 100));

		// initializes app params 
		this.initializeAppParams();
		
		return gameSetParameters;
	}
    
    /**
     * Initializes the application params.
     */
    private void initializeAppParams() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    	this.appParams = new AppParams();
    	
    	this.appParams.setBelgianGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefAreBelgianGamesAllowed, false));
    	this.appParams.setPenaltyGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePenaltyGamesAllowed, false));
    	this.appParams.setPassedGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePassedGamesAllowed, true));
    	this.appParams.setDisplayNextDealer(preferences.getBoolean(PreferenceConstants.PrefDisplayNextDealer, true));
		this.appParams.setPriseAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPriseAuthorized, true));
		this.appParams.setPetiteAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPetiteAuthorized, false));
		this.appParams.setDeadPlayerAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsOneDeadPlayerAuthorized, false));
		this.appParams.setDeadPlayerAutomaticallySelected(preferences.getBoolean(PreferenceConstants.PrefIsDeadPlayerAutomaticallySelected, true));
		this.appParams.setMiseryAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsMiseryAuthorized, false));
		
		// display preferences
		this.appParams.setDisplayGamesInReverseOrder(preferences.getBoolean(PreferenceConstants.PrefDisplayGamesInReverseOrder, true));
		this.appParams.setDisplayGlobalScoresForEachGame(preferences.getBoolean(PreferenceConstants.PrefDisplayGlobalScoresForEachGame, false));
		this.appParams.setKeepScreenOn(preferences.getBoolean(PreferenceConstants.PrefKeepScreenOn, true));
		
		// dev preferences
		this.appParams.setDevSimulationMode(preferences.getBoolean(PreferenceConstants.PrefIsSimulationMode, false));
		this.appParams.setDevGameSetCount(preferences.getInt(PreferenceConstants.PrefDevGameSetCount, 5));
		this.appParams.setDevMaxGameCount(preferences.getInt(PreferenceConstants.PrefDevMaxGameCount, 15));
    }

	/* (non-Javadoc)
	 * @see ITarotDroidApp#getTarotDroidUser()
	 */
	public TarotDroidUser getTarotDroidUser() {
		return this.tarotDroidUser;
	}
	
	/* (non-Javadoc)
	 * @see ITarotDroidApp#setTarotDroidUser(TarotDroidUser)
	 */
	public void setTarotDroidUser(TarotDroidUser tarotDroidUser) {
		this.tarotDroidUser = tarotDroidUser;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = preferences.edit();
		if (tarotDroidUser == null) {
			editor.putString(PreferenceConstants.PrefTarotDroidUser, null);
		}
		else {
			editor.putString(PreferenceConstants.PrefTarotDroidUser, new Gson().toJson(tarotDroidUser));
		}
		editor.commit();
	}
    
	/* (non-Javadoc)
	 * @see ITarotDroidApp#getSelectedUser()
	 */
	public GraphUser getSelectedUser() {
		return this.selectedUser;
	}

	/* (non-Javadoc)
	 * @see ITarotDroidApp#setSelectedUser(com.facebook.model.GraphUser)
	 */
	public void setSelectedUser(GraphUser user) {
		this.selectedUser = user;
	}

	/* (non-Javadoc)
	 * @see ITarotDroidApp#getNotificationIds()
	 */
	@Override
	public Map<String, Integer> getNotificationIds() {
		return this.notificationIds;
	}
	
	private List<GraphUser> selectedUsers;
	public void setSelectedUsers(List<GraphUser> users) {
		this.selectedUsers = users;
	}
	
	public List<GraphUser> getSelectedUsers() {
		return this.selectedUsers;
	}
	
	private GraphPlace selectedPlace;
	public GraphPlace getSelectedPlace() {
	    return selectedPlace;
	}

	public void setSelectedPlace(GraphPlace place) {
	    this.selectedPlace = place;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getSQLiteDatabase()
	 */
	@Override
	public SQLiteDatabase getSQLiteDatabase() {
		return SqliteDalService.getSqliteDatabase(this);
	}
}