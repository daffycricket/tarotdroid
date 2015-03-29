package org.nla.tarotdroid.lib.ui.tasks;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.model.TarotDroidUser;

/**
 * An Async Task aimed to signup a user.
 */
public class SignupTask extends BaseAsyncTask<Void, String, String, TarotDroidUser> {

	/**
	 * The user to sign up with.
	 */
	private final TarotDroidUser tarotDroidUser;

	/**
	 * Constructor.
	 * 
	 * @param tarotDroidUser
	 */
	public SignupTask(String email, String password) {
		checkArgument(email != null, "email is null");
		checkArgument(password != null, "password is null");
		this.tarotDroidUser = new TarotDroidUser(email, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Void... params) {

		String toReturn = null;
		try {
			// TODO Set proper url
			HttpPost postRequest = new HttpPost(AppContext.getApplication().getCloudDns());
			postRequest.setHeader("Content-type", "application/json");
			postRequest.setEntity(new StringEntity(gson.toJson(tarotDroidUser)));

			// TODO Do proper actions
			HttpResponse response = this.httpClient.execute(postRequest);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// String content = CharStreams.toString(new
				// InputStreamReader(response.getEntity().getContent()));
				// final Type urlShortenerResponseType = new
				// TypeToken<UrlShortenerResponse>() {}.getType();
				// UrlShortenerResponse urlShortenerResponse =
				// gson.fromJson(content, urlShortenerResponseType);
				// shortenedUrl = urlShortenerResponse.getId();
				toReturn = UUID.randomUUID().toString();
			}
		} catch (Exception e) {
			backgroundException = e;
		}
		return toReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		tarotDroidUser.setUuid(result);
		if (this.callback != null) {
			this.callback.execute(tarotDroidUser, backgroundException);
		}
	}
}