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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.ui.charts.BetsStatsChart;
import org.nla.tarotdroid.lib.ui.charts.GameScoresEvolutionChart;
import org.nla.tarotdroid.lib.ui.charts.IStatsChart;
import org.nla.tarotdroid.lib.ui.charts.KingsStatsChart;
import org.nla.tarotdroid.lib.ui.charts.SuccessesStatsChart;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.ErrorTypes;
import org.nla.tarotdroid.lib.ui.charts.CalledPlayersStatsChart;
import org.nla.tarotdroid.lib.ui.charts.FullBetsStatsChart;
import org.nla.tarotdroid.lib.ui.charts.LeadingPlayersStatsChart;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;

/**
 * Main statistics view that allows a user to display a specific stats view.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class GameSetChartListActivity extends SherlockListActivity {

	/**
	 * Item texts to be displayed in the list.
	 */
	private String[] menuTexts;

	/**
	 * Item summaries to be displayed in the list.
	 */
	private String[] menuSummaries;
	
	/**
	 * Item summaries to be displayed in the list.
	 */
	private IStatsChart[] statCharts;
	
//	/**
//	 * The current game set.
//	 */
//	private GameSet gameSet;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
//			// check params
//			Bundle args = this.getIntent().getExtras();
//			if (args.containsKey(ActivityParams.PARAM_GAMESET_ID)) {
//				this.gameSet = AppContext.getApplication().getDalService().getGameSetById(args.getLong(ActivityParams.PARAM_GAMESET_ID));
//			}
//			else if (args.containsKey(ActivityParams.PARAM_GAMESET_SERIALIZED)) {
//				//this.gameSet = UIHelper.deserializeGameSet(args.getString(ActivityParams.PARAM_GAMESET_SERIALIZED));
//				this.gameSet = (GameSet)args.getSerializable(ActivityParams.PARAM_GAMESET_SERIALIZED);
//			}
//			else {
//				throw new IllegalArgumentException("Game set id or serialized game set must be provided");
//			}
			
			this.auditEvent();
			
			// set action bar properties
			this.setTitle(this.getString(R.string.lblMainStatActivityTitle));
			
			// create charts objects
			IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(this.getGameSet(), "guava");
			IStatsChart[] statChartsTarot4 = new IStatsChart[] { 
					new GameScoresEvolutionChart(gameSetStatisticsComputer),
					new BetsStatsChart(gameSetStatisticsComputer),
					new FullBetsStatsChart(gameSetStatisticsComputer),
					new SuccessesStatsChart(gameSetStatisticsComputer),
					new LeadingPlayersStatsChart(gameSetStatisticsComputer),
			};
			IStatsChart[] statChartsTarot5 = new IStatsChart[]  { 
					new GameScoresEvolutionChart(gameSetStatisticsComputer),
					new KingsStatsChart(gameSetStatisticsComputer),
					new BetsStatsChart(gameSetStatisticsComputer),
					new FullBetsStatsChart(gameSetStatisticsComputer),
					new SuccessesStatsChart(gameSetStatisticsComputer),
					new LeadingPlayersStatsChart(gameSetStatisticsComputer),
					new CalledPlayersStatsChart(gameSetStatisticsComputer),
			};			
			
			// create data structures backing the list adapter
			this.statCharts = this.getGameSet().getGameStyleType() == GameStyleType.Tarot5 ? statChartsTarot5 : statChartsTarot4;
			this.menuTexts = new String[this.statCharts.length];
			this.menuSummaries = new String[this.statCharts.length];
			for (int i = 0; i < this.statCharts.length; ++i) {
			    this.menuTexts[i] = this.statCharts[i].getName();
			    this.menuSummaries[i] = this.statCharts[i].getDescription();
			}

			// create the adapter backing the list
			SimpleAdapter adapter = new SimpleAdapter(
					this, 
					this.getListValues(),
					android.R.layout.simple_list_item_2, 
					new String[] {IStatsChart.NAME, IStatsChart.DESC }, 
					new int[] {android.R.id.text1, android.R.id.text2 }
			);
			this.setListAdapter(adapter);
		} 
		catch (Exception e) {
			AuditHelper.auditError(ErrorTypes.gameSetStatisticsActivityError, e, this);
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
	 * Returns the game set on which activity has to work.
	 * @return
	 */
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}
	
	/**
	 *	Traces creation event. 
	 */
	private void auditEvent() {
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayGameSetStatisticsPage);
	}

	/**
	 * Builds and returns a List<Map<String, String>> for the list adapter.
	 * @return a List<Map<String, String>> for the list adapter.
	 */
	private List<Map<String, String>> getListValues() {
		List<Map<String, String>> values = new ArrayList<Map<String, String>>();
		int length = this.menuTexts.length;
		for (int i = 0; i < length; i++) {
			Map<String, String> v = new HashMap<String, String>();
			v.put(IStatsChart.NAME, this.menuTexts[i]);
			v.put(IStatsChart.DESC, this.menuSummaries[i]);
			values.add(v);
		}
		return values;
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
		super.onListItemClick(l, v, position, id);
		try {
			IStatsChart chart = this.statCharts[position];
			AuditHelper.auditSession(this);
			AuditHelper.auditEvent(chart.getAuditEventType());
		    this.startActivity(chart.execute(this));
		}
		catch (Exception e) {
			Log.v(AppContext.getApplication().getAppLogTag(), "TarotDroid Exception in " + this.getClass().toString(), e);
			Toast.makeText(
				this,
				this.getString(R.string.lblErrorStatisticsComputation, e.getMessage()), 
				Toast.LENGTH_LONG
			).show();
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 * Should ovveride onBackPressed() if we were only to support versions > eclair.
	 */
	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event)  {
	    // back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
