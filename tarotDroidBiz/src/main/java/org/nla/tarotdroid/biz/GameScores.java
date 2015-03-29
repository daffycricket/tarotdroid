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
package org.nla.tarotdroid.biz;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *	A game result, including the score of each player. 
 */
public class GameScores implements Serializable {
	
	/**
     * Serial version ID.
     */
    private static final long serialVersionUID = -4048152045987570259L;
    
    /**
	 * A map of game score of each player.
	 */
	private MapPlayersScores results;
	
	/**
	 * Default constructor.
	 */
	public GameScores() {
		this.results = new MapPlayersScores(); 
	}

	/**
	 * Sets the game score of a player. 
	 * @param player the player.
	 * @param score his score.
	 */
	public void addScore(final Player player, final int score) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		this.results.put(player, score);
	}
	
	/**
	 * Adds points to the game score of a player.
	 * @param player the player.
	 * @param toAdd the points to add.
	 */
	public void updateScore(final Player player, final int toAdd) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		int currentPlayerScore = this.results.get(player);
		this.results.put(player, currentPlayerScore + toAdd);
	}
	
	/**
	 * Returns individual game score of a player. Returns 0 if player is not present in the game player list.
	 * @param player the player.
	 */
	public int getIndividualResult(final PersistableBusinessObject player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		
		// Unit test to add to this rule.
		if (!this.results.containsKey(player)) {
			return 0;
		}
		
		return this.results.get(player);
	}
	
	/**
	 * Returns game score of all players.
	 * @return game score of all players.
	 */
	public MapPlayersScores getResults() {
		return this.results;
	}
	
	/**
	 * Returns the minimum score for this game.
	 * @return the minimum score for this game.
	 */
	public int getMinScore() {
	    return this.results.getMinScore();
	}
	
	/**
     * Returns the maximum score for this game.
     * @return the maximum score for this game.
     */
    public int getMaxScore() {
        return this.results.getMaxScore();
    }
    
    /**
     * Returns a collection of the scores.
     * @return
     */
    public Collection<Integer> values() {
    	return this.results.values();
    }
    
    /**
     * Indicates whether the specified player has the highest score.
     * @param player
     * @return
     */
    public boolean isWinner(final PersistableBusinessObject player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
    	return this.getMaxScore() == this.getIndividualResult(player);
    }
}