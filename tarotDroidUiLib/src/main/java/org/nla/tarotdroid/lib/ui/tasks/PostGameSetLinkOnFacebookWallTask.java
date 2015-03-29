package org.nla.tarotdroid.lib.ui.tasks;

import static com.google.common.base.Preconditions.checkArgument;

import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.lib.helpers.FacebookHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

/**
 * An AsyncTask aimed to post a gameset on Facebook.
 */
public class PostGameSetLinkOnFacebookWallTask extends BaseAsyncTask<GameSet, String, Response, Response> {

	/**
	 * The context.
	 */
	private Activity activity;

	/**
	 * The context.
	 */
	private final GameSet gameSet;

	/**
	 * Indicates whether task was canceled;
	 */
	private final boolean isCanceled;

	/**
	 * Progress dialog to display messages.
	 */
	private ProgressDialog progressDialog;

	/**
	 * The potential short url.
	 */
	private final String shortUrl;

	/**
	 * Constructor using a context and a dal service container.
	 * 
	 * @param context
	 */
	public PostGameSetLinkOnFacebookWallTask(final Activity activity, final ProgressDialog progressDialog, final GameSet gameSet, final String shortUrl) {
		checkArgument(activity != null, "activity is null");
		checkArgument(activity != null, "gameSet is null");
		this.activity = activity;
		this.gameSet = gameSet;
		this.progressDialog = progressDialog;
		this.isCanceled = false;
		this.shortUrl = shortUrl;

		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this.activity);
			this.progressDialog.setCanceledOnTouchOutside(false);
		}
	}

	/**
	 * Attaches task to activity.
	 * 
	 * @param activity
	 */
	public void attach(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Detaches task from activity.
	 */
	public void detach() {
		this.activity = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Response doInBackground(GameSet... params) {
		try {
			Session session = Session.getActiveSession();
			Bundle postParams = new Bundle();
			postParams.putString("name", FacebookHelper.buildName(this.gameSet));
			postParams.putString("caption", FacebookHelper.buildCaption(this.gameSet));
			postParams.putString("description", FacebookHelper.buildDescription(this.gameSet));
			postParams.putString("link", this.shortUrl != null ? shortUrl : FacebookHelper.buildGameSetUrl(this.gameSet));
			postParams.putString("picture", FacebookHelper.buildGameSetPictureUrl(this.gameSet));

			Request request = new Request(session, "me/feed", postParams, HttpMethod.POST);
			return request.executeAndWait();
		} catch (Exception e) {
			this.backgroundException = e;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Response response) {
		this.progressDialog.setOnCancelListener(null);
		if (this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}

		if (this.backgroundException != null) {
			AuditHelper.auditError(ErrorTypes.postGameSetLinkOnFacebookWallTaskError, this.backgroundException, this.activity);
			return;
		}

		if (!this.isCanceled && this.callback != null) {
			this.callback.execute(response, this.backgroundException);
		}
	}
}