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
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

/**
 * A class aimed to transfer data from client to server.
 */
public class SendGameSetTask extends BaseAsyncTask<Void, String, Integer, Object> {

	/**
	 * The context.
	 */
	private final Activity activity;

	/**
	 * The bluetooth adapter.
	 */
	private final BluetoothAdapter bluetoothAdapter;

	/**
	 * The bluetooth device.
	 */
	private final BluetoothDevice bluetoothDevice;

	/**
	 * A progress dialog shown during the game creation and storage.
	 */
	private ProgressDialog dialog;

	/**
	 * The game set to transfer.
	 */
	private final GameSet gameSet;

	/**
	 * The outputstream.
	 */
	private final OutputStream outStream;

	/**
	 * The bluetooth socket.
	 */
	private final BluetoothSocket socket;

	/**
	 * Constructor.
	 * 
	 * @param activity
	 * @param dialog
	 * @param gameSet
	 * @param bluetoothDevice
	 * @param bluetoothAdapter
	 * @throws IOException
	 */
	public SendGameSetTask(final Activity activity, final ProgressDialog dialog, final GameSet gameSet, final BluetoothDevice bluetoothDevice, final BluetoothAdapter bluetoothAdapter)
			throws IOException {
		checkArgument(activity != null, "activity is null");
		checkArgument(gameSet != null, "gameSet is null");
		checkArgument(bluetoothDevice != null, "bluetoothDevice is null");
		checkArgument(bluetoothAdapter != null, "bluetoothAdapter is null");

		this.activity = activity;
		this.dialog = dialog;
		this.gameSet = gameSet;
		this.bluetoothAdapter = bluetoothAdapter;
		this.bluetoothDevice = bluetoothDevice;
		this.socket = this.bluetoothDevice.createRfcommSocketToServiceRecord(AppContext.getApplication().getUuid());
		this.outStream = this.socket.getOutputStream();

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
		this.bluetoothAdapter.cancelDiscovery();
		int nbGameSetDownloaded = 0;
		try {
			// Connect the device through the socket. This will block until it
			// succeeds or throws an exception
			this.socket.connect();

			// indicate the game set is being received
			this.publishProgress(this.activity.getResources().getString(R.string.msgSendingToReceviver));

			// serialize game set
			ObjectOutputStream oos = new ObjectOutputStream(this.outStream);
			oos.writeObject(this.gameSet);
			oos.flush();
			++nbGameSetDownloaded;
		} catch (final IOException ioe) {
			Log.v(AppContext.getApplication().getAppLogTag(), getClass().toString(), ioe);
			this.backgroundException = ioe;
			try {
				this.socket.close();
			} catch (IOException closeException) {
			}
			nbGameSetDownloaded = 0;
		} catch (final Exception e) {
			Log.v(AppContext.getApplication().getAppLogTag(), getClass().toString(), e);
			this.backgroundException = e;
			nbGameSetDownloaded = 0;
		}
		return Integer.valueOf(nbGameSetDownloaded);
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
			this.socket.close();
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
			AuditHelper.auditError(AuditHelper.ErrorTypes.bluetoothSendError, this.backgroundException);
		}

		// display toast if everything's okay
		else {
			Toast.makeText(this.activity, this.activity.getResources().getString(R.string.msgSendSuccededSolo), Toast.LENGTH_LONG).show();

			// executes the potential callback
			if (this.callback != null) {
				this.callback.execute(null, null);
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
		this.dialog.setMessage(this.activity.getResources().getString(R.string.msgWaitingForReceiver, this.bluetoothDevice.getName()));
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