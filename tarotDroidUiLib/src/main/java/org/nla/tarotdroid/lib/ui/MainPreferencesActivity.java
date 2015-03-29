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

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.lib.ui.constants.PreferenceConstants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class MainPreferencesActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {

	/**
	 * Preference changed listener. 
	 */
	private OnSharedPreferenceChangeListener listener;
	
	/**
	 * Shared preferences.
	 */
	private SharedPreferences preferences; 
	
	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 * @see http://stackoverflow.com/questions/531427/how-do-i-display-the-current-value-of-an-android-preference-in-the-preference-sum for preference management.
	 */
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(final Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            this.auditEvent();
            
        	if ((AppContext.getApplication().isAppInDebugMode())) {
        	    MainPreferencesActivity.this.addPreferencesFromResource(R.layout.main_dashboard_preferences_debug);
        	}
        	else {
        	    MainPreferencesActivity.this.addPreferencesFromResource(R.layout.main_dashboard_preferences);
        	}

        	this.preferences = PreferenceManager.getDefaultSharedPreferences(MainPreferencesActivity.this.getBaseContext());
        	this.listener = new OnSharedPreferenceChangeListener() {
				
				/* (non-Javadoc)
				 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
				 */
				@Override
				public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
					
					// dev instances to change
					if (key.equals(PreferenceConstants.PrefDevMaxGameCount)) {
						AppContext.getApplication().getAppParams().setDevMaxGameCount(sharedPreferences.getInt(PreferenceConstants.PrefDevMaxGameCount, 20));
					}
					else if (key.equals(PreferenceConstants.PrefDevGameSetCount)) {
						AppContext.getApplication().getAppParams().setDevGameSetCount(sharedPreferences.getInt(PreferenceConstants.PrefDevGameSetCount, 20));
					}
				}
			};
			this.preferences.registerOnSharedPreferenceChangeListener(this.listener);
        } 
        catch (Exception e) {
        	AuditHelper.auditError(ErrorTypes.mainPreferencesActivityError, e, this);
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
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayMainPreferencePage);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		
		for (String key : this.getPreferenceScreen().getSharedPreferences().getAll().keySet()) {
			if (this.findPreference(key) instanceof EditTextPreference) {
				EditTextPreference pref = (EditTextPreference)this.findPreference(key);
				pref.setSummary(pref.getText());
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	@Override
	public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
		// get associated preference
		if (this.findPreference(key) instanceof EditTextPreference) {
			EditTextPreference pref = (EditTextPreference)this.findPreference(key);
			pref.setSummary(pref.getText());
		}
	}
	
	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
}
