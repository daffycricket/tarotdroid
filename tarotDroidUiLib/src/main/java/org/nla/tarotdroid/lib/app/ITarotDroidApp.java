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

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.nla.tarotdroid.lib.helpers.BluetoothHelper;
import org.nla.tarotdroid.lib.model.TarotDroidUser;
import org.nla.tarotdroid.lib.ui.tasks.LoadDalTask;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.dal.IDalService;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;

/**
 * Global application interface.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public interface ITarotDroidApp {
	
	/**
	 * Returns the application name.
	 * @return
	 */
	String getAppName();
	
	/**
	 * Returns the application version.
	 * @return
	 */
	String getAppVersion();
	
	/**
	 * Returns the application package.
	 * @return
	 */
	String getAppPackage();
	
	/**
	 * Returns the application log tag.
	 * @return
	 */
	String getAppLogTag();
	
	/**
	 * Returns the flurry app id.
	 * @return
	 */
	String getFlurryApplicationId();
	
	/**
	 * Returns the cloud dns.
	 * @return
	 */
	String getCloudDns();

	/**
	 * Returns the facebook cloud url.
	 * @return
	 */
	String getFacebookCloudUrl();
	
	/**
	 * Returns the facebook app url.
	 * @return
	 */
	String getFacebookAppUrl();
	
	/**
	 * Returns android resources.
	 * @return
	 */
	Resources getResources();
	
	/**
	 * Returns the content resolver.
	 * @return
	 */
	ContentResolver getContentResolver();

	/**
	 * Returns global app uuid used for bluetooth.
	 * @return
	 */
	UUID getUuid();

	/**
	 * Returns the service name used for bluetooth. 
	 * @return
	 */
	String getServiceName();
	
	/**
	 * Returns the logged account.
	 * @return
	 */
	GraphUser getLoggedFacebookUser();
	
	
	/**
	 * Sets the logged account.
	 * @return
	 */
	void setLoggedFacebookUser(GraphUser graphUser);
	
	/**
	 * Returns the data access layer service.
	 * @return
	 */
	IDalService getDalService();
	
	/**
	 * Sets the data access layer service.
	 */
	 void setDalService(IDalService dalService);
	
	/**
	 * Returns the application parameters.
	 * @return
	 */
	AppParams getAppParams();
	
	/**
	 * Returns the bluetooth helper.
	 * @return
	 */
	BluetoothHelper getBluetoothHelper();
	
	/**
	 * Indicates whether the application is in debug mode. 
	 * @return
	 */
	boolean isAppInDebugMode();
	
	/**
	 * Indicates whether the application is limited.
	 * @return
	 */
	boolean isAppLimited();
	
	/**
	 * Returns the time the app was last launched.
	 * @return
	 */
	long getLastLaunchTimestamp();
	
    /**
     * Returns the LoadDalTask.
     * @return the LoadDalTask.
     */
	LoadDalTask getLoadDalTask();
	
	/**
	 * Initializes and returns a new GameSetParameters.
	 * @return
	 */
	GameSetParameters initializeGameSetParameters();
	
	/**
	 * Returns the logged in tarotdroid user.
	 * @return
	 */
	TarotDroidUser getTarotDroidUser();
	
	/**
	 * Sets the logged in tarotdroid user.
	 * @param tarotDroidUser
	 */
	void setTarotDroidUser(TarotDroidUser tarotDroidUser);
	
	/**
	 * Returns the selected user.
	 * @return
	 */
	public GraphUser getSelectedUser();
	
	public void setSelectedUsers(List<GraphUser> users);
	
	public List<GraphUser> getSelectedUsers();

	/**
	 * Sets the selected user.
	 * @param user
	 */
	public void setSelectedUser(GraphUser user);
	
	/**
	 * Returns the selected place.
	 * @return
	 */
	public GraphPlace getSelectedPlace();

	/**
	 * Sets the selected place.
	 * @return
	 */
	public void setSelectedPlace(GraphPlace place);
	
	/**
	 * Returns the map of GameSet uuid/notification id being published.
	 */
	public Map<String, Integer> getNotificationIds();

	/**
	 * Returns the db.
	 * @return
	 */
	public SQLiteDatabase getSQLiteDatabase();
	
	/**
	 * Returns the google play package.
	 * @return
	 */
	public String getGooglePlayUrl();
}
