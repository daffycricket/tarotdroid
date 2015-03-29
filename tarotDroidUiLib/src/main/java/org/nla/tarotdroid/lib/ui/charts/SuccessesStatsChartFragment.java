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
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.ui.constants.FragmentParameters;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.Result;
import org.nla.tarotdroid.biz.enums.ResultType;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class SuccessesStatsChartFragment extends ChartFragment {
	
	/**
	 * Default constructor.
	 */
	public SuccessesStatsChartFragment() {
	}

	/**
	 * Creates a SuccessesStatsChartFragment.
	 */
	public static SuccessesStatsChartFragment newInstance() {
		SuccessesStatsChartFragment fragment = new SuccessesStatsChartFragment();
		
		Bundle arguments = new Bundle();
		arguments.putString(FragmentParameters.CHART_TITLE, AppContext.getApplication().getResources().getString(R.string.statNameSuccessesFrequency));
	    fragment.setArguments(arguments);

		return fragment;
	}
	
	/**
	 * Builds a category series using the provided values.
	 * @param mapResultValues
	 * @return a category series using the provided values.
	 */
	protected CategorySeries buildCategoryDataset(final Map<ResultType, Integer> mapResultValues) {
		CategorySeries series = new CategorySeries("Successes/Failures 1");
		
		for (ResultType result : mapResultValues.keySet()) {
			series.add(Result.valueOf(result).getLabel() + " (" + mapResultValues.get(result) + ") ", mapResultValues.get(result));
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
	    	this.buildCategoryDataset(this.statisticsComputer.getResultsCount()), 
	    	this.buildCategoryRenderer(this.statisticsComputer.getResultsColors())
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
