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
package org.nla.tarotdroid.dal.sql;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.PersistableBusinessObject;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.enums.ActionTypes;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.biz.enums.ObjectTypes;
import org.nla.tarotdroid.dal.DalException;
import org.nla.tarotdroid.dal.IDalService;
import org.nla.tarotdroid.dal.sql.adapters.EntityTrackerDatabaseAdapter;
import org.nla.tarotdroid.dal.sql.adapters.GameDatabaseAdapter;
import org.nla.tarotdroid.dal.sql.adapters.GameSetDatabaseAdapter;
import org.nla.tarotdroid.dal.sql.adapters.GameSetParametersDatabaseAdapter;
import org.nla.tarotdroid.dal.sql.adapters.PlayerDatabaseAdapter;
import org.nla.tarotdroid.dal.sql.adapters.TarotDatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Throwables;


/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class SqliteDalService implements IDalService {

	/**
	 * SQLiteDatabase instance.
	 */
	private SQLiteDatabase database;	
	
	/**
	 * TarotDatabaseHelper instance. 
	 */
	private TarotDatabaseHelper dbHelper;
	
	/**
	 * Android context.
	 */
	private Context context;
	
	/**
	 * An adapter to manage SQL accesses related to Player objects.
	 */
	private PlayerDatabaseAdapter playerAdapter;

	/**
	 * An adapter to manage SQL accesses related to Game objects.
	 */
	private GameDatabaseAdapter gameAdapter;
	
	/**
	 * An adapter to manage SQL accesses related to GameSet objects.
	 */
	private GameSetDatabaseAdapter gameSetAdapter;

	/**
	 * An adapter to manage SQL accesses related to GameSetParameters objects.
	 */
	private GameSetParametersDatabaseAdapter gameSetParametersAdapter;
	
	/**
	 * An adapter to manage SQL accesses related to Trackers.
	 */
	private EntityTrackerDatabaseAdapter tracker;
		
	/**
	 * The cached list of GameSets.
	 */
	private List<GameSet> gameSets;
	
	/**
	 * The cached list of incorrect GameSets.
	 */
	private List<GameSet> incorrectGameSets;
	
	/**
	 * The cached list of Games cached by GameSets.
	 */
	private Map<Long, List<BaseGame>> gamesByGameSets;
	
	/**
	 * Internal log.
	 */
	private StringBuilder logBuffer;
	
	/**
	 * Helper method 
	 * @param context
	 * @return
	 */
	public static SQLiteDatabase getSqliteDatabase(Context context) {
		return new TarotDatabaseHelper(context).getReadableDatabase();
	}
	
	/**
	 * Creates an instance of SqliteDalService.
	 * @param context the android context.
	 */
	public SqliteDalService(final Context context) /*throws DalException*/ {
		checkArgument(context != null, "context is null");
		this.context = context;
		this.logBuffer = new StringBuilder();
		this.initialize();
	}
	
	@Override
	public void initialize() /*throws DalException*/ {
		try {
			// initialize db objects
			this.dbHelper = new TarotDatabaseHelper(this.context);
			
			// open the db
			this.openDatabase();
			
			// initialize adapter objects
			this.playerAdapter = new PlayerDatabaseAdapter(this.database);
			this.gameAdapter = new GameDatabaseAdapter(this.database, this.playerAdapter.getPlayersByIdInRepository(), this.playerAdapter.getGamesPlayerListsInRepository());
			this.gameSetAdapter = new GameSetDatabaseAdapter(this.database);
			this.gameSetParametersAdapter = new GameSetParametersDatabaseAdapter(this.database);
			this.tracker = new EntityTrackerDatabaseAdapter(this.database);
			this.gameSets = new ArrayList<GameSet>();
			
			// TODO : DEBUG HERE
			
			// retrieve all gamesets
			this.loadAllGameSets();
			
			// make sure all gamesets
			this.checkAllGameSets();
		}
		catch (Exception e) {
			this.logBuffer.append(Throwables.getStackTraceAsString(e));
			e.printStackTrace();
			
			this.gameSets = newArrayList();
			this.incorrectGameSets = newArrayList();
		}
	}
	
	/**
	 * Make sure all loaded gamesets are correct.
	 */
	private void checkAllGameSets() {
		this.incorrectGameSets = newArrayList();
		
		// identify all game sets with a problem
		for(GameSet gameSet : this.gameSets) {
			
			// make sure there are players
			if (gameSet.getPlayers() == null) {
				this.incorrectGameSets.add(gameSet);
				continue;
			}
			
			// make sure there are parameters
			if (gameSet.getGameSetParameters() == null) {
				this.incorrectGameSets.add(gameSet);
				continue;				
			}
			
			// make sure the game style type is set
			if (gameSet.getGameStyleType() == null || gameSet.getGameStyleType() == GameStyleType.None) {
				this.incorrectGameSets.add(gameSet);
				continue;
			}
		}
		
		// remove incorrect games from cache 
		for (GameSet gameSet : this.incorrectGameSets) {
			this.gameSets.remove(gameSet);
		}
	}	

	/**
	 * Properly opens the database.
	 */
	private void openDatabase() {
		// first, try to close any existing db
		try {
			this.dbHelper.close();
		}
		catch (Exception e) {
			this.logBuffer.append(Throwables.getStackTraceAsString(e));
		}
		
		// then, open the db 
		this.database = this.dbHelper.getWritableDatabase();
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#isStoringGameSets()
	 */
	@Override
	public boolean isStoringGameSets() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getAllPlayers()
	 */
	@Override
	public List<Player> getAllPlayers() {
		try {
            return new ArrayList<Player>(this.playerAdapter.getPlayersInRepository());
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
        	return new ArrayList<Player>();
        }
	}
	

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getPlayerByName(java.lang.String)
	 */
	@Override
	public Player getPlayerByName(String name) throws DalException {
		if (name == null) {
			return null;
		}
		for (Player player : this.playerAdapter.getPlayersInRepository()) {
			if (player.getName().equalsIgnoreCase(name)) {
				return player; 
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getPlayerByUuid(java.lang.String)
	 */
	@Override
	public Player getPlayerByUuid(String uuid) throws DalException {
		if (uuid == null) {
			return null;
		}
		for (Player player : this.playerAdapter.getPlayersInRepository()) {
			if (player.getUuid().equalsIgnoreCase(uuid)) {
				return player; 
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#loadAllGameSets()
	 */
	@Override
	public void loadAllGameSets() throws DalException {
		try {
			// get game sets
            this.gameSets = this.gameSetAdapter.fetchAllGameSets();
            
            // TODO Comment before releasing
            // used to test ACRA
//            if (5 == 5)
//				throw new RuntimeException();
            
            // get all games, sorted by game sets
            this.gamesByGameSets = this.gameAdapter.fetchAllGames();
            
            // finalize game set creation
            for (GameSet gameSet : this.gameSets) {
            	
            	try {
            	
	                // get game set parameters
	                gameSet.setGameSetParameters(this.getGameSetParametersForGameSetId(gameSet.getId()));
	
	                // get players
	                gameSet.setPlayers(this.getPlayersForGameSetId(gameSet.getId()));
	
	                // get games
	                List<BaseGame> games = this.gamesByGameSets.get(gameSet.getId());
	                if (games != null) {
		                for (BaseGame game : games) {
		                	gameSet.addGame(game);
		                }
	                }
            	}
            	catch(Exception gameSetException) {
            		this.logBuffer.append(Throwables.getStackTraceAsString(gameSetException));
            		gameSetException.printStackTrace();
            	}
            }
        }
        catch (Exception globalException) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(globalException));
        	globalException.printStackTrace();
        }
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getAllGameSets()
	 */
	@Override
	public List<GameSet> getAllGameSets() {
		return this.gameSets;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getGameSetById(long)
	 */
	@Override
	public GameSet getGameSetById(long id) {
		GameSet toReturn = null;
		if (this.gameSets != null) {
			for (GameSet gameSet : this.gameSets) {
				if (gameSet.getId() == id) {
					toReturn = gameSet;
					break;
				}
			}
		}
		return toReturn;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getGameSetCount()
	 */
	@Override
	public int getGameSetCount() throws DalException {
		try {
            return this.gameSetAdapter.countGameSets();
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
            throw new DalException(e);
        }
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#saveGameSet(org.nla.tarotdroid.biz.GameSet)
	 */
	@Override
	public void saveGameSet(final GameSet gameSet) throws DalException {
		try {
			checkArgument(gameSet != null, "gameSet is null");
			//checkArgument(!gameSet.isPersisted(), "gameSet was already been persisted");
			
            // persist gameset
			this.beginTransaction();
            this.gameSetAdapter.storeGameSet(gameSet);
            
            // persist gameset children
            this.savePlayersInGameSet(gameSet);
            this.saveGameSetParametersInGameSet(gameSet.getGameSetParameters(), gameSet);

            // persist games
            for (BaseGame game : gameSet) {
            	this.saveGameNonTransactionnal(game, gameSet);
            }
            
            // store game sets in memory cache
            this.gameSets.add(gameSet);
                        
            this.endTransaction(true);
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
        	this.endTransaction(false);
            throw new DalException(e);
        }
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#updateGameSetAfterSync(org.nla.tarotdroid.biz.GameSet)
	 */
	@Override
	public void updateGameSetAfterSync(final GameSet gameSet) throws DalException {
		try {
			checkArgument(gameSet.isPersisted(), "gameSet hasn't been persisted");
			this.beginTransaction();
			this.gameSetAdapter.updateGameSetAfterSync(gameSet);
    		this.endTransaction(true);
		}
		catch (Exception e) {
			this.logBuffer.append(Throwables.getStackTraceAsString(e));
			e.printStackTrace();
			this.endTransaction(false);
			throw new DalException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#updateGame(org.nla.tarotdroid.biz.BaseGame)
	 */
	@Override
	public void updateGame(final BaseGame game, final GameSet gameSet) throws DalException {
		checkArgument(game != null, "game is null");
		checkArgument(gameSet != null, "gameSet is null");
		try {
			this.beginTransaction();
			checkArgument(game.isPersisted(), "game hasn't been persisted");
			this.gameAdapter.updateGame(game, gameSet);
			gameSet.setSyncTimestamp(null);
			this.gameSetAdapter.updateGameSet(gameSet);
			this.endTransaction(true);
		}
		catch (Exception e) {
			this.logBuffer.append(Throwables.getStackTraceAsString(e));
			e.printStackTrace();
			this.endTransaction(false);
			throw new DalException(e);
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#deleteGameSet(org.nla.tarotdroid.biz.GameSet)
	 */
	@Override
	public void deleteGameSet(final GameSet gameSet) throws DalException {
		try {
			checkArgument(gameSet != null, "gameSet is null");
			checkArgument(gameSet.isPersisted(), "gameSet hasn't been persisted");
			
            // delete parameters
			this.beginTransaction();
            this.gameSetParametersAdapter.deleteGameSetParameters(gameSet.getId());

            // delete games
            this.deleteGamesInGameSet(gameSet);
            
            // delete gameSet
            this.gameSetAdapter.deleteGameSet(gameSet.getId());
            
            // delete link gameset/players
            this.playerAdapter.deleteAllGameSetPlayersOfGameSet(gameSet.getId());
            this.gameSets.remove(gameSet);
            this.tracker.trackEntity(gameSet, ActionTypes.Removal, ObjectTypes.GameSet);
            this.endTransaction(true);
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
        	this.endTransaction(false);
            throw new DalException(e);
        }
	}
	
	/**
	 * @param gameSet
	 */
	private void deleteGamesInGameSet(final GameSet gameSet) throws DalException {
		try {
			checkArgument(gameSet != null, "gameSet is null");
			checkArgument(gameSet.isPersisted(), "gameSet hasn't been persisted");
			
			//this.beginTransaction();
            //delete games
            List<Long> allGameIds = this.gameAdapter.deleteAllGamesInGameSet(gameSet.getGameIds());
            // delete link games/players
            this.playerAdapter.deleteAllGamePlayersOfGames(allGameIds);
            //this.endTransaction(true);
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
        	//this.endTransaction(false);
            throw new DalException(e);
        }
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#saveGame(org.nla.tarotdroid.biz.GameSet, org.nla.tarotdroid.biz.BaseGame)
	 */
	@Override
	public void saveGame(final BaseGame game, final GameSet gameSet) throws DalException {
		try {
			checkArgument(game != null, "game is null");
			//checkArgument(!game.isPersisted(), "game was already persisted");
			checkArgument(gameSet != null, "gameSet is null");
			checkArgument(gameSet.isPersisted(), "gameSet hasn't been persisted");
			
			this.beginTransaction();
            this.gameAdapter.storeGame(game, gameSet);
            this.playerAdapter.storeGamePlayers(game);
            gameSet.setSyncTimestamp(null);
            this.gameSetAdapter.updateGameSet(gameSet);
            this.endTransaction(true);
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
        	this.endTransaction(false);
            throw new DalException(e);
        }
	}
	
	/**
	 * Equivalent to saveGame() but without a transactional context.
	 * @param game
	 * @param gameSet
	 * @throws DalException
	 */
	private void saveGameNonTransactionnal(final BaseGame game, final GameSet gameSet) throws DalException {
		try {
			checkArgument(game != null, "game is null");
			//checkArgument(!game.isPersisted(), "game was already persisted");
			checkArgument(gameSet != null, "gameSet is null");
			checkArgument(gameSet.isPersisted(), "gameSet hasn't been persisted");
			
            this.gameAdapter.storeGame(game, gameSet);
            this.playerAdapter.storeGamePlayers(game);
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
            throw new DalException(e);
        }
	}

	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#deleteGame(org.nla.tarotdroid.biz.BaseGame)
	 */
	@Override
	public void deleteGame(final BaseGame game, final GameSet gameSet) throws DalException {
		try {
			checkArgument(game != null, "game is null");
			checkArgument(game.isPersisted(), "game hasn't been persisted");
			
			this.beginTransaction();
            this.gameAdapter.deleteGame(game.getId());
            this.playerAdapter.deleteAllGamePlayersOfGame(game.getId());
            this.gameSetAdapter.updateGameSet(gameSet);
            gameSet.setSyncTimestamp(null);
            this.endTransaction(true);
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
        	this.endTransaction(false);
            throw new DalException(e);
        }	
	}

	/**
	 * @param player
	 */
	@Override
	public void savePlayer(final Player player) throws DalException {
		try {
			checkArgument(player != null, "player is null");
//            if (!player.isPersisted()) {
            	
            	// check whether player doesn't already exist in db, if so reuse it ands don't actually persist specified player
            	PersistableBusinessObject persistedPlayer = this.getPlayerByName(player.getName());
            	if (persistedPlayer != null) {
            		player.setId(persistedPlayer.getId());
            	}
            	// if no, persist specified player and add it to repository
            	else {
            		this.beginTransaction();
            		this.playerAdapter.storePlayer(player);
            		this.endTransaction(true);
            		//this.playerAdapter.getPlayersInRepository().put(player.getUuid(), player);
            	}
//            }
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
        	this.endTransaction(false);
            throw new DalException(e);
        }
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#updatePlayer(org.nla.tarotdroid.biz.Player)
	 */
	@Override
	public void updatePlayer(final Player player) throws DalException {
		try {
			checkArgument(player.isPersisted(), "player hasn't been persisted");
			this.beginTransaction();
			this.playerAdapter.updatePlayer(player);
			player.setSyncTimestamp(null);
//    		this.tracker.trackEntity(player, ActionTypes.Update, ObjectTypes.Player);
    		this.endTransaction(true);
		}
		catch (Exception e) {
			this.logBuffer.append(Throwables.getStackTraceAsString(e));
			e.printStackTrace();
			this.endTransaction(false);
			throw new DalException(e);
		}
	}
	

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#updatePlayerAfterSync(org.nla.tarotdroid.biz.Player)
	 */
	@Override
	public void updatePlayerAfterSync(final Player player, final String newUuid) throws DalException {
		try {
			//checkArgument(player.isPersisted(), "player hasn't been persisted");
			this.beginTransaction();
			this.playerAdapter.updatePlayerAfterSync(player, newUuid);
    		this.endTransaction(true);
		}
		catch (Exception e) {
			this.logBuffer.append(Throwables.getStackTraceAsString(e));
			e.printStackTrace();
			this.endTransaction(false);
			throw new DalException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getTrackers()
	 */
	public Map<ObjectTypes, Map<ActionTypes, List<String>>> getTrackers() {
		Map<ObjectTypes, Map<ActionTypes, List<String>>> trackers = null;
		try {
			trackers = this.tracker.getTrackedEntities();
		}
		catch (Exception e) {
			trackers = newHashMap();
		}
		return trackers;
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#deleteTrackersForObjectType(org.nla.tarotdroid.biz.enums.ObjectTypes)
	 */
	@Override
	public void deleteTrackersForObjectType(ObjectTypes objectType) throws DalException {
		try {
			this.tracker.clearTrackedEntitiesForObjects(objectType);
		}
		catch (Exception e) {
			this.logBuffer.append(Throwables.getStackTraceAsString(e));
			e.printStackTrace();
			throw new DalException(e);
		}
	}
	
	/**
	 * @param playerList
	 * @param gameSet
	 */
	private void savePlayersInGameSet(final GameSet gameSet) throws DalException {
		try {
			checkArgument(gameSet != null, "gameSet is null");
			checkArgument(gameSet.isPersisted(), "gameSet hasn't been persisted");
			checkArgument(gameSet.getPlayers() != null, "playerList is null");
			
            // persist each player and children		
            for (Player player : gameSet.getPlayers()) {
            	this.savePlayer(player);
            }
            
            // persist link between GameSet and Players
            this.playerAdapter.storeGameSetPlayers(gameSet);
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
            throw new DalException(e);
        }	
	}

	/**
	 * @param gameSetParamters
	 * @param gameSet
	 */
	private void saveGameSetParametersInGameSet(final GameSetParameters gameSetParameters, final GameSet gameSet) throws DalException {
		try {
			checkArgument(gameSetParameters != null, "gameSetParamters is null");
//			checkArgument(!gameSetParameters.isPersisted(), "gameSetParamters was already persisted");
			checkArgument(gameSet != null, "gameSet is null");
			checkArgument(gameSet.isPersisted(), "gameSet hasn't been persisted");
			
            this.gameSetParametersAdapter.storeGameSetParameters(gameSetParameters, gameSet.getId());
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
            throw new DalException(e);
        }
	}
	
	/**
	 * @param gameSetId
	 * @return
	 */
	private GameSetParameters getGameSetParametersForGameSetId(final long gameSetId) {
		GameSetParameters gameSetParameters = this.gameSetParametersAdapter.fetchGameSetParametersForGameSetId(gameSetId);
        return gameSetParameters;
	}
	
	/**
	 * @param gameSetId
	 * @return
	 */
	private PlayerList getPlayersForGameSetId(final long gameSetId) {
	    return this.playerAdapter.getGameSetsPlayerListsInRepository().get(gameSetId);
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#reInitDal()
	 */
	@Override
	public void reInitDal() throws DalException {
	    try {
            new TarotDatabaseHelper(this.context).initializeDb();
            
            // clear cached data
			this.playerAdapter.clear();
			this.gameAdapter.clear();
			this.gameSetAdapter.clear();
			this.gameSetParametersAdapter.clear();
			this.gameSets = new ArrayList<GameSet>();
        }
        catch (Exception e) {
        	this.logBuffer.append(Throwables.getStackTraceAsString(e));
        	e.printStackTrace();
            throw new DalException(e);
        }
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#close()
	 */
	@Override
	public void close() {
		try {
			this.database.close();
		} 
		catch (Exception e) {
			this.logBuffer.append(Throwables.getStackTraceAsString(e));
			e.printStackTrace();
		}
	}

	/**
	 * Starts a transactional context.
	 */
	private void beginTransaction() {
		this.database.beginTransaction();
		
	}

	/**
	 * Ends a transactional context, either as a rollback if commit = false or as a commit.
	 * @param commit
	 */
	private void endTransaction(final boolean commit) {
		if (commit) {
			this.database.setTransactionSuccessful();
		}
		this.database.endTransaction();
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.dal.IDalService#getLog()
	 */
	@Override
	public String getLog() {
		return this.logBuffer.toString();
	}
}
