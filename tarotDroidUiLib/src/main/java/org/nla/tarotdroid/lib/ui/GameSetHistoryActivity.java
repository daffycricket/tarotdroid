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
package org.nla.tarotdroid.lib.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.nla.tarotdroid.lib.ui.tasks.RemoveGameSetTask;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.dal.DalException;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.lib.helpers.AuditHelper.EventTypes;
import org.nla.tarotdroid.lib.helpers.BluetoothHelper;
import org.nla.tarotdroid.lib.helpers.FacebookHelper;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.constants.ActivityParams;
import org.nla.tarotdroid.lib.ui.constants.RequestCodes;
import org.nla.tarotdroid.lib.ui.controls.ThumbnailItem;
import org.nla.tarotdroid.lib.ui.tasks.ExportToExcelTask;
import org.nla.tarotdroid.lib.ui.tasks.IAsyncCallback;
import org.nla.tarotdroid.lib.ui.tasks.PostGameSetLinkOnFacebookWallTask;
import org.nla.tarotdroid.lib.ui.tasks.ReceiveGameSetTask;
import org.nla.tarotdroid.lib.ui.tasks.RemoveAllGameSetsTask;
import org.nla.tarotdroid.lib.ui.tasks.SendGameSetTask;
import org.nla.tarotdroid.lib.ui.tasks.UpSyncGameSetTask;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.common.base.Charsets;

/**
 * Displays all the stored game sets.
 * 
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class GameSetHistoryActivity extends SherlockListActivity {

	/**
	 * A listener to transfer a game set to a selected bluetooth device.
	 */
	private class BluetoothDeviceClickListener implements DialogInterface.OnClickListener {

		/**
		 * The device names towards which to send the GameSet.
		 */
		private final String[] bluetoothDeviceNames;

		/**
		 * The GameSet to transfer.
		 */
		private final GameSet gameSet;

		/**
		 * Constructor.
		 * 
		 * @param gameSet
		 * @param bluetoothDeviceNames
		 */
		public BluetoothDeviceClickListener(final GameSet gameSet, final String[] bluetoothDeviceNames) {
			this.gameSet = gameSet;
			this.bluetoothDeviceNames = bluetoothDeviceNames;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.DialogInterface.OnClickListener#onClick(android.content
		 * .DialogInterface, int)
		 */
		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			AuditHelper.auditEvent(AuditHelper.EventTypes.actionBluetoothSendGameSet);
			GameSetHistoryActivity.this.sendGameSetOverBluetooth(this.gameSet, GameSetHistoryActivity.this.bluetoothHelper.getBluetoothDevice(this.bluetoothDeviceNames[which]));
		}
	}

	/**
	 * Internal adapter to back up the list.
	 */
	private class GameSetAdapter extends ArrayAdapter<GameSet> {

		/**
		 * Constructs a GameSetAdapter.
		 * 
		 * @param context
		 * @param gameSets
		 */
		public GameSetAdapter(Context context, List<GameSet> gameSets) {
			super(context, R.layout.thumbnail_item, gameSets);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GameSet gameSet = this.getItem(position);

			int drawableId;
			switch (gameSet.getGameStyleType()) {
			case Tarot3:
				if (gameSet.getFacebookPostTs() != null) {
					drawableId = R.drawable.icon_3players_facebook;
				} else {
					drawableId = R.drawable.icon_3players;
				}
				break;
			case Tarot4:
				if (gameSet.getFacebookPostTs() != null) {
					drawableId = R.drawable.icon_4players_facebook;
				} else {
					drawableId = R.drawable.icon_4players;
				}
				break;
			case Tarot5:
				if (gameSet.getFacebookPostTs() != null) {
					drawableId = R.drawable.icon_5players_facebook;
				} else {
					drawableId = R.drawable.icon_5players;
				}
				break;
			case None:
			default:
				throw new IllegalStateException("unknown gameSet type: " + gameSet.getGameStyleType());
			}

			ThumbnailItem thumbnailItem = new ThumbnailItem(this.getContext(), drawableId, UIHelper.buildGameSetHistoryTitle(gameSet), UIHelper.buildGameSetHistoryDescription(gameSet));

			return thumbnailItem;
		}
	}

	/**
	 * A list item.
	 */
	private static class Item {

		protected enum ItemTypes {
			edit, exportToExcel, publishOnFacebook, publishOnTwitter, remove, transferOverBluetooth
		};

		public final int icon;

		public final ItemTypes itemType;

		public final String text;

		public Item(String text, Integer icon, ItemTypes itemType) {
			this.text = text;
			this.icon = icon;
			this.itemType = itemType;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	/**
	 * Facebook post response.
	 */
	private interface PostResponse extends GraphObject {
		String getId();
	}

	/**
	 * A listener to delete a game set.
	 */
	private class RemoveGameSetDialogClickListener implements DialogInterface.OnClickListener {

		/**
		 * The GameSet to transfer.
		 */
		private final GameSet gameSet;

		/**
		 * Constructor.
		 * 
		 * @param gameSet
		 */
		public RemoveGameSetDialogClickListener(final GameSet gameSet) {
			this.gameSet = gameSet;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.content.DialogInterface.OnClickListener#onClick(android.content
		 * .DialogInterface, int)
		 */
		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				RemoveGameSetTask removeGameSetTask = new RemoveGameSetTask(GameSetHistoryActivity.this, GameSetHistoryActivity.this.progressDialog, this.gameSet);
				removeGameSetTask.setCallback(refreshCallback);
				removeGameSetTask.execute();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
			default:
				break;
			}
		}
	}

	/**
	 * Items for non limited app.
	 */
	private static final Item[] allItems = { new Item(AppContext.getApplication().getResources().getString(R.string.lblPublishGameSet), R.drawable.facebook_white, Item.ItemTypes.publishOnFacebook),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblPublishGameSet), R.drawable.facebook_white, Item.ItemTypes.publishOnTwitter),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblEditGameSet), android.R.drawable.ic_menu_edit, Item.ItemTypes.edit),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblDeleteGameSet), R.drawable.gd_action_bar_trashcan, Item.ItemTypes.remove),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblBluetoothSend), R.drawable.stat_sys_data_bluetooth, Item.ItemTypes.transferOverBluetooth),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblExcelExport), R.drawable.ic_excel, Item.ItemTypes.exportToExcel), };

	/**
	 * Comparator of gamesets based upon their creation dates.
	 */
	private static final Comparator<GameSet> gameSetCreationDateDescendingComparator = new Comparator<GameSet>() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final GameSet arg0, final GameSet arg1) {
			return (arg1.getCreationTs().compareTo(arg0.getCreationTs()));
		}
	};

	/**
	 * Items for limited app.
	 */
	private static final Item[] limitedItems = {
			new Item(AppContext.getApplication().getResources().getString(R.string.lblPublishGameSet), R.drawable.facebook_white, Item.ItemTypes.publishOnFacebook),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblPublishGameSet), R.drawable.facebook_white, Item.ItemTypes.publishOnTwitter),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblEditGameSet), android.R.drawable.ic_menu_edit, Item.ItemTypes.edit),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblDeleteGameSet), R.drawable.gd_action_bar_trashcan, Item.ItemTypes.remove),
			new Item(AppContext.getApplication().getResources().getString(R.string.lblBluetoothSend), R.drawable.stat_sys_data_bluetooth, Item.ItemTypes.transferOverBluetooth) };

	/**
	 * Key used in storing the pendingReauthRequest flag
	 */
	private static final String PENDING_REAUTH_KEY = "pendingReauthRequest";

	/**
	 * List of additional write permissions being requested
	 */
	private static final List<String> PUBLISH_PERMISSIONS = Arrays.asList("publish_actions");

	/**
	 * List of read permissions being requested.
	 */
	private static final List<String> READ_PERMISSIONS = Arrays.asList("email");

	/**
	 * Activity code to flag an incoming activity result is due to a new
	 * permissions request.
	 */
	private static final int REAUTH_ACTIVITY_CODE = 100;

	/**
	 * The bluetooth helper.
	 */
	private BluetoothHelper bluetoothHelper;

	@SuppressWarnings("rawtypes")
	private AsyncTask currentRunningTask;

	/**
	 * Callback called when excel export task is done.
	 */
	private final IAsyncCallback<String> excelExportCallback = new IAsyncCallback<String>() {

		@Override
		public void execute(String filePath, Exception e) {

			// TODO Check exception
			onGameSetExportedToExcelFile(filePath);
		}
	};

	/**
	 * "Yes / No" export excel by email.
	 */
	private final DialogInterface.OnClickListener exportExcelByEmailDialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				try {
					Uri uri = Uri.fromFile(new File(tempExcelFilePath));

					StringBuilder contentText = new StringBuilder();
					contentText.append(getString(R.string.lblDbExportEmailContent));

					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("message/rfc822");
					intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.lblDbExportEmailTitle));
					intent.putExtra(Intent.EXTRA_TEXT, contentText.toString());
					intent.putExtra(Intent.EXTRA_STREAM, uri);

					startActivity(Intent.createChooser(intent, getString(R.string.lblDbExportAndroidIntentTitle)));
				} catch (Exception e) {
					AuditHelper.auditError(ErrorTypes.exportExcelError, e, GameSetHistoryActivity.this);
				}
				break;
			case DialogInterface.BUTTON_NEGATIVE:
			default:
				break;
			}
		}
	};

	/**
	 * Facebook session state change callback.
	 */
	private final Session.StatusCallback facebookSessionStatusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	/**
	 * Http client for communication with the cloud.
	 */
	private DefaultHttpClient httpClient;

	/**
	 * Indicates an on-going reauthorization request
	 */
	private boolean pendingReauthRequest;

	/**
	 * Callback used after post on facebook wall.
	 */
	private final IAsyncCallback<Response> postToFacebookWallCallback = new IAsyncCallback<Response>() {

		@Override
		public void execute(Response facebookResponse, Exception e) {
			// TODO Check Exception
			GameSetHistoryActivity.this.onPostPublishGameSetOnFacebookWall(facebookResponse);
		}
	};

	/**
	 * The progress dialog.
	 */
	private ProgressDialog progressDialog;

	/**
	 * The AsyncTask to receive a GameSet.
	 */
	private ReceiveGameSetTask receiveGameSetTask;

	/**
	 * Callback used to refresh the list.
	 */
	private final IAsyncCallback<Object> refreshCallback = new IAsyncCallback<Object>() {

		@Override
		public void execute(Object isNull, Exception e) {
			// TODO Check if exception must not be handled
			GameSetHistoryActivity.this.refresh();
		}
	};

	/**
	 * "Yes / No" remove all gamesets dialog box listener.
	 */
	private final DialogInterface.OnClickListener removeAllGameSetsDialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				RemoveAllGameSetsTask removeAllGameSetsTask = new RemoveAllGameSetsTask(GameSetHistoryActivity.this, GameSetHistoryActivity.this.progressDialog);
				removeAllGameSetsTask.setCallback(refreshCallback);
				removeAllGameSetsTask.execute();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
			default:
				break;
			}
		}
	};

	/**
	 * The AsyncTask to send a GameSet.
	 */
	private SendGameSetTask sendGameSetTask;

	/**
	 * Temporary path for Excel path. Use with care, this is pretty much a
	 * global variable (it's used between differents inner classes).
	 */
	private String tempExcelFilePath;

	/**
	 * Temporary GameSet. Use with care, this is pretty much a global variable
	 * (it's used between differents inner classes).
	 */
	private GameSet tempGameSet;

	/**
	 * Facebook ui lifecyle manager.
	 */
	private UiLifecycleHelper uiHelper;

	/**
	 * Callback to execute when post has been posted to facebook.
	 */
	private final IAsyncCallback<Object> upSyncCallback = new IAsyncCallback<Object>() {

		@Override
		public void execute(Object object, Exception e) {
			// TODO Check exception
			GameSetHistoryActivity.this.publishGameSetOnFacebook();
		}
	};

	/**
	 * Traces creation event.
	 */
	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayGameSetHistoryPage);
	}

	/**
	 * Handle facebook error.
	 * 
	 * @param error
	 */
	private void handleFacebookError(final FacebookRequestError error) {
		DialogInterface.OnClickListener listener = null;
		String dialogBody = null;

		if (error == null) {
			dialogBody = getString(R.string.error_dialog_default_text);
		} else {

			dialogBody = error.toString();

			// Show the error and pass in the listener so action
			// can be taken, if necessary.
			new AlertDialog.Builder(this).setPositiveButton(R.string.error_dialog_button_text, listener).setTitle(R.string.error_dialog_title).setMessage(dialogBody).show();
		}
	}

	/**
	 * Checks whether the current session is allowed to publish on facebook.
	 * 
	 * @return true if the session can publish, false otherwisee.
	 */
	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null && session.getPermissions().containsAll(PUBLISH_PERMISSIONS);
	}

	/**
	 * Checks whether the current session is allowed to read on facebook.
	 * 
	 * @return
	 */
	private boolean hasReadPermission() {
		Session session = Session.getActiveSession();
		return session != null && session.getPermissions().containsAll(READ_PERMISSIONS);
	}

	/**
	 * Indicates whether the bluetooth is activated on the device.
	 * 
	 * @return
	 */
	private boolean isBluetoothActivated() {
		boolean isActivated = GameSetHistoryActivity.this.bluetoothHelper.isBluetoothEnabled();
		if (!isActivated) {
			Toast.makeText(GameSetHistoryActivity.this, AppContext.getApplication().getResources().getString(R.string.msgActivateBluetooth), Toast.LENGTH_SHORT).show();
		}
		return isActivated;
	}

	/**
	 * Starts the whole Facebook post process.
	 * 
	 * @param session
	 */
	private void launchPostProcess(final Session session) {

		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(R.drawable.icon_facebook_released);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText("Publication en cours");

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();

		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {

			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (session == Session.getActiveSession()) {
					if (user != null) {
						int notificationId = FacebookHelper.showNotificationStartProgress(GameSetHistoryActivity.this);
						AppContext.getApplication().getNotificationIds().put(tempGameSet.getUuid(), notificationId);

						AppContext.getApplication().setLoggedFacebookUser(user);
						UpSyncGameSetTask task = new UpSyncGameSetTask(GameSetHistoryActivity.this, progressDialog);
						task.setCallback(GameSetHistoryActivity.this.upSyncCallback);
						task.execute(tempGameSet);
						currentRunningTask = task;
					}
				}
				if (response.getError() != null) {
					// //progressDialog.dismiss();
					// Session newSession = new
					// Session(GameSetHistoryActivity.this);
					// Session.setActiveSession(newSession);
					// newSession.openForPublish(new
					// Session.OpenRequest(GameSetHistoryActivity.this).setPermissions(Arrays.asList("publish_actions",
					// "email")).setCallback(facebookSessionStatusCallback));
				}
			}
		});
		request.executeAsync();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// cancel send task if running
		try {
			if (this.sendGameSetTask != null) {
				this.sendGameSetTask.cancel(true);
			}
		} catch (Exception e) {
		}

		// cancel receive task if running
		try {
			if (this.receiveGameSetTask != null) {
				this.receiveGameSetTask.cancel(true);
			}
		} catch (Exception e) {
		}

		super.onBackPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);

			// facebook lifecycle objects
			this.uiHelper = new UiLifecycleHelper(this, facebookSessionStatusCallback);
			this.uiHelper.onCreate(savedInstanceState);

			this.auditEvent();
			// initialize progress dialog
			this.progressDialog = new ProgressDialog(this);
			this.progressDialog.setCanceledOnTouchOutside(false);

			this.httpClient = new DefaultHttpClient();
			HttpParams httpParams = new BasicHttpParams();
			HttpProtocolParams.setContentCharset(httpParams, Charsets.UTF_8.toString());
			httpClient.setParams(httpParams);

			// initialize bluetooth
			this.bluetoothHelper = AppContext.getApplication().getBluetoothHelper();
			this.bluetoothHelper.setActivity(this);

			// set excuse as background image
			this.getListView().setCacheColorHint(0);
			this.getListView().setBackgroundResource(R.drawable.img_excuse);

			// set action bar properties
			this.setTitle();
			this.registerForContextMenu(this.getListView());

			// set internal properties
			this.tempExcelFilePath = null;
			this.tempGameSet = null;

			// wait for the dal to be initiated to refresh the game sets
			if (AppContext.getApplication().getLoadDalTask().getStatus() == AsyncTask.Status.RUNNING) {
				AppContext.getApplication().getLoadDalTask().showDialogOnActivity(this, this.getResources().getString(R.string.msgGameSetsRetrieval));
				AppContext.getApplication().getLoadDalTask().setCallback(new IAsyncCallback<String>() {

					@Override
					public void execute(String result, Exception e) {

						// TODO Check exception
						if (result != null && result.toString().length() > 0) {
							UIHelper.showSimpleRichTextDialog(GameSetHistoryActivity.this, result, "Erreur de chargement");
						}

						GameSetHistoryActivity.this.refresh();
					}
				});
			}
			// refresh the game sets
			else {
				this.refresh();
			}

			Object currentRunningTask = getLastNonConfigurationInstance();
			if (currentRunningTask != null) {
				if (currentRunningTask instanceof UpSyncGameSetTask) {
					UpSyncGameSetTask task = (UpSyncGameSetTask) currentRunningTask;
					task.attach(this);
				} else if (currentRunningTask instanceof PostGameSetLinkOnFacebookWallTask) {
					PostGameSetLinkOnFacebookWallTask task = (PostGameSetLinkOnFacebookWallTask) currentRunningTask;
					task.attach(this);
				}
			}

			this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					onListItemClick(pos);
					return false;
				}
			});

			this.getListView().setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
					onListItemClick(pos);
				}
			});
		} catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.gameSetHistoryActivityError, e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockListActivity#onCreateOptionsMenu(android
	 * .view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		SubMenu subMenuBlueTooth = menu.addSubMenu(this.getString(R.string.lblBluetoothItem));
		com.actionbarsherlock.view.MenuItem miBluetooth = subMenuBlueTooth.getItem();
		miBluetooth.setIcon(R.drawable.stat_sys_data_bluetooth);
		miBluetooth.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		com.actionbarsherlock.view.MenuItem miBlueToothDiscover = subMenuBlueTooth.add(R.string.lblBluetoothDiscover).setIcon(R.drawable.ic_menu_allfriends);
		com.actionbarsherlock.view.MenuItem miBlueToothGetDiscoverable = subMenuBlueTooth.add(R.string.lblBluetoothGetDiscoverable).setIcon(android.R.drawable.ic_menu_myplaces);
		com.actionbarsherlock.view.MenuItem miBlueToothReceive = subMenuBlueTooth.add(R.string.lblBluetoothReceive).setIcon(R.drawable.ic_menu_download);
		com.actionbarsherlock.view.MenuItem miBlueToothHelp = subMenuBlueTooth.add(R.string.lblBluetoothHelp).setIcon(android.R.drawable.ic_menu_info_details);

		miBlueToothDiscover.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				if (isBluetoothActivated()) {
					GameSetHistoryActivity.this.bluetoothHelper.startDiscovery();
					AuditHelper.auditEvent(AuditHelper.EventTypes.actionBluetoothDiscoverDevices);
				}
				return true;
			}
		});

		miBlueToothGetDiscoverable.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				if (isBluetoothActivated()) {
					GameSetHistoryActivity.this.bluetoothHelper.setBluetoothDeviceDiscoverable();
					AuditHelper.auditEvent(AuditHelper.EventTypes.actionBluetoothSetDiscoverable);
				}
				return true;
			}
		});

		miBlueToothReceive.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				if (isBluetoothActivated()) {
					// retrieve game count
					int gameSetCount;
					try {
						gameSetCount = AppContext.getApplication().getDalService().getGameSetCount();
					} catch (DalException de) {
						gameSetCount = 0;
					}

					// prevent user from downloading if game set count > 5 and
					// limited version
					if (AppContext.getApplication().isAppLimited() && gameSetCount >= 5) {
						Toast.makeText(GameSetHistoryActivity.this, AppContext.getApplication().getResources().getString(R.string.msgLimitedVersionInformation), Toast.LENGTH_SHORT).show();
					}

					// ok for download
					else {
						try {
							GameSetHistoryActivity.this.receiveGameSetTask = new ReceiveGameSetTask(GameSetHistoryActivity.this, GameSetHistoryActivity.this.progressDialog,
									GameSetHistoryActivity.this.bluetoothHelper.getBluetoothAdapter());
							GameSetHistoryActivity.this.receiveGameSetTask.setCallback(refreshCallback);
							GameSetHistoryActivity.this.receiveGameSetTask.execute();
							AuditHelper.auditEvent(AuditHelper.EventTypes.actionBluetoothReceiveGameSet);
						} catch (IOException ioe) {
							Log.v(AppContext.getApplication().getAppLogTag(), "TarotDroid Exception in " + this.getClass().toString(), ioe);
							Toast.makeText(GameSetHistoryActivity.this, AppContext.getApplication().getResources().getString(R.string.msgBluetoothError, ioe), Toast.LENGTH_SHORT).show();
						}
					}
				}
				return true;
			}
		});

		miBlueToothHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				UIHelper.showSimpleRichTextDialog(GameSetHistoryActivity.this, AppContext.getApplication().getResources().getText(R.string.msgHelpBluetooth).toString(), AppContext.getApplication()
						.getResources().getString(R.string.titleHelpBluetooth));
				return true;
			}
		});

		// TODO Improve Massive excel export
		// if (!AppContext.getApplication().isAppLimited()) {
		// com.actionbarsherlock.view.MenuItem miGlobalExport =
		// menu.add(this.getString(R.string.lblExcelExport)).setIcon(R.drawable.ic_excel);
		// miGlobalExport.setShowAsAction(com.actionbarsherlock.view.MenuItem.SHOW_AS_ACTION_NEVER);
		// //miGlobalExport.setIcon(R.drawable.ic_excel);
		//
		// miGlobalExport.setOnMenuItemClickListener(new
		// OnMenuItemClickListener() {
		// @Override
		// public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem
		// item) {
		// ExportToExcelTask task = new
		// ExportToExcelTask(GameSetHistoryActivity.this, progressDialog);
		// task.execute();
		// return true;
		// }
		// });
		// }

		com.actionbarsherlock.view.MenuItem miBin = menu.add(this.getString(R.string.lblInitDalItem));
		miBin.setShowAsAction(com.actionbarsherlock.view.MenuItem.SHOW_AS_ACTION_NEVER);
		miBin.setIcon(R.drawable.gd_action_bar_trashcan);

		miBin.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				AlertDialog.Builder builder = new AlertDialog.Builder(GameSetHistoryActivity.this);
				builder.setTitle(GameSetHistoryActivity.this.getString(R.string.titleReinitDalYesNo));
				builder.setMessage(Html.fromHtml(GameSetHistoryActivity.this.getText(R.string.msgReinitDalYesNo).toString()));
				builder.setPositiveButton(GameSetHistoryActivity.this.getString(R.string.btnOk), GameSetHistoryActivity.this.removeAllGameSetsDialogClickListener);
				builder.setNegativeButton(GameSetHistoryActivity.this.getString(R.string.btnCancel), GameSetHistoryActivity.this.removeAllGameSetsDialogClickListener).show();
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				return true;
			}
		});

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.uiHelper.onDestroy();
		try {
			this.bluetoothHelper.cancelDiscovery();
		} catch (Exception e) {
		}
	}

	// /**
	// * Read session opened callback.
	// */
	// private Session.StatusCallback readSessionOpenedStatusCallback = new
	// Session.StatusCallback() {
	// @Override
	// public void call(Session session, SessionState state, Exception
	// exception) {
	// requestPublishPermissions(session);
	// }
	// };

	/**
	 * Called when excel export task is done.
	 * 
	 * @param filePath
	 */
	private void onGameSetExportedToExcelFile(String filePath) {
		// should never happen
		if (filePath == null) {
			UIHelper.showSimpleRichTextDialog(this, this.getString(R.string.msgDbExportFailed), this.getString(R.string.titleDbExportFailed));
		}

		else {
			this.tempExcelFilePath = filePath;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(this.getString(R.string.titleGameSetExportedToExcelFile));
			builder.setMessage(Html.fromHtml(this.getText(R.string.msgGameSetExportedToExcelFile).toString()));
			builder.setPositiveButton(this.getString(R.string.btnOk), this.exportExcelByEmailDialogClickListener);
			builder.setNegativeButton(this.getString(R.string.btnCancel), this.exportExcelByEmailDialogClickListener);
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.show();
		}
	}

	/**
	 * Manage click on GameSet.
	 * 
	 * @param pos
	 */
	private void onListItemClick(final int pos) {
		final GameSet gameSet = (GameSet) getListAdapter().getItem(pos);

		final Item[] items = AppContext.getApplication().isAppLimited() ? limitedItems : allItems;

		ListAdapter adapter = new ArrayAdapter<Item>(this, android.R.layout.select_dialog_item, android.R.id.text1, items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// User super class to create the View
				View v = super.getView(position, convertView, parent);
				TextView tv = (TextView) v.findViewById(android.R.id.text1);

				// Put the image on the TextView
				tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

				// Add margin between image and text (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				tv.setCompoundDrawablePadding(dp5);

				return v;
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(String.format(this.getString(R.string.lblGameSetHistoryActivityMenuTitle), new SimpleDateFormat("dd/MM/yy").format(gameSet.getCreationTs())));

		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int itemIndex) {

				Item item = items[itemIndex];

				if (item.itemType == Item.ItemTypes.publishOnFacebook) {

					// check for active internet connexion first
					// see post
					// http://stackoverflow.com/questions/2789612/how-can-i-check-whether-an-android-device-is-connected-to-the-web
					ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

					if (networkInfo != null && networkInfo.isConnected()) {

						if (gameSet.getGameCount() == 0) {
							Toast.makeText(GameSetHistoryActivity.this, R.string.lblFacebookImpossibleToPublishGamesetWithNoGame, Toast.LENGTH_SHORT).show();
						}

						else if (!AppContext.getApplication().getNotificationIds().isEmpty()) {
							Toast.makeText(GameSetHistoryActivity.this, R.string.lblFacebookGamesetBeingPublished, Toast.LENGTH_SHORT).show();
						}

						else {
							tempGameSet = gameSet;

							// // TODO Improve in later version
							// ShortenUrlTask shortenUrlTask = new
							// ShortenUrlTask(FacebookHelper.buildGameSetUrl(tempGameSet));
							// shortenUrlTask.setCallback(urlShortenedCallback);
							// shortenUrlTask.execute();

							startPostProcess();
						}
					} else {
						Toast.makeText(GameSetHistoryActivity.this, getString(R.string.titleInternetConnexionNecessary), Toast.LENGTH_LONG).show();
					}
				} else if (item.itemType == Item.ItemTypes.publishOnTwitter) {
					Toast.makeText(GameSetHistoryActivity.this, "TODO: Publish on twitter", Toast.LENGTH_LONG).show();

					Intent intent = new Intent(GameSetHistoryActivity.this, TwitterConnectActivity.class);
					startActivity(intent);
				} else if (item.itemType == Item.ItemTypes.remove) {
					RemoveGameSetDialogClickListener removeGameSetDialogClickListener = new RemoveGameSetDialogClickListener(gameSet);
					AlertDialog.Builder builder = new AlertDialog.Builder(GameSetHistoryActivity.this);
					builder.setTitle(GameSetHistoryActivity.this.getString(R.string.titleRemoveGameSetYesNo));
					builder.setMessage(Html.fromHtml(GameSetHistoryActivity.this.getText(R.string.msgRemoveGameSetYesNo).toString()));
					builder.setPositiveButton(GameSetHistoryActivity.this.getString(R.string.btnOk), removeGameSetDialogClickListener);
					builder.setNegativeButton(GameSetHistoryActivity.this.getString(R.string.btnCancel), removeGameSetDialogClickListener).show();
					builder.setIcon(android.R.drawable.ic_dialog_alert);
				} else if (item.itemType == Item.ItemTypes.transferOverBluetooth) {
					if (!GameSetHistoryActivity.this.bluetoothHelper.isBluetoothEnabled()) {
						Toast.makeText(GameSetHistoryActivity.this, GameSetHistoryActivity.this.getString(R.string.msgActivateBluetooth), Toast.LENGTH_LONG).show();
					}

					try {
						// make sure at least one device was discovered
						if (GameSetHistoryActivity.this.bluetoothHelper.getBluetoothDeviceCount() == 0) {
							Toast.makeText(GameSetHistoryActivity.this, GameSetHistoryActivity.this.getString(R.string.msgRunDiscoverDevicesFirst), Toast.LENGTH_LONG).show();
						}

						// display devices and download
						final String[] items = GameSetHistoryActivity.this.bluetoothHelper.getBluetoothDeviceNames();

						AlertDialog.Builder builder = new AlertDialog.Builder(GameSetHistoryActivity.this);
						builder.setTitle(GameSetHistoryActivity.this.getString(R.string.lblSelectBluetoothDevice));
						builder.setItems(items, new BluetoothDeviceClickListener(gameSet, items));
						AlertDialog alert = builder.create();
						alert.show();
					} catch (Exception e) {
						AuditHelper.auditError(ErrorTypes.gameSetHistoryActivityError, e, GameSetHistoryActivity.this);
					}
				} else if (item.itemType == Item.ItemTypes.exportToExcel) {
					try {
						if (!AppContext.getApplication().isAppLimited()) {

							ExportToExcelTask task = new ExportToExcelTask(GameSetHistoryActivity.this, progressDialog);
							task.setCallback(excelExportCallback);
							task.execute(gameSet);
						}
					} catch (Exception e) {
						Toast.makeText(GameSetHistoryActivity.this, AppContext.getApplication().getResources().getText(R.string.msgGameSetExportError).toString() + e.getMessage(), Toast.LENGTH_LONG)
								.show();
						AuditHelper.auditError(ErrorTypes.excelFileStorage, e);
					}
				} else if (item.itemType == Item.ItemTypes.edit) {
					// SharedPreferences preferences =
					// PreferenceManager.getDefaultSharedPreferences(GameSetHistoryActivity.this);

					// set selected gameset as session gameset
					// AppContext.getApplication().getBizService().setGameSet(gameSet);

					// // Get non DAL stored parameters property from shared
					// preferences
					// UIHelper.fillNonComputationPreferences(gameSet.getGameSetParameters(),
					// preferences);

					// start tab gameset activity
					Intent intent = new Intent(GameSetHistoryActivity.this, TabGameSetActivity.class);
					intent.putExtra(ActivityParams.PARAM_GAMESET_ID, gameSet.getId());
					GameSetHistoryActivity.this.startActivityForResult(intent, RequestCodes.DISPLAY_WITH_FACEBOOK);
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockListActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		this.uiHelper.onPause();
	}

	/**
	 * Method called asynchronously after publishGameSetOnFacebookWall().
	 * 
	 * @param facebookResponse
	 */
	private void onPostPublishGameSetOnFacebookWall(final Response facebookResponse) {
		PostResponse postResponse = facebookResponse.getGraphObjectAs(PostResponse.class);

		int notificationId = AppContext.getApplication().getNotificationIds().get(tempGameSet.getUuid());
		AppContext.getApplication().getNotificationIds().remove(tempGameSet.getUuid());
		if (postResponse != null && postResponse.getId() != null) {
			FacebookHelper.showNotificationStopProgressSuccess(this, FacebookHelper.buildGameSetUrl(tempGameSet), notificationId);
			try {
				tempGameSet.setFacebookPostTs(new Date());
				AppContext.getApplication().getDalService().updateGameSetAfterSync(tempGameSet);
				refresh();
			} catch (DalException e) {
				e.printStackTrace();
				AuditHelper.auditError(ErrorTypes.updateGameSetError, e, this);
			}
			// this.publishGameSetOnFacebookApp();
		} else {
			FacebookHelper.showNotificationStopProgressFailure(this, notificationId);
			this.handleFacebookError(facebookResponse.getError());
		}
	}

	// /**
	// * Published session opened callback.
	// */
	// private Session.StatusCallback publishSessionOpenedCallback = new
	// Session.StatusCallback() {
	// @Override
	// public void call(Session session, SessionState state, Exception
	// exception) {
	// launchPostProcess(session);
	// }
	// };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockListActivity#onRestoreInstanceState
	 * (android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		this.pendingReauthRequest = savedInstanceState.getBoolean(PENDING_REAUTH_KEY, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		this.uiHelper.onResume();
		if (AppContext.getApplication().getLoadDalTask().getStatus() == AsyncTask.Status.FINISHED) {
			this.refresh();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		Object toReturn = null;
		if (currentRunningTask == null) {
			toReturn = null;
		} else if (currentRunningTask instanceof UpSyncGameSetTask) {
			UpSyncGameSetTask task = (UpSyncGameSetTask) currentRunningTask;
			task.detach();
			toReturn = task;
		} else if (currentRunningTask instanceof PostGameSetLinkOnFacebookWallTask) {
			PostGameSetLinkOnFacebookWallTask task = (PostGameSetLinkOnFacebookWallTask) currentRunningTask;
			task.detach();
			toReturn = task;
		}

		return toReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(PENDING_REAUTH_KEY, this.pendingReauthRequest);
		this.uiHelper.onSaveInstanceState(outState);
	}

	// /**
	// * Post game set to Facebook Wall.
	// */
	// private void publishGameSetOnFacebookApp() {
	// PostGameSetOnFacebookAppTask postGameSetOnFacebookAppTask = new
	// PostGameSetOnFacebookAppTask(this, this.progressDialog, tempGameSet);
	// postGameSetOnFacebookAppTask.setCallback(this.postToFacebookAppCallback);
	// postGameSetOnFacebookAppTask.execute();
	// }

	// /**
	// * Method called asynchronously after publishGameSetOnFacebookApp().
	// * @param facebookResponse
	// */
	// private void onPostPublishGameSetOnFacebookApp(final Response
	// facebookResponse) {
	// PostResponse postResponse =
	// facebookResponse.getGraphObjectAs(PostResponse.class);
	//
	// if (postResponse == null || postResponse.getId() == null) {
	// }
	// }

	/**
	 * Called when session changed.
	 * 
	 * @param session
	 * @param state
	 * @param exception
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		// ask for publish permissions
		if (tempGameSet != null && session.getState() == SessionState.OPENED && !hasPublishPermission()) {
			this.requestPublishPermissions(session);
		}

		// publish
		else if (this.tempGameSet != null && session.getState() == SessionState.OPENED_TOKEN_UPDATED && hasReadPermission() && hasPublishPermission()) {
			this.launchPostProcess(session);
		}

		// some problem, restart whole publication process
		else if (this.tempGameSet != null && session.isClosed()) {
			session.closeAndClearTokenInformation();
			Toast.makeText(this, "Vous devez relancer la publication Facebook...", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Opens a read session before opening a publish session.
	 * 
	 * @param callback
	 */
	private void openFacebookSessionForRead(Session session) {
		if (session != null) {
			// session.openForRead(new
			// Session.OpenRequest(this).setPermissions(READ_PERMISSIONS).setCallback(readSessionOpenedStatusCallback));
			session.openForRead(new Session.OpenRequest(this).setPermissions(READ_PERMISSIONS));
		}
	}

	/**
	 * Publish the game set on Facebook.
	 */
	private void publishGameSetOnFacebook() {
		Session session = Session.getActiveSession();
		this.pendingReauthRequest = false;
		if (session != null) {
			if (this.hasPublishPermission()) {
				this.publishGameSetOnFacebookWall();
			} else {
				this.pendingReauthRequest = true;
				this.requestPublishPermissions(session);
			}
		}
	}

	/**
	 * Post game set to Facebook Wall.
	 */
	private void publishGameSetOnFacebookWall() {
		PostGameSetLinkOnFacebookWallTask postGameSetLinkOnFacebookWallTask = new PostGameSetLinkOnFacebookWallTask(this, this.progressDialog, tempGameSet, null);
		postGameSetLinkOnFacebookWallTask.setCallback(this.postToFacebookWallCallback);
		postGameSetLinkOnFacebookWallTask.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.nla.tarotdroid.ui.IRefreshableGameSetContainer#refresh()
	 */
	// @Override
	public void refresh() {
		List<GameSet> gameSets = AppContext.getApplication().getDalService().getAllGameSets();
		Collections.sort(gameSets, gameSetCreationDateDescendingComparator);
		this.setListAdapter(new GameSetAdapter(this, gameSets));
		this.setTitle();
	}

	/**
	 * Initiates a new request for permissions.
	 * 
	 * @param session
	 */
	private void requestPublishPermissions(Session session) {
		if (!this.hasPublishPermission()) {

			// request publish permissions if session was just opened
			if (session.getState() == SessionState.OPENED) {

				// HACK because of bug explained here
				// http://stackoverflow.com/questions/14037010/statuscallback-not-called-after-requestnewreadpermissions-then-requestnewpublish
				// Session.openActiveSessionFromCache(this);
				// END HACK

				// Session.NewPermissionsRequest newPermissionsRequest = new
				// Session.NewPermissionsRequest(this,
				// PUBLISH_PERMISSIONS).setRequestCode(REAUTH_ACTIVITY_CODE).setCallback(publishSessionOpenedCallback);
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PUBLISH_PERMISSIONS).setRequestCode(REAUTH_ACTIVITY_CODE);
				session.requestNewPublishPermissions(newPermissionsRequest);
			}

			// do nothing if session was already updated
			else if (session.getState() == SessionState.OPENED_TOKEN_UPDATED) {
			}
		}
	}

	/**
	 * Runs a task to transfer a game set.
	 */
	private void sendGameSetOverBluetooth(final GameSet gameSet, final BluetoothDevice bluetoothDevice) {
		try {
			AuditHelper.auditEvent(EventTypes.actionBluetoothSendGameSet);
			this.sendGameSetTask = new SendGameSetTask(this, this.progressDialog, gameSet, bluetoothDevice, this.bluetoothHelper.getBluetoothAdapter());
			this.sendGameSetTask.setCallback(refreshCallback);
			this.sendGameSetTask.execute();
		} catch (IOException ioe) {
			Log.v(AppContext.getApplication().getAppLogTag(), "TarotDroid Exception in " + this.getClass().toString(), ioe);
			Toast.makeText(GameSetHistoryActivity.this, this.getResources().getString(R.string.msgBluetoothError, ioe), Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Sets adhoc title.
	 */
	private void setTitle() {
		if (AppContext.getApplication().getDalService() == null || AppContext.getApplication().getDalService().getAllGameSets().size() == 0) {
			this.setTitle(this.getResources().getString(R.string.lblGameSetHistoryActivityTitleNone));
		} else if (AppContext.getApplication().getDalService().getAllGameSets().size() == 1) {
			this.setTitle(this.getResources().getString(R.string.lblGameSetHistoryActivityTitleSingle));
		} else {
			this.setTitle(this.getResources().getString(R.string.lblGameSetHistoryActivityTitlePlural, AppContext.getApplication().getDalService().getAllGameSets().size()));
		}

	}

	/**
	 * Attempts to start the publish process.
	 */
	private void startPostProcess() {
		Session session = Session.getActiveSession();
		if (session == null || session.isClosed()) {
			session = new Session(this);
			Session.setActiveSession(session);
		}

		if (!hasReadPermission()) {
			openFacebookSessionForRead(session);
			return;
		}

		if (!hasPublishPermission()) {
			requestPublishPermissions(session);
			return;
		}

		this.launchPostProcess(session);
	}
}