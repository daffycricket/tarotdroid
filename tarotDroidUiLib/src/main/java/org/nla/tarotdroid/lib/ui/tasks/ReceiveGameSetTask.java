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

import java.io.IOException;
import java.io.ObjectInputStream;

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.lib.helpers.UIHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

/**
 * A class aimed to transfer data from server pov.
 */
public class ReceiveGameSetTask extends BaseAsyncTask<Void, String, Integer, Object> {

	/**
	 * The context.
	 */
	private final Activity activity;

	/**
	 * A progress dialog shown during the game creation and storage.
	 */
	private ProgressDialog dialog;

	/**
	 * The bluetooth server socket.
	 */
	private final BluetoothServerSocket serverSocket;

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param dialog
	 * @param bluetoothAdapter
	 */
	public ReceiveGameSetTask(final Activity activity, final ProgressDialog dialog, final BluetoothAdapter bluetoothAdapter) throws IOException {
		checkArgument(activity != null, "activity is null");
		checkArgument(bluetoothAdapter != null, "bluetoothAdapter is null");
		this.activity = activity;
		this.dialog = dialog;
		this.serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(AppContext.getApplication().getServiceName(), AppContext.getApplication().getUuid());

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
	protected Integer doInBackground(final Void... params) {
		int nbGSDownloaded = 0;
		try {
			// wait for a connection
			BluetoothSocket socket = null;
			while (true) {
				socket = this.serverSocket.accept();
				if (socket != null) {
					this.serverSocket.close();
					break;
				}
			}

			// indicate the game set is being received
			this.publishProgress(this.activity.getResources().getString(R.string.msgDownloadingFromSender));

			// deserialize the game set
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			GameSet gameSet = (GameSet) ois.readObject();
			ois.close();
			socket.close();

			// store the game set and its children
			this.publishProgress(this.activity.getResources().getString(R.string.msgStoringDownloadedGameSet));
			AppContext.getApplication().getDalService().saveGameSet(gameSet);

			// increment game set count
			nbGSDownloaded++;
		} catch (Exception e) {
			Log.v(getClass().toString(), "Logged Exception", e);
			this.backgroundException = e;
		}
		return nbGSDownloaded;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
		try {
			this.serverSocket.close();
		} catch (IOException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final Integer nbGameSets) {
		// hide busy idicator
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}

		// display toast if error happened
		if (this.backgroundException != null) {
			UIHelper.showSimpleRichTextDialog(this.activity,
					this.activity.getString(R.string.msgBluetoothTransferProblem, AppContext.getApplication().getAppVersion(), this.backgroundException.getMessage()),
					this.activity.getString(R.string.titleBluetoothTransferProblem));
			AuditHelper.auditError(ErrorTypes.bluetoothReceiveError, this.backgroundException);
		}

		// display toast if everything's okay
		else {
			Toast.makeText(this.activity, this.activity.getResources().getString(R.string.msgDownloadSuccededSolo), Toast.LENGTH_LONG).show();

			// executes the potential callback
			if (this.callback != null) {
				this.callback.execute(null, this.backgroundException);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected final void onPreExecute() {
		this.dialog.setMessage(this.activity.getResources().getString(R.string.msgWaitingForSender));
		this.dialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
	 */
	@Override
	protected void onProgressUpdate(final String... messages) {
		if (messages.length > 0) {
			this.dialog.setMessage(messages[0]);
		}
	}
}