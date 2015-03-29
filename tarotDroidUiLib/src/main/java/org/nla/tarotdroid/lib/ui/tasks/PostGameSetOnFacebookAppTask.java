package org.nla.tarotdroid.lib.ui.tasks;

import static com.google.common.base.Preconditions.checkArgument;

import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.FacebookHelper;
import org.nla.tarotdroid.lib.ui.cloud.GameSetGraphObject;
import org.nla.tarotdroid.lib.ui.cloud.PlayGraphAction;
import org.nla.tarotdroid.biz.GameSet;

import android.app.Activity;
import android.app.ProgressDialog;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

/**
 * An AsyncTask aimed to post a gameset on Facebook.
 */
public class PostGameSetOnFacebookAppTask extends BaseAsyncTask<Void, String, Response, Response> {

	/**
	 * Facebook app namespace.
	 */
	private static final String POST_ACTION_PATH = "me/org_nla_tarotdroid:play";

	/**
	 * The context.
	 */
	private final Activity activity;

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
	private final ProgressDialog progressDialog;

	/**
	 * Constructor.
	 * 
	 * @param activity
	 * @param progressDialog
	 * @param gameSet
	 */
	public PostGameSetOnFacebookAppTask(final Activity activity, final ProgressDialog progressDialog, final GameSet gameSet) {
		checkArgument(activity != null, "activity is null");
		checkArgument(activity != null, "gameSet is null");
		this.activity = activity;
		this.gameSet = gameSet;
		this.isCanceled = false;
		this.progressDialog = progressDialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Response doInBackground(Void... params) {
		try {
			PlayGraphAction playGraphAction = GraphObject.Factory.create(PlayGraphAction.class);

			// set facebok users related to the action
			// playGraphAction.setTags(selectedUsers);

			// set place related to the action
			// playGraphAction.setPlace(selectedPlace);

			// set gameset related to the action
			GameSetGraphObject gameSetGraphObject = GraphObject.Factory.create(GameSetGraphObject.class);
			gameSetGraphObject.setUrl(FacebookHelper.buildGameSetUrl(this.gameSet));
			playGraphAction.setGame(gameSetGraphObject);
			playGraphAction.setMessage(FacebookHelper.buildCaption(this.gameSet));

			// execute request asynchronously
			Request request = new Request(Session.getActiveSession(), POST_ACTION_PATH, null, HttpMethod.POST);
			request.setGraphObject(playGraphAction);

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
	protected void onPostExecute(final Response response) {
		this.progressDialog.setOnCancelListener(null);
		if (this.progressDialog != null && this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}

		if (this.backgroundException != null) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.postGameSetOnFacebookAppError, this.backgroundException, this.activity);
			return;
		}

		if (!this.isCanceled && this.callback != null) {
			this.callback.execute(response, null);
		}
	}
}