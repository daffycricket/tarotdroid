package org.nla.tarotdroid.lib.ui.tasks;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.google.common.io.CharStreams;
import com.google.gson.reflect.TypeToken;

/**
 * An AsyncTask aimed to post a gameset on Facebook.
 */
public class ShortenUrlTask extends BaseAsyncTask<Void, String, String, String> {

	/**
	 * Url Shortener Request, described here
	 * https://developers.google.com/url-shortener/v1/getting_started.
	 */
	private class UrlShortenerRequest {
		/**
		 * Long url to shorten.
		 */
		private String longUrl;

		/**
		 * @return the longUrl
		 */
		@SuppressWarnings("unused")
		protected String getLongUrl() {
			return this.longUrl;
		}

		/**
		 * @param longUrl
		 *            the longUrl to set
		 */
		protected void setLongUrl(String longUrl) {
			this.longUrl = longUrl;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return gson.toJson(this);
		}

	}

	/**
	 * Url Shortener Response, described here
	 * https://developers.google.com/url-shortener/v1/getting_started.
	 */
	private class UrlShortenerResponse {

		/**
		 * Short url.
		 */
		private String id;

		/**
		 * Type of action.
		 */
		private String kind;

		/**
		 * Initial long url.
		 */
		private String longUrl;

		/**
		 * @return the id
		 */
		protected String getId() {
			return this.id;
		}

		/**
		 * @return the kind
		 */
		@SuppressWarnings("unused")
		protected String getKind() {
			return this.kind;
		}

		/**
		 * @return the longUrl
		 */
		@SuppressWarnings("unused")
		protected String getLongUrl() {
			return this.longUrl;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		@SuppressWarnings("unused")
		protected void setId(String id) {
			this.id = id;
		}

		/**
		 * @param kind
		 *            the kind to set
		 */
		@SuppressWarnings("unused")
		protected void setKind(String kind) {
			this.kind = kind;
		}

		/**
		 * @param longUrl
		 *            the longUrl to set
		 */
		@SuppressWarnings("unused")
		protected void setLongUrl(String longUrl) {
			this.longUrl = longUrl;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return gson.toJson(this);
		}
	}

	/**
	 * Indicates whether task was canceled;
	 */
	private final boolean isCanceled;

	/**
	 * The url to shorten.
	 */
	private final String urlToShorten;

	public ShortenUrlTask(final String urlToShorten) {
		checkArgument(urlToShorten != null, "urlToShorten is null");
		this.urlToShorten = urlToShorten;
		this.isCanceled = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Void... params) {

		String shortenedUrl = null;
		try {
			UrlShortenerRequest urlShortenerRequest = new UrlShortenerRequest();
			urlShortenerRequest.setLongUrl(this.urlToShorten);

			// TODO get url from properties
			HttpPost postRequest = new HttpPost("https://www.googleapis.com/urlshortener/v1/url");
			postRequest.setHeader("Content-type", "application/json");
			postRequest.setEntity(new StringEntity(gson.toJson(urlShortenerRequest)));

			HttpResponse response = this.httpClient.execute(postRequest);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String content = CharStreams.toString(new InputStreamReader(response.getEntity().getContent()));

				final Type urlShortenerResponseType = new TypeToken<UrlShortenerResponse>() {
				}.getType();
				UrlShortenerResponse urlShortenerResponse = gson.fromJson(content, urlShortenerResponseType);

				shortenedUrl = urlShortenerResponse.getId();
			}
		} catch (Exception e) {
			this.backgroundException = e;
		}
		return shortenedUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String shortenedUrl) {

		if (!this.isCanceled && this.callback != null) {
			this.callback.execute(shortenedUrl, this.backgroundException);
		}
	}
}