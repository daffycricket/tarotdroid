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

import org.nla.tarotdroid.lib.helpers.AuditHelper;

import android.content.Context;
import android.content.Intent;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public interface IStatsChart {

	/**
	 * A constant for the name field in a list activity.
	 */
	String NAME = "name";
	
	/**
	 * A constant for the description field in a list activity.
	 */
	String DESC = "desc";

	/**
	 * Returns the chart name.
	 * @return the chart name
	 */
	String getName();

	/**
	 * Returns the chart description.
	 * @return the chart description
   	 */
	String getDescription();
	
	/**
	 * Returns the audit event type.
	 * @return the audit event type.
	 */
	AuditHelper.EventTypes getAuditEventType();
	  
	/**
	 * Executes the chart and returns it as an Intent.
	 * @param context
	 * @return the chart as an Intent
	 */
	Intent execute(Context context);
}
