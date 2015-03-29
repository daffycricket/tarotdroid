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
import org.nla.tarotdroid.lib.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * An AsyncTask aimed to remove the last game.
 */
public class RemoveAllGameSetsTask extends BaseAsyncTask<Void, Void, Void, Object> {

	/**
	 * The context.
	 */
	private final Activity activity;

	/**
	 * A progress dialog shown during the game creation and storage.
	 */
	private ProgressDialog dialog;

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param dialog
	 */
	public RemoveAllGameSetsTask(final Activity activity, final ProgressDialog dialog) {
		checkArgument(activity != null, "activity is null");
		this.activity = activity;
		this.dialog = dialog;

		if (this.dialog == null) {
			this.dialog = new ProgressDialog(this.activity);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected final Void doInBackground(final Void... voids) {
		try {
			AppContext.getApplication().getDalService().reInitDal();
		} catch (Exception e) {
			this.backgroundException = e;
			Log.v(AppContext.getApplication().getAppLogTag(), "TarotDroid Exception in " + this.getClass().toString(), e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected final void onPostExecute(final Void unused) {
		// hide busy idicator
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}

		// display toast if error happened
		if (this.backgroundException != null) {
			Toast.makeText(this.activity, "DAL Error: " + this.backgroundException, Toast.LENGTH_LONG).show();
		}

		// display toast if everything's okay
		else {
			Toast.makeText(this.activity, this.activity.getResources().getString(R.string.msgGameSetsDeleted), Toast.LENGTH_LONG).show();
		}

		// executes the potential callback
		if (this.callback != null) {
			this.callback.execute(null, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected final void onPreExecute() {
		this.dialog.setMessage(this.activity.getResources().getString(R.string.msgGameSetsDeletion));
		this.dialog.show();
	}
}
