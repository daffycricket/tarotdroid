package org.nla.tarotdroid.lib.ui.tasks;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.File;
import java.io.PrintStream;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.DatabaseHelper;

import android.app.Activity;
import android.os.Environment;

/**
 * An AsyncTask aimed to post a gameset on Facebook.
 */
public class ExportDatabaseTask extends BaseAsyncTask<Void, String, String[], String[]> {

	/**
	 * The context.
	 */
	private final Activity activity;

	/**
	 * Potential exception that happened in the background.
	 */
	private Exception backgroundException;

	/**
	 * Flag indicating whether an error occured in the background.
	 */
	private boolean backroundErrorHappened;

	/**
	 * The potential callback to post execute.
	 */
	private IAsyncCallback<String[]> callback;

	/**
	 * Indicates whether task was canceled;
	 */
	private final boolean isCanceled;

	/**
	 * Constructor using a context and a dal service container.
	 * 
	 * @param context
	 */
	public ExportDatabaseTask(final Activity activity) {
		checkArgument(activity != null, "activity is null");
		this.activity = activity;
		this.isCanceled = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String[] doInBackground(Void... params) {

		String[] contentToExport = new String[2];
		try {
			DatabaseHelper databaseHelper = new DatabaseHelper(this.activity, AppContext.getApplication().getSQLiteDatabase());
			contentToExport[0] = databaseHelper.exportContent();
		} catch (Exception e) {
			contentToExport[0] = null;
			this.backroundErrorHappened = true;
			this.backgroundException = e;
		}

		// try to store content in file
		if (contentToExport[0] != null) {
			try {
				File sdcard = Environment.getExternalStorageDirectory();
				File tarotDroidDir = new File(sdcard.getAbsolutePath(), "TarotDroid");
				if (!tarotDroidDir.exists()) {
					tarotDroidDir.mkdir();
				}
				File exportFile = new File(tarotDroidDir, "export.xml");
				exportFile.createNewFile();

				PrintStream printStream = new PrintStream(exportFile);
				printStream.print(contentToExport[0]);
				printStream.close();
				contentToExport[1] = exportFile.getAbsolutePath();
			} catch (Exception e) {
				e.printStackTrace();
				contentToExport[1] = null;
			}
		}

		return contentToExport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String[] exportedContent) {

		// display error if exception occured
		if (this.backroundErrorHappened) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.exportDatabaseError, this.backgroundException, this.activity);
			return;
		}

		// else executes potential callback
		else {
			if (!this.isCanceled && this.callback != null) {
				this.callback.execute(exportedContent, this.backgroundException);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	}

	/**
	 * Sets the callback.
	 * 
	 * @param callback
	 */
	@Override
	public void setCallback(final IAsyncCallback<String[]> callback) {
		this.callback = callback;
	}
}