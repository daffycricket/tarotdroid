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

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.nla.tarotdroid.dal.DalException;
import org.nla.tarotdroid.dal.sql.adapters.enums.GameTypes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class DatabaseV2ToV4Adapter extends BaseAdapter {
	
	/**
	 * Map between new player ids (long) and former game uuids (text). 
	 */
	private Map<String, Long> mapPlayerIds;
	
	/**
	 * Map between new gameset ids (long) and former gameset uuids (text). 
	 */
	private Map<String, Long> mapGameSetIds;
	
	/**
	 * Map between new game ids (long) and former game uuids (text). 
	 */
	private Map<String, Long> mapGameIds;
	
	/**
	 * Map between former game uuids and misery players ids.
	 */
	private Map<String, Long> mapGameMiseryPlayerIds;

	/**
	 * Map between former game uuids and dead players ids.
	 */
	private Map<String, Long> mapGameDeadPlayerIds;

	/**
	 * Constructs a DatabaseV2ToV4Adapter instance.
	 * @param context
	 */
	protected DatabaseV2ToV4Adapter(final SQLiteDatabase database) {
		super(database);
		this.mapGameSetIds = newHashMap();
		this.mapGameIds = newHashMap();
		this.mapPlayerIds = newHashMap();
		this.mapGameMiseryPlayerIds = newHashMap();
		this.mapGameDeadPlayerIds = newHashMap();
	}
	
	/**
	 * Executes the data transfer between v2 to v3 schemas.
	 */
	protected void execute() throws DalException {
		this.transferPlayers();
		this.cacheMiseryAndDeadPlayers();
		this.transferGameSets();
		this.transferGameSetPlayers();
		this.transferGameSetParameters();
		this.transferStandardGames();
		this.transferBelgianGames();
		this.transferGamePlayers();
	}

	/**
	 * Transfers the gamesets from table gameset (v2) to table t_gameset (v3).
	 */
	private void transferGameSets() throws DalException {
		List<ContentValues> valuesToInsert = newArrayList();
		try {
			// read all game sets
			Cursor dataSource = this.database.rawQuery("select * from gameset", null);
			while(dataSource.moveToNext()) {
				ContentValues newValues = new ContentValues();
				
				newValues.put(DatabaseV3Constants.COL_GAMESET_TIMESTAMP, dataSource.getString(dataSource.getColumnIndex("timestamp")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_TYPE, dataSource.getString(dataSource.getColumnIndex("gamesettype")));
				//newValues.put(DatabaseV3Constants.COL_GAMESET_TOURNAMENT_ID, dataSource.getString(dataSource.getColumnIndex("tournament_id")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_UUID, dataSource.getString(dataSource.getColumnIndex("id")));
				
				valuesToInsert.add(newValues);
			}
			dataSource.close();
			
			// insert new rows
			for(ContentValues values : valuesToInsert) {
				long id = this.database.insert(DatabaseV3Constants.TABLE_GAMESET, null, values);
				this.mapGameSetIds.put(values.get(DatabaseV3Constants.COL_GAMESET_UUID).toString(), id);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferGameSets()", e);
		}
	}
	
	/**
	 * Transfers the links between the game sets and their players.
	 */
	private void transferGameSetPlayers() throws DalException {
		List<ContentValues> valuesToInsert = newArrayList();
		try {
			// read all gameset/players
			Cursor dataSource = this.database.rawQuery("select * from gameset_players", null);
			while(dataSource.moveToNext()) {
				String gameSetUuid = dataSource.getString(dataSource.getColumnIndex("gameset_id"));
				String playerUuid = dataSource.getString(dataSource.getColumnIndex("player_id"));
				
				ContentValues newValues = new ContentValues();
				newValues.put(DatabaseV3Constants.COL_GAMESET_PLAYERS_GAMESET_ID, this.mapGameSetIds.get(gameSetUuid));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PLAYERS_PLAYER_ID, this.mapPlayerIds.get(playerUuid));	
				valuesToInsert.add(newValues);
			}
			dataSource.close();
			
			// insert new rows
			for(ContentValues values : valuesToInsert) {
				this.database.insert(DatabaseV3Constants.TABLE_GAMESET_PLAYERS, null, values);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferGameSetPlayers()", e);
		}
	}
	
	/**
	 * Transfers the game set parameters.
	 */
	private void transferGameSetParameters() throws DalException {
		List<ContentValues> valuesToInsert = newArrayList();
		try {
			// read all gameset/players
			Cursor dataSource = this.database.rawQuery("select * from parameters", null);
			while(dataSource.moveToNext()) {
				ContentValues newValues = new ContentValues();
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_ID, this.mapGameSetIds.get(dataSource.getString(dataSource.getColumnIndex("id"))));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_UUID, UUID.randomUUID().toString());
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PETITERATE, 1);
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PRISERATE, dataSource.getInt(dataSource.getColumnIndex("priserate")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDERATE, dataSource.getInt(dataSource.getColumnIndex("garderate")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDESANSRATE, dataSource.getInt(dataSource.getColumnIndex("gardesansrate")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDECONTRERATE, dataSource.getInt(dataSource.getColumnIndex("gardecontrerate")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PETITEBASEPOINTS, 10);
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PRISEBASEPOINTS, dataSource.getInt(dataSource.getColumnIndex("prisebasepoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDEBASEPOINTS, dataSource.getInt(dataSource.getColumnIndex("gardebasepoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDESANSBASEPOINTS, dataSource.getInt(dataSource.getColumnIndex("gardesansbasepoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDECONTREBASEPOINTS, dataSource.getInt(dataSource.getColumnIndex("gardecontrebasepoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_MISERYPOINTS, dataSource.getInt(dataSource.getColumnIndex("miserypoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_HANDFULPOINTS, dataSource.getInt(dataSource.getColumnIndex("poigneepoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_DOUBLEHANDFULPOINTS, dataSource.getInt(dataSource.getColumnIndex("doublepoigneepoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_TRIPLEHANDFULPOINTS, dataSource.getInt(dataSource.getColumnIndex("triplepoigneepoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_KIDATTHEENDPOINTS, dataSource.getInt(dataSource.getColumnIndex("kidattheendpoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_BELGIANBASESTEPPOINTS, dataSource.getInt(dataSource.getColumnIndex("belgianbasesteppoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDSUCCEEDEDPOINTS, dataSource.getInt(dataSource.getColumnIndex("chelemannouncedandsuccededpoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDFAILEDPOINTS, dataSource.getInt(dataSource.getColumnIndex("chelemannouncedandfailedpoints")));
				newValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMNOTANNOUNCEDANDSUCCEEDEDPOINTS, dataSource.getInt(dataSource.getColumnIndex("chelemnonannouncedandsuccededpoints")));
				valuesToInsert.add(newValues);
			}
			dataSource.close();
			
			// insert new rows
			for(ContentValues values : valuesToInsert) {
				this.database.insert(DatabaseV3Constants.TABLE_GAMESET_PARAMETERS, null, values);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferGameSetParameters()", e);
		}
	}
	
	/**
	 * Transfers the standard games.
	 */
	private void transferStandardGames() throws DalException {
		try {
			this.transferStd3AndStd4Games();
			this.transferStd5Games();
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferStandardGames()", e);
		}
	}
	
	/**
	 * Transfers the belgian games.
	 */
	private void transferBelgianGames() throws DalException {
		try {
			this.transferBelgian3Games();
			this.transferBelgian4Games();
			this.transferBelgian5Games();
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferBelgianGames()", e);
		}
	}
	
	/**
	 * Transfer the standard 3 and 4 games.
	 */
	private void transferStd3AndStd4Games() throws DalException {
		try {
			Cursor dataSource = this.database.rawQuery("select * from game, stdgame where game.id = stdgame.id", null);
			
			Map<String, ContentValues> mapUuidGame = newHashMap();
			Map<String, ContentValues> mapUuidStdGame = newHashMap();
			
			while(dataSource.moveToNext()) {
				// references that need to be looked for in the cached maps
				String gameUuid = dataSource.getString(dataSource.getColumnIndex("id"));
				String gameSetUuid = dataSource.getString(dataSource.getColumnIndex("gameset_id"));
				String leaderUuid = dataSource.getString(dataSource.getColumnIndex("leader_id"));
				
				// type of game
				String gameType = dataSource.getString(dataSource.getColumnIndex("gametype"));
				String targetGameType = gameType.equals("std3game") ? GameTypes.std3game.toString() : GameTypes.std4game.toString() ;
				
				// game table
				ContentValues newGameValues = new ContentValues();
				newGameValues.put(DatabaseV3Constants.COL_GAME_UUID, gameUuid);
				newGameValues.put(DatabaseV3Constants.COL_GAME_DEAD1_ID, this.mapGameDeadPlayerIds.get(gameUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_GAMESET_ID, this.mapGameSetIds.get(gameSetUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_INDEX, dataSource.getInt(dataSource.getColumnIndex("indecs")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TIMESTAMP, dataSource.getInt(dataSource.getColumnIndex("timestamp")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TYPE, targetGameType);
				mapUuidGame.put(gameUuid, newGameValues);
				
				// stdgame table
				ContentValues newStdGameValues = new ContentValues();
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_LEADER_ID, this.mapPlayerIds.get(leaderUuid));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_BET, dataSource.getString(dataSource.getColumnIndex("bet")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_OUDLERS, dataSource.getInt(dataSource.getColumnIndex("oudlers")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_POINTS, dataSource.getInt(dataSource.getColumnIndex("points")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_HANDFUL, dataSource.getString(dataSource.getColumnIndex("poignee_teamtype")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_DOUBLEHANDFUL, dataSource.getString(dataSource.getColumnIndex("dblpoignee_teamtype")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_TRIPLEHANDFUL, dataSource.getString(dataSource.getColumnIndex("tplpoignee_teamtype")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_KIDATTHEEND, dataSource.getString(dataSource.getColumnIndex("kidattheendtype")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_SLAM, dataSource.getString(dataSource.getColumnIndex("chelemtype")));
				mapUuidStdGame.put(gameUuid, newStdGameValues);
			}
			dataSource.close();
			
			// insert new game rows
			for(String gameUuid : mapUuidGame.keySet()) {
				ContentValues newGameValues = mapUuidGame.get(gameUuid);
				ContentValues newStdGameValues = mapUuidStdGame.get(gameUuid);
				
				// insert into game with new id 
				long id = this.database.insert(DatabaseV3Constants.TABLE_GAME, null, newGameValues);
				
				// insert into stdgame with id generated previously
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_ID, id);
				this.database.insert(DatabaseV3Constants.TABLE_STDGAME, null, newStdGameValues);
				
				// cache association uuid => id
				this.mapGameIds.put(gameUuid, id);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferStd3AndStd4Games()", e);
		}
	}
		
	/**
	 * Transfer the standard 5 games.
	 */
	private void transferStd5Games() throws DalException {
		try {
			Cursor dataSource = this.database.rawQuery("select * from game, std5game where game.id = std5game.id", null);
			
			Map<String, ContentValues> mapUuidGame = newHashMap();
			Map<String, ContentValues> mapUuidStdGame = newHashMap();
			
			while(dataSource.moveToNext()) {
				// references that need to be looked for in the cached maps
				String gameUuid = dataSource.getString(dataSource.getColumnIndex("id"));
				String gameSetUuid = dataSource.getString(dataSource.getColumnIndex("gameset_id"));
				String leaderUuid = dataSource.getString(dataSource.getColumnIndex("leader_id"));
				String calledUuid = dataSource.getString(dataSource.getColumnIndex("called_id"));
				
				// game table
				ContentValues newGameValues = new ContentValues();
				newGameValues.put(DatabaseV3Constants.COL_GAME_UUID, gameUuid);
				newGameValues.put(DatabaseV3Constants.COL_GAME_DEAD1_ID, this.mapGameDeadPlayerIds.get(gameUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_GAMESET_ID, this.mapGameSetIds.get(gameSetUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_INDEX, dataSource.getInt(dataSource.getColumnIndex("indecs")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TIMESTAMP, dataSource.getInt(dataSource.getColumnIndex("timestamp")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TYPE, GameTypes.std5game.toString());
				mapUuidGame.put(gameUuid, newGameValues);
				
				// std5game table
				ContentValues newStdGameValues = new ContentValues();
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_LEADER_ID, this.mapPlayerIds.get(leaderUuid));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_CALLED_ID, this.mapPlayerIds.get(calledUuid));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_BET, dataSource.getString(dataSource.getColumnIndex("bet")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_KING, dataSource.getString(dataSource.getColumnIndex("calledking")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_OUDLERS, dataSource.getInt(dataSource.getColumnIndex("oudlers")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_POINTS, dataSource.getInt(dataSource.getColumnIndex("points")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_HANDFUL, dataSource.getString(dataSource.getColumnIndex("poignee_teamtype")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_DOUBLEHANDFUL, dataSource.getString(dataSource.getColumnIndex("dblpoignee_teamtype")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_TRIPLEHANDFUL, dataSource.getString(dataSource.getColumnIndex("tplpoignee_teamtype")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_KIDATTHEEND, dataSource.getString(dataSource.getColumnIndex("kidattheendtype")));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_MISERY, this.mapGameMiseryPlayerIds.get(gameUuid));
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_SLAM, dataSource.getString(dataSource.getColumnIndex("chelemtype")));
				mapUuidStdGame.put(gameUuid, newStdGameValues);
			}
			dataSource.close();
			
			// insert new game rows
			for(String gameUuid : mapUuidGame.keySet()) {
				ContentValues newGameValues = mapUuidGame.get(gameUuid);
				ContentValues newStdGameValues = mapUuidStdGame.get(gameUuid);
				
				// insert into game with new id 
				long id = this.database.insert(DatabaseV3Constants.TABLE_GAME, null, newGameValues);
				
				// insert into stdgame with id generated previously
				newStdGameValues.put(DatabaseV3Constants.COL_STDGAME_ID, id);
				this.database.insert(DatabaseV3Constants.TABLE_STDGAME, null, newStdGameValues);
				
				// cache association uuid => id
				this.mapGameIds.put(gameUuid, id);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferStd5Games()", e);
		}
	}
	
	/**
	 * Transfer the belgian 3 games.
	 */
	private void transferBelgian3Games() throws DalException {
		try {
			Cursor dataSource = this.database.rawQuery("select * from game, belgian3game where game.id = belgian3game.id", null);
			
			Map<String, ContentValues> mapUuidGame = newHashMap();
			Map<String, ContentValues> mapUuidBelgianGame = newHashMap();
			
			while(dataSource.moveToNext()) {
				// references that need to be looked for in the cached maps
				String gameUuid = dataSource.getString(dataSource.getColumnIndex("id"));
				String gameSetUuid = dataSource.getString(dataSource.getColumnIndex("gameset_id"));
				String firstUuid = dataSource.getString(dataSource.getColumnIndex("first_id"));
				String secondUuid = dataSource.getString(dataSource.getColumnIndex("second_id"));
				String thirdUuid = dataSource.getString(dataSource.getColumnIndex("third_id"));
				
				// game table
				ContentValues newGameValues = new ContentValues();
				newGameValues.put(DatabaseV3Constants.COL_GAME_UUID, gameUuid);
				newGameValues.put(DatabaseV3Constants.COL_GAME_DEAD1_ID, this.mapGameDeadPlayerIds.get(gameUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_GAMESET_ID, this.mapGameSetIds.get(gameSetUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_INDEX, dataSource.getInt(dataSource.getColumnIndex("indecs")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TIMESTAMP, dataSource.getInt(dataSource.getColumnIndex("timestamp")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TYPE, GameTypes.belgian3game.toString());
				mapUuidGame.put(gameUuid, newGameValues);
				
				// belgian3game table
				ContentValues newBelgianGameValues = new ContentValues();
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_FIRST_ID, this.mapPlayerIds.get(firstUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_SECOND_ID, this.mapPlayerIds.get(secondUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_THIRD_ID, this.mapPlayerIds.get(thirdUuid));
				mapUuidBelgianGame.put(gameUuid, newBelgianGameValues);
			}
			dataSource.close();
			
			// insert new game rows
			for(String gameUuid : mapUuidGame.keySet()) {
				ContentValues newGameValues = mapUuidGame.get(gameUuid);
				ContentValues newBelgianGameValues = mapUuidBelgianGame.get(gameUuid);
				
				// insert into game with new id 
				long id = this.database.insert(DatabaseV3Constants.TABLE_GAME, null, newGameValues);
				
				// insert into stdgame with id generated previously
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_ID, id);
				this.database.insert(DatabaseV3Constants.TABLE_BELGIANGAME, null, newBelgianGameValues);
				
				// cache association uuid => id
				this.mapGameIds.put(gameUuid, id);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferBelgian3Games()", e);
		}
	}
	
	/**
	 * Transfer the belgian 4 games.
	 */
	private void transferBelgian4Games() throws DalException {
		try {
			Cursor dataSource = this.database.rawQuery("select * from game, belgian4game where game.id = belgian4game.id", null);
			
			Map<String, ContentValues> mapUuidGame = newHashMap();
			Map<String, ContentValues> mapUuidBelgianGame = newHashMap();
			
			while(dataSource.moveToNext()) {
				// references that need to be looked for in the cached maps
				String gameUuid = dataSource.getString(dataSource.getColumnIndex("id"));
				String gameSetUuid = dataSource.getString(dataSource.getColumnIndex("gameset_id"));
				String firstUuid = dataSource.getString(dataSource.getColumnIndex("first_id"));
				String secondUuid = dataSource.getString(dataSource.getColumnIndex("second_id"));
				String thirdUuid = dataSource.getString(dataSource.getColumnIndex("third_id"));
				String fourthUuid = dataSource.getString(dataSource.getColumnIndex("fourth_id"));
				
				// game table
				ContentValues newGameValues = new ContentValues();
				newGameValues.put(DatabaseV3Constants.COL_GAME_UUID, gameUuid);
				newGameValues.put(DatabaseV3Constants.COL_GAME_DEAD1_ID, this.mapGameDeadPlayerIds.get(gameUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_GAMESET_ID, this.mapGameSetIds.get(gameSetUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_INDEX, dataSource.getInt(dataSource.getColumnIndex("indecs")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TIMESTAMP, dataSource.getInt(dataSource.getColumnIndex("timestamp")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TYPE, GameTypes.belgian3game.toString());
				mapUuidGame.put(gameUuid, newGameValues);
				
				// belgian3game table
				ContentValues newBelgianGameValues = new ContentValues();
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_FIRST_ID, this.mapPlayerIds.get(firstUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_SECOND_ID, this.mapPlayerIds.get(secondUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_THIRD_ID, this.mapPlayerIds.get(thirdUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_FOURTH_ID, this.mapPlayerIds.get(fourthUuid));
				mapUuidBelgianGame.put(gameUuid, newBelgianGameValues);
			}
			dataSource.close();
			
			// insert new game rows
			for(String gameUuid : mapUuidGame.keySet()) {
				ContentValues newGameValues = mapUuidGame.get(gameUuid);
				ContentValues newBelgianGameValues = mapUuidBelgianGame.get(gameUuid);
				
				// insert into game with new id 
				long id = this.database.insert(DatabaseV3Constants.TABLE_GAME, null, newGameValues);
				
				// insert into stdgame with id generated previously
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_ID, id);
				this.database.insert(DatabaseV3Constants.TABLE_BELGIANGAME, null, newBelgianGameValues);
				
				// cache association uuid => id
				this.mapGameIds.put(gameUuid, id);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferBelgian4Games()", e);
		}
	}
	
	/**
	 * Transfer the belgian 5 games.
	 */
	private void transferBelgian5Games() throws DalException {
		try {
			Cursor dataSource = this.database.rawQuery("select * from game, belgian5game where game.id = belgian5game.id", null);
			
			Map<String, ContentValues> mapUuidGame = newHashMap();
			Map<String, ContentValues> mapUuidBelgianGame = newHashMap();
			
			while(dataSource.moveToNext()) {
				// references that need to be looked for in the cached maps
				String gameUuid = dataSource.getString(dataSource.getColumnIndex("id"));
				String gameSetUuid = dataSource.getString(dataSource.getColumnIndex("gameset_id"));
				String firstUuid = dataSource.getString(dataSource.getColumnIndex("first_id"));
				String secondUuid = dataSource.getString(dataSource.getColumnIndex("second_id"));
				String thirdUuid = dataSource.getString(dataSource.getColumnIndex("third_id"));
				String fourthUuid = dataSource.getString(dataSource.getColumnIndex("fourth_id"));
				String fifthUuid = dataSource.getString(dataSource.getColumnIndex("fifth_id"));
				
				// game table
				ContentValues newGameValues = new ContentValues();
				newGameValues.put(DatabaseV3Constants.COL_GAME_UUID, gameUuid);
				newGameValues.put(DatabaseV3Constants.COL_GAME_DEAD1_ID, this.mapGameDeadPlayerIds.get(gameUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_GAMESET_ID, this.mapGameSetIds.get(gameSetUuid));
				newGameValues.put(DatabaseV3Constants.COL_GAME_INDEX, dataSource.getInt(dataSource.getColumnIndex("indecs")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TIMESTAMP, dataSource.getInt(dataSource.getColumnIndex("timestamp")));
				newGameValues.put(DatabaseV3Constants.COL_GAME_TYPE, GameTypes.belgian3game.toString());
				mapUuidGame.put(gameUuid, newGameValues);
				
				// belgian3game table
				ContentValues newBelgianGameValues = new ContentValues();
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_FIRST_ID, this.mapPlayerIds.get(firstUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_SECOND_ID, this.mapPlayerIds.get(secondUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_THIRD_ID, this.mapPlayerIds.get(thirdUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_FOURTH_ID, this.mapPlayerIds.get(fourthUuid));
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_FIFTH_ID, this.mapPlayerIds.get(fifthUuid));
				mapUuidBelgianGame.put(gameUuid, newBelgianGameValues);
			}
			dataSource.close();
			
			// insert new game rows
			for(String gameUuid : mapUuidGame.keySet()) {
				ContentValues newGameValues = mapUuidGame.get(gameUuid);
				ContentValues newBelgianGameValues = mapUuidBelgianGame.get(gameUuid);
				
				// insert into game with new id 
				long id = this.database.insert(DatabaseV3Constants.TABLE_GAME, null, newGameValues);
				
				// insert into stdgame with id generated previously
				newBelgianGameValues.put(DatabaseV3Constants.COL_BELGIANGAME_ID, id);
				this.database.insert(DatabaseV3Constants.TABLE_BELGIANGAME, null, newBelgianGameValues);
				
				// cache association uuid => id
				this.mapGameIds.put(gameUuid, id);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferBelgian5Games()", e);
		}
	}

	/**
	 * Transfers the links between the games and their players.
	 */
	private void transferGamePlayers() throws DalException {
		List<ContentValues> valuesToInsert = newArrayList();
		try {
			// read all gameset/players
			Cursor dataSource = this.database.rawQuery("select * from game_players", null);
			while(dataSource.moveToNext()) {
				String gameUuid = dataSource.getString(dataSource.getColumnIndex("game_id"));
				String playerUuid = dataSource.getString(dataSource.getColumnIndex("player_id"));
				
				ContentValues newValues = new ContentValues();
				newValues.put(DatabaseV3Constants.COL_GAME_PLAYERS_GAME_ID, this.mapGameIds.get(gameUuid));
				newValues.put(DatabaseV3Constants.COL_GAME_PLAYERS_PLAYER_ID, this.mapPlayerIds.get(playerUuid));	
				valuesToInsert.add(newValues);
			}
			dataSource.close();
			
			// insert new rows
			for(ContentValues values : valuesToInsert) {
				this.database.insert(DatabaseV3Constants.TABLE_GAME_PLAYERS, null, values);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferGamePlayers()", e);
		}
	}
	
	/**
	 * Transfers the players from table player (v2) to table t_player (v3)
	 */
	private void transferPlayers() throws DalException {
		List<ContentValues> valuesToInsert = newArrayList();
		try {
			// read all players
			Cursor dataSource = this.database.rawQuery("select * from player", null);
			
			// SQL Query to identify a user creation date.
			final String queryFindUserCreation = 
					"select gs.timestamp " + 
					"from player p, gameset_players gsp, gameset gs " +
					"where p.id = {0} " +
					"and p.id = gsp.player_id " +
					"and gs.id = gsp.gameset_id " +
					"order by gs.timestamp asc";
			
			while(dataSource.moveToNext()) {
				String playerId = dataSource.getString(dataSource.getColumnIndex("id"));
				String query = MessageFormat.format(queryFindUserCreation, "'" + playerId + "'");
				Cursor userCreationSource = this.database.rawQuery(query, null);
				userCreationSource.moveToFirst();
				long userCreationTs = userCreationSource.getLong(userCreationSource.getColumnIndex("timestamp"));
				userCreationSource.close();
				
				ContentValues newPlayerValues = new ContentValues();
				newPlayerValues.put(DatabaseV3Constants.COL_PLAYER_NAME, dataSource.getString(dataSource.getColumnIndex("name")));
				newPlayerValues.put(DatabaseV3Constants.COL_PLAYER_UUID, playerId);
				newPlayerValues.put(DatabaseV3Constants.COL_PLAYER_TIMESTAMP, userCreationTs);
				valuesToInsert.add(newPlayerValues);
			}
			dataSource.close();
			
			// insert new players and store their ids in the map
			for(ContentValues values : valuesToInsert) {
				long id = this.database.insert(DatabaseV3Constants.TABLE_PLAYER, null, values);
				this.mapPlayerIds.put(values.get(DatabaseV3Constants.COL_PLAYER_UUID).toString(), id);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in transferPlayers()", e);
		}
	}
	
	/**
	 * Caches the links between game uuids and new player ids
	 */
	private void cacheMiseryAndDeadPlayers() throws DalException {
		try {
			Cursor dataSource = this.database.rawQuery("select * from game_misclinkplayers", null);
			while(dataSource.moveToNext()) {
				String linkType = dataSource.getString(dataSource.getColumnIndex("linktype"));
				String gameUuid = dataSource.getString(dataSource.getColumnIndex("game_id"));
				String playerUuid = dataSource.getString(dataSource.getColumnIndex("player_id"));
				
				if (linkType.equalsIgnoreCase("dead")) {
					this.mapGameDeadPlayerIds.put(gameUuid, this.mapPlayerIds.get(playerUuid));
				}
				else if (linkType.equalsIgnoreCase("misery")) {
					this.mapGameMiseryPlayerIds.put(gameUuid, this.mapPlayerIds.get(playerUuid));
				}
			}
			dataSource.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new DalException("Problem in cacheMiseryAndDeadPlayers()", e);
		}	
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.sql.adapters.BaseAdapter#clear()
	 */
	@Override
	public void clear() {
	}
}