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
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;
import org.nla.tarotdroid.lib.helpers.AuditHelper;
import org.nla.tarotdroid.lib.helpers.AuditHelper.EventTypes;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public abstract class BaseStatsChart implements IStatsChart {
    
    /**
     * The name of the chart.
     */
    private String name;
    
    /**
     * The description of the chart.
     */
    private String description;
    
    /**
     * The audit event type;
     */
    private AuditHelper.EventTypes auditEventType;
    
    /**
     * The statistics computer.
     */
    protected IGameSetStatisticsComputer statisticsComputer;  
    
	/**
	 * Creates a BaseStatsChart.
	 * @param name
	 * @param description
	 */
	protected BaseStatsChart(final String name, final String description, final IGameSetStatisticsComputer gameSetStatisticsComputer, final AuditHelper.EventTypes auditEventType) {
		if (name == null) {
		    throw new IllegalArgumentException("name is null");
		}
        if (description == null) {
            throw new IllegalArgumentException("description is null");
        }
        if (auditEventType == null) {
            throw new IllegalArgumentException("auditEventType is null");
        }        
        if (gameSetStatisticsComputer == null) {
            throw new IllegalArgumentException("gameSetStatisticsComputer is null");
        }
		
	    this.name = name;
		this.description = description;
		this.statisticsComputer = gameSetStatisticsComputer;
		this.auditEventType = auditEventType;
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
	 * @see org.nla.tarotdroid.ui.controls.IStatsChart#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.ui.controls.IStatsChart#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/* (non-Javadoc)
	 * @see IStatsChart#getAuditEventType()
	 */
	@Override
	public EventTypes getAuditEventType() {
		return this.auditEventType;
	}
}
