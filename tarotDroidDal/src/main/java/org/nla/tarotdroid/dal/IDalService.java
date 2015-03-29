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
package org.nla.tarotdroid.dal;

import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.enums.ActionTypes;
import org.nla.tarotdroid.biz.enums.ObjectTypes;

/**
 * TODO : Decouple from  org.nla.tarotdroid.dal.sql.adapters.EntityTrackerDatabaseAdapter
 * TODO : Delete this interface 
 *          - no other implementation of the DAL service will ever be written
 *          - strong coupling exists btw app and sql dal service through one static method
 * @author nicocaro
 *
 */
public interface IDalService {

	void initialize() throws DalException;
	
	/**
	 * Indicates whether this implementation actually stores GameSets and Games.
	 * @return true if this implementation stores GameSets and Games, false otherwise.
	 */
	boolean isStoringGameSets();
	
	/**
	 * Returns all players in the repository.
	 * @return all players in the repository.
	 */
	List<Player> getAllPlayers();
	
	/**
	 * Returns the player for his name.
	 * @param name
	 * @return the player for his name.
	 */
	Player getPlayerByName(String name) throws DalException;

	/**
	 * Returns the player for his uuid.
	 * @param uuid
	 * @return the player for his uuid.
	 */
	Player getPlayerByUuid(String uuid) throws DalException;
	
	/**
	 * Persists a player if it's not already persisted.
	 * @param player
	 * @throws DalException
	 */
	void savePlayer(final Player player) throws DalException;
	
	/**
	 * Updates a player that was previously persisted.
	 * @param player
	 * @throws DalException
	 */
	void updatePlayer(final Player player) throws DalException;
	
	/**
	 * Updates the cloud info of a player that was previously persisted.
	 * @param player
	 * @param newUuid
	 * @throws DalException
	 */
	void updatePlayerAfterSync(Player player, String newUuid) throws DalException;

	/**
	 * Loads all the GameSets into the repository.
	 */
	void loadAllGameSets() throws DalException;
	
	/**
	 * Returns all the GameSets previously loaded by a call to loadAllGameSets().
	 * @return all the GameSets previously loaded by a call to loadAllGameSets().
	 */
	List<GameSet> getAllGameSets();
	
	/**
	 * Returns the GameSet of specified id.
	 * @param id The GameSet id.
	 * @return The GameSet of specified id
	 */
	GameSet getGameSetById(long id);
	
	/**
	 * Returns the game set count in the repository.
	 * @return the game set count in the repository.
	 */
	int getGameSetCount() throws DalException;

	/**
	 * Persists the specified GameSet and all its children..
	 * @param gameSet the GameSet to persist.
	 */
	void saveGameSet(GameSet gameSet) throws DalException;
		
	/**
	 * Deletes the specified GameSet.
	 * @param gameSet the GameSet to delete.
	 */
	void deleteGameSet(GameSet gameSet) throws DalException;
	
	/**
	 * Persists the specified BaseGame, that is either creates it if it doesn't exist.
	 * @param game the BaseGame to persist.
	 * @param gameSet the GameSet associated to the BaseGame.
	 */
	void saveGame(BaseGame game, GameSet gameSet) throws DalException;
	
	/**
	 * Updates the cloud info of a gameSet that was previously persisted.
	 * @param gameSet
	 * @throws DalException
	 */
	void updateGameSetAfterSync(final GameSet gameSet) throws DalException;

	/**
	 * Updates a game that was previously persisted.
	 * @param game
	 * @throws DalException
	 */
	void updateGame(BaseGame game, GameSet gameSet) throws DalException;
	
	/**
	 * Deletes the specified BaseGame.
	 * @param game the BaseGame to delete.
	 * @param gameSet the GameSet associated to the BaseGame.
	 */
	void deleteGame(BaseGame game, GameSet gameSet) throws DalException;
	
	/**
	 * Returns all entity trackers.
	 * @return all the entity trackers.
	 */
	Map<ObjectTypes, Map<ActionTypes, List<String>>> getTrackers();
	
	/**
	 * Deletes tracker for given object type.
	 * @param objectType
	 */
	void deleteTrackersForObjectType(ObjectTypes objectType)  throws DalException;
	
	/**
	 * Fully reinitialize underlying structure.
	 */
	void reInitDal() throws DalException;
	
	/**
	 * Allows to close the inner context.
	 */
	void close();

	/**
	 * @return
	 */
	String getLog();
}