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

import java.util.List;

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.lib.ui.constants.ResultCodes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 *	An AsyncTask aimed to remove the last game.
 */
public class RemoveGameTask extends AsyncTask<BaseGame, Void, Void> {

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
	 * Activity to finish if set.
	 */
	private Activity activityToFinish;
	
	
	/**
	 * The current game set.
	 */
	private GameSet gameSet;
	
	/**
	 * Constructor.
	 * @param activity
	 * @param dialog
	 * @param gameSet
	 */
	public RemoveGameTask(final Activity activity, final ProgressDialog dialog, final GameSet gameSet) {
		checkArgument(activity != null, "activity is null");
		checkArgument(dialog != null, "dialog is null");
		checkArgument(gameSet != null, "gameSet is null");
		
		this.activity = activity;
		this.gameSet = gameSet;
		this.dialog = dialog;
		this.backroundErrorHappened = false;
		this.activityToFinish = null;
	}
	
	/**
	 * Constructor.
	 * @param activity
	 * @param activityToFinish
	 * @param gameSet
	 */
	public RemoveGameTask(final Activity activity, final Activity activityToFinish, final GameSet gameSet) {
		checkArgument(activity != null, "activity is null");
		checkArgument(gameSet != null, "gameSet is null");
		
		this.activity = activity;
		this.gameSet = gameSet;
		this.dialog = new ProgressDialog(this.activity);
		this.backroundErrorHappened = false;
		this.activityToFinish = activityToFinish;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected final void onPreExecute() {
		this.dialog.setMessage(this.activity.getResources().getString(R.string.msgGameDeletion));
		this.dialog.show();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected final Void doInBackground(final BaseGame... games) {
		try {
			
			// remove game of given index
			if (games != null && games.length == 1) {
				// remove all games starting and after selected game 
				List<BaseGame> removedGames = this.gameSet.removeGameAndAllSubsequentGames(games[0]);
				
				// delete from dal only if gameset is persisted
				if (this.gameSet.isPersisted()) {
					// TODO optimize...
					for (BaseGame removedGame : removedGames) {
						AppContext.getApplication().getDalService().deleteGame(removedGame, this.gameSet);
					}
				}
			}
			else {
				// remove last game from game set, and get a reference to it before it's removed 
				BaseGame removedGame = this.gameSet.removeLastGame();

				// delete from dal only if gameset is persisted
				if (this.gameSet.isPersisted()) {
					AppContext.getApplication().getDalService().deleteGame(removedGame, this.gameSet);
				}
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
                    this.activity.getResources().getText(R.string.msgDeleteGameFromDalException).toString() + this.backgroundException, 
                    Toast.LENGTH_LONG
            ).show();
		}

		// display toast if everything's okay
		else {
			Toast.makeText(
					this.activity, 
					this.activity.getResources().getString(R.string.msgGameDeleted), 
					Toast.LENGTH_LONG
			).show();
		}

		// finish the activity if set
		if (this.activityToFinish != null) {

			// go back to main previous activity
			this.activityToFinish.setResult(ResultCodes.RemovedGame_Ok);
			this.activityToFinish.finish();
		}
	}
}
