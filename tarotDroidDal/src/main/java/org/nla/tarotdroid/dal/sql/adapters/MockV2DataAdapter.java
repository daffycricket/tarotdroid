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

import java.util.Map;
import java.util.UUID;

import org.nla.tarotdroid.biz.enums.ChelemType;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.biz.enums.TeamType;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class MockV2DataAdapter extends BaseAdapter {
	
	/**
	 * Map between new player ids (long) and former game uuids (text). 
	 */
	private Map<String, String> mapPlayerIds;

	/**
	 * Constructs a DatabaseV2ToV3Adapter instance.
	 * @param context
	 */
	protected MockV2DataAdapter(final SQLiteDatabase database) {
		super(database);
		this.mapPlayerIds = newHashMap();
	}
	
	/**
	 * Executes the data transfer between v2 to v3 schemas.
	 */
	protected void execute() {
		this.createPlayers();
		this.createTarot3GameSetWithoutDeadPlayer();
		this.createTarot3GameSetWithDeadPlayer();
		this.createTarot4GameSetWithoutDeadPlayer();
		this.createTarot4GameSetWithtDeadPlayer();
		this.createTarot5GameSetWithoutDeadPlayer();
		this.createTarot5GameSetWithtDeadPlayer();
	}

	/**
	 * Create mock players.
	 */
	private void createPlayers() {
		String[] playerNames = {"Arthur", "NicoL", "NicoR", "Gui", "PM", "Cyril", "FloM", "FloV", "Damien", "Cyril", "Alex", "Carole", "JK", "Mika", "Ludas", "Jimmy", "Vince", "Valentin"};
		
		try {
			for (String playerName : playerNames) {
				String uuid = UUID.randomUUID().toString();
				
				ContentValues newValues = new ContentValues();
				newValues.put("id", uuid);
				newValues.put("name", playerName);
				
				this.database.insertOrThrow("player", null, newValues);
				this.mapPlayerIds.put(playerName, uuid);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create mock tarot 3 gameset.
	 */
	private void createTarot3GameSetWithoutDeadPlayer() {
		try {
			// game set creation
			String gameSetUuid = UUID.randomUUID().toString();
			ContentValues newValues = new ContentValues();
			newValues.put("id", gameSetUuid);
			newValues.put("timestamp", System.currentTimeMillis());
			newValues.put("gamesettype", GameStyleType.Tarot3.toString());
			this.database.insertOrThrow("gameset", null, newValues);
			
			// players association
			this.createGameSetPlayers(gameSetUuid, "Arthur", "NicoL", "Gui");
			
			// parameters
			this.createGameSetParameters(gameSetUuid);
			
			// game 1 : standard
			this.createStandard3Game(gameSetUuid, 1, BetType.Garde, null, null, null, "Arthur", "NicoL", "Gui");

			// game 2 : standard
			this.createStandard3Game(gameSetUuid, 2, BetType.Prise, TeamType.DefenseTeam, TeamType.LeadingTeam, ChelemType.AnnouncedAndSucceeded, "Arthur", "NicoL", "Gui");
			
			// game 3 : belgian
			this.createBelgian3Game(gameSetUuid, 2, "Arthur", "NicoL", "Gui");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create mock tarot 3 gameset with dead player.
	 */
	private void createTarot3GameSetWithDeadPlayer() {
		try {
			// game set creation
			String gameSetUuid = UUID.randomUUID().toString();
			ContentValues newValues = new ContentValues();
			newValues.put("id", gameSetUuid);
			newValues.put("timestamp", System.currentTimeMillis());
			newValues.put("gamesettype", GameStyleType.Tarot3.toString());
			this.database.insertOrThrow("gameset", null, newValues);
			
			// players association
			this.createGameSetPlayers(gameSetUuid, "Arthur", "NicoL", "Gui", "NicoR");
			
			// parameters
			this.createGameSetParameters(gameSetUuid);
			
			// game 1 : standard
			this.createStandard3Game(gameSetUuid, 1, BetType.Garde, TeamType.LeadingTeam, null, null, "Arthur", "NicoL", "Gui", "NicoR");

			// game 2 : standard
			this.createStandard3Game(gameSetUuid, 2, BetType.Prise, TeamType.DefenseTeam, TeamType.LeadingTeam, null, "NicoR", "Arthur", "NicoL", "Gui");
			
			// game 3 : belgian
			this.createBelgian3Game(gameSetUuid, 2, "Gui", "NicoR", "Arthur", "NicoL");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create mock tarot 4 gameset.
	 */
	private void createTarot4GameSetWithoutDeadPlayer() {
		try {
			// game set creation
			String gameSetUuid = UUID.randomUUID().toString();
			ContentValues newValues = new ContentValues();
			newValues.put("id", gameSetUuid);
			newValues.put("timestamp", System.currentTimeMillis());
			newValues.put("gamesettype", GameStyleType.Tarot4.toString());
			this.database.insertOrThrow("gameset", null, newValues);
			
			// players association
			this.createGameSetPlayers(gameSetUuid, "Arthur", "NicoL", "Gui", "NicoR");
			
			// parameters
			this.createGameSetParameters(gameSetUuid);
			
			// game 1 : standard
			this.createStandard4Game(gameSetUuid, 1, BetType.GardeSans, TeamType.LeadingTeam, TeamType.DefenseTeam, ChelemType.AnnouncedAndFailed, "Arthur", "NicoL", "Gui", "NicoR");

			// game 2 : standard
			this.createStandard4Game(gameSetUuid, 2, BetType.GardeContre, TeamType.LeadingTeam, TeamType.LeadingTeam, ChelemType.NotAnnouncedButSucceeded, "Arthur", "NicoL", "Gui", "NicoR");
			
			// game 3 : belgian
			this.createBelgian4Game(gameSetUuid, 2, "Arthur", "NicoL", "Gui", "NicoR");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create mock tarot 4 gameset with dead players.
	 */
	private void createTarot4GameSetWithtDeadPlayer() {
		try {
			// game set creation
			String gameSetUuid = UUID.randomUUID().toString();
			ContentValues newValues = new ContentValues();
			newValues.put("id", gameSetUuid);
			newValues.put("timestamp", System.currentTimeMillis());
			newValues.put("gamesettype", GameStyleType.Tarot4.toString());
			this.database.insertOrThrow("gameset", null, newValues);
			
			// players association
			this.createGameSetPlayers(gameSetUuid, "Arthur", "NicoL", "Gui", "NicoR", "Cyril");
			
			// parameters
			this.createGameSetParameters(gameSetUuid);
			
			// game 1 : standard
			this.createStandard4Game(gameSetUuid, 1, BetType.GardeSans, TeamType.LeadingTeam, null, ChelemType.AnnouncedAndFailed, "Arthur", "NicoL", "Gui", "NicoR", "Cyril");

			// game 2 : standard
			this.createStandard4Game(gameSetUuid, 2, BetType.GardeContre, null, TeamType.LeadingTeam, null, "Cyril", "Arthur", "NicoL", "Gui", "NicoR");
			
			// game 3 : belgian
			this.createBelgian4Game(gameSetUuid, 2, "NicoR", "Cyril", "Arthur", "NicoL", "Gui");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create mock tarot 5 gameset.
	 */
	private void createTarot5GameSetWithoutDeadPlayer() {
		try {
			// game set creation
			String gameSetUuid = UUID.randomUUID().toString();
			ContentValues newValues = new ContentValues();
			newValues.put("id", gameSetUuid);
			newValues.put("timestamp", System.currentTimeMillis());
			newValues.put("gamesettype", GameStyleType.Tarot5.toString());
			this.database.insertOrThrow("gameset", null, newValues);
			
			// players association
			this.createGameSetPlayers(gameSetUuid, "Arthur", "NicoL", "Gui", "NicoR", "Cyril");
			
			// parameters
			this.createGameSetParameters(gameSetUuid);
			
			// game 1 : standard
			this.createStandard5Game(gameSetUuid, 1, null, KingType.Diamonds, BetType.GardeSans, null, TeamType.DefenseTeam, null, "Arthur", "NicoL", "Gui", "NicoR", "Cyril");

			// game 2 : standard
			this.createStandard5Game(gameSetUuid, 2, "NicoL", KingType.Clubs, BetType.GardeContre, TeamType.LeadingTeam, TeamType.LeadingTeam, ChelemType.NotAnnouncedButSucceeded, "Arthur", "NicoL", "Gui", "NicoR", "Cyril");
			
			// game 3 : belgian
			this.createBelgian5Game(gameSetUuid, 2, "Arthur", "NicoL", "Gui", "NicoR", "Cyril");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create mock tarot 5 gameset with dead players.
	 */
	private void createTarot5GameSetWithtDeadPlayer() {
		try {
			// game set creation
			String gameSetUuid = UUID.randomUUID().toString();
			ContentValues newValues = new ContentValues();
			newValues.put("id", gameSetUuid);
			newValues.put("timestamp", System.currentTimeMillis());
			newValues.put("gamesettype", GameStyleType.Tarot5.toString());
			this.database.insertOrThrow("gameset", null, newValues);
			
			// players association
			this.createGameSetPlayers(gameSetUuid, "Arthur", "NicoL", "Gui", "NicoR", "Cyril", "Ludas");
			
			// parameters
			this.createGameSetParameters(gameSetUuid);
			
			// game 1 : standard
			this.createStandard5Game(gameSetUuid, 1, "Gui", KingType.Hearts, BetType.GardeSans, TeamType.LeadingTeam, null, ChelemType.AnnouncedAndFailed, "Arthur", "NicoL", "Gui", "NicoR", "Cyril", "Ludas");

			// game 2 : standard
			this.createStandard5Game(gameSetUuid, 2, "Gui", KingType.Spades, BetType.GardeContre, null, TeamType.LeadingTeam, null, "Ludas", "Arthur", "NicoL", "Gui", "NicoR", "Cyril");
			
			// game 3 : belgian
			this.createBelgian5Game(gameSetUuid, 2, "Cyril", "Ludas", "Arthur", "NicoL", "Gui", "NicoR");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the gameset players association    
	 * @param gameSetUuid
	 * @param playerNames
	 */
	private void createGameSetPlayers(final String gameSetUuid, String... playerNames) {
		for (String playerName : playerNames) {
			ContentValues playerValues = new ContentValues();
			playerValues.put("gameset_id", gameSetUuid);
			playerValues.put("player_id", this.mapPlayerIds.get(playerName));
			this.database.insertOrThrow("gameset_players", null, playerValues);
		}
	}
	
	/**
	 * Creates the game players association    
	 * @param gameUuid
	 * @param playerNames
	 */
	private void createGamePlayers(final String gameUuid, String... playerNames) {
		for (String playerName : playerNames) {
			ContentValues playerValues = new ContentValues();
			playerValues.put("game_id", gameUuid);
			playerValues.put("player_id", this.mapPlayerIds.get(playerName));
			this.database.insertOrThrow("game_players", null, playerValues);
		}
	}
	
	/**
	 * Creates a dead player link.
	 * @param game1Uuid
	 * @param string
	 */
	private void createDeadPlayerLink(String gameUuid, String playerName) {
		ContentValues linkValues = new ContentValues();
		linkValues.put("game_id", gameUuid);
		linkValues.put("linktype", "dead");
		linkValues.put("player_id", this.mapPlayerIds.get(playerName));
		this.database.insertOrThrow("game_misclinkplayers", null, linkValues);
	}
	
	/**
	 * Creates a misery player link.
	 * @param game1Uuid
	 * @param string
	 */
	private void createMiseryPlayerLink(String gameUuid, String playerName) {
		ContentValues linkValues = new ContentValues();
		linkValues.put("game_id", gameUuid);
		linkValues.put("linktype", "misery");
		linkValues.put("player_id", this.mapPlayerIds.get(playerName));
		this.database.insertOrThrow("game_misclinkplayers", null, linkValues);
	}
	
	/**
	 * @param gameSetUuid
	 * @param index
	 * @param betType
	 * @param kidAtTheEndType
	 * @param handfulTeamType
	 * @param chelemType
	 * @param playerNames
	 */
	private void createStandard3Game(final String gameSetUuid, int index, BetType betType, TeamType kidAtTheEndType, TeamType handfulTeamType, ChelemType chelemType, String... playerNames) {
		String game1Uuid = UUID.randomUUID().toString();
		ContentValues game1Values = new ContentValues();
		game1Values.put("id", game1Uuid);
		game1Values.put("gameset_id", gameSetUuid);
		game1Values.put("indecs", index);
		game1Values.put("timestamp", System.currentTimeMillis());
		game1Values.put("gametype", "std3game");
		
		ContentValues stdGame1Values = new ContentValues();
		stdGame1Values.put("id", game1Uuid);
		stdGame1Values.put("leader_id", this.mapPlayerIds.get(playerNames[0]));
		stdGame1Values.put("bet", betType.toString());
		stdGame1Values.put("oudlers", 2);
		stdGame1Values.put("points", 50);
		if (handfulTeamType != null) {
			stdGame1Values.put("poignee_teamtype", handfulTeamType.toString());
		}
		if (kidAtTheEndType != null) {
			stdGame1Values.put("kidattheendtype", kidAtTheEndType.toString());
		}
		if (chelemType != null) {
			stdGame1Values.put("chelemtype", chelemType.toString());
		}
		this.database.insertOrThrow("game", null, game1Values);
		this.database.insertOrThrow("stdgame", null, stdGame1Values);
		
		this.createGamePlayers(game1Uuid, playerNames);
		
		if (playerNames.length == 4) {
			this.createDeadPlayerLink(game1Uuid, playerNames[3]);
		}
	}

	/**
	 * @param gameSetUuid
	 * @param index
	 * @param playerNames
	 */
	private void createBelgian3Game(final String gameSetUuid, int index, String... playerNames) {
		String gameUuid = UUID.randomUUID().toString();
		ContentValues gameValues = new ContentValues();
		gameValues.put("id", gameUuid);
		gameValues.put("gameset_id", gameSetUuid);
		gameValues.put("indecs", index);
		gameValues.put("timestamp", System.currentTimeMillis());
		gameValues.put("gametype", "belgian3game");
		
		ContentValues belgianGameValues = new ContentValues();
		belgianGameValues.put("id", gameUuid);
		belgianGameValues.put("first_id", this.mapPlayerIds.get(playerNames[0]));
		belgianGameValues.put("second_id", this.mapPlayerIds.get(playerNames[1]));
		belgianGameValues.put("third_id", this.mapPlayerIds.get(playerNames[2]));
		
		this.database.insertOrThrow("game", null, gameValues);
		this.database.insertOrThrow("belgian3game", null, belgianGameValues);
		
		this.createGamePlayers(gameUuid, playerNames);
		
		if (playerNames.length == 4) {
			this.createDeadPlayerLink(gameUuid, playerNames[3]);
		}
	}
	
	/**
	 * @param gameSetUuid
	 * @param index
	 * @param betType
	 * @param kidAtTheEndType
	 * @param handfulTeamType
	 * @param chelemType
	 * @param playerNames
	 */
	private void createStandard4Game(final String gameSetUuid, int index, BetType betType, TeamType kidAtTheEndType, TeamType handfulTeamType, ChelemType chelemType, String... playerNames) {
		String game1Uuid = UUID.randomUUID().toString();
		ContentValues game1Values = new ContentValues();
		game1Values.put("id", game1Uuid);
		game1Values.put("gameset_id", gameSetUuid);
		game1Values.put("indecs", index);
		game1Values.put("timestamp", System.currentTimeMillis());
		game1Values.put("gametype", "stdgame");
		
		ContentValues stdGame1Values = new ContentValues();
		stdGame1Values.put("id", game1Uuid);
		stdGame1Values.put("leader_id", this.mapPlayerIds.get(playerNames[0]));
		stdGame1Values.put("bet", betType.toString());
		stdGame1Values.put("oudlers", 2);
		stdGame1Values.put("points", 50);
		if (handfulTeamType != null) {
			stdGame1Values.put("dblpoignee_teamtype", handfulTeamType.toString());
		}
		if (kidAtTheEndType != null) {
			stdGame1Values.put("kidattheendtype", kidAtTheEndType.toString());
		}
		if (chelemType != null) {
			stdGame1Values.put("chelemtype", chelemType.toString());
		}
		
		this.database.insertOrThrow("game", null, game1Values);
		this.database.insertOrThrow("stdgame", null, stdGame1Values);
		
		this.createGamePlayers(game1Uuid, playerNames);
		
		if (playerNames.length == 5) {
			this.createDeadPlayerLink(game1Uuid, playerNames[4]);
		}
	}

	/**
	 * @param gameSetUuid
	 * @param index
	 * @param playerNames
	 */
	private void createBelgian4Game(final String gameSetUuid, int index, String... playerNames) {
		String gameUuid = UUID.randomUUID().toString();
		ContentValues gameValues = new ContentValues();
		gameValues.put("id", gameUuid);
		gameValues.put("gameset_id", gameSetUuid);
		gameValues.put("indecs", index);
		gameValues.put("timestamp", System.currentTimeMillis());
		gameValues.put("gametype", "belgian4game");
		
		ContentValues belgianGameValues = new ContentValues();
		belgianGameValues.put("id", gameUuid);
		belgianGameValues.put("first_id", this.mapPlayerIds.get(playerNames[0]));
		belgianGameValues.put("second_id", this.mapPlayerIds.get(playerNames[1]));
		belgianGameValues.put("third_id", this.mapPlayerIds.get(playerNames[2]));
		belgianGameValues.put("fourth_id", this.mapPlayerIds.get(playerNames[3]));
		
		this.database.insertOrThrow("game", null, gameValues);
		this.database.insertOrThrow("belgian4game", null, belgianGameValues);
		
		this.createGamePlayers(gameUuid, playerNames);
		
		if (playerNames.length == 5) {
			this.createDeadPlayerLink(gameUuid, playerNames[4]);
		}
	}
	
	/**
	 * @param gameSetUuid
	 * @param index
	 * @param betType
	 * @param kidAtTheEndType
	 * @param handfulTeamType
	 * @param chelemType
	 * @param playerNames
	 */
	private void createStandard5Game(final String gameSetUuid, int index, String playerWithMisery, KingType kingType, BetType betType, TeamType kidAtTheEndType, TeamType handfulTeamType, ChelemType chelemType, String... playerNames) {
		String game1Uuid = UUID.randomUUID().toString();
		ContentValues game1Values = new ContentValues();
		game1Values.put("id", game1Uuid);
		game1Values.put("gameset_id", gameSetUuid);
		game1Values.put("indecs", index);
		game1Values.put("timestamp", System.currentTimeMillis());
		game1Values.put("gametype", "std5game");
		
		ContentValues stdGame1Values = new ContentValues();
		stdGame1Values.put("id", game1Uuid);
		stdGame1Values.put("leader_id", this.mapPlayerIds.get(playerNames[0]));
		stdGame1Values.put("calledking", kingType.toString());
		stdGame1Values.put("called_id", this.mapPlayerIds.get(playerNames[2]));
		stdGame1Values.put("bet", betType.toString());
		stdGame1Values.put("oudlers", 2);
		stdGame1Values.put("points", 50);
		if (handfulTeamType != null) {
			stdGame1Values.put("tplpoignee_teamtype", handfulTeamType.toString());
		}
		if (kidAtTheEndType != null) {
			stdGame1Values.put("kidattheendtype", kidAtTheEndType.toString());
		}
		if (chelemType != null) {
			stdGame1Values.put("chelemtype", chelemType.toString());
		}
		
		this.database.insertOrThrow("game", null, game1Values);
		this.database.insertOrThrow("std5game", null, stdGame1Values);
		
		this.createGamePlayers(game1Uuid, playerNames);
		
		if (playerNames.length == 6) {
			this.createDeadPlayerLink(game1Uuid, playerNames[5]);
		}
		
		if (playerWithMisery != null) {
			this.createMiseryPlayerLink(game1Uuid, playerWithMisery);
		}
	}

	/**
	 * @param gameSetUuid
	 * @param index
	 * @param playerNames
	 */
	private void createBelgian5Game(final String gameSetUuid, int index, String... playerNames) {
		String gameUuid = UUID.randomUUID().toString();
		ContentValues gameValues = new ContentValues();
		gameValues.put("id", gameUuid);
		gameValues.put("gameset_id", gameSetUuid);
		gameValues.put("indecs", index);
		gameValues.put("timestamp", System.currentTimeMillis());
		gameValues.put("gametype", "belgian5game");
		
		ContentValues belgianGameValues = new ContentValues();
		belgianGameValues.put("id", gameUuid);
		belgianGameValues.put("first_id", this.mapPlayerIds.get(playerNames[0]));
		belgianGameValues.put("second_id", this.mapPlayerIds.get(playerNames[1]));
		belgianGameValues.put("third_id", this.mapPlayerIds.get(playerNames[2]));
		belgianGameValues.put("fourth_id", this.mapPlayerIds.get(playerNames[3]));
		belgianGameValues.put("fifth_id", this.mapPlayerIds.get(playerNames[4]));
		
		this.database.insertOrThrow("game", null, gameValues);
		this.database.insertOrThrow("belgian5game", null, belgianGameValues);
		
		this.createGamePlayers(gameUuid, playerNames);
		
		if (playerNames.length == 6) {
			this.createDeadPlayerLink(gameUuid, playerNames[5]);
		}
	}
	
	/**
	 * Create mock game set parameters for given game set id.
	 */
	private void createGameSetParameters(final String gameSetUuid) {
		try {
			// game set creation
			
			ContentValues newParametersValues = new ContentValues();
			newParametersValues.put("id", gameSetUuid);
			newParametersValues.put("priserate", 1);
			newParametersValues.put("garderate", 2);
			newParametersValues.put("gardesansrate", 4);
			newParametersValues.put("gardecontrerate", 6);
			newParametersValues.put("prisebasepoints", 25);
			newParametersValues.put("gardebasepoints", 50);
			newParametersValues.put("gardesansbasepoints", 100);
			newParametersValues.put("gardecontrebasepoints", 150);
			newParametersValues.put("miserypoints", 10);
			newParametersValues.put("poigneepoints", 20);
			newParametersValues.put("doublepoigneepoints", 30);
			newParametersValues.put("triplepoigneepoints", 40);
			newParametersValues.put("kidattheendpoints", 20);
			newParametersValues.put("belgianbasesteppoints", 100);
			newParametersValues.put("chelemannouncedandsuccededpoints", 240);
			newParametersValues.put("chelemannouncedandfailedpoints", -240);
			newParametersValues.put("chelemnonannouncedandsuccededpoints", 120);
			this.database.insertOrThrow("parameters", null, newParametersValues);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.sql.adapters.BaseAdapter#clear()
	 */
	@Override
	public void clear() {
	}
}