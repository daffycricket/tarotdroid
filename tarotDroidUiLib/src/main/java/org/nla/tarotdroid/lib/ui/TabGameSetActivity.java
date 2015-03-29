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
import static com.google.common.collect.Maps.newHashMap;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.lib.ui.constants.ActivityParams;
import org.nla.tarotdroid.lib.ui.tasks.IAsyncCallback;
import org.nla.tarotdroid.lib.ui.tasks.PostGameSetLinkOnFacebookWallTask;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.app.AppParams;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.dal.DalException;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ParameterTypes;
import org.nla.tarotdroid.lib.helpers.FacebookHelper;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.GameCreationActivity.GameType;
import org.nla.tarotdroid.lib.ui.constants.RequestCodes;
import org.nla.tarotdroid.lib.ui.constants.ResultCodes;
import org.nla.tarotdroid.lib.ui.tasks.UpSyncGameSetTask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

/**
 * The main game set activity.
 * 
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class TabGameSetActivity extends SherlockFragmentActivity {

	/**
	 * Facebook post response.
	 */
	private interface PostResponse extends GraphObject {
		String getId();
	}

	/**
	 * An adapter backing up the game set pager internal fragments.
	 * 
	 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
	 */
	protected class TabGameSetPagerAdapter extends FragmentPagerAdapter {

		/**
		 * The list of fragments to display in the pager.
		 */
		private final List<Fragment> fragments;

		/**
		 * @param fm
		 * @param fragments
		 */
		public TabGameSetPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return this.fragments.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return AppContext.getApplication().getResources().getString(R.string.lblGamesTitle);
			case 1:
				return AppContext.getApplication().getResources().getString(R.string.lblSynthesisTitle);
			default:
				return "Unknown[" + position + "]";
			}
		}
	}

	/**
	 * The singleton instance.
	 */
	private static TabGameSetActivity instance;

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
	 * Flag to indicate a publish ation was required.
	 */
	// private boolean requestedPublishPermissions;

	/**
	 * Returns the singleton instance.
	 * 
	 * @return the singleton instance.
	 */
	public static TabGameSetActivity getInstance() {
		return instance;
	}

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
	 * The current game set.
	 */
	protected GameSet gameSet;

	/**
	 * The GameSetGames fragment, to be displayed in the pager.
	 */
	private GameSetGamesFragment gameSetGamesFragment;

	/**
	 * The GameSetSynthesis fragment, to be displayed in the pager.
	 */
	private GameSetSynthesisFragment gameSetSynthesisFragment;

	/**
	 * "Yes / No" leaving dialog box listener.
	 */
	private final DialogInterface.OnClickListener leavingDialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				TabGameSetActivity.this.finish();
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				break;
			default:
				break;
			}
		}
	};

	/**
	 * The pager.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter.
	 */
	private PagerAdapter mPagerAdapter;

	/**
	 * Callback used after post on facebook wall.
	 */
	private final IAsyncCallback<Response> postToFacebookWallCallback = new IAsyncCallback<Response>() {

		@Override
		public void execute(Response facebookResponse, Exception e) {
			// TODO Check exception
			TabGameSetActivity.this.onPostPublishGameSetOnFacebookWall(facebookResponse);
		}
	};

	/**
	 * The progress dialog.
	 */
	protected ProgressDialog progressDialog;

	/**
	 * "Yes / No" publish on facebook dialog box listener.
	 */
	private final DialogInterface.OnClickListener publishOnFacebookDialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: {
				if (TabGameSetActivity.this.gameSet.getGameCount() == 0) {
					Toast.makeText(TabGameSetActivity.this, TabGameSetActivity.this.getString(R.string.lblFacebookImpossibleToPublishGamesetWithNoGame), Toast.LENGTH_SHORT).show();
				}

				else if (!AppContext.getApplication().getNotificationIds().isEmpty()) {
					Toast.makeText(TabGameSetActivity.this, TabGameSetActivity.this.getString(R.string.lblFacebookGamesetBeingPublished), Toast.LENGTH_SHORT).show();
				}

				Session session = Session.getActiveSession();
				if (session == null || session.isClosed()) {
					session = new Session(TabGameSetActivity.this);
					Session.setActiveSession(session);
				}

				startPostProcess();
				break;
			}
			case DialogInterface.BUTTON_NEGATIVE:
			default:
				break;
			}
		}
	};

	/**
	 * "Yes / No" publish on facebook dialog box listener.
	 */
	private final DialogInterface.OnClickListener publishOnFacebookDialogOrLeaveClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(final DialogInterface dialog, final int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE: {
				if (TabGameSetActivity.this.gameSet.getGameCount() == 0) {
					Toast.makeText(TabGameSetActivity.this, TabGameSetActivity.this.getString(R.string.lblFacebookImpossibleToPublishGamesetWithNoGame), Toast.LENGTH_SHORT).show();
				}

				else if (!AppContext.getApplication().getNotificationIds().isEmpty()) {
					Toast.makeText(TabGameSetActivity.this, TabGameSetActivity.this.getString(R.string.lblFacebookGamesetBeingPublished), Toast.LENGTH_SHORT).show();
				}

				else {
					startPostProcess();
				}
				break;
			}
			case DialogInterface.BUTTON_NEGATIVE:
			default:
				TabGameSetActivity.this.finish();
				break;
			}
		}
	};

	// /**
	// * Callback used after post on facebook app.
	// */
	// private IAsyncCallback<Response> postToFacebookAppCallback = new
	// IAsyncCallback<Response>() {
	//
	// @Override
	// public void execute(final Response facebookResponse) {
	// TabGameSetActivity.this.onPostPublishGameSetOnFacebookApp(facebookResponse);
	// }
	// };

	/**
	 * The shortened url for the game set on the cloud.
	 */
	private String shortenedUrl;

	/**
	 * The starting page index.
	 */
	private int startPage;

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
			TabGameSetActivity.this.publishGameSetOnFacebook();
		}
	};

	/**
	 * Traces creation event.
	 */
	private void auditEvent() {
		if (this.gameSet == null) {
			AuditHelper.auditEvent(AuditHelper.EventTypes.tabGameSetActivity_auditEvent_GameSetIsNull);

			UIHelper.showSimpleRichTextDialog(this, this.getString(R.string.msgUnmanagedErrorGameSetLost), this.getString(R.string.titleUnmanagedErrorGameSetLost));
			this.finish();

		} else if (this.gameSet.getGameCount() == 0) {
			Map<ParameterTypes, Object> parameters = newHashMap();
			parameters.put(ParameterTypes.gameStyleType, this.gameSet.getGameStyleType());
			parameters.put(ParameterTypes.playerCount, this.gameSet.getPlayers().size());
			AuditHelper.auditEvent(AuditHelper.EventTypes.displayTabGameSetPageWithNewGameSetAction, parameters);
		} else {
			AuditHelper.auditEvent(AuditHelper.EventTypes.displayTabGameSetPageWithExistingGameSetAction);
		}
	}

	/**
	 * Builds the menu for devices with version >= 4.2.
	 */
	private void buildMenuForNewAndroidDevices(Menu menu) {
		MenuItem miSettings = menu.add(this.getString(R.string.lblPrefsItem));
		miSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		miSettings.setIcon(R.drawable.perm_group_system_tools);
		miSettings.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				Intent intent = new Intent(TabGameSetActivity.this, TabGameSetPreferencesActivity.class);
				TabGameSetActivity.this.startActivity(intent);
				return true;
			}
		});

		MenuItem miFacebook = menu.add(this.getString(R.string.lblPublishGameSet));
		miFacebook.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		miFacebook.setIcon(R.drawable.ic_facebook);
		miFacebook.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {

				if (gameSet.getGameCount() == 0) {
					Toast.makeText(TabGameSetActivity.this, TabGameSetActivity.this.getString(R.string.lblFacebookImpossibleToPublishGamesetWithNoGame), Toast.LENGTH_SHORT).show();
				} else {
					TabGameSetActivity.this.showPublishOnFacebookDialog(false);
				}
				return true;
			}
		});

		MenuItem miHelp = menu.add(this.getString(R.string.lblHelpItem));
		miHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		miHelp.setIcon(R.drawable.gd_action_bar_help);
		miHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				UIHelper.showSimpleRichTextDialog(TabGameSetActivity.this, TabGameSetActivity.this.getText(R.string.msgHelp).toString(), TabGameSetActivity.this.getString(R.string.titleHelp));
				return true;
			}
		});
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

	/**
	 * Builds a menu including a submenu for old devices.
	 */
	private void buildMenuForOldAndroidDevices(Menu menu) {
		SubMenu subMenuMore = menu.addSubMenu("+");
		MenuItem subMenuMoreItem = subMenuMore.getItem();
		subMenuMoreItem.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_light);
		subMenuMoreItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		MenuItem miSettings = subMenuMore.add(this.getString(R.string.lblPrefsItem));
		miSettings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		miSettings.setIcon(R.drawable.perm_group_system_tools);
		miSettings.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				Intent intent = new Intent(TabGameSetActivity.this, TabGameSetPreferencesActivity.class);
				TabGameSetActivity.this.startActivity(intent);
				return true;
			}
		});

		MenuItem miFacebook = subMenuMore.add(this.getString(R.string.lblPublishGameSet));
		miFacebook.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		miFacebook.setIcon(R.drawable.ic_facebook);
		miFacebook.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {

				if (gameSet.getGameCount() == 0) {
					Toast.makeText(TabGameSetActivity.this, TabGameSetActivity.this.getString(R.string.lblFacebookImpossibleToPublishGamesetWithNoGame), Toast.LENGTH_SHORT).show();
				} else {
					TabGameSetActivity.this.showPublishOnFacebookDialog(false);
				}
				return true;
			}
		});

		MenuItem miHelp = subMenuMore.add(this.getString(R.string.lblHelpItem));
		miHelp.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		miHelp.setIcon(R.drawable.gd_action_bar_help);
		miHelp.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				UIHelper.showSimpleRichTextDialog(TabGameSetActivity.this, TabGameSetActivity.this.getText(R.string.msgHelp).toString(), TabGameSetActivity.this.getString(R.string.titleHelp));
				return true;
			}
		});
	}

	/**
	 * Returns the current game set.
	 * 
	 * @return
	 */
	public GameSet getGameSet() {
		return this.gameSet;
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

	// /**
	// * Post game set to Facebook Wall.
	// */
	// private void publishGameSetOnFacebookApp() {
	// PostGameSetOnFacebookAppTask postGameSetOnFacebookAppTask = new
	// PostGameSetOnFacebookAppTask(this, this.progressDialog,
	// AppContext.getApplication().getBizService().getGameSet());
	// postGameSetOnFacebookAppTask.setCallback(this.postToFacebookAppCallback);
	// postGameSetOnFacebookAppTask.execute();
	// }
	//
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
	// // post error event
	// }
	// }

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
	 * Initialize the fragments to be paged
	 */
	private void initialisePaging() {
		this.gameSetGamesFragment = GameSetGamesFragment.newInstance(/*
																	 * this.gameSet
																	 */);
		this.gameSetSynthesisFragment = GameSetSynthesisFragment.newInstance(/*
																			 * this.
																			 * gameSet
																			 */);

		List<Fragment> fragments = newArrayList();
		fragments.add(this.gameSetGamesFragment);
		fragments.add(this.gameSetSynthesisFragment);

		this.mPagerAdapter = new TabGameSetPagerAdapter(super.getSupportFragmentManager(), fragments);
		this.mPager = (ViewPager) super.findViewById(R.id.pager);
		this.mPager.setAdapter(this.mPagerAdapter);
		this.mPager.setCurrentItem(this.startPage - 1);

		TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
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
		text.setText(this.getString(R.string.msgFacebookNotificationInProgress));

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
						int notificationId = FacebookHelper.showNotificationStartProgress(TabGameSetActivity.this);
						AppContext.getApplication().getNotificationIds().put(TabGameSetActivity.this.gameSet.getUuid(), notificationId);

						AppContext.getApplication().setLoggedFacebookUser(user);
						UpSyncGameSetTask task = new UpSyncGameSetTask(TabGameSetActivity.this, progressDialog);
						task.setCallback(TabGameSetActivity.this.upSyncCallback);
						task.execute(TabGameSetActivity.this.gameSet);
					}
				}
				if (response.getError() != null) {
					AuditHelper.auditErrorAsString(ErrorTypes.facebookNewMeRequestFailed, response.getError().toString(), TabGameSetActivity.this);
				}
			}
		});
		request.executeAsync();
	}

	/**
	 * Navigates towards the correct belgian creation activity depending on the
	 * current gameset type.
	 */
	private void navigateTowardsBelgianGameCreationActivity() {
		this.navigateTowardsGameCreationActivity(GameCreationActivity.GameType.Belgian);
	}

	/**
	 * Navigates towards the correct creation activity.
	 */
	private void navigateTowardsGameCreationActivity(GameType gametype) {
		Intent intent = new Intent(TabGameSetActivity.this, GameCreationActivity.class);
		intent.putExtra(ActivityParams.PARAM_TYPE_OF_GAME, gametype.toString());
		// if (!this.gameSet.isPersisted()) {
		// //intent.putExtra(ActivityParams.PARAM_GAMESET_SERIALIZED,
		// UIHelper.serializeGameSet(this.gameSet));
		// intent.putExtra(ActivityParams.PARAM_GAMESET_SERIALIZED,
		// TabGameSetActivity.this.gameSet);
		// }
		// else {
		// intent.putExtra(ActivityParams.PARAM_GAMESET_ID,
		// this.gameSet.getId());
		// }
		this.startActivityForResult(intent, RequestCodes.ADD_GAME);
	}

	/**
	 * Navigates towards the creation activity depending on the current gameset
	 * type.
	 */
	private void navigateTowardsPassedGameCreationActivity() {
		this.navigateTowardsGameCreationActivity(GameCreationActivity.GameType.Pass);
	}

	/**
	 * Navigates towards the creation activity depending on the current gameset
	 * type.
	 */
	private void navigateTowardsPenaltyGameCreationActivity() {
		this.navigateTowardsGameCreationActivity(GameCreationActivity.GameType.Penalty);
	}

	/**
	 * Navigates towards the correct standard creation activity depending on the
	 * current gameset type.
	 */
	private void navigateTowardsStandardGameCreationActivity() {
		this.navigateTowardsGameCreationActivity(GameCreationActivity.GameType.Standard);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.uiHelper.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

		// // very ugly code... should find a way to refresh properly the uris
		// of players, or at least on't put a loop in here...
		// if (requestCode == RequestCodes.DISPLAY_PLAYER && resultCode ==
		// ResultCodes.PlayerPictureChanged) {
		// String playerPictureUri =
		// data.getExtras().getString(ResultCodes.PlayerPictureUriCode);
		// String playerName =
		// data.getExtras().getString(ResultCodes.PlayerNameCode);
		//
		// for (Player player :
		// AppContext.getApplication().getBizService().getGameSet().getPlayers())
		// {
		// if (player.getName().equals(playerName)) {
		// player.setPictureUri(playerPictureUri);
		// }
		// }
		// }

		if (requestCode == RequestCodes.ADD_GAME && resultCode == ResultCodes.AddGame_Ok) {
			UIHelper.showModifyOrDeleteGameMessage(this);

			// // if necessary, refresh gameset instance and propagate it
			// towards fragments
			// if (data.hasExtra(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
			// this.gameSet =
			// (GameSet)data.getSerializableExtra(ActivityParams.PARAM_GAMESET_SERIALIZED);
			// // this.gameSetGamesFragment.setGameSet(this.gameSet);
			// // this.gameSetSynthesisFragment.setGameSet(this.gameSet);
			// }
			// String gameSetSerialized =
			// data.getStringExtra(ActivityParams.PARAM_GAMESET_SERIALIZED);
			// this.gameSet = UIHelper.deserializeGameSet(gameSetSerialized);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String dialogTitle = this.gameSet.isPersisted() ? this.getString(R.string.titleExitGameSetYesNoWithDAL) : this.getString(R.string.titleExitGameSetYesNo);
		String dialogMessage = this.gameSet.isPersisted() ? this.getText(R.string.msgExitGameSetYesNoWithDAL).toString() : this.getText(R.string.msgExitGameSetYesNo).toString();

		builder.setTitle(dialogTitle);
		builder.setMessage(Html.fromHtml(dialogMessage));
		builder.setPositiveButton(this.getString(R.string.btnOk), this.leavingDialogClickListener);
		builder.setNegativeButton(this.getString(R.string.btnCancel), this.leavingDialogClickListener).show();
		builder.setIcon(android.R.drawable.ic_dialog_alert);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.setContentView(R.layout.simple_titles);

			// check params
			Bundle args = this.getIntent().getExtras();
			if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
				this.gameSet = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
			} else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
				// this.gameSet =
				// UIHelper.deserializeGameSet(args.getString(ActivityParams.PARAM_GAMESET_SERIALIZED));
				this.gameSet = (GameSet) args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
			} else {
				throw new IllegalArgumentException("Game set id or serialized game set must be provided");
			}

			// instantiate fragments
			this.gameSetGamesFragment = GameSetGamesFragment.newInstance(/*
																		 * this.
																		 * gameSet
																		 */);
			this.gameSetSynthesisFragment = GameSetSynthesisFragment.newInstance(/*
																				 * this
																				 * .
																				 * gameSet
																				 */);

			// facebook lifecycle objects
			this.uiHelper = new UiLifecycleHelper(this, this.facebookSessionStatusCallback);
			this.uiHelper.onCreate(savedInstanceState);

			this.auditEvent();
			instance = this;

			// set keep screen on
			UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());

			// initialize the pager
			this.initialisePaging();

			ActionBar mActionBar = getSupportActionBar();
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(true);
			this.progressDialog = new ProgressDialog(this);
			this.progressDialog.setCancelable(false);

			UIHelper.showAssociateFacebookPictureToUserMessage(this);
		} catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.tabGameSetActivityError, e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu
	 * (android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		AppParams appParams = AppContext.getApplication().getAppParams();

		if (appParams.isBelgianGamesAllowed() || appParams.isPenaltyGamesAllowed() || appParams.isPassedGamesAllowed()) {
			// prepare menu
			SubMenu subMenuAdd = menu.addSubMenu(R.string.lblAddGameItem);
			MenuItem subMenuAddItem = subMenuAdd.getItem();
			subMenuAddItem.setIcon(R.drawable.gd_action_bar_add);
			subMenuAddItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

			// Add standard game, always present
			MenuItem miAddStdGame = subMenuAdd.add(R.string.lblAddGameStandardItem);
			miAddStdGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
					TabGameSetActivity.this.navigateTowardsStandardGameCreationActivity();
					return true;
				}
			});

			// Add belgian games, optional
			if (appParams.isBelgianGamesAllowed()) {
				MenuItem miAddBelgianGame = subMenuAdd.add(R.string.lblAddGameBelgianItem);
				miAddBelgianGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
						TabGameSetActivity.this.navigateTowardsBelgianGameCreationActivity();
						return true;
					}
				});
			}

			// Add pass, optional
			if (appParams.isPassedGamesAllowed()) {
				MenuItem miAddPassedGame = subMenuAdd.add(R.string.lblAddGamePassedItem);
				miAddPassedGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
						TabGameSetActivity.this.navigateTowardsPassedGameCreationActivity();
						return true;
					}
				});
			}

			// Add penalty, optional
			if (appParams.isPenaltyGamesAllowed()) {
				MenuItem miAddPenaltyGame = subMenuAdd.add(R.string.lblAddGamePenaltyItem);
				miAddPenaltyGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
						TabGameSetActivity.this.navigateTowardsPenaltyGameCreationActivity();
						return true;
					}
				});
			}
		} else {
			MenuItem miAddGame = menu.add(this.getString(R.string.lblAddGameItem));
			miAddGame.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			miAddGame.setIcon(R.drawable.gd_action_bar_add);
			miAddGame.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
					TabGameSetActivity.this.navigateTowardsStandardGameCreationActivity();
					return true;
				}
			});
		}

		MenuItem miStats = menu.add(this.getString(R.string.lblDisplayStatsItem));
		miStats.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		miStats.setIcon(R.drawable.gd_action_bar_pie_chart);
		miStats.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(com.actionbarsherlock.view.MenuItem item) {
				Intent intent = null;

				// running android version >= ICS, show new ui
				if (android.os.Build.VERSION.SDK_INT >= 14) {
					intent = new Intent(TabGameSetActivity.this, GameSetChartViewPagerActivity.class);
				}
				// prevent problem of incorrect pie charts for versions < ICS =>
				// use former activity
				else {
					intent = new Intent(TabGameSetActivity.this, GameSetChartListActivity.class);
				}
				TabGameSetActivity.this.startActivity(intent);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.uiHelper.onDestroy();
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

		int notificationId = AppContext.getApplication().getNotificationIds().get(this.gameSet.getUuid());
		AppContext.getApplication().getNotificationIds().remove(this.gameSet.getUuid());
		if (postResponse != null && postResponse.getId() != null) {
			FacebookHelper.showNotificationStopProgressSuccess(this, FacebookHelper.buildGameSetUrl(this.gameSet), notificationId);
			try {
				this.gameSet.setFacebookPostTs(new Date());
				this.gameSet.setSyncTimestamp(new Date());
				AppContext.getApplication().getDalService().updateGameSetAfterSync(this.gameSet);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockFragmentActivity#onRestoreInstanceState
	 * (android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
			this.gameSet = (GameSet) savedInstanceState.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		try {
			super.onResume();
			this.uiHelper.onResume();
			this.invalidateOptionsMenu();
		} catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.tabGameSetActivityOnResumeError, e);
			this.finish();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		this.uiHelper.onSaveInstanceState(outState);
		outState.putSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED, this.gameSet);
	}

	/**
	 * Called when session changed.
	 * 
	 * @param session
	 * @param state
	 * @param exception
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		// ask for publish permissions
		if (session.getState() == SessionState.OPENED && !hasPublishPermission()) {
			this.requestPublishPermissions(session);
		}

		// publish
		else if (session.getState() == SessionState.OPENED_TOKEN_UPDATED && hasReadPermission() && hasPublishPermission()) {
			this.launchPostProcess(session);
		}

		// some problem, restart whole publication process
		else if (session.isClosed()) {
			session.closeAndClearTokenInformation();
			Toast.makeText(this, this.getString(R.string.titleRestartFacebookPublication), Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStart() {
		try {
			super.onStart();
			AuditHelper.auditSession(this);
		} catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.tabGameSetActivityOnStartError, e);
			this.finish();
		}
	}

	/**
	 * Opens a read session before opening a publish session.
	 * 
	 * @param callback
	 */
	private void openFacebookSessionForRead(Session session) {
		if (session != null && !session.isOpened()) {
			session.openForRead(new Session.OpenRequest(this).setPermissions(READ_PERMISSIONS));
		}
	}

	/**
	 * Publish the game set on Facebook.
	 */
	private void publishGameSetOnFacebook() {
		Session session = Session.getActiveSession();
		if (session != null) {
			if (this.hasPublishPermission()) {
				this.publishGameSetOnFacebookWall();
			} else {
				AuditHelper.auditErrorAsString(ErrorTypes.facebookHasNoPublishPermission, "La permission de publication n'est pas présente sur la session", this);
			}
		}
	}

	/**
	 * Post game set to Facebook Wall.
	 */
	private void publishGameSetOnFacebookWall() {
		PostGameSetLinkOnFacebookWallTask postGameSetLinkOnFacebookWallTask = new PostGameSetLinkOnFacebookWallTask(this, this.progressDialog, this.gameSet, this.shortenedUrl);
		postGameSetLinkOnFacebookWallTask.setCallback(this.postToFacebookWallCallback);
		postGameSetLinkOnFacebookWallTask.execute();
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
	 * Show publish feed dialog.
	 */
	private void showPublishOnFacebookDialog(boolean isLeavingDialog) {
		// check for active internet connexion first
		// see post
		// http://stackoverflow.com/questions/2789612/how-can-i-check-whether-an-android-device-is-connected-to-the-web
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(this.getString(R.string.titleFacebookPublish));
			builder.setMessage(Html.fromHtml(this.getString(R.string.msgFacebookPublish)));
			if (isLeavingDialog) {
				builder.setPositiveButton(this.getString(R.string.btnOk), this.publishOnFacebookDialogOrLeaveClickListener);
				builder.setNegativeButton(this.getString(R.string.btnCancel), this.publishOnFacebookDialogOrLeaveClickListener).show();
			} else {
				builder.setPositiveButton(this.getString(R.string.btnOk), this.publishOnFacebookDialogClickListener);
				builder.setNegativeButton(this.getString(R.string.btnCancel), this.publishOnFacebookDialogClickListener).show();
			}
			builder.setIcon(android.R.drawable.ic_dialog_alert);
		} else {
			Toast.makeText(this, this.getString(R.string.titleInternetConnexionNecessary), Toast.LENGTH_LONG).show();
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
			this.openFacebookSessionForRead(session);
			return;
		}

		if (!hasPublishPermission()) {
			this.requestPublishPermissions(session);
			return;
		}

		launchPostProcess(session);
	}
}