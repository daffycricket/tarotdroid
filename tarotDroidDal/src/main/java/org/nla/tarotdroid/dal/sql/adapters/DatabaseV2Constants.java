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
public class DatabaseV2Constants {
	
	/**
	 * Prevents from constructing an object.
	 */
	private DatabaseV2Constants() {
	}
	
    /**
     * GameEntity table creation statement.
     */
    protected static final String CREATE_TB_GAMEENTITY = 
            "create table game " +
            "(" +
                    "id text primary key " +
                    ",gameset_id text not null " +
                    ",indecs integer not null " +
                    ",timestamp integer not null " +
                    ",gametype text not null " +
                    ",dealer_id text " +
            ");";
    
    /**
     * Index on table Game, column gameset_id.
     */
    protected static final String CREATE_IDX_GAME_GAMESET_ID = "create index idx_game_gameset_id on game(gameset_id);";
    
    protected static final String DROP_IDX_GAME_GAMESET_ID = "drop index if exists idx_game_gameset_id;";

    /**
     * GameEntity table drop statement.
     */
    protected static final String DROP_TB_GAMEENTITY = "drop table if exists game";
    
    /**
     * GamePlayersEntity table creation statement.
     */
    protected static final String CREATE_TB_GAMEPLAYERSENTITY = 
            "create table game_players" +
            "(" +
                    "game_id text not null " +
                    ",player_id text not null " +
            ");";
    
    /**
     * Index on table game_players, column game_id.
     */
    protected static final String CREATE_IDX_GAMEPLAYERS_GAME_ID = "create index idx_gameplayers_game_id on game_players(game_id);";
    
    protected static final String DROP_IDX_GAMEPLAYERS_GAME_ID = "drop index if exists idx_gameplayers_game_id;";
    
    /**
     * GamePlayersEntity table drop statement.
     */
    protected static final String DROP_TB_GAMEPLAYERSENTITY = "drop table if exists game_players";

    /**
     * GamePlayersEntity table creation statement.
     */
    protected static final String CREATE_TB_GAMEMISCLINKPLAYERSENTITY = 
            "create table game_misclinkplayers" +
            "(" +
                    "game_id text not null " +
                    ",linktype text not null " +
                    ",player_id text not null " +
            ");";
    
    /**
     * Index on table game_misclinkplayers, column game_id.
     */
    protected static final String CREATE_IDX_GAMEMISCLINKPPLAYERS_GAME_ID = "create index idx_gamemiscklinkplayers_game_id on game_misclinkplayers(game_id);";
    
    protected static final String DROP_IDX_GAMEMISCLINKPPLAYERS_GAME_ID =  "drop index if exists idx_gamemiscklinkplayers_game_id;";
    
    /**
     * GameDeadPlayersEntity table drop statement.
     */
    protected static final String DROP_TB_GAMEMISCLINKPLAYERSENTITY = "drop table if exists game_misclinkplayers";
    
    /**
     * GameSetEntity table creation statement.
     */
    protected static final String CREATE_TB_GAMESETENTITY = 
            "create table gameset" +
            "(" +
                    "id text primary key " +
                    ",timestamp integer not null " +
                    ",gamesettype text not null " +
                    ",tournament_id text " +
                    ",miscdata text " +
            ");";

    /**
     * Index on table game_misclinkplayers, column game_id.
     */
    protected static final String CREATE_IDX_GAMESET_TOURNAMENT_ID = "create index idx_gameset_tournament_id on gameset(tournament_id);";
    
    protected static final String DROP_IDX_GAMESET_TOURNAMENT_ID = "drop index if exists idx_gameset_tournament_id;";
    
    /**
     * GameSetEntity table drop statement.
     */
    protected static final String DROP_TB_GAMESETENTITY = "drop table if exists gameset";
    
    /**
     * GameSetPlayersEntity table creation statement.
     */
    protected static final String CREATE_TB_GAMESETPLAYERSENTITY = 
            "create table gameset_players " +
            "( " +
                    "gameset_id text not null " +
                    ",player_id text not null " +
            ");";

    /**
     * Index on table gameset_players, column gameset_id.
     */
    protected static final String CREATE_IDX_GAMESETPLAYERS_GAMESET_ID = "create index idx_gamesetplayers_gameset_id on gameset_players(gameset_id);";
    
    protected static final String DROP_IDX_GAMESETPLAYERS_GAMESET_ID = "drop index if exists idx_gamesetplayers_gameset_id;";
    
    /**
     * GameSetPlayersEntity table drop statement.
     */
    protected static final String DROP_TB_GAMESETPLAYERSENTITY = "drop table if exists gameset_players";
    
    /**
     * GameSetParametersEntity table creation statement.
     */
    protected static final String CREATE_TB_GAMESETPARAMETERSENTITY = 
            "create table parameters" +
            "(" +
                    "id text primary key " +
                    ",priserate integer not null " +
                    ",garderate integer not null " +
                    ",gardesansrate integer not null " +
                    ",gardecontrerate integer not null " +
                    ",prisebasepoints integer not null " +
                    ",gardebasepoints integer not null " +
                    ",gardesansbasepoints integer not null " +
                    ",gardecontrebasepoints integer not null " +
                    ",miserypoints integer not null " +
                    ",poigneepoints integer not null " +
                    ",doublepoigneepoints integer not null " +
                    ",triplepoigneepoints integer not null " +
                    ",kidattheendpoints integer not null " +
                    ",belgianbasesteppoints integer not null " +
                    ",chelemannouncedandsuccededpoints integer not null " +
                    ",chelemannouncedandfailedpoints integer not null " +
                    ",chelemnonannouncedandsuccededpoints integer not null " +
            ");";

    /**
     * GameSetParametersEntity table drop statement.
     */
    protected static final String DROP_TB_GAMESETPARAMETERSENTITY = "drop table if exists parameters";
    
    /**
     * PlayerEntity table creation statement.
     */
    protected static final String CREATE_TB_PLAYERENTITY = 
            "create table player" +
            "(" +
                    "id text primary key " +
                    ",name text not null " +
            ");";

    /**
     * Index on table player, column name.
     */
    protected static final String CREATE_IDX_PLAYER_NAME = "create index idx_player_name on player(name);";
    
    protected static final String DROP_IDX_PLAYER_NAME = "drop index if exists idx_player_name;";

    /**
     * PlayerEntity table drop statement.
     */
    protected static final String DROP_TB_PLAYERENTITY = "drop table if exists player";
    
    /**
     * StandardTarot5GameEntity table creation statement.
     */
    protected static final String CREATE_TB_STANDARDTAROT5GAMEENTITY = 
            "create table std5game" +
            "(" +
                    "id text primary key " +
                    ",leader_id integer not null " +
                    ",called_id integer not null " +
                    ",bet text not null " +
                    ",calledking text not null " +
                    ",oudlers integer not null " +
                    ",points integer not null " +
                    ",kidattheend_id text " +
                    ",chelemtype text " +
            ");";

    /**
     * StandardTarot5GameEntity table drop statement.
     */
    protected static final String DROP_TB_STANDARDTAROT5GAMEENTITY = "drop table if exists std5game";
    
    /**
     * StandardTarot34GameEntity table creation statement.
     */
    protected static final String CREATE_TB_STANDARDTAROTGAMEENTITY = 
            "create table stdgame" +
            "(" +
                    "id text primary key " +
                    ",leader_id text " +
                    ",bet text  " +
                    ",oudlers integer  " +
                    ",points integer  " +
                    ",kidattheend_id text " +
                    ",chelemtype text " +
            ");";

    /**
     * StandardTarot34GameEntity table drop statement.
     */
    protected static final String DROP_TB_STANDARDTAROTGAMEENTITY = "drop table if exists stdgame";

    /**
     * BelgianTarot4GameEntity table creation statement.
     */
    protected static final String CREATE_TB_BELGIANTAROT3GAMEENTITY = 
            "create table belgian3game" +
            "(" +
                    "id text primary key " +
                    ",first_id text " +
                    ",second_id text " +
                    ",third_id text " +
            ");";

    /**
     * BelgianTarot4GameEntity table drop statement.
     */
    protected static final String DROP_TB_BELGIANTAROT3GAMEENTITY = "drop table if exists belgian3game";
    
    /**
     * BelgianTarot4GameEntity table creation statement.
     */
    protected static final String CREATE_TB_BELGIANTAROT4GAMEENTITY = 
            "create table belgian4game" +
            "(" +
                    "id text primary key " +
                    ",first_id text " +
                    ",second_id text " +
                    ",third_id text " +
                    ",fourth_id text " +
            ");";

    /**
     * BelgianTarot4GameEntity table drop statement.
     */
    protected static final String DROP_TB_BELGIANTAROT4GAMEENTITY = "drop table if exists belgian4game";
    
    /**
     * BelgianTarot4GameEntity table creation statement.
     */
    protected static final String CREATE_TB_BELGIANTAROT5GAMEENTITY = 
            "create table belgian5game" +
            "(" +
                    "id text primary key " +
                    ",first_id text " +
                    ",second_id text " +
                    ",third_id text " +
                    ",fourth_id text " +
                    ",fifth_id text " +
            ");";

    /**
     * BelgianTarot4GameEntity table drop statement.
     */
    protected static final String DROP_TB_BELGIANTAROT5GAMEENTITY = "drop table if exists belgian5game";
    
    /**
     * Update n°1 : add column KIDATTHEENDTYPE to tables stdgame and std5game statements. 
     */
    protected static final String ALTER_TB_STANDARDTAROTGAME_ADD_KIDATTHEENDTYPE_COLUMN = "alter table stdgame add column kidattheendtype text;";
    protected static final String ALTER_TB_STANDARDTAROT5GAME_ADD_KIDATTHEENDTYPE_COLUMN = "alter table std5game add column kidattheendtype text;";

    /**
     * Update n°2 : add columns poignee_teamtype, dblpoignee_teamtype and tplpoignee_teamtype to tables stdgame and std5game statements. 
     */
    protected static final String ALTER_TB_STANDARDTAROTGAME_ADD_POIGNEE_TEAMTYPE_COLUMN = "alter table stdgame add column poignee_teamtype text;";
    protected static final String ALTER_TB_STANDARDTAROTGAME_ADD_DBLPOIGNEE_TEAMTYPE_COLUMN = "alter table stdgame add column dblpoignee_teamtype text;";
    protected static final String ALTER_TB_STANDARDTAROTGAME_ADD_TPLPOIGNEE_TEAMTYPE_COLUMN = "alter table stdgame add column tplpoignee_teamtype text;";
    
    protected static final String ALTER_TB_STANDARDTAROT5GAME_ADD_POIGNEE_TEAMTYPE_COLUMN = "alter table std5game add column poignee_teamtype text;";
    protected static final String ALTER_TB_STANDARDTAROT5GAME_ADD_DBLPOIGNEE_TEAMTYPE_COLUMN = "alter table std5game add column dblpoignee_teamtype text;";
    protected static final String ALTER_TB_STANDARDTAROT5GAME_ADD_TPLPOIGNEE_TEAMTYPE_COLUMN = "alter table std5game add column tplpoignee_teamtype text;";   
    
    /**
     * Update n°3 : add column db_version to table gameset statement. 
     */
    protected static final String ALTER_TB_GAMESET_ADD_DBVERSION_COLUMN = "alter table gameset add column db_version integer;";

}
