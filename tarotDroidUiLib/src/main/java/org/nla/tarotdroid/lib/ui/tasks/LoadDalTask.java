package org.nla.tarotdroid.lib.ui.tasks;

import static com.google.common.base.Preconditions.checkArgument;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.dal.IDalService;
import org.nla.tarotdroid.dal.sql.SqliteDalService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/**
 * An AsyncTask aimed to partially retrieve and display all GameSets from the
 * DAL.
 */
public class LoadDalTask extends BaseAsyncTask<Void, Void, String, String> {

	/**
	 * The context.
	 */
	private final Context context;

	/**
	 * The DAL service to be created.
	 */
	private IDalService dalService;

	/**
	 * The progress dialog.
	 */
	private ProgressDialog progressDialog;

	/**
	 * Constructor using a context and a dal service container.
	 * 
	 * @param context
	 * @param dalServiceContainer
	 */
	public LoadDalTask(final Context context) {
		checkArgument(context != null, "context is null");

		this.context = context;
		this.progressDialog = new ProgressDialog(this.context);
		this.dalService = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Void... params) {
		// try {
		this.dalService = new SqliteDalService(this.context);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String isNull) {
		if (this.progressDialog != null && this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}

		AppContext.getApplication().setDalService(this.dalService);

		if (this.callback != null) {
			this.callback.execute(this.dalService.getLog(), null);
		}
	}

	/**
	 * Shows the progress dialog and a specific message.
	 */
	public void showDialogOnActivity(final Activity activity, String text) {
		if (this.progressDialog != null && this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}
		this.progressDialog = new ProgressDialog(activity);
		this.progressDialog.setMessage(text);
		this.progressDialog.show();

		this.progressDialog.setOnCancelListener(new OnCancelListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * android.content.DialogInterface.OnCancelListener#onCancel(android
			 * .content.DialogInterface)
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				progressDialog.dismiss();
				activity.finish();
			}
		});
	}
}