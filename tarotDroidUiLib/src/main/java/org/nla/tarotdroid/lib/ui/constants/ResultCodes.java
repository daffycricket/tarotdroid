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
package org.nla.tarotdroid.lib.ui.constants;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class ResultCodes {
	/**
	 * Game added.
	 */
	public static final int AddGame_Ok = 101;
	
	/**
	 * Game addition canceled.
	 */
	public static final int AddGame_Cancel = 102;
	
	/**
	 * Game removed.
	 */
	public static final int RemovedGame_Ok = 549743;	
	
	/**
	 * Game settings updated.
	 */
	public static final int UpdateGameSettings_Ok = 201;
	
	/**
	 * Game settings update canceled.
	 */
	public static final int UpdateGameSettings_Cancel = 202;
	
	/**
	 * Preferences updated.
	 */
	public static final int UpdatePreferences_Ok = 301;
	
	/**
	 * Preferences update canceled.
	 */
	public static final int UpdatePreferences_Cancel = 302;

	/**
	 * Notifies the caller the user changed the player picture.
	 */
	public static final int PlayerPictureChanged = 4179896;
	
	/**
	 * The player picture uri code.
	 */
	public static final String PlayerPictureUriCode = "playerPictureUri";	

	/**
	 * Notifies the caller the settings have changed.
	 */
	public static final int SettingsChanged = 1456;

	/**
	 * Irrelevant result code.
	 */
	public static final int Irrelevant = 871;

	/**
	 * The player name code.
	 */
	public static final String PlayerNameCode = "playerNameCode";
	
	/**
	 * Notifies the caller something was published on Facebook.
	 */
	public static final int PublishedOnFacebook = 87461; 	
}
