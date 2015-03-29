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
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.lib.app.AppContext;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.google.common.base.Throwables;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class AuditHelper {
	
	/**
	 * List of user events.
	 */
	private static final List<UserEventTypes> userEvents = newArrayList();
	
	/**
	 * Set the time span of a session to 10 minutes (roughly the max time users need to play a game). 
	 */
	static {
		if (!AppContext.getApplication().isAppInDebugMode()) {
			FlurryAgent.setContinueSessionMillis(180000);
		}
	}
	
	/**
	 * Enumeration of all user events.
	 */
	public enum UserEventTypes {		
		clickedNewGameSet,
		clickedGameSetHistory
	};
	
	/**
	 * Enumeration of all available auditable events.
	 */
	public enum EventTypes {
		
		// main features
		displayMainDashboardPage,
		displayNewGameSetDashboardPage,
		displayGameSetHistoryPage,
		displayPlayerListPage,
		displayCommunityPage,
		displayMainPreferencePage,
		
		// bluetooth
		actionBluetoothDiscoverDevices,
		actionBluetoothSetDiscoverable,
		actionBluetoothReceiveGameSet,
		actionBluetoothSendGameSet,
		
		// game set
		displayGameSetCreationPage,
		displayTabGameSetPreferencePage,
		displayTabGameSetPageWithNewGameSetAction,
		displayTabGameSetPageWithExistingGameSetAction,
		
		// game creation
		displayGameCreationV2Page,
		displayGameCreationV1Page,
		displayGameReadV1Page,
		displayGameReadV2Page,
		
		// player statistics
		displayPlayerStatisticsPage,
		
		// game set statistics
		displayGameSetStatisticsPage,
		displayGameSetStatisticsScoresEvolutionChart,
		displayGameSetStatisticsBetsDistribution,
		displayGameSetStatisticsLeadingPlayerRepartition,
		displayGameSetStatisticsCalledPlayerRepartition,
		displayGameSetStatisticsKingsRepartition,
		displayGameSetStatisticsResultsDistribution,
		displayCharts,

		// problem in TabGameSetActivity.onCreateOptionsMenu()
		tabGameSetActivity_onCreateOptionsMenu_GameSetParametersIsNull,
		tabGameSetActivity_auditEvent_GameSetIsNull
	}
	
	/**
	 * Enumeration of all available auditable event's parameters.
	 */
	public enum ParameterTypes {
		gameStyleType,
		gameType,
		playerCount,
		version
	}
	
	/**
	 * Enumeration of all available known error.
	 */
	public enum ErrorTypes {
		activityCreationError,
		unexpectedError,
		dalInitializationError,
		globalUncaughtError, 
		bluetoothReceiveError,
		bluetoothSendError,
		notificationError,
		excelFileStorage,
		displayAndRemoveGameDialogActivityCreationError,
		gameCreationActivityError,
		gameSetHistoryActivityError,
		bluetoothBeforeSendError,
		gameSetCreationActivityError,
		gameSetStatisticsActivityError,
		mainDashBoardActivityError,
		mainPreferencesActivityError,
		newGameSetDashboardActivityError,
		playerListActivityError,
		playerStatisticsActivityError,
		tabGameSetActivityError,
		tabGameSetPreferencesActivityError,
		gameReadViewPagerActivityError,
		tabGameSetActivityOnStartError,
		tabGameSetActivityOnResumeError,
		facebookNewMeRequestFailed,
		facebookNewMeRequestFailedWithUserNull,
		upSyncGameSetTaskError,
		postGameSetLinkOnFacebookWallTaskError,
		postGameSetOnFacebookAppError,
		persistGameTaskError,
		playerSelectorActivityError,
		updateGameSetError, 
		exportDatabaseError,
		importDatabaseError,
		exportExcelError,
		facebookHasNoPublishPermission
	}
	
	/**
	 * Prevent from constructing the object.
	 */
	private AuditHelper() {
	}
	
	/**
	 * Tracks a user event.
	 * @param userEventTypes
	 */
	public static void trackUserEvent(final UserEventTypes userEventTypes) {
		checkArgument(userEventTypes != null, "userEventTypes is null");
		userEvents.add(userEventTypes);
	}
	
	/**
	 * Starts a tracking session.
	 * @param context
	 */
	public static void auditSession(final Context context) {
		FlurryAgent.onStartSession(context, AppContext.getApplication().isAppInDebugMode() ? "JA7HZLWC2VU8V1AGBVQL" : AppContext.getApplication().getFlurryApplicationId());
	}
	
	/**
	 * Finishes a tracking session.
	 * @param context
	 */
	public static void stopSession(final Context context) {
		FlurryAgent.onEndSession(context);
	}
	
	/**
	 * Sends an audit event.
	 * @param eventType
	 */
	public static void auditEvent(final EventTypes eventType) {
		auditEvent(eventType, null);
	}
	
	/**
	 * Sends an audit event.
	 * @param eventType
	 * @param parameters
	 */
	public static void auditEvent(final EventTypes eventType, final Map<ParameterTypes, Object> parameters) {
		try {
			Map<String, String> toSend = newHashMap();
			toSend.put(ParameterTypes.version.toString(), AppContext.getApplication().getAppVersion());
			
			if (parameters != null) { 
				for(Map.Entry<ParameterTypes, Object> entry : parameters.entrySet()) {
					try {
						toSend.put(new String(entry.getKey().toString()), entry.getValue().toString());
					}
					catch (Exception e) {
					}
				}
			}
			FlurryAgent.logEvent(eventType.toString(), toSend, true);
		}
		catch (Exception ex) {
			Log.v(AppContext.getApplication().getAppLogTag(), "AuditHelper.auditEvent()", ex);
		}
	}
	

	/**
	 * Sends an error audit trail and displays the message on the activity if possible.
	 * @param errorType
	 * @param exception
	 * @param activity
	 */
	public static void auditError(final ErrorTypes errorType, final Throwable exception, final Activity activity) {
		if (activity != null) {	
			UIHelper.showSimpleRichTextDialog(activity, exception.toString(), "Unexpected error");
		}
		
		String exceptionAsString = exception == null ? null : Throwables.getStackTraceAsString(exception); 
		auditError(errorType, exceptionAsString);
	}
	
	/**
	 * Sends an error audit trail and displays the message on the activity if possible.
	 * @param errorType
	 * @param exception
	 * @param activity
	 */
	public static void auditErrorAsString(final ErrorTypes errorType, final String exceptionAsString, final Activity activity) {
		if (activity != null) {
			UIHelper.showSimpleRichTextDialog(activity, exceptionAsString, "Unexpected error");
		}
		auditError(errorType, exceptionAsString);
	}
	
	/**
	 * Sends an error audit trail.
	 * @param errorType
	 * @param exception
	 */
	public static void auditError(final ErrorTypes errorType, final Throwable exception) {
		String exceptionAsString = exception == null ? null : Throwables.getStackTraceAsString(exception); 
		auditError(errorType, exceptionAsString);
	}
	
	/**
	 * Sends an error audit trail.
	 * @param errorType
	 * @param exceptionAsString
	 */
	public static void auditError(final ErrorTypes errorType, final String exceptionAsString) {
		try {
			ErrorTypes errorTypeToAudit = errorType;
			if (errorTypeToAudit == null) {
				errorTypeToAudit = ErrorTypes.unexpectedError;
			}

			if (exceptionAsString != null) {
				FlurryAgent.onError(errorTypeToAudit.toString(), android.os.Build.MODEL + "|" + android.os.Build.VERSION.SDK_INT, exceptionAsString);
			}
			else {
				FlurryAgent.onError(errorTypeToAudit.toString(), android.os.Build.MODEL + "|" + android.os.Build.VERSION.SDK_INT, "NULL_EXCEPTION");
			}
		}
		catch (Exception ex) {
			Log.v(AppContext.getApplication().getAppLogTag(), "AuditHelper.auditError()", ex);
		}
	}
}