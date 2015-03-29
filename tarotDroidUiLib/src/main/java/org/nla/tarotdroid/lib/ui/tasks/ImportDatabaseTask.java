package org.nla.tarotdroid.lib.ui.tasks;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.DatabaseHelper;

import android.app.Activity;
import android.os.Environment;

/**
 * An AsyncTask aimed to post a gameset on Facebook.
 */
public class ImportDatabaseTask extends BaseAsyncTask<Void, String, String, String> {

	/**
	 * The context.
	 */
	private final Activity activity;

	/**
	 * Flag indicating whether an error occured in the background.
	 */
	private boolean backroundErrorHappened;

	/**
	 * Path of the file to import.
	 */
	private final String importFilePath;

	/**
	 * Indicates whether task was canceled;
	 */
	private final boolean isCanceled;

	/**
	 * Constructor using a context and a dal service container.
	 * 
	 * @param context
	 */
	public ImportDatabaseTask(final Activity activity, final String importFilePath) {
		checkArgument(activity != null, "activity is null");
		checkArgument(importFilePath != null, "importFileUri is null");
		this.activity = activity;
		this.isCanceled = false;
		this.importFilePath = importFilePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Void... params) {

		String exportFileUri = null;
		try {
			DatabaseHelper databaseHelper = new DatabaseHelper(this.activity, AppContext.getApplication().getSQLiteDatabase());

			File sdcard = Environment.getExternalStorageDirectory();
			File tarotDroidDir = new File(sdcard.getAbsolutePath(), "TarotDroid");
			if (!tarotDroidDir.exists()) {
				tarotDroidDir.mkdir();
			}
			File exportFile = new File(tarotDroidDir, "export_backup.xml");
			exportFile.createNewFile();

			PrintStream printStream = new PrintStream(exportFile);
			printStream.print(databaseHelper.exportContent());
			printStream.close();

			// File importFile = new File(tarotDroidDir, "import.xml");
			File importFile = new File(this.importFilePath);

			StringBuilder xmlContent = new StringBuilder();
			BufferedReader reader = new BufferedReader(new FileReader(importFile));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				xmlContent.append(line);
			}
			reader.close();

			databaseHelper.importContent(xmlContent.toString());
			AppContext.getApplication().getDalService().initialize();
			exportFileUri = "imported";
		} catch (Exception e) {
			this.backroundErrorHappened = true;
			this.backgroundException = e;
		}
		return exportFileUri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String databaseContent) {

		// display error if exception occured
		if (this.backroundErrorHappened) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.importDatabaseError, this.backgroundException, this.activity);
			return;
		}

		// else executes potential callback
		else {
			if (!this.isCanceled && this.callback != null) {
				this.callback.execute(databaseContent, backgroundException);
			}
		}
	}
}