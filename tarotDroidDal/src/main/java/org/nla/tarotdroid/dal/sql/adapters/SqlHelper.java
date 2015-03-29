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
package org.nla.tarotdroid.dal.sql.adapters;

import java.util.List;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class SqlHelper {
	
	private SqlHelper() {
	}
	
	/**
	 * TODO : improve if possible using GUAVA
	 * @param gameIds
	 * @return
	 */
	public static String formatIdsForSqlInClause(final List<Long> gameIds) {
		StringBuffer idsAsSqlString = new StringBuffer();
		idsAsSqlString.append(" in (");
		for (Long gameId : gameIds) {
			idsAsSqlString.append(gameId);
			idsAsSqlString.append(", ");  
		}
		idsAsSqlString.append("0"); // added fake id to ease string formating
		idsAsSqlString.append(")");
		
		return idsAsSqlString.toString();
	}


}
