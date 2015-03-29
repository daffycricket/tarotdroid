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
package org.nla.tarotdroid.lib.ui.tasks;

import static com.google.common.base.Preconditions.checkArgument;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.ui.TabGameSetActivity;
import org.nla.tarotdroid.lib.ui.constants.ActivityParams;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 *	An AsyncTask aimed to create a game set and navigate towards the TabGameSetActivity.
 */
public class StartNewGameSetTask extends AsyncTask<Void, Void, Void> {

	/**
	 * The activity.
	 */
	private Activity activity;

	/**
	 * A progress dialog shown during the game creation and storage.
	 */
	private final ProgressDialog dialog;

	/**
	 * Flag indicating whether an error occured in the background. 
	 */
	private boolean backroundErrorHappened;

	/**
	 * Potential exception that happened in the background.
	 */
	private Exception backgroundException;
	
	/**
	 * Game set to persist.
	 */
	private GameSet gameSet;
	
	/**
	 * Constructor.
	 * @param context
	 * @param refreshableGameSetContainer
	 */
	public StartNewGameSetTask(final Activity activity, final ProgressDialog dialog, final GameSet gameSet) {
		checkArgument(activity != null, "activity is null");
		checkArgument(dialog != null, "dialog is null");
		checkArgument(gameSet != null, "gameSet is null");
		
		this.activity = activity;
		this.dialog = dialog;
		this.gameSet = gameSet;
		this.backroundErrorHappened = false;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected final void onPreExecute() {
		this.dialog.setMessage(this.activity.getResources().getString(R.string.msgGameSetCreation));
		this.dialog.show();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected final Void doInBackground(final Void... voids) {
		// store new game set in Dal only in some cases : app is not limited, or app is limited but there are less than 5 game sets already stored
		boolean gameSetToBeStored = !AppContext.getApplication().isAppLimited();
		try {
			gameSetToBeStored = gameSetToBeStored || (AppContext.getApplication().isAppLimited() && AppContext.getApplication().getDalService().getGameSetCount() < 5);   
			if (gameSetToBeStored) {
				AppContext.getApplication().getDalService().saveGameSet(this.gameSet);
			}
		}
		catch (Exception e) {
			this.backroundErrorHappened = true;
			this.backgroundException = e;
			Log.v(AppContext.getApplication().getAppLogTag(), this.getClass().toString(), e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected final void onPostExecute(final Void unused) {
		// hide busy idicator
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}

		// display toast if error happened
		if (this.backroundErrorHappened) {
            Toast.makeText(
            		this.activity, 
                    "Error: " + this.backgroundException, 
                    Toast.LENGTH_LONG
            ).show();
		}
		// navigate towards game set if everything's ok
		else {
            // navigate towards activity
			Intent intent = new Intent(this.activity, TabGameSetActivity.class);
			if (!this.gameSet.isPersisted()) {
				//intent.putExtra(ActivityParams.PARAM_GAMESET_SERIALIZED, UIHelper.serializeGameSet(this.gameSet));
				intent.putExtra(ActivityParams.PARAM_GAMESET_SERIALIZED, this.gameSet);
			}
			else {
				intent.putExtra(ActivityParams.PARAM_GAMESET_ID, this.gameSet.getId());
			}
			
			this.activity.startActivity(intent);
			this.activity.finish();
		}
	}
}
