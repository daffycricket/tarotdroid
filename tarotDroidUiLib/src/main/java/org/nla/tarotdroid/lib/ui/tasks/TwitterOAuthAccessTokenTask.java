package org.nla.tarotdroid.lib.ui.tasks;

import org.nla.tarotdroid.lib.ui.TwitterConnectActivity;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.os.AsyncTask;
import android.util.Log;


public class TwitterOAuthAccessTokenTask extends AsyncTask<String, Void, Exception>
{
	private String verifier;
	
	private RequestToken requestToken;
	
	private AccessToken accessToken;
	
	private Twitter twitter;
	
	private User user;
	
	public TwitterOAuthAccessTokenTask(Twitter twitter, AccessToken accessToken, String verifier) {
		this.verifier = verifier;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Exception doInBackground(String... params) {
		Exception toReturn = null;
		
		try {
			accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
			user = twitter.showUser(accessToken.getUserId());
			
		}
		catch(TwitterException e) {
			Log.e(TwitterConnectActivity.class.getName(), "TwitterError: " + e.getErrorMessage());
			toReturn = e;
		}
		catch(Exception e) {
			Log.e(TwitterConnectActivity.class.getName(), "Error: " + e.getMessage());
			toReturn = e;
		}
		
		return toReturn;
	}
	
	@Override
	protected void onPostExecute(Exception exception) {
		//onRequestTokenRetrieved(exception);
	}
}