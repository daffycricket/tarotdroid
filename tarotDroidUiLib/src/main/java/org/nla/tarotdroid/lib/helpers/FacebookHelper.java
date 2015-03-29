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
package org.nla.tarotdroid.lib.helpers;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Random;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.helpers.UrlHelper;
import org.nla.tarotdroid.lib.ui.NotificationActivity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.common.base.Joiner;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public final class FacebookHelper {
	
	private static final Random RANDOM = new Random();
	
	/**
	 * Default constructor.
	 */
	private FacebookHelper() {
	}
	
	/**
	 * Returns the url of the gameset for facebook. 
	 * @param gameSet
	 * @return
	 */
	public static String buildGameSetUrl(final GameSet gameSet) {
		return UrlHelper.buildLink(gameSet, AppContext.getApplication().getFacebookCloudUrl());
	}
	
	/**
	 * Returns the url of the picture.
	 * @param gameSet
	 * @return
	 */
	public static String buildGameSetPictureUrl(final GameSet gameSet) {
		return UrlHelper.buildPictureLink(gameSet);
	}
	
	/**
	 * 
	 * @param gameSet
	 * @return
	 */
	public static String buildCaption(final GameSet gameSet) {
		return AppContext.getApplication().getResources().getString(R.string.lblFacebookPostCaption, Joiner.on( ", " ).skipNulls().join(gameSet.getPlayers().getPlayerNames()), gameSet.getGameCount());
	}

	/**
	 * @param gameSet
	 * @return
	 */
	public static String buildName(final GameSet gameSet) {
		return AppContext.getApplication().getResources().getString(R.string.lblFacebookPostName);
	}

	/**
	 * Build description to be posted on facebook timeline.
	 * @param gameSet
	 * @return
	 */
	public static String buildDescription(final GameSet gameSet) {
		StringBuffer toReturn = new StringBuffer();
		
		Player winner = null;
		for (Player player : gameSet.getPlayers().getPlayers()) {
			if (gameSet.isWinner(player)) {
				winner = player;
			}
		}
		
		if (winner != null) {
			toReturn.append(
				AppContext.getApplication().getResources().getString(
					R.string.lblFacebookPostHasWon, 
					winner.getName(), 
					gameSet.getGameSetScores().getIndividualResultsAtGameOfIndex(gameSet.getGameCount(), winner))
				);
		}
		
		return toReturn.toString();
	}
		
	/**
	 * Shows a notification indicating the publish is in progress. 
	 * @param activity
	 * @return
	 */
	public static int showNotificationStartProgress(Activity activity) {
		checkArgument(activity != null, "activity is null");
		
		int notificationId = RANDOM.nextInt();
		try {
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(activity)
			        .setSmallIcon(R.drawable.icon_notification_small)
			        .setContentTitle(AppContext.getApplication().getResources().getString(R.string.titleFacebookNotificationInProgress))
			        .setContentText(AppContext.getApplication().getResources().getString(R.string.msgFacebookNotificationInProgress))
			        .setProgress(0, 0, true);
			
			NotificationManager mNotificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(notificationId, mBuilder.build());
		}
		// Problem with older versions of android or devices, need to provide a PendingIntent that leads to an activity
		catch (IllegalArgumentException iae) {
			Intent intent = new Intent(activity, NotificationActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(activity)
			        .setSmallIcon(R.drawable.icon_notification_small)
			        .setContentTitle(AppContext.getApplication().getResources().getString(R.string.titleFacebookNotificationInProgress))
			        .setContentText(AppContext.getApplication().getResources().getString(R.string.msgFacebookNotificationInProgress))
			        .setProgress(0, 0, true)
			        .setContentIntent(contentIntent);
			
			NotificationManager mNotificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(notificationId, mBuilder.build());
		}

		return notificationId;
	}
	
	/**
	 * Shows a notification indicating the publish is finished.
	 * @param activity
	 * @param url
	 * @param notificationId
	 */
	public static void showNotificationStopProgressSuccess(Activity activity, String url, int notificationId) {
		checkArgument(activity != null, "activity is null");
		checkArgument(activity != null, "url is null");
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(activity)
		        .setSmallIcon(R.drawable.icon_notification_small)
		        .setContentTitle(AppContext.getApplication().getResources().getString(R.string.titleFacebookNotificationDone))
		        .setContentText(AppContext.getApplication().getResources().getString(R.string.msgFacebookNotificationDone))
		        .setAutoCancel(true)
		        .setProgress(0, 0, false);
		
		mBuilder.setContentIntent(PendingIntent.getActivity(activity, 0, intent, 0));
		
		NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(notificationId, mBuilder.build());
	}

	/**
	 * Shows a notification indicating the publish has failed.
	 * @param activity
	 * @param notificationId
	 */
	public static void showNotificationStopProgressFailure(Activity activity, int notificationId) {
		checkArgument(activity != null, "activity is null");
		
		Intent intent = new Intent(activity, NotificationActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(activity)
		        .setSmallIcon(R.drawable.icon_notification_small)
		        .setContentTitle(AppContext.getApplication().getResources().getString(R.string.titleFacebookNotificationError))
		        .setContentText(AppContext.getApplication().getResources().getString(R.string.msgFacebookNotificationError))
		        .setAutoCancel(true)
		        .setProgress(0, 0, false)
		        .setContentIntent(contentIntent);
						
		NotificationManager mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(notificationId, mBuilder.build());
		
		AppContext.getApplication().getNotificationIds().clear();
	}
}