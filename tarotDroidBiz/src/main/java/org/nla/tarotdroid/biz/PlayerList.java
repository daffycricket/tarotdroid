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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class PlayerList extends PersistableBusinessObject implements Iterable<Player>, Serializable {

	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
    private static final long serialVersionUID = 2084754563287251480L;
    
    /**
	 * The players as a List.
	 */
	private List<Player> players;
	
	/**
	 * Default contstructor.
	 */
	public PlayerList() {
		this(null);
	}
	
	/**
	 * @param players
	 */
	public PlayerList(final List<Player> players) {
		this.players = new ArrayList<Player>();
		if (players != null) {
			for (Player player : players) {
				this.add(player);
			}
		}
	}

	/**
	 * Returns a copy of the list containing the players.
	 * @return a copy of the list containing the players.
	 */
	public List<Player> getPlayers() {
		List<Player> copy = new ArrayList<Player>();
		copy.addAll(this.players);
		return copy;
	}

	/**
	 * @param players the players to set.
	 */
	protected void setPlayers(final List<Player> players) {
		this.players = players;
	}
	
	/**
	 * Sets the player name to player of specified index (starting at 1). 
	 * index can't be under 1 or above this.size()
	 * @param index
	 * @param name
	 */
	public void setPlayerName(final int index, final String name) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}
		if (index < 1) {
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "index can't be under 1 : %d", index));
		}
		if (index > this.size()) {
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "index can't be above %d : %d", this.size(), index));
		}
		this.players.get(index - 1).setName(name);
	}
	
	
	/**
	 * Adds a player to the list by specifying his name.
	 * @param player
	 * @return
	 */
	public boolean add(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}
		Player newPlayer = new Player();
		newPlayer.setName(name);
		return this.players.add(newPlayer);
	}
	
	/**
	 * Adds a player to the list.
	 * @param player
	 * @return
	 */
	public boolean add(final Player player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		return this.players.add(player);
	}
	
	/**
	 * Removes a player from the list by specifying his name.
	 * @param name
	 * @return
	 */
	public boolean remove(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}
		Player newPlayer = new Player();
		newPlayer.setName(name);
		return this.players.remove(newPlayer);
	}
	
	/**
	 * Removes a player from the list.
	 * @param player
	 * @return
	 */
	public boolean remove(final PersistableBusinessObject player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		return this.players.remove(player);
	}
	
	/**
	 * Returns the size of the list.
	 * @return the size of the list.
	 */
	public int size() {
		return this.players.size();
	}
	
	/**
	 * Indicates whether the list contains the specified player.
	 * @param player
	 * @return
	 */
	public boolean contains(final PersistableBusinessObject player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		return this.players.contains(player);
	}
	
	/**
	 * Gets the player at specified index.
	 * index can't be under 1 or above this.size()
	 * @param index
	 * @return
	 */
	public Player get(final int index) {
		if (index < 1) {
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "index can't be under 1 : %d", index));
		}
		if (index > this.size()) {
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "index can't be above %d : %d", this.size(), index));
		}	
		return this.players.get(index - 1);
	}

	/**
	 * Returns a view of the portion of this list between the specified minIndex, inclusive, and maxIndex, inclusive.
	 * minIndex can't be under 1, maxIndex can't be above this.size()
	 * @param minIndex
	 * @param maxIndex
	 * @return
	 */
	public PlayerList subList(final int minIndex, final int maxIndex) {
		if (minIndex < 1) {
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "minIndex can't be under 1 : %d", minIndex));
		}
		if (maxIndex > this.size()) {
			throw new IllegalArgumentException(String.format(Locale.getDefault(), "maxIndex can't be above %d : %d", this.size(), maxIndex));
		}		
		PlayerList toReturn = new PlayerList();
		toReturn.setPlayers(this.players.subList(minIndex - 1, maxIndex));
		return toReturn;
	}
	
	/**
	 * Returns the player right after the specified player. If the specified player is the last in the list, then
	 * the first player is returned.  
	 * @param player
	 * @return the player right after the specified player.
	 */
	public Player getNextPlayer(final PersistableBusinessObject player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		if (!this.contains(player)) {
			throw new IllegalArgumentException("player isn't in the player list=" + player);
		}
		
		int playerIndex = this.players.indexOf(player);
		int nextPlayerIndex = ++playerIndex; 
		if (nextPlayerIndex == this.players.size()) {
			nextPlayerIndex = 0;
		}
		
		return this.players.get(nextPlayerIndex);
	}
	
	/**
     * Returns the player names.
     * @return the player names as a String[]. 
     */
    public String[] getPlayerNames() {
        String[] playerNames = new String[this.size()];
        for (int i = 1; i <= this.size(); ++i) {
            playerNames[i - 1] = this.get(i).toString();
        }
        return playerNames;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Player> iterator() {
		return this.players.iterator();
	}
}
