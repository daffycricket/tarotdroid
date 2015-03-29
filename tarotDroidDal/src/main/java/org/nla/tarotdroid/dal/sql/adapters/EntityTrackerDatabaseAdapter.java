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

import static com.google.common.collect.Maps.newHashMap;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.enums.ActionTypes;
import org.nla.tarotdroid.biz.enums.ObjectTypes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class EntityTrackerDatabaseAdapter extends BaseAdapter {
	
	/**
	 * SQL Query to get all the tracked entities.
	 */
	private static final String GetAllTrackedEntitiesQuery = MessageFormat.format("select * from {0}", DatabaseV5Constants.TABLE_TRACKER);
	
	/**
	 * Constructs a EntityTrackerDatabaseAdapter.
	 * @param database
	 */
	public EntityTrackerDatabaseAdapter(final SQLiteDatabase database) {
		super(database);
	}
	
	/**
	 * Tracks an entity update if it's already been persisted to the cloud.
	 * @param toTrack The entity to track.
	 */
	public void trackEntity(final PersistableBusinessObject toTrack, final ActionTypes actionType, final ObjectTypes objectType) {
		this.database.insertOrThrow(DatabaseV5Constants.TABLE_TRACKER, null, this.createTrackerContentValues(toTrack, actionType, objectType));
	}

	/**
	 * Returns a ContentValues object representing the specified entity to track.
	 * @param toTrack The entity to convert into a ContentValues.
	 * @return The ContentValues object representing the entity to track.
	 */
	private ContentValues createTrackerContentValues(final PersistableBusinessObject toTrack, final ActionTypes actionType, final ObjectTypes objectType) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_TRACKER_TIMESTAMP, System.currentTimeMillis());
		values.put(DatabaseV5Constants.COL_TRACKER_ENTITY_ID, toTrack.getId());
//		values.put(DatabaseV5Constants.COL_TRACKER_ENTITY_CLOUD_ID, toTrack.getCloudId());
		values.put(DatabaseV5Constants.COL_TRACKER_ENTITY_TYPE, objectType.toString());
		values.put(DatabaseV5Constants.COL_TRACKER_ACTION, actionType.toString());
		return values;
	}
	
	/**
	 * Get a Map<ObjectTypes, Map<ActionTypes, List<Long>>> representing the created/modified/deleted ids.
	 * @param sinceTimeMillis
	 * @return
	 */
	public Map<ObjectTypes, Map<ActionTypes, List<String>>> getTrackedEntities() {
		
		Map<ActionTypes, List<String>> idsForGame = newHashMap();
		//idsForGame.put(ActionTypes.Creation, new ArrayList<Long>());
		idsForGame.put(ActionTypes.Removal, new ArrayList<String>());
		//idsForGame.put(ActionTypes.Update, new ArrayList<String>());
		
		Map<ActionTypes, List<String>> idsForPlayer = newHashMap();
		//idsForPlayer.put(ActionTypes.Creation, new ArrayList<Long>());
		idsForPlayer.put(ActionTypes.Removal, new ArrayList<String>());
		//idsForPlayer.put(ActionTypes.Update, new ArrayList<String>());
		
		Map<ActionTypes, List<String>> idsForGameSet = newHashMap();
		//idsForGameSet.put(ActionTypes.Creation, new ArrayList<Long>());
		idsForGameSet.put(ActionTypes.Removal, new ArrayList<String>());
		//idsForGameSet.put(ActionTypes.Update, new ArrayList<String>());
		
		Map<ObjectTypes, Map<ActionTypes, List<String>>> toReturn = newHashMap();
		toReturn.put(ObjectTypes.Player, idsForPlayer);
		toReturn.put(ObjectTypes.GameSet, idsForGameSet);
		
		Cursor dataSource = this.database.rawQuery(GetAllTrackedEntitiesQuery, null);
		while(dataSource.moveToNext()) {
			ActionTypes actionType = ActionTypes.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_TRACKER_ACTION)));
			ObjectTypes objectType = ObjectTypes.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_TRACKER_ENTITY_TYPE)));
			String entityId = dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_TRACKER_ENTITY_ID));
//			String entityCloudId = dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_TRACKER_ENTITY_CLOUD_ID));
//			
//			if (actionType == ActionTypes.Removal) {
//				toReturn.get(objectType).get(actionType).add(entityId);
//			}
//			else {
//				toReturn.get(objectType).get(actionType).add(entityId);
//			}
			
			toReturn.get(objectType).get(actionType).add(entityId);
			
		}
		dataSource.close();
				
		return toReturn;
	}
	
	/**
	 * Remove all tracked entities.
	 */
	public void clearTrackedEntities() {
		this.database.delete(DatabaseV5Constants.TABLE_TRACKER, null, null);
	}
	
	/**
	 * Remove all tracked entities for specified object type.
	 * @param objectType
	 */
	public void clearTrackedEntitiesForObjects(final ObjectTypes objectType) {
		this.database.delete(DatabaseV5Constants.TABLE_TRACKER, DatabaseV5Constants.COL_TRACKER_ENTITY_TYPE + "=?" , new String[] { objectType.toString() });
	}
}