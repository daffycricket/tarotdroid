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

import java.security.MessageDigest;
import java.text.DateFormat;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.lib.ui.constants.PreferenceConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public final class UIHelper {

	/**
	 * Number of days before the user will be prompted for rating the app.
	 */
    private final static int DAYS_UNTIL_PROMPT = 3;
    
    /**
     * Number of launches before the user will be prompted for rating the app. 
     */
    private final static int LAUNCHES_UNTIL_PROMPT = 7;
    
    /**
     * Gson instance used to serialize/deserialize objects.
     */
    private static final Gson gson = new Gson();
	
	/**
	 * Default constructor.
	 */
	private UIHelper() {
	}
	
	/**
	 * "Yes"/"No" dialog box click listener.
	 */
	private static DialogInterface.OnClickListener richTextDialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(final DialogInterface dialog, final int which) {
	        switch (which) {
	            case DialogInterface.BUTTON_POSITIVE:
	            case DialogInterface.BUTTON_NEGATIVE:
	            default:
	        	break;
	        }
	    }
	};
	
	/**
	 * Sets "keep screen on" or "off" 
	 * @param activity
	 * @param keepScreenOn
	 */
	public static void setKeepScreenOn(final Activity activity, final boolean keepScreenOn) {
		if (keepScreenOn) {
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} 
		else {
			activity.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
	/**
	 * Shows a dialog with a rich text and a yes button.
	 * @param activity
	 * @param richText
	 */
	public static void showSimpleRichTextDialog(final Context context, final String richText, final String title) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View alertDialogView = factory.inflate(R.layout.alert_dialog, null);
        TextView txtDialog = (TextView)alertDialogView.findViewById(R.id.txtDialog);
        txtDialog.setText(Html.fromHtml(richText));
        txtDialog.setMovementMethod(LinkMovementMethod.getInstance());
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setPositiveButton(context.getString(R.string.btnOk), UIHelper.richTextDialogClickListener);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setView(alertDialogView);
		builder.show();
	}
	
	/**
	 * Displays a nice message and stops the current activity.
	 * @param activity
	 */
	public static void closeSmoothlyIfExceptionHappen(final Activity activity) {
		checkArgument(activity != null, "activity is null");
		UIHelper.showSimpleRichTextDialog(
				activity,  
				activity.getString(R.string.msgUnmanagedErrorGameSetLost),
				activity.getString(R.string.titleUnmanagedErrorGameSetLost)
		);
		activity.finish();
	}
	
	/**
	 * Shows a message telling the user he can modify or update a game.
	 * @param context
	 */
	public static void showModifyOrDeleteGameMessage(final Context context) {
		checkArgument(context != null, "context is null");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
		// display message if first time
		if (!prefs.getBoolean(PreferenceConstants.PrefGameDeletionOrModificationMessageAlreadyShown, false)) { 
			UIHelper.showSimpleRichTextDialog(
				context, 
				context.getString(R.string.msgYouCanModifyAGame),
				context.getString(R.string.titleYouCanModifyAGame)
			);
			
			SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PreferenceConstants.PrefGameDeletionOrModificationMessageAlreadyShown, true);
            editor.commit();
        }
	}

	/**
	 * Shows a message telling the user he can associate a facebook picture to a user.
	 * @param context
	 */
	public static void showAssociateFacebookPictureToUserMessage(final Context context) {
		checkArgument(context != null, "context is null");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
		// display message if first time
		if (!prefs.getBoolean(PreferenceConstants.PrefAssociateFacebookPictureToUserMessageAlreadyShown, false)) { 
			UIHelper.showSimpleRichTextDialog(
				context, 
				context.getString(R.string.msgYouCanAssociateAPictureToAPlayer),
				context.getString(R.string.titleYouCanAssociateAPictureToAPlayer)
			);
			
			SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(PreferenceConstants.PrefAssociateFacebookPictureToUserMessageAlreadyShown, true);
            editor.commit();
        }
	}
	
	/**
	 * Decides whether to show the "please rate my app dialog" 
	 * Code found here http://www.androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater
	 * @param context
	 */
    public static void trackAppLaunched(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PreferenceConstants.PrefAppRater, 0);
        if (prefs.getBoolean(PreferenceConstants.PrefDontShowAgain, false)) { 
        	return ; 
        }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong(PreferenceConstants.PrefLaunchCount, 0) + 1;
        editor.putLong(PreferenceConstants.PrefLaunchCount, launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong(PreferenceConstants.PrefDateFirstLaunch, 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong(PreferenceConstants.PrefDateFirstLaunch, date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(context, editor);
            }
        }
        editor.commit();
    }
    
    /**
     * Show rate dialog.
     * @param context
     * @param editor
     */
    private static void showRateDialog(final Context context, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle(context.getString(R.string.lblRateAppTitle));

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView tv = new TextView(context);
        tv.setText(context.getString(R.string.lblRateAppContent));
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        
        Button b1 = new Button(context);
        b1.setText(context.getString(R.string.lblRateNow));
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.nla.tarotdroid")));
                dialog.dismiss();
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(context);
        b2.setText(context.getString(R.string.lblRateLater));
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(context);
        b3.setText(context.getString(R.string.lblRateNever));
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean(PreferenceConstants.PrefDontShowAgain, true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);        
        dialog.show();        
    }
	
//	/**
//	 * 
//	 * @param gameSetParameters
//	 * @param preferences
//	 */
//	public static void fillNonComputationPreferences(final GameSetParameters gameSetParameters, final SharedPreferences preferences) {
//		// game prefs
//		gameSetParameters.setBelgianGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefAreBelgianGamesAllowed, false));
//		gameSetParameters.setPenaltyGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePenaltyGamesAllowed, false));
//		gameSetParameters.setPassedGamesAllowed(preferences.getBoolean(PreferenceConstants.PrefArePassedGamesAllowed, true));
//		gameSetParameters.setDisplayNextDealer(preferences.getBoolean(PreferenceConstants.PrefDisplayNextDealer, true));
//		gameSetParameters.setPriseAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPriseAuthorized, true));
//		gameSetParameters.setPetiteAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsPetiteAuthorized, false));
//		gameSetParameters.setDeadPlayerAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsOneDeadPlayerAuthorized, false));
//		gameSetParameters.setDeadPlayerAutomaticallySelected(preferences.getBoolean(PreferenceConstants.PrefIsDeadPlayerAutomaticallySelected, true));
//		gameSetParameters.setMiseryAuthorized(preferences.getBoolean(PreferenceConstants.PrefIsMiseryAuthorized, false));
//		// display preferences
//		gameSetParameters.setDisplayGamesInReverseOrder(preferences.getBoolean(PreferenceConstants.PrefDisplayGamesInReverseOrder, true));
//		gameSetParameters.setDisplayGlobalScoresForEachGame(preferences.getBoolean(PreferenceConstants.PrefDisplayGlobalScoresForEachGame, false));
//		gameSetParameters.setKeepScreenOn(preferences.getBoolean(PreferenceConstants.PrefKeepScreenOn, true));
//		// dev preferences
//		gameSetParameters.setDevSimulationMode(preferences.getBoolean(PreferenceConstants.PrefIsSimulationMode, false));
//		gameSetParameters.setDevGameSetCount(preferences.getInt(PreferenceConstants.PrefDevGameSetCount, 5));
//		gameSetParameters.setDevMaxGameCount(preferences.getInt(PreferenceConstants.PrefDevMaxGameCount, 15));
//	}
	
//	/**
//	 * Returns a serialized version of the GameSet.
//	 * @param gameSet
//	 * @return a serialized version of the GameSet.
//	 */
//	public static String serializeGameSet(GameSet gameSet) {
//		checkArgument(gameSet != null, "gameSet is null");
//		return gson.toJson(gameSet);
//	}
//	
//	/**
//	 * Deserializes a game set from a string
//	 * @param gameSetSerialized
//	 * @return a game set
//	 */
//	public static GameSet deserializeGameSet(String gameSetSerialized) {
//		checkArgument(gameSetSerialized != null, "gameSetSerialized is null");
//		return gson.fromJson(gameSetSerialized, GameSet.class);
//	}
	
//	/**
//	 * Serializes the current game set in default preferences.
//	 * @param activity
//	 */
//	public static void serializeGameSetInPreferences(Activity activity, GameSet gameSet) {
//		checkArgument(activity != null, "activity is null");
//		checkArgument(gameSet != null, "gameSet is null");
//		serializeGameSetInPreferences(PreferenceManager.getDefaultSharedPreferences(activity), gameSet);
//	}
//	
//	/**
//	 * Serializes the current game set in specified preferences.
//	 * @param preferences
//	 */
//	public static void serializeGameSetInPreferences(SharedPreferences preferences, GameSet gameSet) {
//		checkArgument(preferences != null, "preferences is null");
//		checkArgument(gameSet != null, "gameSet is null");
//		try {
//			Editor editor = preferences.edit();
//			editor.putString(
//					PreferenceConstants.PrefCurrentSerializedGameSet, 
//					serializeGameSet(gameSet)
//			);
//			editor.commit();
//		}
//		catch (Exception e) {
//			// do nothing
//		}
//	}
//	
//	/**
//	 * Retrives 
//	 * @param activity
//	 */
//	public static GameSet deserializeGameSetFromPreferences(SharedPreferences preferences) {
//		checkArgument(preferences != null, "preferences is null");
//		GameSet toReturn = null;
//		try {
//			String serializedGameSet = preferences.getString(PreferenceConstants.PrefCurrentSerializedGameSet, null);
//			if (serializedGameSet != null && serializedGameSet.length() != 0) {
//				toReturn = deserializeGameSet(serializedGameSet);
//			}
//		}
//		catch (Exception e) {
//			// do nothing
//		}
//		
//		return toReturn;
//	}
//	
//	/**
//	 * Clear the serialized game set currently in preferences.
//	 * @param activity
//	 */
//	public static void clearSerializedGameSetInPreferences(Activity activity) {
//		checkArgument(activity != null, "activity is null");
//		clearSerializedGameSetInPreferences(PreferenceManager.getDefaultSharedPreferences(activity));
//	}
//	
//	/**
//	 * Clear the serialized game set currently in preferences.
//	 * @param activity
//	 */
//	public static void clearSerializedGameSetInPreferences(SharedPreferences preferences) {
//		checkArgument(preferences != null, "preferences is null");
//		try {
//			Editor editor = preferences.edit();
//			editor.remove(PreferenceConstants.PrefCurrentSerializedGameSet);
//			editor.commit();
//		}
//		catch (Exception e) {
//			// do nothing
//		}
//	}
	
	/**
	 * @param gameSet
	 * @return
	 */
	public static String buildGameSetHistoryTitle(final GameSet gameSet) {
		String formatedDate = DateFormat.getInstance().format(gameSet.getCreationTs());
		
		return String.format(
				AppContext.getApplication().getResources().getString(R.string.lblGameSetTitle),
				formatedDate
		);	
	}
	
	/**
	 * @param gameSet
	 * @return
	 */
	public static String buildGameSetHistoryDescription(final GameSet gameSet) {
		String playersIdsAsString = "";
		for (Player player : gameSet.getPlayers()) {
			playersIdsAsString += player.getName() + ", "; 
		}
		String gameCountAsString = Integer.toString(gameSet.getGameCount()); 
		
		String toFormat = "0".equals(gameCountAsString) 
					? AppContext.getApplication().getResources().getString(R.string.lblGameSetDescriptionSingle)
					: AppContext.getApplication().getResources().getString(R.string.lblGameSetDescriptionPlural);
		
		return String.format(
			toFormat,
			gameCountAsString,
			playersIdsAsString.substring(0, playersIdsAsString.length() - 2)
		);	
	}
    
	/**
	 * Returns a localized String of a KingType enumeration item.
	 * @param king
	 * @return a localized String of a KingType enumeration item.
	 */
	public static String getKingTranslation(final KingType king) {
		switch(king) {
			case Clubs:
				return AppContext.getApplication().getResources().getString(R.string.lblClubsColor);
			case Diamonds:
				return AppContext.getApplication().getResources().getString(R.string.lblDiamondsColor);
			case Hearts:
				return AppContext.getApplication().getResources().getString(R.string.lblHeartsColor);
			case Spades:
				return AppContext.getApplication().getResources().getString(R.string.lblSpadesColor);
			default :
				return "unknow Colors"; 
		}
	}
	
	/**
	 * Returns a localized String of a BetType enumeration item.
	 * @param bet
	 * @return	a localized String of a BetType enumeration item.
	 */
	public static String getBetTranslation(final BetType bet) {
		switch (bet) {
			case Prise :
				return AppContext.getApplication().getResources().getString(R.string.priseDescription);
			case Petite :
				return AppContext.getApplication().getResources().getString(R.string.petiteDescription);
			case Garde :
				return AppContext.getApplication().getResources().getString(R.string.gardeDescription);
			case GardeSans :
				return AppContext.getApplication().getResources().getString(R.string.gardeSansDescription);
			case GardeContre :
				return AppContext.getApplication().getResources().getString(R.string.gardeContreDescription);
			case Belge :
				return AppContext.getApplication().getResources().getString(R.string.belgeDescription);
			default :
				return "unknow BetType";
		}
	}
	
	/**
	 * Returns a localized String of a BetType enumeration item.
	 * @param bet
	 * @return	a localized String of a BetType enumeration item.
	 */
	public static String getShortBetTranslation(final BetType bet) {
		switch (bet) {
			case Petite :
				return AppContext.getApplication().getResources().getString(R.string.shortPetiteDescription);
			case Prise :
				return AppContext.getApplication().getResources().getString(R.string.shortPriseDescription);
			case Garde :
				return AppContext.getApplication().getResources().getString(R.string.shortGardeDescription);
			case GardeSans :
				return AppContext.getApplication().getResources().getString(R.string.shortGardeSansDescription);
			case GardeContre :
				return AppContext.getApplication().getResources().getString(R.string.shortGardeContreDescription);
			case Belge :
				return AppContext.getApplication().getResources().getString(R.string.shortBelgeDescription);
			default :
				return "unknow BetType";
		}
	}

	/**
	 * 
	 * @param activity
	 * @return
	 */
	public static String getFacebookHashKey(Activity activity) {
		checkArgument(activity != null, "activity is null");
		String toReturn = null;
		try {
		    PackageInfo info = activity.getPackageManager().getPackageInfo(AppContext.getApplication().getAppPackage(), PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        toReturn = Base64.encodeToString(md.digest(), Base64.DEFAULT);
		    }
		}
		catch (Exception e) {
			toReturn = e.toString();
		}
		
		return toReturn;
	}
	
    /**
     * Returns the photo associated to a contact, if it has one.  
     * @param context The context.
     * @param contactId The contact id.
     * @return The photo associated to a contact, if it has one.
     */
    public static Bitmap getContactPicture(final Context context, final String contactId) {
            // the photo to return
        Bitmap photoBitmap = null;

        // get the contact id
        String photoId = null;
                
            // query for contact name and photo  
            Cursor contact =  context.getContentResolver().query(
                        Contacts.CONTENT_URI,
                        new String[] { Contacts.DISPLAY_NAME, Contacts.PHOTO_ID }, 
                        Contacts._ID + "=?",
                        new String[]{ contactId }, 
                    null
            );

            // get name and photo id
        if (contact.moveToFirst()) {
                photoId = contact.getString(contact.getColumnIndex(Contacts.PHOTO_ID));
        }
        contact.close();
        
        // if possible, try to get the photo 
        if (photoId != null) {

                // query for actual photo
                Cursor photo = context.getContentResolver().query(
                                Data.CONTENT_URI,
                                new String[] { Photo.PHOTO },
                                Data._ID + "=?",
                                new String[]{ photoId },
                                null
                );
                
                // get the photo
                if(photo.moveToFirst()) {
                        byte[] photoBlob = photo.getBlob(photo.getColumnIndex(Photo.PHOTO));
                        photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.length);
                }
                photo.close();
        }
        
        return photoBitmap;
    }
}
