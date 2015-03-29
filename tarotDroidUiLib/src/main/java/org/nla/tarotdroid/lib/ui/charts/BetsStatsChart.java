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
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.UIHelper;

import android.content.Context;
import android.content.Intent;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class BetsStatsChart extends BaseStatsChart {

	/**
	 * Creates a KingsStatsChart.
	 */
	public BetsStatsChart(final IGameSetStatisticsComputer gameSetStatisticsComputer) {
		super(
			AppContext.getApplication().getResources().getString(R.string.statNameBetsFrequency),
			AppContext.getApplication().getResources().getString(R.string.statDescBetsFrequency),
            gameSetStatisticsComputer,
			AuditHelper.EventTypes.displayGameSetStatisticsBetsDistribution
		);
	}
	
	/**
	 * Builds a category series using the provided values.
	 * @return the category series
	 */
	protected CategorySeries buildCategoryDataset(final Map<BetType, Integer> mapBetsValues) {
		CategorySeries series = new CategorySeries(AppContext.getApplication().getResources().getString(R.string.statNameBetsFrequency));
		for (BetType bet : mapBetsValues.keySet()) {
			series.add(UIHelper.getBetTranslation(bet) + " (" + mapBetsValues.get(bet) + ")", mapBetsValues.get(bet));
		}
		return series;
	}

	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.ui.controls.IStatsChart#execute(android.content.Context)
	 */
	@Override
	public Intent execute(final Context context) {
	    return ChartFactory.getPieChartIntent(
	    	context, 
	    	this.buildCategoryDataset(this.statisticsComputer.getBetCount()), 
	    	this.buildCategoryRenderer(this.statisticsComputer.getBetCountColors()), 
	    	AppContext.getApplication().getResources().getString(R.string.statNameBetsFrequency)
	    );
	}
}
