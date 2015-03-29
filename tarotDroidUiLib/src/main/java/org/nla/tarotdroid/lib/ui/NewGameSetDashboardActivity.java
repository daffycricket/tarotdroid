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

import java.util.List;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.ui.constants.ActivityParams;
import org.nla.tarotdroid.lib.ui.controls.ThumbnailItem;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.enums.GameStyleType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;

/**
 * Main dashboard. 
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class NewGameSetDashboardActivity extends SherlockListActivity {
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
            this.auditEvent();
			
            // set excuse as background image
			this.getListView().setCacheColorHint(0);
			this.getListView().setBackgroundResource(R.drawable.img_excuse);
            
			// set action bar properties
			this.setTitle(AppContext.getApplication().getResources().getString(R.string.lblMainActivityTitle, AppContext.getApplication().getAppVersion()));

			// initializes the views
			this.initializeViews();
		} 
        catch (Exception e) {
        	AuditHelper.auditError(AuditHelper.ErrorTypes.newGameSetDashboardActivityError, e, this);
        }
    }
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		AuditHelper.auditSession(this);
	}
	
	/**
	 *	Traces creation event. 
	 */
	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayNewGameSetDashboardPage);
	}
	
	/**
	 * Initializes the views.
	 */
	private void initializeViews() {
		DashboardOption newTarot3Option = new DashboardOption(
				R.drawable.icon_3players, 
				R.string.lblNewTarot3, 
				R.string.lblNewTarot3Description, 
				R.id.new_tarot3_gameset_item
		);
		DashboardOption newTarot4Option = new DashboardOption(
				R.drawable.icon_4players, 
				R.string.lblNewTarot4, 
				R.string.lblNewTarot4Description, 
				R.id.new_tarot4_gameset_item
		);
		DashboardOption newTarot5Option = new DashboardOption(
				R.drawable.icon_5players, 
				R.string.lblNewTarot5, 
				R.string.lblNewTarot5Description, 
				R.id.new_tarot5_gameset_item
		);
		
		List<DashboardOption> options = newArrayList();
		options.add(newTarot3Option);
		options.add(newTarot4Option);
		options.add(newTarot5Option);
		this.setListAdapter(new DashboardOptionAdapter(this, options));
	}
	
	/**
	 * Internal adapter to back up the list data. 
	 */
	private class DashboardOptionAdapter extends ArrayAdapter<DashboardOption> {

		/**
		 * Constructs a DashboardOptionAdapter.
		 * @param context
		 * @param objects
		 */
		public DashboardOptionAdapter(Context context, List<DashboardOption> objects) {
			super(context, R.layout.thumbnail_item, objects);	
		}
		
		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
			DashboardOption option = this.getItem(position);
			ThumbnailItem thumbnailItem = new ThumbnailItem(this.getContext(), option.getDrawableId(), option.getTitleResourceId(), option.getContentResourceId());
	        return thumbnailItem;
	    }
	}
	
	/* (non-Javadoc)
	 * @see greendroid.app.GDListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {		
        // handle click selection
		DashboardOption option = (DashboardOption)this.getListAdapter().getItem(position);
		GameStyleType gameStyleType = null;
		int tagValue = ((Integer)option.getTag()).intValue();
		if (tagValue == R.id.new_tarot3_gameset_item) {
			gameStyleType = GameStyleType.Tarot3;
		}
		else if (tagValue == R.id.new_tarot4_gameset_item) {
			gameStyleType = GameStyleType.Tarot4;
		}
		else { // tagValue == R.id.new_tarot5_gameset_item
			gameStyleType = GameStyleType.Tarot5;
		}
		
		Intent intent = new Intent(this, PlayerSelectorActivity.class);
		intent.putExtra(ActivityParams.PARAM_TYPE_OF_GAMESET, gameStyleType.toString());
		this.startActivity(intent);
		this.finish();
	}
}