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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.ui.constants.ResultCodes;
import org.nla.tarotdroid.lib.ui.controls.FacebookThumbnailItem;
import org.nla.tarotdroid.lib.ui.controls.ThumbnailItem;
import org.nla.tarotdroid.lib.ui.tasks.IAsyncCallback;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.lib.ui.constants.RequestCodes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class PlayerListActivity extends SherlockListActivity {

	/**
	 * Internal adapter to backup the list data.
	 */
	private class PlayerAdapter extends ArrayAdapter<Player> {

		/**
		 * Constructs a PlayerAdapter().
		 * 
		 * @param context
		 * @param players
		 */
		public PlayerAdapter(Context context, List<Player> players) {
			super(context, R.layout.thumbnail_item, players);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Player player = this.getItem(position);

			View item = null;

			// TODO Improve using convertView

			// facebook pic was set
			if (player.getFacebookId() != null) {
				item = new FacebookThumbnailItem(this.getContext(), player.getFacebookId(), player.getName(), this.getContext().getString(R.string.lblPlayerStatsCreateOn,
						player.getCreationTs().toLocaleString()));
			}

			// contact pic was set
			else if (player.getPictureUri() != null) {
				item = new ThumbnailItem(this.getContext(), Uri.parse(player.getPictureUri()), R.drawable.icon_android, player.getName(), this.getContext().getString(R.string.lblPlayerStatsCreateOn,
						player.getCreationTs().toLocaleString()));
			}

			// no pic
			else {
				item = new ThumbnailItem(this.getContext(), R.drawable.icon_android, player.getName(), this.getContext().getString(R.string.lblPlayerStatsCreateOn,
						player.getCreationTs().toLocaleString()));
			}
			//
			//
			//
			// if (player.getFacebookId() != null) {
			// item = new FacebookThumbnailItem(
			// this.getContext(),
			// player.getFacebookId(),
			// player.getName(),
			// this.getContext().getString(R.string.lblPlayerStatsCreateOn,
			// player.getCreationTs().toLocaleString())
			// );
			//
			// }
			// else {
			// item = new ThumbnailItem(
			// this.getContext(),
			// R.drawable.icon_android,
			// player.getName(),
			// this.getContext().getString(R.string.lblPlayerStatsCreateOn,
			// player.getCreationTs().toLocaleString())
			// );
			// }

			return item;
		}
	}

	/**
	 * Compare two players upon their names.
	 */
	private final Comparator<Player> playerNameComparator = new Comparator<Player>() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final Player player1, final Player player2) {
			if (player1 == null) {
				return -1;
			}
			if (player2 == null) {
				return 1;
			}

			return player1.getName().toLowerCase().compareTo(player2.getName().toLowerCase());
		}
	};

	/**
	 * Traces creation event.
	 */
	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayPlayerListPage);
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
		if (requestCode == RequestCodes.DISPLAY_PLAYER) {
			if (resultCode == ResultCodes.PlayerPictureChanged) {
				this.refresh();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see greendroid.app.GDActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle icicle) {
		try {
			super.onCreate(icicle);
			this.auditEvent();

			// set excuse as background image
			this.getListView().setCacheColorHint(0);
			this.getListView().setBackgroundResource(R.drawable.img_excuse);

			// set action bar properties
			this.setTitle(this.getResources().getString(R.string.lblPlayerListActivityTitle));

			// wait for the dal to be initiated to refresh the player list
			if (AppContext.getApplication().getLoadDalTask().getStatus() == AsyncTask.Status.RUNNING) {
				AppContext.getApplication().getLoadDalTask().showDialogOnActivity(this, this.getResources().getString(R.string.msgGameSetsRetrieval));
				AppContext.getApplication().getLoadDalTask().setCallback(new IAsyncCallback<String>() {

					@Override
					public void execute(String isNull, Exception e) {
						// Check exception
						PlayerListActivity.this.refresh();
					}
				});
			}
			// refresh the player list
			else {
				this.refresh();
			}
		} catch (Exception e) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.playerListActivityError, e, this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * greendroid.app.GDListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, PlayerStatisticsActivity.class);
		intent.putExtra("player", ((Player) this.getListAdapter().getItem(position)).getName());
		this.startActivityForResult(intent, RequestCodes.DISPLAY_PLAYER);
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

	/**
	 * Refreshes the player list.
	 */
	private void refresh() {
		try {

			List<Player> players = AppContext.getApplication().getDalService().getAllPlayers();
			Collections.sort(players, playerNameComparator);
			this.setListAdapter(new PlayerAdapter(this, players));
		} catch (final Exception e) {
			Log.v(AppContext.getApplication().getAppLogTag(), this.getClass().toString(), e);
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			AuditHelper.auditError(AuditHelper.ErrorTypes.unexpectedError, e);

		}
	}
}