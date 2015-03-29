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
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.charts.BetsStatsChartFragment;
import org.nla.tarotdroid.lib.ui.charts.CalledPlayersStatsChartFragment;
import org.nla.tarotdroid.lib.ui.charts.ChartFragment;
import org.nla.tarotdroid.lib.ui.charts.FullBetsStatsChartFragment;
import org.nla.tarotdroid.lib.ui.charts.GameScoresEvolutionChartFragment;
import org.nla.tarotdroid.lib.ui.charts.KingsStatsChartFragment;
import org.nla.tarotdroid.lib.ui.charts.SuccessesStatsChartFragment;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.lib.ui.charts.LeadingPlayersStatsChartFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

/**
 * A view pager activity aimed to show game charts.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class GameSetChartViewPagerActivity extends SherlockFragmentActivity {

	/**
	 * The pager.
	 */
	private ViewPager mPager;
	
	/**
	 * The pager adapter.
	 */
	private PagerAdapter mPagerAdapter;
	
	/**
	 * The chart fragments.
	 */
	private List<ChartFragment> chartFragments;
	
	/**
	 * The game set statistics computer.
	 */
	private static IGameSetStatisticsComputer gameSetStatisticsComputer;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			this.setContentView(R.layout.simple_titles);

//			// check params
//			checkArgument(this.getIntent().getExtras().containsKey(ActivityParams.PARAM_GAMESET_ID), "Game set id must be provided");
//			this.gameSet = AppContext.getApplication().getDalService().getGameSetById(this.getIntent().getExtras().getLong(ActivityParams.PARAM_GAMESET_ID));

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
			this.setTitle(this.getString(R.string.lblMainStatActivityTitle));
			
			// set keep screen on 
			UIHelper.setKeepScreenOn(this, AppContext.getApplication().getAppParams().isKeepScreenOn());

			// initialize the pager
			this.initialisePaging();
			
			ActionBar mActionBar = getSupportActionBar();
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(true);
		}
		catch (Exception e) {
			AuditHelper.auditError(AuditHelper.ErrorTypes.tabGameSetActivityError, e, this);
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
		AuditHelper.auditEvent(AuditHelper.EventTypes.displayCharts);
	}
	
	/**
	 * Returns the game set on which activity has to work.
	 * @return
	 */
	private GameSet getGameSet() {
		return TabGameSetActivity.getInstance().gameSet;
	}
	
	/**
	 * Returns the instance of the game set statistics computer. 
	 * @return
	 */
	public static IGameSetStatisticsComputer getGameSetStatisticsComputer() {
		return gameSetStatisticsComputer;
	}

	/**
	 * Initialize the fragments to be paged
	 */
	private void initialisePaging() {
		// instantiate statistics computer
		gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(this.getGameSet(), "guava");
		
		// instantiate fragments
		this.chartFragments = newArrayList();
		this.chartFragments.add(GameScoresEvolutionChartFragment.newInstance());
		this.chartFragments.add(LeadingPlayersStatsChartFragment.newInstance());
		this.chartFragments.add(BetsStatsChartFragment.newInstance());
		this.chartFragments.add(FullBetsStatsChartFragment.newInstance());
		this.chartFragments.add(SuccessesStatsChartFragment.newInstance());
		if (this.getGameSet().getGameStyleType() == GameStyleType.Tarot5) {
			this.chartFragments.add(CalledPlayersStatsChartFragment.newInstance());
			this.chartFragments.add(KingsStatsChartFragment.newInstance());
		}
		
		// populate adapter and pager
		this.mPagerAdapter = new ChartViewPagerAdapter(super.getSupportFragmentManager(), this.chartFragments);
		this.mPager = (ViewPager) super.findViewById(R.id.pager);
		this.mPager.setAdapter(this.mPagerAdapter);
		
		// show indicator
		TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
	}
	
	/**
	 * An adapter backing up the chart pager internal fragments.
	 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
	 */
	protected class ChartViewPagerAdapter extends FragmentPagerAdapter {

		/**
		 * The list of fragments to display in the pager.
		 */
		private final List<ChartFragment> fragments;

		/**
		 * @param fm
		 * @param fragments
		 */
		public ChartViewPagerAdapter(FragmentManager fm, List<ChartFragment> fragments) {
			super(fm);
			this.fragments = fragments;
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
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return this.fragments.size();
		}
		
	    /* (non-Javadoc)
	     * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
	     */
	    @Override
	    public CharSequence getPageTitle(int position) {
	    	return this.fragments.get(position).getChartTitle();
	    }
	}
}