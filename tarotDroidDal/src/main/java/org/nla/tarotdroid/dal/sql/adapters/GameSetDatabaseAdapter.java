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

import static com.google.common.base.Preconditions.checkArgument;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.enums.GameStyleType;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class GameSetDatabaseAdapter extends BaseAdapter {

	/**
	 * SQL Query to get all the game sets in the repository.
	 */
	private static final String GetAllGameSetsQuery = MessageFormat.format("select * from {0}", DatabaseV5Constants.TABLE_GAMESET);
	
	/**
	 * SQL Query to get the game sets with a specified id.
	 */
	private static final String GetGameSetForIdQuery = MessageFormat.format("select * from {0} where {1} = ", DatabaseV5Constants.TABLE_GAMESET, DatabaseV5Constants.COL_GAMESET_ID);
	
	/**
	 * SQL Query to count all game sets.
	 */
	private static final String CountGameSetsQuery = MessageFormat.format("select count(*) as cnt from {0}", DatabaseV5Constants.TABLE_GAMESET);
	
	/**
	 * Constructs a GameSetDatabaseAdapter from an Android context.
	 * @param context
	 */
	public GameSetDatabaseAdapter(final SQLiteDatabase database) {
		super(database);
	}

	/**
	 * Persists a new GameSet.
	 * @param gameSet The GameSet to persist.
	 */
	public void storeGameSet(final GameSet gameSet) {
		ContentValues initialValues = this.createContentValues(gameSet);
		long id = this.database.insertOrThrow(DatabaseV5Constants.TABLE_GAMESET, null, initialValues);
		gameSet.setId(id);
	}

	/**
	 * Updates a GameSet.
	 * @param gameSet The GameSet to update.
	 */
	public void updateGameSet(final GameSet gameSet) {
		ContentValues updatedValues = this.createContentValues(gameSet);
		this.database.update(DatabaseV5Constants.TABLE_GAMESET, updatedValues, DatabaseV5Constants.COL_GAMESET_ID + "=?", new String[]{ String.valueOf(gameSet.getId()) });
	}
	
	/**
	 * Updates a gameset after he was persisted on cloud.
	 */
	public void updateGameSetAfterSync(final GameSet gameSet) {
		checkArgument(gameSet.getSyncTimestamp() != null, "game set sync timestamp is null");
		checkArgument(gameSet.getSyncAccount() != null, "game set sync account is null");
		ContentValues updatedGameSetValues = new ContentValues();
		updatedGameSetValues.put(DatabaseV5Constants.COL_GAMESET_SYNC_TIMESTAMP, gameSet.getSyncTimestamp().getTime());
		updatedGameSetValues.put(DatabaseV5Constants.COL_GAMESET_SYNC_ACCOUNT, gameSet.getSyncAccount());
		updatedGameSetValues.put(DatabaseV5Constants.COL_GAMESET_FACEBOOK_TIMESTAMP, gameSet.getFacebookPostTs() != null ? gameSet.getFacebookPostTs().getTime() : 0);
		this.database.update(DatabaseV5Constants.TABLE_GAMESET, updatedGameSetValues, DatabaseV5Constants.COL_GAMESET_ID + "=?", new String[]{ String.valueOf(gameSet.getId()) });
		
		return;
	}

	/**
	 * Deletes an existing GameSet.
	 * @param id
	 * @return True if the GameSet is successfully deleted, false otherwise.
	 */
	public boolean deleteGameSet(final long id) {
		return this.database.delete(DatabaseV5Constants.TABLE_GAMESET, DatabaseV5Constants.COL_GAMESET_ID + "=?", new String[]{new Long(id).toString()}) > 0;
	}
	
	/**
	 * Returns the game set count.
	 * @return the game set count.
	 */
	public int countGameSets() {
		return (int)this.database.compileStatement(CountGameSetsQuery).simpleQueryForLong();
	}
	
	/**
	 * Reads from the database and returns a list of all GameSets.
	 * @return A list of all the GameSets in the database.
	 */
	public List<GameSet> fetchAllGameSets() {
		Cursor dataSource = this.database.rawQuery(GetAllGameSetsQuery, null);
		List<GameSet> gameSets = new ArrayList<GameSet>();
		while(dataSource.moveToNext()) {
			GameSet gameSet = this.createFromCursor(dataSource);
			gameSets.add(gameSet);
		}
		dataSource.close();
		return gameSets;
	}

	/**
	 * Returns the GameSet identified by the specified id.
	 * @param gameSetId The identifier of the GameSet to delete.
	 * @return The GameSet identified by the specified id.
	 */
	public GameSet fetchGameSet(final long gameSetId) {
		Cursor dataSource = this.database.rawQuery(GetGameSetForIdQuery, new String[]{new Long(gameSetId).toString()});
		GameSet gameSet = null;
		if (dataSource != null && dataSource.moveToFirst()) {
			gameSet = this.createFromCursor(dataSource);
		}
		dataSource.close();
		return gameSet;
	}
	
	/**
	 * Creates a GameSet from a cursor.
	 * @param dataSource The source cursor.
	 * @return A GameSet whose properties are extracted from the cursor.
	 */
	private GameSet createFromCursor(Cursor dataSource) {
		if(dataSource == null) {
			throw new IllegalArgumentException("dataSource is null");
		}
		if(dataSource.isClosed()) {
			throw new IllegalArgumentException("dataSource is closed");
		}
		if(dataSource.isAfterLast()) {
			throw new IllegalArgumentException("dataSource has already been totally parsed");
		}
		if(dataSource.isBeforeFirst()) {
			throw new IllegalArgumentException("dataSource hasn't yet been been moved to first element");
		}
		
		GameSet gameSet = new GameSet();
		gameSet.setId(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_ID)));
		gameSet.setCreationTs(new Date(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_TIMESTAMP))));
		gameSet.setUuid(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_UUID)));
		gameSet.setGameStyleType((GameStyleType.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_TYPE)))));
		gameSet.setSyncAccount(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_SYNC_ACCOUNT)));
		long syncTimestamp = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_SYNC_TIMESTAMP));
		gameSet.setSyncTimestamp(syncTimestamp != 0 ? new Date(syncTimestamp) : null);
		long facebookTimestamp = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_FACEBOOK_TIMESTAMP));
		gameSet.setFacebookPostTs(facebookTimestamp != 0 ? new Date(facebookTimestamp) : null);

		return gameSet;
	}

	/**
	 * Returns a ContentValues object representing the specified GameSet.
	 * @param gameSet The GameSet to convert into a ContentValues.
	 * @return A ContentValues object representing the specified GameSet.
	 */
	private ContentValues createContentValues(final GameSet gameSet) {		
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_GAMESET_UUID, gameSet.getUuid());
		if (gameSet.getCreationTs() != null) {
			values.put(DatabaseV5Constants.COL_GAMESET_TIMESTAMP, gameSet.getCreationTs().getTime());
		}
		values.put(DatabaseV5Constants.COL_GAMESET_TYPE, gameSet.getGameStyleType().toString());
		values.put(DatabaseV5Constants.COL_GAMESET_SYNC_TIMESTAMP, 0);
		values.put(DatabaseV5Constants.COL_GAMESET_FACEBOOK_TIMESTAMP, 0);

		if (gameSet.getId() != Long.MIN_VALUE) {
			values.put(DatabaseV5Constants.COL_GAMESET_ID, gameSet.getId());
		}
		return values;
	}
}