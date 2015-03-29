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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.primitives.Ints;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public final class MapPlayersScores implements Serializable {

	/**
     * Serial version ID.
     */
    private static final long serialVersionUID = -4240946536946538648L;
    
    /**
	 * Map of players scores.
	 */
	private Map<Player, Integer> mapPlayersScores;
    
	/**
	 *	Default constructor.
	 */
	public MapPlayersScores() {
		super();
		this.mapPlayersScores = new HashMap<Player, Integer>();
	}

	/**
	 * Gets the player score.
	 * @param player
	 * @return
	 */
	public Integer get(final PersistableBusinessObject player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		return this.mapPlayersScores.get(player);
	}

	/**
	 * Adds a new player score.
	 * @param player
	 * @param score
	 * @return
	 */
	public void put(final Player player, final int score) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		this.mapPlayersScores.put(player, score);    
	}

	/**
	 * Indicates whether player score is set.
	 * @param player
	 * @return
	 */
	public boolean containsKey(final PersistableBusinessObject player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		return this.mapPlayersScores.containsKey(player);
	}
	
	/**
	 * Returns a collection of the score values.
	 * @return
	 */
	public Collection<Integer> values() {
		return this.mapPlayersScores.values();
	}

	/**
	 * Gets the player rank.
	 * @param player
	 * @return
	 */
	public int getPlayerRank(final PersistableBusinessObject player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}

		// get a sorted list of the player scores
		List<Integer> sortedResults = new ArrayList<Integer>(this.mapPlayersScores.values());
		Collections.sort(sortedResults, Collections.reverseOrder());

		// check player score against each ordered score in the list.
		// the index of the equality is the rank of the user 
		for (int i = 1; i <= sortedResults.size(); ++i) {
			if (sortedResults.get(i - 1).intValue() == this.mapPlayersScores.get(player).intValue())
			{
				return i;
			}
		}

		return 0;
	}
	
	/**
	 * Gets the first player.
	 * @return the first player.
	 */
	public PersistableBusinessObject getFirstPlayer() {
		Player firstPlayer = null;
		int aexEquoCount = 0;
		int maxScore = this.getMaxScore(); 
		for(final Player player : this.mapPlayersScores.keySet()) {
			if (this.mapPlayersScores.get(player) == maxScore) {
				firstPlayer = player;
				aexEquoCount +=1;
			}
		}
		
		return aexEquoCount <= 1 ? firstPlayer : null;
	}

    /**
     * Returns the maximum score.
     * @return the maximum score.
     */
    public int getMaxScore() {
    	return Ints.max(Ints.toArray(this.mapPlayersScores.values()));
    }

    /**
     * Returns the minimum score.
     * @return the minimum score.
     */
    public int getMinScore() {
    	return Ints.min(Ints.toArray(this.mapPlayersScores.values()));
    }
}
