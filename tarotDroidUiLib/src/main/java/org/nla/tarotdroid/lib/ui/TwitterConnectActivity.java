package org.nla.tarotdroid.lib.ui;

import org.nla.tarotdroid.lib.ui.tasks.IAsyncCallback;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.helpers.ConnexionHelper;
import org.nla.tarotdroid.lib.ui.tasks.ShortenUrlTask;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TwitterConnectActivity extends Activity {

	private class OAuthAccessTokenTask extends AsyncTask<String, Void, Exception> {
		@Override
		protected Exception doInBackground(String... params) {
			Exception toReturn = null;

			try {
				accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
				user = twitter.showUser(accessToken.getUserId());

			} catch (TwitterException e) {
				Log.e(TwitterConnectActivity.class.getName(), "TwitterError: " + e.getErrorMessage());
				toReturn = e;
			} catch (Exception e) {
				Log.e(TwitterConnectActivity.class.getName(), "Error: " + e.getMessage());
				toReturn = e;
			}

			return toReturn;
		}

		@Override
		protected void onPostExecute(Exception exception) {
			onRequestTokenRetrieved(exception);
		}
	}

	/**
	 * Function to update status
	 * */
	class updateTwitterStatus extends AsyncTask<String, String, String> {

		/**
		 * getting Places JSON
		 * */
		@Override
		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key));
				builder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret));

				// Access Token
				String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

				// Update status
				twitter4j.Status response = twitter.updateStatus(status);

				Log.d("Status", "> " + response.getText());
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "Status tweeted successfully", Toast.LENGTH_SHORT).show();
					// Clearing EditText field
					txtUpdate.setText("");
				}
			});
		}

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TwitterConnectActivity.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
	}

	// Shared Preferences
	private static SharedPreferences mSharedPreferences;
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";

	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";

	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";
	// Preference Constants
	static String PREFERENCE_NAME = "twitter_oauth";
	/**
	 * Oauth request token.
	 */
	private static RequestToken requestToken;

	/**
	 * Twitter.
	 */
	private static Twitter twitter;
	static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";
	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	/**
	 * Oauth access token.
	 */
	private AccessToken accessToken;

	// Login button
	Button btnLoginTwitter;

	// Logout button
	Button btnLogoutTwitter;

	// Update status button
	Button btnUpdateStatus;

	// lbl update
	TextView lblUpdate;

	TextView lblUserName;

	// Progress dialog
	ProgressDialog pDialog;

	// // Internet Connection detector
	// private ConnectionHelper cd;

	// Alert Dialog Manager
	// AlertDialogManager alert = new AlertDialogManager();

	// EditText for update
	EditText txtUpdate;

	/**
	 * Twitter user.
	 */
	private User user;

	/**
	 * Check user already logged in your application using twitter Login flag is
	 * fetched from Shared Preferences
	 * */
	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}

	/**
	 * Function to login twitter
	 * */
	private void loginToTwitter() {

		// Check if already logged in
		if (!isTwitterLoggedInAlready()) {
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(getString(R.string.twitter_consumer_key));
			builder.setOAuthConsumerSecret(getString(R.string.twitter_consumer_secret));
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
						TwitterConnectActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));

					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "Already Logged into twitter", Toast.LENGTH_LONG).show();
					}
				}
			});
			thread.start();
		} else {
			// user already logged into twitter
			Toast.makeText(getApplicationContext(), "Already Logged into twitter", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Function to logout from twitter It will just clear the application shared
	 * preferences
	 * */
	private void logoutFromTwitter() {
		// Clear the shared preferences
		Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();

		// After this take the appropriate action
		// I am showing the hiding/showing buttons again
		// You might not needed this code
		btnLogoutTwitter.setVisibility(View.GONE);
		btnUpdateStatus.setVisibility(View.GONE);
		txtUpdate.setVisibility(View.GONE);
		lblUpdate.setVisibility(View.GONE);
		lblUserName.setText("");
		lblUserName.setVisibility(View.GONE);

		btnLoginTwitter.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_connect);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Check if Internet present
		if (!ConnexionHelper.isConnectedToInternet(this)) {
			// Internet Connection is not present
			// alert.showAlertDialog(TwitterConnectActivity.this,
			// "Internet Connection Error",
			// "Please connect to working Internet connection", false);
			// stop executing code by return

			Toast.makeText(getApplicationContext(), "Connect to internet first...", Toast.LENGTH_SHORT).show();

			return;
		}

		// All UI elements
		btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);
		btnUpdateStatus = (Button) findViewById(R.id.btnUpdateStatus);
		btnLogoutTwitter = (Button) findViewById(R.id.btnLogoutTwitter);
		txtUpdate = (EditText) findViewById(R.id.txtUpdateStatus);
		lblUpdate = (TextView) findViewById(R.id.lblUpdate);
		lblUserName = (TextView) findViewById(R.id.lblUserName);

		// Shared Preferences
		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);

		/**
		 * Twitter login button click event will call loginToTwitter() function
		 * */
		btnLoginTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Call login twitter function
				loginToTwitter();
			}
		});

		/**
		 * Button click event to Update Status, will call updateTwitterStatus()
		 * function
		 * */
		btnUpdateStatus.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Call update status function
				// Get the status from EditText
				String status = txtUpdate.getText().toString();

				// Check for blank text
				if (status.trim().length() > 0) {
					// update status
					new updateTwitterStatus().execute(status);
				} else {
					// EditText is empty
					Toast.makeText(getApplicationContext(), "Please enter status message", Toast.LENGTH_SHORT).show();
				}
			}
		});

		/**
		 * Button click event for logout from twitter
		 * */
		btnLogoutTwitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Call logout twitter function
				logoutFromTwitter();
			}
		});

		/**
		 * This if conditions is tested once is redirected from twitter page.
		 * Parse the uri to get oAuth Verifier
		 * */
		if (!isTwitterLoggedInAlready()) {
			Uri uri = getIntent().getData();
			if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {

				// oAuth verifier
				String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
				new OAuthAccessTokenTask().execute(verifier);
			} else {
				loginToTwitter();
			}
		} else {
			ShortenUrlTask shortenUrlTask = new ShortenUrlTask("http://tarotdroid.appspot.com/showGameSet.html?gameSetId=7813b1c8-68f5-497d-bd17-44c735d25c7b");
			shortenUrlTask.setCallback(new IAsyncCallback<String>() {

				@Override
				public void execute(String shortUrl, Exception e) {
					// TODO Check exception
					onUrlShortened(shortUrl);
				}
			});
			shortenUrlTask.execute();
		}
	}

	private void onRequestTokenRetrieved(Exception result) {

		if (result != null) {
			Toast.makeText(this, result.getMessage(), Toast.LENGTH_LONG).show();
		}

		else {
			try {
				// Shared Preferences
				Editor editor = mSharedPreferences.edit();

				// After getting access token, access token secret
				// store them in application preferences
				editor.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
				editor.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
				// Store login status - true
				editor.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
				editor.commit(); // save changes

				Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

				// Hide login button
				btnLoginTwitter.setVisibility(View.GONE);

				// Show Update Twitter
				lblUpdate.setVisibility(View.VISIBLE);
				txtUpdate.setVisibility(View.VISIBLE);
				btnUpdateStatus.setVisibility(View.VISIBLE);
				btnLogoutTwitter.setVisibility(View.VISIBLE);

				// Getting user details from twitter
				String username = user.getName();

				// Displaying in xml ui
				lblUserName.setText(Html.fromHtml("<b>Welcome " + username + "</b>"));

				ShortenUrlTask shortenUrlTask = new ShortenUrlTask("http://tarotdroid.appspot.com/showGameSet.html?gameSetId=7813b1c8-68f5-497d-bd17-44c735d25c7b");
				shortenUrlTask.setCallback(new IAsyncCallback<String>() {

					@Override
					public void execute(String shortUrl, Exception e) {
						// TODO Check exception
						onUrlShortened(shortUrl);
					}
				});
				shortenUrlTask.execute();

			} catch (Exception ex) {
				// Check log for login errors
				Log.e("Twitter Login Error", "> " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void onUrlShortened(String shortUrl) {

		String status = "@tarotdroid Nouvelle partie de tarot terminée " + shortUrl + " avec #tarotdroid pour #android";
		new updateTwitterStatus().execute(status);
	}
}