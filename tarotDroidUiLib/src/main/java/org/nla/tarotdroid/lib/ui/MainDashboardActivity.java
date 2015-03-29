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

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.controls.ThumbnailItem;
import org.nla.tarotdroid.lib.ui.tasks.IAsyncCallback;
import org.nla.tarotdroid.lib.ui.tasks.ImportDatabaseTask;
import org.nla.tarotdroid.lib.ui.tasks.InsertMockGameSetsTask;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.ui.tasks.ExportDatabaseTask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.ipaulpro.afilechooser.utils.FileUtils;

/**
 * The main dashboard.
 * 
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class MainDashboardActivity extends SherlockActivity {

	/**
	 * Internal adapter to back up the dashboard list data.
	 */
	private class DashboardOptionAdapter extends ArrayAdapter<DashboardOption> {

		/**
		 * Constructs a DashboardOptionAdapter.
		 * 
		 * @param context
		 * @param objects
		 */
		public DashboardOptionAdapter(Context context, List<DashboardOption> objects) {
			super(context, R.layout.thumbnail_item, objects);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DashboardOption option = this.getItem(position);
			return new ThumbnailItem(this.getContext(), option.getDrawableId(), option.getTitleResourceId(), option.getContentResourceId());
		}
	}

	/**
	 * Constant to use for file selection.
	 */
	private static final int FILE_SELECT_CODE = 0;

	/**
	 * Called when export db task is done.
	 */
	private final IAsyncCallback<String[]> exportDatabaseCallback = new IAsyncCallback<String[]>() {

		@Override
		public void execute(String[] databaseContent, Exception e) {
			// TODO Check exception
			onDatabaseExported(databaseContent);
		}
	};

	/**
	 * The image that takes user to facebook page.
	 */
	private ImageView imgLikeUsOnFacebook;

	/**
	 * Called when import db task is done.
	 */
	private final IAsyncCallback<String> importDatabaseCallback = new IAsyncCallback<String>() {

		@Override
		public void execute(String object, Exception e) {
			// TODO Check exception
			onDatabaseImported();
		}
	};

	/**
	 * The task to insert mock data.
	 */
	private InsertMockGameSetsTask insertMockGameSetsTask;

	/**
	 * The list of options to display.
	 */
	private ListView listOptions;

	/**
	 * Builds the menu for devices with version >= 4.2.
	 */
	private void buildMenuForNewAndroidDevices(Menu menu) {

		MenuItem miExportDB = menu.add(R.string.lblDbExportItem);
		miExportDB.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		miExportDB.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				ExportDatabaseTask exportDatabaseTask = new ExportDatabaseTask(MainDashboardActivity.this);
				exportDatabaseTask.setCallback(exportDatabaseCallback);
				exportDatabaseTask.execute();
				return true;
			}
		});

		MenuItem miImportDB = menu.add(R.string.lblDbImportItem);
		miImportDB.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		miImportDB.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				showFileChooser();
				return true;
			}
		});

		MenuItem miContact = menu.add(R.string.lblContactItem);
		miContact.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		miContact.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { MainDashboardActivity.this.getString(R.string.urlEmail) });
				intent.putExtra(Intent.EXTRA_TEXT, buildMessageBody().toString());
				startActivity(Intent.createChooser(intent, getString(R.string.lblContactUs)));
				return true;
			}
		});

		if (AppContext.getApplication().isAppInDebugMode()) {
			MenuItem miMockData = menu.add(R.string.lblMockItem);
			miMockData.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			miMockData.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					MainDashboardActivity.this.insertMockGameSetsTask = new InsertMockGameSetsTask(MainDashboardActivity.this, AppContext.getApplication().getAppParams().getDevGameSetCount(),
							AppContext.getApplication().getAppParams().getDevMaxGameCount());
					MainDashboardActivity.this.insertMockGameSetsTask.execute();
					return true;
				}
			});
		}
	}

	/**
	 * Builds a menu including a submenu for old devices.
	 */
	private void buildMenuForOldAndroidDevices(Menu menu) {
		SubMenu subMenuMore = menu.addSubMenu("+");
		MenuItem subMenuMoreItem = subMenuMore.getItem();
		subMenuMoreItem.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_light);
		subMenuMoreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		MenuItem miExportDB = subMenuMore.add(R.string.lblDbExportItem);
		miExportDB.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				ExportDatabaseTask exportDatabaseTask = new ExportDatabaseTask(MainDashboardActivity.this);
				exportDatabaseTask.setCallback(exportDatabaseCallback);
				exportDatabaseTask.execute();
				return true;
			}
		});

		MenuItem miImportDB = subMenuMore.add(R.string.lblDbImportItem);
		miImportDB.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		miImportDB.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				showFileChooser();
				return true;
			}
		});

		MenuItem miContact = subMenuMore.add(R.string.lblContactItem);
		miContact.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		miContact.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { MainDashboardActivity.this.getString(R.string.urlEmail) });
				intent.putExtra(Intent.EXTRA_TEXT, buildMessageBody().toString());
				startActivity(Intent.createChooser(intent, getString(R.string.lblContactUs)));
				return true;
			}
		});

		if (AppContext.getApplication().isAppInDebugMode()) {
			MenuItem miMockData = subMenuMore.add(R.string.lblMockItem);
			miMockData.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
			miMockData.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					MainDashboardActivity.this.insertMockGameSetsTask = new InsertMockGameSetsTask(MainDashboardActivity.this, AppContext.getApplication().getAppParams().getDevGameSetCount(),
							AppContext.getApplication().getAppParams().getDevMaxGameCount());
					MainDashboardActivity.this.insertMockGameSetsTask.execute();
					return true;
				}
			});
		}
	}

	/**
	 * Builds the email message body.
	 * 
	 * @return
	 */
	private StringBuilder buildMessageBody() {
		StringBuilder contentText = new StringBuilder();
		contentText.append("TarotDroid version: " + AppContext.getApplication().getAppPackage() + "[" + AppContext.getApplication().getAppVersion() + "]");
		contentText.append("\n");
		contentText.append("Android version: " + android.os.Build.VERSION.SDK_INT);
		contentText.append("\n");
		contentText.append("Device: " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + "[" + android.os.Build.DEVICE + "]");
		contentText.append("\n");
		return contentText;
	}

	/**
	 * Initializes the views.
	 */
	private void initializeViews(final Bundle savedInstanceState) {
		DashboardOption newGameOption = new DashboardOption(R.drawable.icon_3cards_released, R.string.lblNewGameSet, R.string.lblNewGameSetDetails, R.id.new_gameset_item);
		DashboardOption historyOption = new DashboardOption(R.drawable.icon_folder_released, R.string.lblGameSetHistory, R.string.lblGameSetHistoryDetails, R.id.history_item);
		DashboardOption playerStatisticsOption = new DashboardOption(R.drawable.icon_community, R.string.lblPlayers, R.string.lblPlayersDetails, R.id.player_statistics_item);
		DashboardOption marketOption = new DashboardOption(R.drawable.icon_market, R.string.lblGooglePlay, R.string.lblGooglePlayDetails, R.id.google_play_item);
		DashboardOption goFullOption = new DashboardOption(R.drawable.icon, R.string.lblFullVersion, R.string.lblFullVersionDetails, R.id.full_version_item);
		DashboardOption myAccountOption = new DashboardOption(R.drawable.icon_community, R.string.lblMyAccount, R.string.lblMyAccountDetails, R.id.my_account_item);

		List<DashboardOption> options = newArrayList();
		options.add(newGameOption);
		options.add(historyOption);
		options.add(playerStatisticsOption);
		options.add(myAccountOption);
		options.add(marketOption);

		if (AppContext.getApplication().isAppLimited()) {
			options.add(goFullOption);
		}

		this.listOptions.setAdapter(new DashboardOptionAdapter(this, options));
		this.listOptions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				onListItemClick(position);
			}
		});
		// this.setListAdapter(new DashboardOptionAdapter(this, options));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();

				String path = null;
				try {
					path = FileUtils.getPath(this, uri);
				} catch (URISyntaxException e) {
					path = null;
				}

				if (path != null) {
					onImportFileSelected(path);
				} else {

				}
			}
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (this.insertMockGameSetsTask != null && this.insertMockGameSetsTask.getStatus() == Status.RUNNING) {
			this.insertMockGameSetsTask.cancel(true);
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
			this.setContentView(R.layout.main_dashboard);

			this.listOptions = (ListView) this.findViewById(R.id.listOptions);
			this.imgLikeUsOnFacebook = (ImageView) this.findViewById(R.id.imgLikeUsOnFacebook);

			this.imgLikeUsOnFacebook.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(MainDashboardActivity.this.getString(R.string.urlFaceBook)));
					MainDashboardActivity.this.startActivity(intent);
				}
			});

			// setup all the views
			this.initializeViews(savedInstanceState);

			UIHelper.trackAppLaunched(this);
		} catch (Exception e) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.mainDashBoardActivityError, e, this);
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
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem miPrefs = menu.add(R.string.lblPrefsItem).setIcon(R.drawable.perm_group_system_tools);
		miPrefs.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		miPrefs.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(MainDashboardActivity.this, MainPreferencesActivity.class);
				MainDashboardActivity.this.startActivity(intent);
				return true;
			}
		});

		MenuItem miAbout = menu.add(R.string.lblAboutItem).setIcon(R.drawable.gd_action_bar_info);
		miAbout.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		miAbout.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int idMsgAbout = AppContext.getApplication().isAppLimited() ? R.string.msgAboutLimited : R.string.msgAbout;
				UIHelper.showSimpleRichTextDialog(MainDashboardActivity.this, MainDashboardActivity.this.getText(idMsgAbout).toString(),
						MainDashboardActivity.this.getString(R.string.titleAbout, AppContext.getApplication().getAppVersion()));
				return true;
			}
		});

		if (android.os.Build.VERSION.SDK_INT > 15) {
			this.buildMenuForNewAndroidDevices(menu);
		} else {
			this.buildMenuForOldAndroidDevices(menu);
		}

		return true;
	}

	private void onDatabaseExported(String[] databaseContent) {

		// should never happen
		if (databaseContent == null || databaseContent[0] == null) {
			UIHelper.showSimpleRichTextDialog(this, this.getString(R.string.msgDbExportFailed), this.getString(R.string.titleDbExportFailed));
		}

		else {
			try {
				StringBuilder contentText = buildMessageBody();
				// if (AppContext.getApplication().isAppInDebugMode()) {
				// contentText.append("Facebook hash key: [" +
				// UIHelper.getFacebookHashKey(this) + "]");
				// contentText.append("\n");
				// }
				contentText.append("Export (" + databaseContent[0].length() + " characters) \n" + databaseContent[0]);
				contentText.append("\n");

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { this.getString(R.string.urlEmail) });
				intent.putExtra(Intent.EXTRA_SUBJECT, "Export log");
				intent.putExtra(Intent.EXTRA_TEXT, contentText.toString());

				if (databaseContent[1] != null) {
					Uri uri = Uri.fromFile(new File(databaseContent[1]));
					intent.putExtra(Intent.EXTRA_STREAM, uri);
				}

				this.startActivity(Intent.createChooser(intent, "Envoyer l'export..."));
			} catch (Exception e) {
				AuditHelper.auditError(AuditHelper.ErrorTypes.exportDatabaseError, e, this);
			}
		}
	}

	/**
	 * Called when import is finished.
	 */
	private void onDatabaseImported() {
		Toast.makeText(this, this.getString(R.string.lblDbHelperImportDone), Toast.LENGTH_SHORT).show();
	}

	/**
	 * Called when import file is selected.
	 * 
	 * @param filePath
	 */
	private void onImportFileSelected(String filePath) {
		ImportDatabaseTask importDatabaseTask = new ImportDatabaseTask(MainDashboardActivity.this, filePath);
		importDatabaseTask.setCallback(importDatabaseCallback);
		importDatabaseTask.execute();
	}

	/**
	 * Called when user clicks on an option.
	 * 
	 * @param position
	 */
	protected void onListItemClick(int position) {
		DashboardOption option = (DashboardOption) this.listOptions.getAdapter().getItem(position);
		Intent intent;
		int tagValue = ((Integer) option.getTag()).intValue();

		if (tagValue == R.id.new_gameset_item) {
			intent = new Intent(this, NewGameSetDashboardActivity.class);
			this.startActivity(intent);
		} else if (tagValue == R.id.history_item) {
			intent = new Intent(this, GameSetHistoryActivity.class);
			this.startActivity(intent);
		} else if (tagValue == R.id.player_statistics_item) {
			intent = new Intent(this, PlayerListActivity.class);
			this.startActivity(intent);
		} else if (tagValue == R.id.google_play_item) {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(AppContext.getApplication().getGooglePlayUrl()));
			this.startActivity(intent);
		} else if (tagValue == R.id.gmail_item) {
			intent = new Intent(Intent.ACTION_SEND);
			intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
			intent.setType("plain/text");
			intent.putExtra(Intent.EXTRA_EMAIL, new String[] { this.getString(R.string.urlEmail) });
			// intent.setData(Uri.parse(this.getString(R.string.urlEmail)));
			this.startActivity(Intent.createChooser(intent, null));
		} else if (tagValue == R.id.full_version_item) {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(this.getString(R.string.urlGooglePlayFullApp)));
			this.startActivity(intent);
		} else if (tagValue == R.id.my_account_item) {
			intent = new Intent(this, SigninActivity.class);
			this.startActivity(intent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		AuditHelper.auditSession(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		AuditHelper.stopSession(this);
	}

	/**
	 * Shows the file chooser.
	 */
	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(Intent.createChooser(intent, this.getString(R.string.lblDbHelperPickXMLFile)), FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			UIHelper.showSimpleRichTextDialog(this, this.getString(R.string.msgDbHelperYouNeedAFileExplorer), this.getString(R.string.titleDbHelperYouNeedAFileExplorer));
		}
	}
}