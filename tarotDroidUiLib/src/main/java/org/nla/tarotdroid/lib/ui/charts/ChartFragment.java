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
package org.nla.tarotdroid.lib.ui.charts;

import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.lib.ui.GameSetChartViewPagerActivity;
import org.nla.tarotdroid.lib.ui.constants.FragmentParameters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * A base fragment for all chart fragments.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public abstract class ChartFragment extends SherlockFragment
{
    /**
     * The statistics computer.
     */
    protected IGameSetStatisticsComputer statisticsComputer;  
	/**
	 * The main layout
	 */
	private FrameLayout frameLayout;
	
	/**
	 * Creates a ChartFragment.
	 * TODO: Find a way to decouple from chart activity.
	 */
	protected ChartFragment() {
		this.statisticsComputer = GameSetChartViewPagerActivity.getGameSetStatisticsComputer();
	}
		
	/**
	 * Builds a category renderer to use the provided kings.
	 * @return the category renderer
	 */
	protected DefaultRenderer buildCategoryRenderer(final int[] colors) {
		DefaultRenderer renderer = new DefaultRenderer();
		renderer.setLabelsTextSize(20);
		renderer.setLegendTextSize(20);
		renderer.setMargins(new int[] {20, 30, 15, 20});
		for (int color : colors) {
			SimpleSeriesRenderer r = new SimpleSeriesRenderer();
			r.setColor(color);
			renderer.addSeriesRenderer(r);
			renderer.setShowLabels(true);
			renderer.setShowLegend(true);
		}
		return renderer;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.frameLayout = (FrameLayout)inflater.inflate(R.layout.belgian_game_read, container, false);
		return this.frameLayout;
	}
	
	/**
	 * Returns the chart title.
	 * @return
	 */
	public String getChartTitle() {
		if (this.getArguments() != null && this.getArguments().getString(FragmentParameters.CHART_TITLE) != null) {
			return this.getArguments().getString(FragmentParameters.CHART_TITLE);
		}
		return "UNDEFINED_TITLE";
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
    public void onSaveInstanceState(Bundle outState) {
    	// HACK found on http://code.google.com/p/android/issues/detail?id=19917 to prevent error "Unable to pause activity" (described on web site) 
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }
}