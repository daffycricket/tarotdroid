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

import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.nla.tarotdroid.lib.ui.constants.FragmentParameters;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.lib.helpers.UIHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class KingsStatsChartFragment extends ChartFragment {
	
	/**
	 * Default constructor.
	 */
	public KingsStatsChartFragment() {
	}

	/**
	 * Creates a KingsStatsChartFragment.
	 */
	public static KingsStatsChartFragment newInstance() {
		KingsStatsChartFragment fragment = new KingsStatsChartFragment();
		
		Bundle arguments = new Bundle();
		arguments.putString(FragmentParameters.CHART_TITLE, AppContext.getApplication().getResources().getString(R.string.statNameCalledKingFrequency));
	    fragment.setArguments(arguments);

		return fragment;
	}
	
	/**
	 * Builds a category series using the provided values.
	 * @return the category series
	 */
	protected CategorySeries buildCategoryDataset(final Map<KingType, Integer> mapKingValues) {
		CategorySeries series = new CategorySeries(AppContext.getApplication().getResources().getString(R.string.statNameCalledKingFrequency));
		for (KingType king : mapKingValues.keySet()) {
			if (mapKingValues.get(king) != 0) {
				series.add(UIHelper.getKingTranslation(king) + " (" + mapKingValues.get(king) + ")", mapKingValues.get(king));
			}
		}
		return series;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.ui.ChartFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

	    return ChartFactory.getPieChartView(
	    	this.getActivity(), 
	    	this.buildCategoryDataset(this.statisticsComputer.getKingCount()), 
	    	this.buildCategoryRenderer(this.statisticsComputer.getKingCountColors())
	    );
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
