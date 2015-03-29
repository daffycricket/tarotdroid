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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.dal.DalException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class PlayerDatabaseAdapter extends BaseAdapter {
	
	/**
	 * SQL Query to get all the players in the repository.
	 */
	private static final String GetAllPlayersQuery = MessageFormat.format("select * from {0}", DatabaseV5Constants.TABLE_PLAYER);
	
	/**
	 * SQL Query to get all the players in game sets.
	 */
	private static final String GetPlayersInGameSetsQuery = MessageFormat.format("select {1}, {2} from {0}", DatabaseV5Constants.TABLE_GAMESET_PLAYERS, DatabaseV5Constants.COL_GAMESET_PLAYERS_GAMESET_ID, DatabaseV5Constants.COL_GAMESET_PLAYERS_PLAYER_ID); 
	
	/**
	 * SQL Query to get all the players in games.
	 */
	private static final String GetPlayersInGamesQuery = MessageFormat.format("select {1}, {2} from {0}", DatabaseV5Constants.TABLE_GAME_PLAYERS, DatabaseV5Constants.COL_GAME_PLAYERS_GAME_ID, DatabaseV5Constants.COL_GAME_PLAYERS_PLAYER_ID);
	
	/**
	 * Cache of all the players in the repository.
	 */
	private Map<Long, Player> playersByIdInRepository;
	
	/**
	 * Cache of all the players in the repository.
	 */
	private Map<String, Player> playersByUuidInRepository;
	
	/**
	 * Cache of all the games players in the repository.
	 */
	private Map<Long, PlayerList> gamesPlayerListsInRepository;
	
	/**
	 * Cache of all the gamesets players in the repository.
	 */
	private Map<Long, PlayerList> gameSetsPlayerListsInRepository;
	
	/**
	 * Constructs a PlayerDatabaseAdapter.
	 * @param database
	 */
	public PlayerDatabaseAdapter(final SQLiteDatabase database) {
		super(database);
		this.loadPlayersFromRepository();
		this.loadGameSetsPlayerListsFromRepository();
		this.loadGamesPlayerListsFromRepository();
	}
	
	/**
	 * @return the playersInRepository
	 */
	public Map<Long, Player> getPlayersByIdInRepository() {
		return this.playersByIdInRepository;
	}

	/**
	 * @return the playersInRepository
	 */
	public Collection<Player> getPlayersInRepository() {
		return this.playersByIdInRepository.values();
	}

	/**
	 * @return the gamesPlayerListsInRepository
	 */
	public Map<Long, PlayerList> getGamesPlayerListsInRepository() {
		return this.gamesPlayerListsInRepository;
	}

	/**
	 * @return the gameSetsPlayerListsInRepository
	 */
	public Map<Long, PlayerList> getGameSetsPlayerListsInRepository() {
		return this.gameSetsPlayerListsInRepository;
	}

	/**
	 * Persists a new Player.
	 * @param entity The Player to persist.
	 */
	public void storePlayer(final Player player) {
		ContentValues initialValues = this.createPlayerContentValues(player);
		long id = this.database.insertOrThrow(DatabaseV5Constants.TABLE_PLAYER, null, initialValues);
		player.setId(id);
		this.playersByIdInRepository.put(player.getId(), player);
		this.playersByUuidInRepository.put(player.getUuid(), player);
	}
	
	/**
	 * Updates a player that was previously persisted.
	 * @param entity The Player to update.
	 */
	public void updatePlayer(final Player player) {
		ContentValues updatedValues = this.createPlayerContentValues(player);		
		this.database.update(DatabaseV5Constants.TABLE_PLAYER, updatedValues, DatabaseV5Constants.COL_PLAYER_ID + "=?", new String[]{ String.valueOf(player.getId()) });
		this.playersByIdInRepository.put(player.getId(), player);
		this.playersByUuidInRepository.put(player.getUuid(), player);
	}
	
	/**
	 * Updates a player after he was persisted on cloud.
	 */
	public void updatePlayerAfterSync(final Player player, final String newUuid) throws DalException {
		long currentPlayerId = player.getId();

		// update player table
		try {
			ContentValues updatedPlayerValues = new ContentValues();
			updatedPlayerValues.put(DatabaseV5Constants.COL_PLAYER_SYNC_TIMESTAMP, player.getSyncTimestamp().getTime());
			updatedPlayerValues.put(DatabaseV5Constants.COL_PLAYER_SYNC_ACCOUNT, player.getSyncAccount());
			if (newUuid != null) {
				updatedPlayerValues.put(DatabaseV5Constants.COL_PLAYER_UUID, newUuid);
			}
			this.database.update(DatabaseV5Constants.TABLE_PLAYER, updatedPlayerValues, DatabaseV5Constants.COL_PLAYER_ID + "=?", new String[]{ String.valueOf(currentPlayerId) });
			
			// update internal cache if update succeeded
			if (newUuid != null) {
				playersByUuidInRepository.remove(player.getUuid());
				player.setUuid(newUuid);
				playersByUuidInRepository.put(newUuid, player);
			}
		}
		catch (Exception e) {
			throw new DalException("Problem when updating player sync data on table " + DatabaseV5Constants.TABLE_PLAYER, e);
		}
	}
	
	/**
	 * Persists a new GamePlayers.
	 * @param entity The GamePlayersEntity to persist.
	 */
	public void storeGamePlayersEntity(final GamePlayers entity) {
		ContentValues initialValues = this.createGamePlayersContentValues(entity);
		this.database.insertOrThrow(DatabaseV5Constants.TABLE_GAME_PLAYERS, null, initialValues);
		this.cachePlayerInGamesPlayersList(entity.getGameId(), this.playersByIdInRepository.get(entity.getPlayerId()));
	}
	
	/**
	 * Persists a new GameSetPlayers.
	 * @param entity The GameSetPlayers to persist.
	 */
	public void storeGameSetPlayersEntity(final GameSetPlayers entity) {
		ContentValues initialValues = this.createGameSetPlayersContentValues(entity);
		this.database.insertOrThrow(DatabaseV5Constants.TABLE_GAMESET_PLAYERS, null, initialValues);
		this.cachePlayerInGameSetsPlayersList(entity.getGameSetId(), this.playersByIdInRepository.get(entity.getPlayerId()));
	}
	
	/**
	 * Reads and caches all players in the repository.
	 */
	private void loadPlayersFromRepository() {
		this.playersByIdInRepository = newHashMap();
		this.playersByUuidInRepository = newHashMap();

		Cursor dataSource = this.database.rawQuery(GetAllPlayersQuery, null);
		while(dataSource.moveToNext()) {
			Player player = this.fetchFromCursor(dataSource);
			this.playersByIdInRepository.put(player.getId(), player);
			this.playersByUuidInRepository.put(player.getUuid(), player);
		}
		dataSource.close();
	}
	
	/**
	 * Reads and caches all games PlayerLists in the repository.
	 */
	private void loadGamesPlayerListsFromRepository() {
		this.gamesPlayerListsInRepository = newHashMap();
		Cursor dataSource = this.database.rawQuery(GetPlayersInGamesQuery, null);
		while(dataSource.moveToNext()) {
			long gameId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_PLAYERS_GAME_ID));
			Player player = this.playersByIdInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_PLAYERS_PLAYER_ID)));
			
			this.cachePlayerInGamesPlayersList(gameId, player);
		}
		dataSource.close();
	}
	
	/**
	 * Reads and caches all GameSets PlayerLists in the repository.
	 */
	private void loadGameSetsPlayerListsFromRepository() {
		this.gameSetsPlayerListsInRepository = newHashMap();
		Cursor dataSource = this.database.rawQuery(GetPlayersInGameSetsQuery, null);
		while(dataSource.moveToNext()) {
			long gameSetId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_PLAYERS_GAMESET_ID));
			Player player = this.playersByIdInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAMESET_PLAYERS_PLAYER_ID)));
			
			this.cachePlayerInGameSetsPlayersList(gameSetId, player);
		}
		dataSource.close();
	}
	
	/**
	 * Caches a player in a game player list.
	 * @param gameUUID
	 * @param player
	 */
	private void cachePlayerInGamesPlayersList(final long gameId, final Player player) {
		if (this.gamesPlayerListsInRepository.containsKey(gameId)) {
			PlayerList list = this.gamesPlayerListsInRepository.get(gameId);
			list.add(player);
		}
		else {
			PlayerList list = new PlayerList();
			list.add(player);
			this.gamesPlayerListsInRepository.put(gameId, list);
		}
	}
	
	/**
	 * Caches a player in a game set player list.
	 * @param gameUUID
	 * @param player
	 */
	private void cachePlayerInGameSetsPlayersList(final long gameSetId, Player player) {
		if (this.gameSetsPlayerListsInRepository.containsKey(gameSetId)) {
			PlayerList list = this.gameSetsPlayerListsInRepository.get(gameSetId);
			list.add(player);
		}
		else {
			PlayerList list = new PlayerList();
			list.add(player);
			this.gameSetsPlayerListsInRepository.put(gameSetId, list);
		}
	}
	
	/**
	 * Uncaches a game player list.
	 * @param gameId
	 */
	private void uncachePlayersListOfGame(final long gameId) {
		this.gamesPlayerListsInRepository.remove(gameId);
	}

	/**
	 * Uncaches a game set player list.
	 * @param gameSetId
	 */
	private void uncachePlayersListOfGameSet(final long gameSetId) {
		this.gameSetsPlayerListsInRepository.remove(gameSetId);
	}
	
	/**
	 * Returns a Player identified by his name.
	 * @param name The player name.
	 * @return A Player identified by his name.
	 */
	public Player fetchPlayer(final String name) throws SQLException {
		Player player = null;
		Cursor dataSource = this.database.query(
			true, 
			DatabaseV5Constants.TABLE_PLAYER, 
			new String[] {
				DatabaseV5Constants.COL_PLAYER_ID,
				DatabaseV5Constants.COL_PLAYER_UUID,
				DatabaseV5Constants.COL_PLAYER_NAME,
				DatabaseV5Constants.COL_PLAYER_TIMESTAMP,
				DatabaseV5Constants.COL_PLAYER_PICTURE_URI,
				DatabaseV5Constants.COL_PLAYER_EMAIL,
				DatabaseV5Constants.COL_PLAYER_FACEBOOK_ID,
				DatabaseV5Constants.COL_PLAYER_SYNC_ACCOUNT,
				DatabaseV5Constants.COL_PLAYER_SYNC_TIMESTAMP
			},
			DatabaseV5Constants.COL_PLAYER_NAME + "=?", 
			new String[]{ name }, 
			null, 
			null, 
			null, 
			null
		);
			
		if (dataSource != null && dataSource.moveToFirst()) {
			player = this.fetchFromCursor(dataSource);
		}
		dataSource.close();

		return player;
	}
	
	/**
	 * Returns a Player from a cursor.
	 * @param dataSource The cursor.
	 * @return A Player from a cursor.
	 */
	private Player fetchFromCursor(final Cursor dataSource) {
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
		
		Player player = new Player();
		player.setId(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_ID)));
		player.setUuid(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_UUID)));
		player.setName(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_NAME)));
		player.setCreationTs(new Date(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_TIMESTAMP))));
		player.setPictureUri(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_PICTURE_URI)));
		player.setEmail(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_EMAIL)));
		player.setFacebookId(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_FACEBOOK_ID)));
		player.setSyncAccount(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_SYNC_ACCOUNT)));
		long syncTimestamp = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_PLAYER_SYNC_TIMESTAMP));
		player.setSyncTimestamp(syncTimestamp != 0 ? new Date(syncTimestamp) : null);
		return player;
	}

	/**
	 * Returns a ContentValues object representing the specified Player.
	 * @param player The Player to convert into a ContentValues.
	 * @return The ContentValues object representing the specified Player.
	 */
	private ContentValues createPlayerContentValues(final Player player) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_PLAYER_NAME, player.getName());
		values.put(DatabaseV5Constants.COL_PLAYER_UUID, player.getUuid());
		if (player.getCreationTs() != null) {
			values.put(DatabaseV5Constants.COL_PLAYER_TIMESTAMP, player.getCreationTs().getTime());
		}
		values.put(DatabaseV5Constants.COL_PLAYER_PICTURE_URI, player.getPictureUri());
		values.put(DatabaseV5Constants.COL_PLAYER_EMAIL, player.getEmail());
		values.put(DatabaseV5Constants.COL_PLAYER_FACEBOOK_ID, player.getFacebookId());
		values.put(DatabaseV5Constants.COL_PLAYER_SYNC_TIMESTAMP, 0);

		if (player.getId() != Long.MIN_VALUE) {
			values.put(DatabaseV5Constants.COL_PLAYER_ID, player.getId());
		}
		return values;
	}
	
	/**
	 * Returns ContentValues object representing the specified GamePlayers.
	 * @param entity The GamePlayers to convert into a ContentValues.
	 * @return The ContentValues object representing the specified GamePlayers.
	 */
	private ContentValues createGamePlayersContentValues(final GamePlayers gamePlayers) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_GAME_PLAYERS_GAME_ID, gamePlayers.getGameId());
		values.put(DatabaseV5Constants.COL_GAME_PLAYERS_PLAYER_ID, gamePlayers.getPlayerId());
		return values;
	}
	
	/**
	 * Returns ContentValues object representing the specified GameSetPlayers.
	 * @param entity The GameSetPlayers to convert into a ContentValues.
	 * @return The ContentValues object representing the specified GameSetPlayers.
	 */
	private ContentValues createGameSetPlayersContentValues(final GameSetPlayers entity) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_GAMESET_PLAYERS_GAMESET_ID, entity.getGameSetId());
		values.put(DatabaseV5Constants.COL_GAMESET_PLAYERS_PLAYER_ID, entity.getPlayerId());
		return values;
	}

	/**
	 * Deletes all game/players links for given game ids.
	 * @param gameIds The list of game ids. 
	 */
	public void deleteAllGamePlayersOfGames(final List<Long> gameIds) {
		this.database.delete(DatabaseV5Constants.TABLE_GAME_PLAYERS, DatabaseV5Constants.COL_GAME_PLAYERS_GAME_ID + SqlHelper.formatIdsForSqlInClause(gameIds), null);
		for(long gameId : gameIds) {
			this.uncachePlayersListOfGameSet(gameId);
		}
	}

	/**
	 * Deletes all game/players links for given game id.
	 * @param gameId The game id. 
	 */
	public void deleteAllGamePlayersOfGame(final long gameId) {
		this.database.delete(DatabaseV5Constants.TABLE_GAME_PLAYERS, DatabaseV5Constants.COL_GAME_PLAYERS_GAME_ID + "=?", new String[]{new Long(gameId).toString()});
		this.uncachePlayersListOfGameSet(gameId);
	}
	
	/**
	 * Deletes all game/players links for given GameSet id.
	 * @param gameSetId The game set id.
	 */
	public void deleteAllGameSetPlayersOfGameSet(final long gameSetId) {
		this.database.delete(DatabaseV5Constants.TABLE_GAMESET_PLAYERS, DatabaseV5Constants.COL_GAMESET_PLAYERS_GAMESET_ID + "=?", new String[]{new Long(gameSetId).toString()});
		this.uncachePlayersListOfGame(gameSetId);
	}
		
	/**
	 * Convenient class to retrieve/insert links between Game and Player tables.
	 */
	private class GamePlayers {

		/**
		 * The game id.
		 */
		private long gameId;
		
		/**
		 * The player id.
		 */
		private long playerId;

		/**
		 * @return the gameUid
		 */
		public long getGameId() {
			return gameId;
		}

		/**
		 * @param gameUid the gameUid to set
		 */
		public void setGameId(final long gameId) {
			this.gameId = gameId;
		}

		/**
		 * @return the playerUid
		 */
		public long getPlayerId() {
			return this.playerId;
		}

		/**
		 * @param playerUid the playerUid to set
		 */
		public void setPlayerId(final long playerId) {
			this.playerId = playerId;
		}	
	}
	
	/**
	 * Convenient class to retrieve/insert links between GameSet and Player tables.
	 */	
	private class GameSetPlayers {

		/**
		 * The game set id.
		 */
		private long gameSetId;
		
		/**
		 * The player id.
		 */
		private long playerId;

		/**
		 * @return the gameSetUid
		 */
		public long getGameSetId() {
			return this.gameSetId;
		}

		/**
		 * @param l the gameSetUid to set
		 */
		public void setGameSetId(final long id) {
			this.gameSetId = id;
		}

		/**
		 * @return the playerId
		 */
		public long getPlayerId() {
			return playerId;
		}

		/**
		 * @param playerUid the playerUid to set
		 */
		public void setPlayerId(final long playerId) {
			this.playerId = playerId;
		}
	}
	
	/**
	 * Persists the game/players of specified game set.
	 * @param gameSet
	 */
	public void storeGameSetPlayers(final GameSet gameSet) {
		if(gameSet == null) {
			throw new IllegalArgumentException("gameSet is null");
		}
		if(!gameSet.isPersisted()) {
			throw new IllegalArgumentException("gameSet hasn't been persisted");
		}
		for(Player player : gameSet.getPlayers()) {
			GameSetPlayers gameSetPlayersEntity = new GameSetPlayers();
			gameSetPlayersEntity.setGameSetId(gameSet.getId());
			gameSetPlayersEntity.setPlayerId(player.getId());
			ContentValues initialValues = this.createGameSetPlayersContentValues(gameSetPlayersEntity);
			this.database.insertOrThrow(DatabaseV5Constants.TABLE_GAMESET_PLAYERS, null, initialValues);
			
			Player actualPlayerToCache = this.playersByUuidInRepository.get(player.getUuid());
			if (actualPlayerToCache == null) {
				actualPlayerToCache = this.playersByIdInRepository.get(player.getId());
			}
			
			this.cachePlayerInGameSetsPlayersList(gameSet.getId(), actualPlayerToCache);
		}
	}

	/**
	 * Persists the game/players of specified game.
	 * @param game
	 */
	public void storeGamePlayers(final BaseGame game) {
		if(game == null) {
			throw new IllegalArgumentException("game is null");
		}
		if(!game.isPersisted()) {
			throw new IllegalArgumentException("game hasn't been persisted");
		}
		
		for(Player player : game.getPlayers()) {
			GamePlayers gamePlayersEntity = new GamePlayers();
			gamePlayersEntity.setGameId(game.getId());
			gamePlayersEntity.setPlayerId(player.getId());
			ContentValues initialValues = this.createGamePlayersContentValues(gamePlayersEntity);
			this.database.insertOrThrow(DatabaseV5Constants.TABLE_GAME_PLAYERS, null, initialValues);
			
			Player actualPlayerToCache = this.playersByUuidInRepository.get(player.getUuid());
			if (actualPlayerToCache == null) {
				actualPlayerToCache = this.playersByIdInRepository.get(player.getId());
			}
			
			this.cachePlayerInGamesPlayersList(game.getId(), actualPlayerToCache);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.sql.adapters.BaseAdapter#clear()
	 */
	@Override
	public void clear() {
		this.playersByUuidInRepository.clear();
		this.playersByIdInRepository.clear();
		this.gamesPlayerListsInRepository.clear();
		this.gameSetsPlayerListsInRepository.clear();
	}
}