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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.lib.helpers.AuditHelper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class GameScoresEvolutionChart extends BaseStatsChart {

    /**
     * Map of player count and corresponding PointStyle arrays.
     */
    private static final Map<Integer, PointStyle[]> MAP_PLAYERCOUNT_POINTSTYLES = new HashMap<Integer, PointStyle[]>();
    static { 
        GameScoresEvolutionChart.MAP_PLAYERCOUNT_POINTSTYLES.put(3, new PointStyle[]{PointStyle.POINT, PointStyle.POINT, PointStyle.POINT});
        GameScoresEvolutionChart.MAP_PLAYERCOUNT_POINTSTYLES.put(4, new PointStyle[]{PointStyle.POINT, PointStyle.POINT, PointStyle.POINT, PointStyle.POINT});
        GameScoresEvolutionChart.MAP_PLAYERCOUNT_POINTSTYLES.put(5, new PointStyle[]{PointStyle.POINT, PointStyle.POINT, PointStyle.POINT, PointStyle.POINT, PointStyle.POINT});
        GameScoresEvolutionChart.MAP_PLAYERCOUNT_POINTSTYLES.put(6, new PointStyle[]{PointStyle.POINT, PointStyle.POINT, PointStyle.POINT, PointStyle.POINT, PointStyle.POINT, PointStyle.POINT});
    }
    
	/**
	 * Creates a GameScoresEvolutionChart.
	 */
	public GameScoresEvolutionChart(final IGameSetStatisticsComputer gameSetStatisticsComputer) {
		super(
			AppContext.getApplication().getResources().getString(R.string.statNameScoreEvolution),
			AppContext.getApplication().getResources().getString(R.string.statDescScoreEvolution),
			gameSetStatisticsComputer,
			AuditHelper.EventTypes.displayGameSetStatisticsScoresEvolutionChart
		);
	}

	  /**
	   * Sets a few of the series renderer settings.
	   * 
	   * @param renderer the renderer to set the properties to
	   * @param title the chart title
	   * @param xTitle the title for the X axis
	   * @param yTitle the title for the Y axis
	   * @param xMin the minimum value on the X axis
	   * @param xMax the maximum value on the X axis
	   * @param yMin the minimum value on the Y axis
	   * @param yMax the maximum value on the Y axis
	   * @param axesColor the axes color
	   * @param labelsColor the labels color
	   */
	  protected void setChartSettings(final XYMultipleSeriesRenderer renderer, final String title, final String xTitle,
	          final String yTitle, final double xMin, final double xMax, final double yMin, final double yMax, final int axesColor,
	          final int labelsColor) {
	    renderer.setChartTitle(title);
	    renderer.setXTitle(xTitle);
	    renderer.setYTitle(yTitle);
	    renderer.setXAxisMin(xMin);
	    renderer.setXAxisMax(xMax);
	    renderer.setYAxisMin(yMin);
	    renderer.setYAxisMax(yMax);
	    renderer.setAxesColor(axesColor);
	    renderer.setLabelsColor(labelsColor);
	    renderer.setPanLimits(new double[] {xMin, xMax, yMin, yMax});
	    renderer.setZoomLimits(new double[] {xMin, xMax, yMin, yMax});
	  }
	  
	  /**
	   * Builds an XY multiple series renderer.
	   * @param styles the series point styles
	   * @return the XY multiple series renderers
	   */
	@SuppressWarnings("deprecation")
	protected XYMultipleSeriesRenderer buildRenderer(final int[] colors, final PointStyle[] styles) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    renderer.setPointSize(5f);
	    renderer.setMargins(new int[] {20, 30, 15, 20});
	    int length = colors.length;
	    for (int i = 0; i < length; i++) {
	      XYSeriesRenderer r = new XYSeriesRenderer();
	      r.setColor(colors[i]);
	      r.setPointStyle(styles[i]);
	      r.setLineWidth(5f);
	      renderer.addSeriesRenderer(r);
	    }
	    renderer.setXLabels(5);
	    renderer.setYLabels(10);
	    renderer.setDisplayChartValues(true);
	    return renderer;
	  }
	  
	  /**
	   * Builds an XY multiple time dataset using the provided values.
	   * 
	   * @return the XY multiple time dataset
	   */
	  
	  
	  protected XYMultipleSeriesDataset buildDataset(final String[] playerNames, final List<double[]> playerGameScoresArrays) {
		  XYMultipleSeriesDataset gameSetDataSet = new XYMultipleSeriesDataset();
		  for (int playerIndex = 0; playerIndex < playerNames.length; ++playerIndex) {
			  XYSeries playerSeries = new XYSeries(playerNames[playerIndex]);
			  double[] playerGameScoresArray = playerGameScoresArrays.get(playerIndex);
			  for (int gameIndex = 0; gameIndex < playerGameScoresArray.length; ++gameIndex) {
				  playerSeries.add(gameIndex, playerGameScoresArray[gameIndex]);
			  }
			  gameSetDataSet.addSeries(playerSeries);
		  }
		  return gameSetDataSet;
	  }
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.ui.controls.IStatsChart#execute(android.content.Context)
	 */
	@Override
	public Intent execute(final Context context) {
		List<double[]> scores = this.statisticsComputer.getScores();
	    XYMultipleSeriesRenderer renderer = this.buildRenderer(this.statisticsComputer.getScoresColors(), this.getScoresPointStyles());
	    this.setChartSettings(
	    	renderer, 
	    	AppContext.getApplication().getResources().getString(R.string.statNameScoreEvolution), 
	    	AppContext.getApplication().getResources().getString(R.string.lblScoreEvolutionGames), 
	    	AppContext.getApplication().getResources().getString(R.string.lblScoreEvolutionPoints), 
	    	0,
	    	this.statisticsComputer.getGameCount() + 1, 
	    	this.statisticsComputer.getMaxAbsoluteScore() * -1 - 100, 
	    	this.statisticsComputer.getMaxAbsoluteScore() + 100, 
	    	Color.GRAY, 
	    	Color.LTGRAY
	    );

	    return ChartFactory.getLineChartIntent(
	    	context, 
	    	this.buildDataset(this.statisticsComputer.getPlayerNames(), scores), 
	    	renderer
	    );
	}
	
    /**
     * Returns an array corresponding to the scores point styles.
     * @return an array corresponding to the scores point styles. 
     */
    private PointStyle[] getScoresPointStyles() {
        int playerCount = this.statisticsComputer.getPlayerCount();
        if (!GameScoresEvolutionChart.MAP_PLAYERCOUNT_POINTSTYLES.containsKey(playerCount)) {
            throw new IllegalStateException("missing PointStyle array or illegal player count:" + playerCount);
        }
        
        return GameScoresEvolutionChart.MAP_PLAYERCOUNT_POINTSTYLES.get(playerCount);
    }
}
