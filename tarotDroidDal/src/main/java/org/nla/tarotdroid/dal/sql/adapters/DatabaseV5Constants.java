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

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class DatabaseV5Constants {
	
	/**
	 * Prevents from constructing an object.
	 */
	private DatabaseV5Constants() {
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PLAYER
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_PLAYER = "t_player";
	protected static final String COL_PLAYER_ID = "id";
	protected static final String COL_PLAYER_UUID = "uuid";
	protected static final String COL_PLAYER_NAME = "name";
	protected static final String COL_PLAYER_TIMESTAMP = "timestamp";
	protected static final String COL_PLAYER_PICTURE_URI = "picture_uri";
	protected static final String COL_PLAYER_FACEBOOK_ID = "facebook_id";
	protected static final String COL_PLAYER_EMAIL = "email";
	protected static final String COL_PLAYER_SYNC_ACCOUNT = "sync_account";
	protected static final String COL_PLAYER_SYNC_TIMESTAMP = "sync_timestamp";
	protected static final String COL_PLAYER_FULLNAME = "full_name";
	
	/**
	 * Create table t_player statement.
	 */
	protected static final String CREATE_TB_PLAYER = 
		"create table " + TABLE_PLAYER +
		" (" +
			COL_PLAYER_ID + " integer primary key, " +
			COL_PLAYER_UUID + " text not null, " +
			COL_PLAYER_NAME + " text not null, " +
			COL_PLAYER_TIMESTAMP + " integer not null default '2012-05-18 00:00:00', " +
			COL_PLAYER_PICTURE_URI + " text, " +
			COL_PLAYER_EMAIL + " text, " +
			COL_PLAYER_FACEBOOK_ID + " text, " +
			COL_PLAYER_SYNC_ACCOUNT + " text, " +
			COL_PLAYER_SYNC_TIMESTAMP + " integer, " +
			COL_PLAYER_FULLNAME + " text " +
		");";
		
	/**
	 * Create indexes on t_player statements.
	 */
	protected static final String CREATE_IDX_PLAYER_NAME = "create index idx_player_name on " + TABLE_PLAYER + "(" + COL_PLAYER_NAME + ");";
	protected static final String CREATE_IDX_PLAYER_UUID = "create index idx_player_uuid on " + TABLE_PLAYER + "(" + COL_PLAYER_UUID + ");";

	/**
	 * Drop player table statement.
	 */
	protected static final String DROP_TB_PLAYER = "drop table if exists " + TABLE_PLAYER;
	
	/**
	 * updates table t_player statements (version 4 => version 5).
	 */	
	protected static final String ALTER_TB_PLAYER_ADD_EMAIL_COLUMN = "alter table " + TABLE_PLAYER + " add column " + COL_PLAYER_EMAIL + " text;";
	protected static final String ALTER_TB_PLAYER_ADD_FACEBOOK_ID_COLUMN = "alter table " + TABLE_PLAYER + " add column " + COL_PLAYER_FACEBOOK_ID + " text;";
	protected static final String ALTER_TB_PLAYER_ADD_SYNC_ACCOUNT_COLUMN = "alter table " + TABLE_PLAYER + " add column " + COL_PLAYER_SYNC_ACCOUNT + " text;";
	protected static final String ALTER_TB_PLAYER_ADD_SYNC_TIMESTAMP_COLUMN = "alter table " + TABLE_PLAYER + " add column " + COL_PLAYER_SYNC_TIMESTAMP + " integer;";

	/**
	 * updates table t_player statements (version 5 => version 5_1).
	 */	
	protected static final String ALTER_TB_PLAYER_ADD_FULLNAME_COLUMN = "alter table " + TABLE_PLAYER + " add column " + COL_PLAYER_FULLNAME + " text;";
	protected static final String CREATE_IDX_PLAYER_FULLNAME = "create index idx_player_fullname on " + TABLE_PLAYER + "(" + COL_PLAYER_FULLNAME + ");";
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GAME SET
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_GAMESET = "t_gameset";
	protected static final String COL_GAMESET_ID = "id";
	protected static final String COL_GAMESET_UUID = "uuid";
	protected static final String COL_GAMESET_TIMESTAMP = "timestamp";
	protected static final String COL_GAMESET_TYPE = "type";
	protected static final String COL_GAMESET_TOURNAMENT_ID = "tournament_id";
	protected static final String COL_GAMESET_SYNC_ACCOUNT = "sync_account";
	protected static final String COL_GAMESET_SYNC_TIMESTAMP = "sync_timestamp";
	protected static final String COL_GAMESET_FACEBOOK_TIMESTAMP = "facebook_timestamp";
	
	/**
	 * Create table t_gameset statement.
	 */
	protected static final String CREATE_TB_GAMESET = 
		"create table " + TABLE_GAMESET +
		" (" +
			COL_GAMESET_ID + " integer primary key, " +
			COL_GAMESET_UUID + " text not null, " +
			COL_GAMESET_TIMESTAMP + " integer not null, " +
			COL_GAMESET_TYPE + " text not null, " +
			COL_GAMESET_TOURNAMENT_ID + " integer, " +
			COL_GAMESET_SYNC_ACCOUNT + " text, " +
			COL_GAMESET_SYNC_TIMESTAMP + " integer, " +
			COL_GAMESET_FACEBOOK_TIMESTAMP + " integer " +
		");";
	
	/**
	 * Create indexes on t_gameset statements.
	 */
	protected static final String CREATE_IDX_GAMESET_UUID = "create index idx_gameset_uuid on " + TABLE_PLAYER + "(" + COL_GAMESET_UUID + ");";
	
	/**
	 * Drop table t_gameset statement.
	 */
	protected static final String DROP_TB_GAMESET = "drop table if exists " + TABLE_GAMESET;
	
	/**
	 * updates table t_gameset statements (version 4 => version 5).
	 */	
	protected static final String ALTER_TB_GAMESET_ADD_SYNC_ACCOUNT_COLUMN = "alter table " + TABLE_GAMESET + " add column " + COL_GAMESET_SYNC_ACCOUNT + " text;";
	protected static final String ALTER_TB_GAMESET_ADD_SYNC_TIMESTAMP_COLUMN = "alter table " + TABLE_GAMESET + " add column " + COL_GAMESET_SYNC_TIMESTAMP + " integer;";
	protected static final String ALTER_TB_GAMESET_ADD_FACEBOOK_TIMESTAMP_COLUMN = "alter table " + TABLE_GAMESET + " add column " + COL_GAMESET_FACEBOOK_TIMESTAMP + " integer;";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Links GAME SET / PLAYERS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_GAMESET_PLAYERS = "t_gameset_players";
	protected static final String COL_GAMESET_PLAYERS_GAMESET_ID = "gameset_id";
	protected static final String COL_GAMESET_PLAYERS_PLAYER_ID = "player_id";
	
	/**
	 * Create table t_gameset_players statement.
	 */
	protected static final String CREATE_TB_GAMESET_PLAYERS = 
		"create table " + TABLE_GAMESET_PLAYERS +
		" (" +
			COL_GAMESET_PLAYERS_GAMESET_ID + " integer not null, " +
			COL_GAMESET_PLAYERS_PLAYER_ID +" integer not null, " +
			"primary key (" + COL_GAMESET_PLAYERS_GAMESET_ID + "," + COL_GAMESET_PLAYERS_PLAYER_ID + ")" +
		");";
	
	/**
	 * Drop table t_gameset_players statement.
	 */
	protected static final String DROP_TB_GAMESET_PLAYERS = "drop table if exists " + TABLE_GAMESET_PLAYERS;
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GAME SET PARAMETERS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_GAMESET_PARAMETERS = "t_gameset_parameters";
	protected static final String COL_GAMESET_PARAMETERS_ID = "id";
	protected static final String COL_GAMESET_PARAMETERS_UUID = "uuid";
	protected static final String COL_GAMESET_PARAMETERS_PETITERATE = "petiterate";
	protected static final String COL_GAMESET_PARAMETERS_PRISERATE = "priserate";
	protected static final String COL_GAMESET_PARAMETERS_GARDERATE = "garderate";
	protected static final String COL_GAMESET_PARAMETERS_GARDESANSRATE = "gardesansrate";
	protected static final String COL_GAMESET_PARAMETERS_GARDECONTRERATE = "gardecontrerate";
	protected static final String COL_GAMESET_PARAMETERS_PETITEBASEPOINTS = "petitebasepoints";
	protected static final String COL_GAMESET_PARAMETERS_PRISEBASEPOINTS = "prisebasepoints";
	protected static final String COL_GAMESET_PARAMETERS_GARDEBASEPOINTS = "gardebasepoints";
	protected static final String COL_GAMESET_PARAMETERS_GARDESANSBASEPOINTS = "gardesansbasepoints";
	protected static final String COL_GAMESET_PARAMETERS_GARDECONTREBASEPOINTS = "gardecontrebasepoints";
	protected static final String COL_GAMESET_PARAMETERS_MISERYPOINTS = "miserypoints";
	protected static final String COL_GAMESET_PARAMETERS_HANDFULPOINTS = "handfulpoints";
	protected static final String COL_GAMESET_PARAMETERS_DOUBLEHANDFULPOINTS = "doublehandfulpoints";
	protected static final String COL_GAMESET_PARAMETERS_TRIPLEHANDFULPOINTS = "triplehandfupoints";
	protected static final String COL_GAMESET_PARAMETERS_KIDATTHEENDPOINTS = "kidattheendpoints";
	protected static final String COL_GAMESET_PARAMETERS_BELGIANBASESTEPPOINTS = "belgianbasesteppoints";
	protected static final String COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDSUCCEEDEDPOINTS = "slamannouncedandsuccededpoints";
	protected static final String COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDFAILEDPOINTS = "slamannouncedandfailedpoints";
	protected static final String COL_GAMESET_PARAMETERS_SLAMNOTANNOUNCEDANDSUCCEEDEDPOINTS = "slamnonannouncedandsuccededpoints";

	/**
	 * Create table t_gameset_parameters statement.
	 */
	protected static final String CREATE_TB_GAMESET_PARAMETERS = 
		"create table " + TABLE_GAMESET_PARAMETERS +
		" (" +
			COL_GAMESET_PARAMETERS_ID + " integer primary key " +
			"," + COL_GAMESET_PARAMETERS_UUID + " text not null " +
			"," + COL_GAMESET_PARAMETERS_PETITERATE + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_PRISERATE + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_GARDERATE + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_GARDESANSRATE + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_GARDECONTRERATE + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_PETITEBASEPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_PRISEBASEPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_GARDEBASEPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_GARDESANSBASEPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_GARDECONTREBASEPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_MISERYPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_HANDFULPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_DOUBLEHANDFULPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_TRIPLEHANDFULPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_KIDATTHEENDPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_BELGIANBASESTEPPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDSUCCEEDEDPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDFAILEDPOINTS + " integer not null " +
			"," + COL_GAMESET_PARAMETERS_SLAMNOTANNOUNCEDANDSUCCEEDEDPOINTS + " integer not null " +
		");";

	/**
	 * Drop table t_gameset_parameters statement.
	 */
	protected static final String DROP_TB_GAMESET_PARAMETERS = "drop table if exists " + TABLE_GAMESET_PARAMETERS;
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GAME
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_GAME = "t_game";
	protected static final String COL_GAME_ID = "id";
	protected static final String COL_GAME_UUID = "uiid";
	protected static final String COL_GAME_GAMESET_ID = "gameset_id";
	protected static final String COL_GAME_INDEX = "indecs";
	protected static final String COL_GAME_TIMESTAMP = "timestamp";
	protected static final String COL_GAME_TYPE = "type";
	protected static final String COL_GAME_DEALER_ID = "dealer_id";
	protected static final String COL_GAME_DEAD1_ID = "dead1_id";
	
	/**
	 * Create table t_game statement.
	 */
	protected static final String CREATE_TB_GAME = 
		"create table " + TABLE_GAME +
		" ( " +
			COL_GAME_ID + " integer primary key, " +
			COL_GAME_UUID + " text not null, " +
			COL_GAME_GAMESET_ID + " not null, " +
			COL_GAME_INDEX + " not null, " +
			COL_GAME_TIMESTAMP + " integer not null, " +
			COL_GAME_TYPE + " not null, " +
			COL_GAME_DEALER_ID  + " integer, " +
			COL_GAME_DEAD1_ID + " integer " +
		");";
		
	/**
	 * Drop table t_game statement.
	 */
	protected static final String DROP_TB_GAME = "drop table if exists " + TABLE_GAME;

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// STANDARD GAME
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_STDGAME = "t_stdgame";
	protected static final String COL_STDGAME_ID = "id";
	protected static final String COL_STDGAME_LEADER_ID = "leader_id";
	protected static final String COL_STDGAME_CALLED_ID = "called_id";
	protected static final String COL_STDGAME_BET = "bet";
	protected static final String COL_STDGAME_KING = "king";
	protected static final String COL_STDGAME_OUDLERS = "oudlers";
	protected static final String COL_STDGAME_POINTS = "points";
	protected static final String COL_STDGAME_HANDFUL = "handful";
	protected static final String COL_STDGAME_DOUBLEHANDFUL = "doublehandful";
	protected static final String COL_STDGAME_TRIPLEHANDFUL = "triplehandful";
	protected static final String COL_STDGAME_KIDATTHEEND = "kidattheend";
	protected static final String COL_STDGAME_MISERY = "misery";
	protected static final String COL_STDGAME_SLAM = "slam";
	
	
	/**
	 * Create table t_stdgame statement.
	 */
	protected static final String CREATE_TB_STDGAME = 
		"create table " + TABLE_STDGAME +
		" ( " +
			COL_STDGAME_ID + " integer primary key, " +
			COL_STDGAME_LEADER_ID + " integer not null, " +
			COL_STDGAME_CALLED_ID + " integer , " +
			COL_STDGAME_BET + " text not null, " +
			COL_STDGAME_KING + " text , " +
			COL_STDGAME_OUDLERS + " integer not null, " +
			COL_STDGAME_POINTS + " points integer not null, " +
			COL_STDGAME_HANDFUL + " text, " +
			COL_STDGAME_DOUBLEHANDFUL + " text, " +
			COL_STDGAME_TRIPLEHANDFUL + " text, " +
			COL_STDGAME_KIDATTHEEND + " text, " +
			COL_STDGAME_MISERY + " integer, " +
			COL_STDGAME_SLAM + " text " +
		");";
	
	/**
	 * Drop table t_stdgame statement.
	 */
	protected static final String DROP_TB_STDGAME = "drop table if exists " + TABLE_STDGAME;

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// BELGIAN GAME
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected static final String TABLE_BELGIANGAME = "t_belgiangame";
	protected static final String COL_BELGIANGAME_ID = "id";
	protected static final String COL_BELGIANGAME_FIRST_ID = "first_id";
	protected static final String COL_BELGIANGAME_SECOND_ID = "second_id";
	protected static final String COL_BELGIANGAME_THIRD_ID = "third_id";
	protected static final String COL_BELGIANGAME_FOURTH_ID = "fourth_id";
	protected static final String COL_BELGIANGAME_FIFTH_ID = "fifth_id";
	
	/**
	 * Create table t_belgiangame statement.
	 */
	protected static final String CREATE_TB_BELGIANGAME = 
		"create table " + TABLE_BELGIANGAME +
		" ( " +
			COL_BELGIANGAME_ID + " integer primary key, " +
			COL_BELGIANGAME_FIRST_ID + " integer not null, " +
			COL_BELGIANGAME_SECOND_ID + " integer not null, " +
			COL_BELGIANGAME_THIRD_ID + " integer not null, " +
			COL_BELGIANGAME_FOURTH_ID + " integer, " +
			COL_BELGIANGAME_FIFTH_ID + " integer " +
		");";

	/**
	 * Drop table t_belgiangame statement.
	 */
	protected static final String DROP_TB_BELGIANGAME = "drop table if exists " + TABLE_BELGIANGAME;
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PENALTY GAME
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_PENALTYGAME = "t_penaltygame";
	protected static final String COL_PENALTYGAME_ID = "id";
	protected static final String COL_PENALTYGAME_PENALTED_ID = "penalted_id";
	protected static final String COL_PENALTYGAME_POINTS = "points";
	protected static final String COL_PENALTYGAME_TYPE = "type";
	
	/**
	 * Create table t_penaltygame statement.
	 */
	protected static final String CREATE_TB_PENALTYGAME = 
		"create table " + TABLE_PENALTYGAME +
		" ( " +
			COL_PENALTYGAME_ID + " integer primary key, " +
			COL_PENALTYGAME_PENALTED_ID + " integer not null, " +
			COL_PENALTYGAME_POINTS + " integer not null, " +
			COL_PENALTYGAME_TYPE + " text " +
		");";

	/**
	 * Drop table t_penaltygame statement.
	 */
	protected static final String DROP_TB_PENALTYGAME = "drop table if exists " + TABLE_PENALTYGAME;
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Links GAME / PLAYERS
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_GAME_PLAYERS = "t_game_players";
	protected static final String COL_GAME_PLAYERS_GAME_ID = "game_id";
	protected static final String COL_GAME_PLAYERS_PLAYER_ID = "player_id";
	
	/**
	 * Create table t_game_players statement.
	 */
	protected static final String CREATE_TB_GAME_PLAYERS = 
		"create table " + TABLE_GAME_PLAYERS +
		" ( " +
			COL_GAME_PLAYERS_GAME_ID + " integer not null, " +
			COL_GAME_PLAYERS_PLAYER_ID + " integer not null, " +
			"primary key (" + COL_GAME_PLAYERS_GAME_ID + "," + COL_GAME_PLAYERS_PLAYER_ID + ")" +
		");";
		
	/**
	 * GamePlayersEntity table drop statement.
	 */
	protected static final String DROP_TB_GAME_PLAYERS = "drop table if exists " + TABLE_GAME_PLAYERS;
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ENTITY TRACKER
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected static final String TABLE_TRACKER = "t_entity_tracker";
	protected static final String COL_TRACKER_ID = "id";
	protected static final String COL_TRACKER_TIMESTAMP = "timestamp";
	protected static final String COL_TRACKER_ENTITY_ID = "entity_id";
	protected static final String COL_TRACKER_ENTITY_TYPE = "entity_type";
	protected static final String COL_TRACKER_ACTION = "action";
			
	/**
	 * Create table t_entity_tracker statement.
	 */
	protected static final String CREATE_TB_TRACKER = 
		"create table " + TABLE_TRACKER +
		" (" +
			COL_TRACKER_ID + " integer primary key, " +
			COL_TRACKER_TIMESTAMP + " integer not null, " +
			COL_TRACKER_ENTITY_ID + " integer not null, " +
			COL_TRACKER_ENTITY_TYPE + " text not null, " +
			COL_TRACKER_ACTION + " text  not null" +
		");";
	
	/**
	 * Drop table t_entity_tracker statement.
	 */
	protected static final String DROP_TB_TRACKER = "drop table if exists " + TABLE_TRACKER;
}
