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

import org.nla.tarotdroid.dal.DalException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class TarotDatabaseHelper extends SQLiteOpenHelper {
	
	/**
	 * Database name.
	 */
	private static final String DATABASE_NAME = "tarotdroid.db";

	/**
	 * Database version.
	 */
	public static final int DATABASE_VERSION = 51;

	/**
	 * 
	 * @param context
	 */
	public TarotDatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onOpen(final SQLiteDatabase db) {
		super.onOpen(db);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(final SQLiteDatabase database) {
		this.dropSchemaV5(database);
		this.createSchemaV5_1(database);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
		
		try {
			
			//           => Version 5_1

			// upgrades between version 1 and version 51
			if (oldVersion == 1 && newVersion == 51) {
				this.upgradeFromVersion1ToVersion2(database);
				this.upgradeFromVersion2ToVersion4(database);
				this.upgradeFromVersion4ToVersion5_1(database);
			}
			
			// upgrades between version 2 and version 51
			else if (oldVersion == 2 && newVersion == 51) {
				this.upgradeFromVersion2ToVersion4(database);
				this.upgradeFromVersion4ToVersion5_1(database);
			}

			// upgrade from version 4 and version 51
			else if (oldVersion == 4 && newVersion == 51) {
				this.upgradeFromVersion4ToVersion5_1(database);
			}			

			// upgrades between version 5 and version 5_1
			// 5 and 5_1 are conceptually the same, but indexes were missing when app created directly schema 5, hence schema 5_1 was created to trigger some update on schema 5 
			else if (oldVersion == 5 && newVersion == 51) {
				this.upgradeFromVersion5ToVersion5_1(database);
			}
		}
		
		// make sure the final schema exists otherwise TarotDroid is ko
		catch (Exception e) {
			this.dropSchemaV2(database);
			this.dropSchemaV5(database);
			this.createSchemaV5_1(database);
		}
	}
	
	/**
	 * Drops the schema version 2 
	 * @param database
	 */
	private void dropSchemaV2(final SQLiteDatabase database) {
		final String[] queries = {
				
			DatabaseV2Constants.DROP_IDX_GAME_GAMESET_ID,
			DatabaseV2Constants.DROP_IDX_GAMEMISCLINKPPLAYERS_GAME_ID,
			DatabaseV2Constants.DROP_IDX_GAMEPLAYERS_GAME_ID,
			DatabaseV2Constants.DROP_IDX_GAMESET_TOURNAMENT_ID,
			DatabaseV2Constants.DROP_IDX_GAMESETPLAYERS_GAMESET_ID,
			DatabaseV2Constants.DROP_IDX_PLAYER_NAME,
			DatabaseV2Constants.DROP_TB_PLAYERENTITY,
			DatabaseV2Constants.DROP_TB_GAMESETENTITY,
			DatabaseV2Constants.DROP_TB_GAMESETPLAYERSENTITY,
			DatabaseV2Constants.DROP_TB_GAMEENTITY,
			DatabaseV2Constants.DROP_TB_GAMEPLAYERSENTITY,
			DatabaseV2Constants.DROP_TB_GAMEMISCLINKPLAYERSENTITY,
			DatabaseV2Constants.DROP_TB_GAMESETPARAMETERSENTITY,
			DatabaseV2Constants.DROP_TB_STANDARDTAROT5GAMEENTITY,
			DatabaseV2Constants.DROP_TB_STANDARDTAROTGAMEENTITY,
			DatabaseV2Constants.DROP_TB_BELGIANTAROT5GAMEENTITY,
			DatabaseV2Constants.DROP_TB_BELGIANTAROT4GAMEENTITY,
			DatabaseV2Constants.DROP_TB_BELGIANTAROT3GAMEENTITY
		};

		for (String query : queries) {
			database.execSQL(query);
		}
	}
	
	/**
	 * Upgrades the db from version 1 to version 2
	 * @param database
	 */
	private void upgradeFromVersion1ToVersion2(final SQLiteDatabase database) {
		final String[] queries = {
			DatabaseV2Constants.ALTER_TB_STANDARDTAROTGAME_ADD_KIDATTHEENDTYPE_COLUMN,
			DatabaseV2Constants.ALTER_TB_STANDARDTAROT5GAME_ADD_KIDATTHEENDTYPE_COLUMN,
			DatabaseV2Constants.ALTER_TB_STANDARDTAROTGAME_ADD_POIGNEE_TEAMTYPE_COLUMN,
			DatabaseV2Constants.ALTER_TB_STANDARDTAROTGAME_ADD_DBLPOIGNEE_TEAMTYPE_COLUMN,
			DatabaseV2Constants.ALTER_TB_STANDARDTAROTGAME_ADD_TPLPOIGNEE_TEAMTYPE_COLUMN,
			DatabaseV2Constants.ALTER_TB_STANDARDTAROT5GAME_ADD_POIGNEE_TEAMTYPE_COLUMN,
			DatabaseV2Constants.ALTER_TB_STANDARDTAROT5GAME_ADD_DBLPOIGNEE_TEAMTYPE_COLUMN,
			DatabaseV2Constants.ALTER_TB_STANDARDTAROT5GAME_ADD_TPLPOIGNEE_TEAMTYPE_COLUMN,
			DatabaseV2Constants.ALTER_TB_GAMESET_ADD_DBVERSION_COLUMN
		};
		
		for (String query : queries) {
			database.execSQL(query);
		}
	}
	
	/**
	 * Upgrades the db from version 2 to version 4
	 * @param database
	 */
	private void upgradeFromVersion2ToVersion4(final SQLiteDatabase database) throws DalException {
		// create new schema
		final String[] queries = {
			DatabaseV3Constants.CREATE_TB_PLAYER,
			DatabaseV3Constants.CREATE_TB_GAMESET,
			DatabaseV3Constants.CREATE_TB_GAMESET_PARAMETERS,
			DatabaseV3Constants.CREATE_TB_GAMESET_PLAYERS,
			DatabaseV3Constants.CREATE_TB_GAME,
			DatabaseV3Constants.CREATE_TB_STDGAME,
			DatabaseV3Constants.CREATE_TB_BELGIANGAME,
			DatabaseV3Constants.CREATE_TB_GAME_PLAYERS
		};
		
		for (String query : queries) {
			database.execSQL(query);
		}
		
		// transfer from schema V2 to schema V4
		this.transferDataFromVersion2ToVersion4(database);
		
		// drop former schema
		this.dropSchemaV2(database);
	}
	
	/**
	 * Drops the schema version 5 
	 * @param database
	 */
	private void dropSchemaV5(final SQLiteDatabase database) {
		database.execSQL(DatabaseV5Constants.DROP_TB_PLAYER);
		database.execSQL(DatabaseV5Constants.DROP_TB_GAMESET_PLAYERS);
		database.execSQL(DatabaseV5Constants.DROP_TB_GAME_PLAYERS);
		database.execSQL(DatabaseV5Constants.DROP_TB_GAMESET);
		database.execSQL(DatabaseV5Constants.DROP_TB_GAME);
		database.execSQL(DatabaseV5Constants.DROP_TB_BELGIANGAME);
		database.execSQL(DatabaseV5Constants.DROP_TB_STDGAME);
		database.execSQL(DatabaseV5Constants.DROP_TB_PENALTYGAME);
		database.execSQL(DatabaseV5Constants.DROP_TB_GAMESET_PARAMETERS);
		database.execSQL(DatabaseV5Constants.DROP_TB_TRACKER);
	}
	
	/**
	 * Upgrades the schema from version 4 to version 5_1
	 * @param database
	 */
	private void upgradeFromVersion4ToVersion5_1(final SQLiteDatabase database) throws DalException {
		database.execSQL(DatabaseV5Constants.ALTER_TB_PLAYER_ADD_EMAIL_COLUMN);
		database.execSQL(DatabaseV5Constants.ALTER_TB_PLAYER_ADD_FACEBOOK_ID_COLUMN);
		database.execSQL(DatabaseV5Constants.ALTER_TB_PLAYER_ADD_SYNC_ACCOUNT_COLUMN);
		database.execSQL(DatabaseV5Constants.ALTER_TB_PLAYER_ADD_SYNC_TIMESTAMP_COLUMN);
		database.execSQL(DatabaseV5Constants.ALTER_TB_PLAYER_ADD_FULLNAME_COLUMN);

		database.execSQL(DatabaseV5Constants.ALTER_TB_GAMESET_ADD_SYNC_ACCOUNT_COLUMN);
		database.execSQL(DatabaseV5Constants.ALTER_TB_GAMESET_ADD_SYNC_TIMESTAMP_COLUMN);
		database.execSQL(DatabaseV5Constants.ALTER_TB_GAMESET_ADD_FACEBOOK_TIMESTAMP_COLUMN);
		
		database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_UUID);
		database.execSQL(DatabaseV5Constants.CREATE_IDX_GAMESET_UUID);
		database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_NAME);
		database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_FULLNAME);
		
		database.execSQL(DatabaseV5Constants.CREATE_TB_PENALTYGAME);
		database.execSQL(DatabaseV5Constants.CREATE_TB_TRACKER);
	}
	
	
	/**
	 * Creates the version 5_1 of the schema.
	 * @param database
	 */
	private void createSchemaV5_1(final SQLiteDatabase database) {
		database.execSQL(DatabaseV5Constants.CREATE_TB_PLAYER);
		database.execSQL(DatabaseV5Constants.CREATE_TB_GAMESET);
		database.execSQL(DatabaseV5Constants.CREATE_TB_GAMESET_PARAMETERS);
		database.execSQL(DatabaseV5Constants.CREATE_TB_GAMESET_PLAYERS);
		database.execSQL(DatabaseV5Constants.CREATE_TB_GAME);
		database.execSQL(DatabaseV5Constants.CREATE_TB_STDGAME);
		database.execSQL(DatabaseV5Constants.CREATE_TB_BELGIANGAME);
		database.execSQL(DatabaseV5Constants.CREATE_TB_PENALTYGAME);
		database.execSQL(DatabaseV5Constants.CREATE_TB_GAME_PLAYERS);
		database.execSQL(DatabaseV5Constants.CREATE_TB_TRACKER);
		database.execSQL(DatabaseV5Constants.CREATE_IDX_GAMESET_UUID);
		database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_NAME);
		database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_UUID);
		database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_FULLNAME);
	}

	/**
	 * Upgrades the schema from version 5 to version 5_1
	 * See http://stackoverflow.com/questions/157392/how-do-i-find-out-if-a-sqlite-index-is-unique-with-sql for details
	 * @param database
	 */
	private void upgradeFromVersion5ToVersion5_1(final SQLiteDatabase database) throws DalException {
		Cursor indexes = null;
		try {
			indexes = database.rawQuery("PRAGMA index_list(" + DatabaseV5Constants.TABLE_PLAYER + ")", null);
			
			// schema 5 created directly, 3 indexes missing
			if (indexes.getCount() == 0) {
				database.execSQL(DatabaseV5Constants.CREATE_IDX_GAMESET_UUID);
				database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_NAME);
				database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_UUID);
			}
			
			// schema 5 created after upgrade from older version, 1 index on player name missing
			else if (indexes.getCount() == 1) {
				database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_NAME);
			}

			indexes.close();
		}
		catch(Exception e) {
			if (indexes != null) {
				indexes.close();
			}
		}
		
		database.execSQL(DatabaseV5Constants.ALTER_TB_PLAYER_ADD_FULLNAME_COLUMN);
		database.execSQL(DatabaseV5Constants.CREATE_IDX_PLAYER_FULLNAME);
	}
	
//	/**
//	 * Creates mock data in the schema version 2.
//	 * @param database
//	 */
//    @SuppressWarnings("unused")
//	private void mockDataDForVersion2(final SQLiteDatabase database) {
//    	MockV2DataAdapter adapter = new MockV2DataAdapter(database);
//    	adapter.execute();
//	}
    
	/**
	 * Transfer the data between the schema version 2 to the schema version 4.
	 * @param database
	 */
    private void transferDataFromVersion2ToVersion4(final SQLiteDatabase database) throws DalException {
    	DatabaseV2ToV4Adapter adapter = new DatabaseV2ToV4Adapter(database);
    	adapter.execute();
	}

	/**
     * Initialize database structure.
     * @param database
     */
    public void initializeDb() {
        this.onCreate(this.getWritableDatabase());
    }
}