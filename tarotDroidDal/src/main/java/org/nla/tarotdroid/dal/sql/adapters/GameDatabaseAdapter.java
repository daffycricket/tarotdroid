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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianBaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.dal.sql.adapters.enums.GameTypes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class GameDatabaseAdapter extends BaseAdapter {
	
	/**
	 * SQL Query to get all the standard games.
	 */
	private static final String GetAllStandardGamesQuery = MessageFormat.format(
			"select * from {0}, {1} where {0}.{2} = {1}.{3}", 
			DatabaseV5Constants.TABLE_GAME,
			DatabaseV5Constants.TABLE_STDGAME,
			DatabaseV5Constants.COL_GAME_ID,
			DatabaseV5Constants.COL_STDGAME_ID
	);
	
	/**
	 * SQL Query to get all the belgian games.
	 */
	private static final String GetAllBelgianGamesQuery = MessageFormat.format(
			"select * from {0}, {1} where {0}.{2} = {1}.{3}", 
			DatabaseV5Constants.TABLE_GAME,
			DatabaseV5Constants.TABLE_BELGIANGAME,
			DatabaseV5Constants.COL_GAME_ID,
			DatabaseV5Constants.COL_BELGIANGAME_ID
	);
	
	/**
	 * SQL Query to get all the penalty games.
	 */
	private static final String GetAllPenaltyGamesQuery = MessageFormat.format(
			"select * from {0}, {1} where {0}.{2} = {1}.{3}", 
			DatabaseV5Constants.TABLE_GAME,
			DatabaseV5Constants.TABLE_PENALTYGAME,
			DatabaseV5Constants.COL_GAME_ID,
			DatabaseV5Constants.COL_PENALTYGAME_ID
	);
	
	/**
	 * SQL Query to get all the passed games.
	 */
	private static final String GetAllPassedGamesQuery = MessageFormat.format(
			"select * from {0} where {1} = ''{2}''", 
			DatabaseV5Constants.TABLE_GAME,
			DatabaseV5Constants.COL_GAME_TYPE,
			GameTypes.passedGame.toString()
	);
		
	/**
	 * SQL Query to get the highest std game id ever.
	 */
	private static final String GetHighestIdQuery = "select max({0}) from {1}";
	
	/**
	 * All players in the repository.
	 */
	private Map<Long, Player> playersInRepository;
	
	/**
	 * All player lists in the repository.
	 */
	private Map<Long, PlayerList> gamesPlayerListsInRepository;
	
	/**
	 * Constructs a GameDatabaseAdapter from an Android context.
	 * @param database
	 * @param playersInRepository
	 * @param gamesPlayerListsInRepository
	 */
	public GameDatabaseAdapter(final SQLiteDatabase database, final Map<Long, Player> playersInRepository, final Map<Long, PlayerList> gamesPlayerListsInRepository) {
		super(database);
		if (playersInRepository == null) {
			throw new IllegalArgumentException("playersInRepository is null");
		}
		if (gamesPlayerListsInRepository == null) {
			throw new IllegalArgumentException("gamesPlayerListsInRepository is null");
		}
		
		this.playersInRepository = playersInRepository;
		this.gamesPlayerListsInRepository = gamesPlayerListsInRepository;
	}
	
	/**
	 * Persists a new Game in a GameSet.
	 * @param game The BaseGame to persist.
	 * @param gameSet The gameSet associated to the game.
	 */
	public void storeGame(final BaseGame game, final GameSet gameSet) {
		
		// find highest id ever
		String getHighestGameIdQuery = MessageFormat.format(GetHighestIdQuery, DatabaseV5Constants.COL_GAME_ID, DatabaseV5Constants.TABLE_GAME);
		String getHighestStdGameIdQuery = MessageFormat.format(GetHighestIdQuery, DatabaseV5Constants.COL_STDGAME_ID, DatabaseV5Constants.TABLE_STDGAME);
		String getHighestBelgianGameIdQuery = MessageFormat.format(GetHighestIdQuery, DatabaseV5Constants.COL_BELGIANGAME_ID, DatabaseV5Constants.TABLE_BELGIANGAME);
		String getHighestPenaltyGameIdQuery = MessageFormat.format(GetHighestIdQuery, DatabaseV5Constants.COL_PENALTYGAME_ID, DatabaseV5Constants.TABLE_PENALTYGAME);
		
		Cursor dataSource = database.rawQuery(getHighestGameIdQuery, null);
		dataSource.moveToFirst();
		int maxGameId = dataSource.getInt(0);
		dataSource.close();
		
		dataSource = database.rawQuery(getHighestStdGameIdQuery, null);
		dataSource.moveToFirst();
		int maxStdGameId = dataSource.getInt(0);
		dataSource.close();

		dataSource = database.rawQuery(getHighestBelgianGameIdQuery, null);
		dataSource.moveToFirst();
		int maxBelgianGameId = dataSource.getInt(0);
		dataSource.close();

		dataSource = database.rawQuery(getHighestPenaltyGameIdQuery, null);
		dataSource.moveToFirst();
		int maxPenaltyGameId = dataSource.getInt(0);
		dataSource.close();
		
		int maxId = Ints.max(maxGameId, maxStdGameId, maxBelgianGameId, maxPenaltyGameId);
		
		// persist game table
		ContentValues gameValues = this.createBaseContentValues(game, gameSet);
		gameValues.put(DatabaseV5Constants.COL_GAME_ID, maxId + 1);
		long id = this.database.insertOrThrow(DatabaseV5Constants.TABLE_GAME, null, gameValues);
		game.setId(id);
		
		// persist standard 5 player style game
		if (game instanceof StandardTarot5Game) {
			ContentValues std5Values = this.createStd5ContentValues((StandardTarot5Game)game);
			this.database.insertOrThrow(DatabaseV5Constants.TABLE_STDGAME, null, std5Values);
		}
		// persist standard 3 or 4 player style game
		else if (game instanceof StandardBaseGame) {
			ContentValues std4Values = this.createStdContentValues((StandardBaseGame)game);
			this.database.insertOrThrow(DatabaseV5Constants.TABLE_STDGAME, null, std4Values);
		}
		// persist belgian 3 player style game
		else if (game instanceof BelgianTarot3Game) {
			ContentValues belgian4Values = this.createBelgian3ContentValues((BelgianTarot3Game)game);
			this.database.insertOrThrow(DatabaseV5Constants.TABLE_BELGIANGAME, null, belgian4Values);
		}
		// persist belgian 4 player style game
		else if (game instanceof BelgianTarot4Game) {
			ContentValues belgian4Values = this.createBelgian4ContentValues((BelgianTarot4Game)game);
			this.database.insertOrThrow(DatabaseV5Constants.TABLE_BELGIANGAME, null, belgian4Values);
		}
		// persist belgian 5 player style game
		else if (game instanceof BelgianTarot5Game) {
			ContentValues belgian5Values = this.createBelgian5ContentValues((BelgianTarot5Game)game);
			this.database.insertOrThrow(DatabaseV5Constants.TABLE_BELGIANGAME, null, belgian5Values);
		}
		// persist penalty game
		else if (game instanceof PenaltyGame) {
			ContentValues penaltyValues = this.createPenaltyContentValues((PenaltyGame)game);
			this.database.insertOrThrow(DatabaseV5Constants.TABLE_PENALTYGAME, null, penaltyValues);
		}
		// persist passed game
		else if (game instanceof PassedGame) {
			// no specific table to persist
		}
	}
	
	/**
	 * Updates a game that was previously persisted.
	 * @param game
	 */
	public void updateGame(final BaseGame game, final GameSet gameSet) {
	
		// update game table
		ContentValues updatedGameValues = this.createBaseContentValues(game, gameSet);
		this.database.update(DatabaseV5Constants.TABLE_GAME, updatedGameValues, DatabaseV5Constants.COL_GAME_ID + "=?", new String[]{ String.valueOf(game.getId()) });
		
		// persist standard 5 player style game
		if (game instanceof StandardTarot5Game) {
			ContentValues std5Values = this.createStd5ContentValues((StandardTarot5Game)game);
			this.database.update(DatabaseV5Constants.TABLE_STDGAME, std5Values, DatabaseV5Constants.COL_GAME_ID + "=?", new String[]{ String.valueOf(game.getId()) });
		}
		// persist standard 3 or 4 player style game
		else if (game instanceof StandardBaseGame) {
			ContentValues std4Values = this.createStdContentValues((StandardBaseGame)game);
			this.database.update(DatabaseV5Constants.TABLE_STDGAME, std4Values, DatabaseV5Constants.COL_GAME_ID + "=?", new String[]{ String.valueOf(game.getId()) });
		}
		// persist belgian 3 player style game
		else if (game instanceof BelgianTarot3Game) {
			ContentValues belgian3Values = this.createBelgian3ContentValues((BelgianTarot3Game)game);
			this.database.update(DatabaseV5Constants.TABLE_BELGIANGAME, belgian3Values, DatabaseV5Constants.COL_GAME_ID + "=?", new String[]{ String.valueOf(game.getId()) });
		}
		// persist belgian 4 player style game
		else if (game instanceof BelgianTarot4Game) {
			ContentValues belgian4Values = this.createBelgian4ContentValues((BelgianTarot4Game)game);
			this.database.update(DatabaseV5Constants.TABLE_BELGIANGAME, belgian4Values, DatabaseV5Constants.COL_GAME_ID + "=?", new String[]{ String.valueOf(game.getId()) });
		}
		// persist belgian 5 player style game
		else if (game instanceof BelgianTarot5Game) {
			ContentValues belgian5Values = this.createBelgian5ContentValues((BelgianTarot5Game)game);
			this.database.update(DatabaseV5Constants.TABLE_BELGIANGAME, belgian5Values, DatabaseV5Constants.COL_GAME_ID + "=?", new String[]{ String.valueOf(game.getId()) });
		}
		// persist penalty game
		else if (game instanceof PenaltyGame) {
			ContentValues penaltyValues = this.createPenaltyContentValues((PenaltyGame)game);
			this.database.update(DatabaseV5Constants.TABLE_PENALTYGAME, penaltyValues, DatabaseV5Constants.COL_GAME_ID + "=?", new String[]{ String.valueOf(game.getId()) });
		}
		// persist passed game
		else if (game instanceof PassedGame) {
			// no specific table to persist
		}
	}

	/**
	 * Delete all games in a game set.
	 * @param gameIds The list of game ids by type of game.
	 * @return The list of deleted game ids.
	 */
	public List<Long> deleteAllGamesInGameSet(final Map<Class<? extends BaseGame>, List<Long>> gameIds) {
		
		List<Long> allGameIds = newArrayList();
		// delete games in child tables
		for (Class<? extends BaseGame> gameClass : gameIds.keySet()) {
			List<Long> gameIdsForGivenClass = gameIds.get(gameClass);
			String gameTable = null;
			if (gameClass.equals(BelgianBaseGame.class)) {
				gameTable = DatabaseV5Constants.TABLE_BELGIANGAME;
			}
			else if (gameClass.equals(StandardBaseGame.class)) {
				gameTable = DatabaseV5Constants.TABLE_STDGAME;
			}
			else if (gameClass.equals(PenaltyGame.class)) {
				gameTable = DatabaseV5Constants.TABLE_PENALTYGAME;
			}
			
			if (gameTable != null) {
				this.database.delete(gameTable, DatabaseV5Constants.COL_GAME_ID + SqlHelper.formatIdsForSqlInClause(gameIdsForGivenClass), null);
			}
			allGameIds.addAll(gameIdsForGivenClass);
		}
		
		// delete games in base table
		this.database.delete(DatabaseV5Constants.TABLE_GAME, DatabaseV5Constants.COL_GAME_ID + SqlHelper.formatIdsForSqlInClause(allGameIds), null);
		
		return allGameIds;
	}
	
	/**
	 * Deletes the game identified by the given id.
	 * @param gameId The game id.
	 */
	public void deleteGame(final long gameId) {
		String gameIdAsString = new Long(gameId).toString();
		this.database.delete(DatabaseV5Constants.TABLE_STDGAME, DatabaseV5Constants.COL_STDGAME_ID + "=?", new String[]{gameIdAsString});
		this.database.delete(DatabaseV5Constants.TABLE_BELGIANGAME, DatabaseV5Constants.COL_BELGIANGAME_ID + "=?", new String[]{gameIdAsString});
		this.database.delete(DatabaseV5Constants.TABLE_PENALTYGAME, DatabaseV5Constants.COL_PENALTYGAME_ID + "=?", new String[]{gameIdAsString});
		this.database.delete(DatabaseV5Constants.TABLE_GAME, DatabaseV5Constants.COL_GAME_ID + "=?", new String[]{gameIdAsString});
	}
	
	/**
	 * Reads the games from the databse and loads them into the repository.
	 * @return The loaded games as a Map<UUID, List<BaseGame>>.
	 */
	public Map<Long, List<BaseGame>> fetchAllGames() {
		
		Map<Long, List<BaseGame>> gamesInTheRepositoryByGameSet = newHashMap();
		this.fetchAllStdGames(gamesInTheRepositoryByGameSet);
		this.fetchAllBelgianGames(gamesInTheRepositoryByGameSet);
		this.fetchAllPenaltyGames(gamesInTheRepositoryByGameSet);
		this.fetchAllPassedGames(gamesInTheRepositoryByGameSet);
		
		Ordering<BaseGame> indexOrdering = Ordering.natural().onResultOf(new Function<BaseGame, Integer>() {
		    public Integer apply(BaseGame from) {
		        return from.getIndex();
		    }
		});
		
		Map<Long, List<BaseGame>> orderdGamesInTheRepositoryByGameSet = newHashMap();
		for (long gameSetId : gamesInTheRepositoryByGameSet.keySet()) {
			List<BaseGame> unorderedGamesForGameSet = gamesInTheRepositoryByGameSet.get(gameSetId);
			List<BaseGame> orderedGamesForGameSet = newArrayList(ImmutableSortedSet.orderedBy(indexOrdering).addAll(unorderedGamesForGameSet).build());
			
			orderdGamesInTheRepositoryByGameSet.put(gameSetId, orderedGamesForGameSet);
		}
		
		return orderdGamesInTheRepositoryByGameSet;
	}
	
	/**
	 * Reads the games from the databse and loads them into the repository.
	 * @return The loaded games as a Map<UUID, List<BaseGame>>.
	 */
	public void fetchAllStdGames(final Map<Long, List<BaseGame>> gamesInTheRepositoryByGameSet) {
		Cursor dataSource = database.rawQuery(GetAllStandardGamesQuery, null);
		
		while(dataSource.moveToNext()) {
			try {
				StandardBaseGame game = null;
				switch(GameTypes.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_TYPE)))) {
					case std3game :
						game = new StandardTarot3Game();
						break;
					case std4game :
						game = new StandardTarot4Game();
						break;
					case std5game :
						game = new StandardTarot5Game();
						break;
					default:
						throw new IllegalStateException("GameDatabaseAdapter.fetchAllStdGames() incorrect game type");
				}
				
				long gameSetId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_GAMESET_ID));
				long gameId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_ID));
				game.setId(gameId);
				game.setPlayers(this.gamesPlayerListsInRepository.get(gameId));
				game.setUuid(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_UUID)));
				game.setCreationTs(new Date(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_TIMESTAMP))));
				game.setIndex(dataSource.getInt(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_INDEX)));
				game.setDeadPlayer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_DEAD1_ID))));
				game.setDealer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_DEALER_ID))));
				
				game.setLeadingPlayer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_LEADER_ID))));
				game.setBet(Bet.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_BET))));
				game.setNumberOfOudlers(dataSource.getInt(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_OUDLERS)));
				game.setPoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_POINTS)));
				game.setTeamWithPoignee(Team.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_HANDFUL))));
				game.setTeamWithDoublePoignee(Team.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_DOUBLEHANDFUL))));
				game.setTeamWithTriplePoignee(Team.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_TRIPLEHANDFUL))));
				game.setPlayerWithMisery(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_MISERY))));
				game.setTeamWithKidAtTheEnd(Team.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_KIDATTHEEND))));
				game.setChelem(Chelem.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_SLAM))));
				
				// data specifics to StandardTarot5Game
				if (game instanceof StandardTarot5Game) {
					StandardTarot5Game std5game = (StandardTarot5Game)game;
					std5game.setCalledKing(King.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_KING))));
					std5game.setCalledPlayer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_STDGAME_CALLED_ID))));
				}
				
				if (!gamesInTheRepositoryByGameSet.containsKey(gameSetId)) {
					gamesInTheRepositoryByGameSet.put(gameSetId, new ArrayList<BaseGame>());
				}
				
				gamesInTheRepositoryByGameSet.get(gameSetId).add(game);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		dataSource.close();
	}
	
	/**
	 * Reads the belgian games from the database and loads them into the repository.
	 * @return The loaded games as a Map<UUID, List<BaseGame>>.
	 */
	public void fetchAllBelgianGames(final Map<Long, List<BaseGame>> gamesInTheRepositoryByGameSet) {
		Cursor dataSource = database.rawQuery(GetAllBelgianGamesQuery, null);
		
		while(dataSource.moveToNext()) {
			BelgianBaseGame game = null;
			switch(GameTypes.valueOf(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_TYPE)))) {
				case belgian3game :
					game = new BelgianTarot3Game();
					break;
				case belgian4game :
					BelgianTarot4Game belgian4game = new BelgianTarot4Game();
					belgian4game.setFourth(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_BELGIANGAME_FOURTH_ID))));
					game = belgian4game;
					break;
				case belgian5game :
					BelgianTarot5Game belgian5game = new BelgianTarot5Game();
					belgian5game.setFourth(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_BELGIANGAME_FOURTH_ID))));
					belgian5game.setFifth(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_BELGIANGAME_FIFTH_ID))));
					game = belgian5game;
					break;
				default:
					throw new IllegalStateException("GameDatabaseAdapter.fetchAllBelgianGames() incorrect game type");
			}
			
			long gameSetId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_GAMESET_ID));
			long gameId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_ID));
			game.setId(gameId);
			game.setPlayers(this.gamesPlayerListsInRepository.get(gameId));
			game.setUuid(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_UUID)));
			game.setCreationTs(new Date(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_TIMESTAMP))));
			game.setIndex(dataSource.getInt(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_INDEX)));
			game.setDeadPlayer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_DEAD1_ID))));
			game.setDealer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_DEALER_ID))));
			
			game.setPlayers(this.gamesPlayerListsInRepository.get(gameId));
			game.setFirst(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_BELGIANGAME_FIRST_ID))));
			game.setSecond(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_BELGIANGAME_SECOND_ID))));
			game.setThird(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_BELGIANGAME_THIRD_ID))));
			
			if (!gamesInTheRepositoryByGameSet.containsKey(gameSetId)) {
				gamesInTheRepositoryByGameSet.put(gameSetId, new ArrayList<BaseGame>());
			}
			gamesInTheRepositoryByGameSet.get(gameSetId).add(game);
		}
		dataSource.close();
	}
	
	/**
	 * Reads the penalty games from the database and loads them into the repository.
	 * @return The loaded games as a Map<UUID, List<BaseGame>>.
	 */
	public void fetchAllPenaltyGames(final Map<Long, List<BaseGame>> gamesInTheRepositoryByGameSet) {
		Cursor dataSource = database.rawQuery(GetAllPenaltyGamesQuery, null);
		
		while(dataSource.moveToNext()) {
			PenaltyGame game = new PenaltyGame();
			long gameSetId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_GAMESET_ID));
			long gameId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_ID));
			game.setId(gameId);
			game.setPlayers(this.gamesPlayerListsInRepository.get(gameId));
			game.setUuid(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_UUID)));
			game.setCreationTs(new Date(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_TIMESTAMP))));
			game.setIndex(dataSource.getInt(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_INDEX)));
			game.setDeadPlayer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_DEAD1_ID))));
			game.setDealer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_DEALER_ID))));
			
			game.setPlayers(this.gamesPlayerListsInRepository.get(gameId));
			game.setPenaltedPlayer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_PENALTYGAME_PENALTED_ID))));
			game.setPenaltyPoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV5Constants.COL_PENALTYGAME_POINTS)));
			game.setPenaltyType(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_PENALTYGAME_TYPE)));
			
			if (!gamesInTheRepositoryByGameSet.containsKey(gameSetId)) {
				gamesInTheRepositoryByGameSet.put(gameSetId, new ArrayList<BaseGame>());
			}
			gamesInTheRepositoryByGameSet.get(gameSetId).add(game);
		}
		dataSource.close();
	}
	
	/**
	 * Reads the passed games from the database and loads them into the repository.
	 * @return The loaded games as a Map<UUID, List<BaseGame>>.
	 */
	public void fetchAllPassedGames(final Map<Long, List<BaseGame>> gamesInTheRepositoryByGameSet) {
		Cursor dataSource = database.rawQuery(GetAllPassedGamesQuery, null);
		
		while(dataSource.moveToNext()) {
			PassedGame game = new PassedGame();
			long gameSetId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_GAMESET_ID));
			long gameId = dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_ID));
			game.setId(gameId);
			game.setPlayers(this.gamesPlayerListsInRepository.get(gameId));
			game.setUuid(dataSource.getString(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_UUID)));
			game.setCreationTs(new Date(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_TIMESTAMP))));
			game.setIndex(dataSource.getInt(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_INDEX)));
			game.setDeadPlayer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_DEAD1_ID))));
			game.setDealer(this.playersInRepository.get(dataSource.getLong(dataSource.getColumnIndex(DatabaseV5Constants.COL_GAME_DEALER_ID))));
			game.setPlayers(this.gamesPlayerListsInRepository.get(gameId));
			
			if (!gamesInTheRepositoryByGameSet.containsKey(gameSetId)) {
				gamesInTheRepositoryByGameSet.put(gameSetId, new ArrayList<BaseGame>());
			}
			gamesInTheRepositoryByGameSet.get(gameSetId).add(game);
		}
		dataSource.close();
	}

	/**
	 * Returns a ContentValues object representing the specified BaseGame.
	 * @param game The BaseGame to convert into a ContentValues.
	 * @param gameSet The GameSet associated to this game.
	 * @return The ContentValues object representing the specified BaseGame
	 */
	private ContentValues createBaseContentValues(final BaseGame game, final GameSet gameSet) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_GAME_UUID, game.getUuid());
		values.put(DatabaseV5Constants.COL_GAME_INDEX, game.getIndex());
		values.put(DatabaseV5Constants.COL_GAME_GAMESET_ID, gameSet.getId());
		if (game.getCreationTs() != null) {
			values.put(DatabaseV5Constants.COL_GAME_TIMESTAMP, game.getCreationTs().getTime());
		}
		values.put(DatabaseV5Constants.COL_GAME_DEALER_ID, game.getDealer() != null ? game.getDealer().getId() : null);
		values.put(DatabaseV5Constants.COL_GAME_DEAD1_ID, game.getDeadPlayer() != null ? game.getDeadPlayer().getId() : null);

		
		// game type depends on the type of the actual instance 
		String gameType = null;
		if (game instanceof StandardTarot3Game) {
			gameType = GameTypes.std3game.toString();
		}
		else if (game instanceof StandardTarot4Game) {
			gameType = GameTypes.std4game.toString();
		}
		else if (game instanceof StandardTarot5Game) {
			gameType = GameTypes.std5game.toString();
		}
		else if (game instanceof BelgianTarot3Game) {
			gameType = GameTypes.belgian3game.toString();
		}		
		else if (game instanceof BelgianTarot4Game) {
			gameType = GameTypes.belgian4game.toString();
		}
		else if (game instanceof BelgianTarot5Game) {
			gameType = GameTypes.belgian5game.toString();
		}
		else if (game instanceof PassedGame) {
			gameType = GameTypes.passedGame.toString();
		}
		else if (game instanceof PenaltyGame) {
			gameType = GameTypes.penaltyGame.toString();
		}
		
		values.put(DatabaseV5Constants.COL_GAME_TYPE, gameType);
		
		return values;
	}

	/**
	 * Returns a ContentValues object representing the specified StandardTarot4Game object.
	 * @param game a StandardTarot4Game
	 * @return a ContentValues object representing the specified StandardTarot4Game object.
	 */
	private ContentValues createStdContentValues(final StandardBaseGame game) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_STDGAME_ID, game.getId());
		values.put(DatabaseV5Constants.COL_STDGAME_LEADER_ID, game.getLeadingPlayer().getId());
		values.put(DatabaseV5Constants.COL_STDGAME_BET, game.getBet().getBetType().toString());
		values.put(DatabaseV5Constants.COL_STDGAME_OUDLERS, game.getNumberOfOudlers());
		values.put(DatabaseV5Constants.COL_STDGAME_POINTS, game.getPoints());
		values.put(DatabaseV5Constants.COL_STDGAME_KIDATTHEEND, game.getTeamWithKidAtTheEnd() != null ? game.getTeamWithKidAtTheEnd().getTeamType().toString() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_HANDFUL, game.getTeamWithPoignee() != null ? game.getTeamWithPoignee().getTeamType().toString() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_DOUBLEHANDFUL, game.getTeamWithDoublePoignee() != null ? game.getTeamWithDoublePoignee().getTeamType().toString() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_TRIPLEHANDFUL, game.getTeamWithTriplePoignee() != null ? game.getTeamWithTriplePoignee().getTeamType().toString() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_MISERY, game.getPlayerWithMisery() != null ? game.getPlayerWithMisery().getId() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_SLAM, game.getChelem() != null ? game.getChelem().getChelemType().toString() : null);
		return values;
	}
	
	/**
	 * Returns a ContentValues object representing the specified StandardTarot5Game object.
	 * @param game a StandardTarot5Game
	 * @return a ContentValues object representing the specified StandardTarot5Game object.
	 */
	private ContentValues createStd5ContentValues(final StandardTarot5Game game) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_STDGAME_ID, game.getId());
		values.put(DatabaseV5Constants.COL_STDGAME_LEADER_ID, game.getLeadingPlayer().getId());
		values.put(DatabaseV5Constants.COL_STDGAME_CALLED_ID, game.getCalledPlayer().getId());
		values.put(DatabaseV5Constants.COL_STDGAME_KING, game.getCalledKing().getKingType().toString());
		values.put(DatabaseV5Constants.COL_STDGAME_BET, game.getBet().getBetType().toString());
		values.put(DatabaseV5Constants.COL_STDGAME_OUDLERS, game.getNumberOfOudlers());
		values.put(DatabaseV5Constants.COL_STDGAME_POINTS, game.getPoints());
		values.put(DatabaseV5Constants.COL_STDGAME_KIDATTHEEND, game.getTeamWithKidAtTheEnd() != null ? game.getTeamWithKidAtTheEnd().getTeamType().toString() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_HANDFUL, game.getTeamWithPoignee() != null ? game.getTeamWithPoignee().getTeamType().toString() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_DOUBLEHANDFUL, game.getTeamWithDoublePoignee() != null ? game.getTeamWithDoublePoignee().getTeamType().toString() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_TRIPLEHANDFUL, game.getTeamWithTriplePoignee() != null ? game.getTeamWithTriplePoignee().getTeamType().toString() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_MISERY, game.getPlayerWithMisery() != null ? game.getPlayerWithMisery().getId() : null);
		values.put(DatabaseV5Constants.COL_STDGAME_SLAM, game.getChelem() != null ? game.getChelem().getChelemType().toString() : null);
		return values;
	}

	/**
	 * Returns a ContentValues object representing the specified BelgianTarot3Game object.
	 * @param game a BelgianTarot3Game
	 * @return a ContentValues object representing the specified BelgianTarot3Game object.
	 */
	private ContentValues createBelgian3ContentValues(final BelgianTarot3Game game) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_BELGIANGAME_ID, game.getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_FIRST_ID, game.getFirst().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_SECOND_ID, game.getSecond().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_THIRD_ID, game.getThird().getId());
		return values;
	}
	
	/**
	 * Returns a ContentValues object representing the specified BelgianTarot4Game object.
	 * @param game a BelgianTarot4Game
	 * @return a ContentValues object representing the specified BelgianTarot4Game object.
	 */
	private ContentValues createBelgian4ContentValues(final BelgianTarot4Game game) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_BELGIANGAME_ID, game.getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_FIRST_ID, game.getFirst().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_SECOND_ID, game.getSecond().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_THIRD_ID, game.getThird().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_FOURTH_ID, game.getFourth().getId());
		return values;
	}

	/**
	 * Returns a ContentValues object representing the specified BelgianTarot5Game object.
	 * @param game a BelgianTarot5Game
	 * @return a ContentValues object representing the specified BelgianTarot5Game object.
	 */
	private ContentValues createBelgian5ContentValues(final BelgianTarot5Game game) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_BELGIANGAME_ID, game.getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_FIRST_ID, game.getFirst().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_SECOND_ID, game.getSecond().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_THIRD_ID, game.getThird().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_FOURTH_ID, game.getFourth().getId());
		values.put(DatabaseV5Constants.COL_BELGIANGAME_FIFTH_ID, game.getFifth().getId());
		return values;
	}
	
	/**
	 * Returns a ContentValues object representing the specified PenaltyGame object.
	 * @param game a PenaltyGame
	 * @return a ContentValues object representing the specified PenaltyGame object.
	 */
	private ContentValues createPenaltyContentValues(final PenaltyGame game) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV5Constants.COL_PENALTYGAME_ID, game.getId());
		values.put(DatabaseV5Constants.COL_PENALTYGAME_PENALTED_ID, game.getPenaltedPlayer().getId());
		values.put(DatabaseV5Constants.COL_PENALTYGAME_POINTS, game.getPenaltyPoints());
		values.put(DatabaseV5Constants.COL_PENALTYGAME_TYPE, game.getPenaltyType());
		return values;
	}
}
