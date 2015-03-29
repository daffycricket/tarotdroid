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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.nla.tarotdroid.biz.computers.BaseGameScoresComputer;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public abstract class BaseGame extends PersistableBusinessObject implements Serializable {
	
	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
    private static final long serialVersionUID = 3092786288170636063L;

    /**
	 * Game set parameters.
	 */
    protected GameSetParameters gameSetParameters;

	/**
	 * The list of players in the game.
	 */
    protected PlayerList players;
	
	/**
	 * The list of dead players.
	 */
	private PlayerList deadPlayers;
	
	/**
	 * The dealer player.
	 */
	private Player dealer;
	
	/**
	 * A map of players in the game to retrieve them by their names;
	 */
	@CloudField(cloudify=false)
	private Map<String, Player> playersMap;
	
	/**
	 * Game index in game set, starting at 1.
	 */
	private int index;
	
	/**
	 * Highest game index in the game set, starting at 1.
	 */
	private int highestGameIndex;
	
	/**
	 * Creation timestamp.
	 */
	private Date creationTs;
	
	/**
	 * Game score computer.
	 */
	@CloudField(cloudify=false)
	protected BaseGameScoresComputer computer;
	
	/**
	 * Default constructor.
	 */
	public BaseGame() {
		super();
		this.players = new PlayerList();
		this.deadPlayers = new PlayerList();
		this.playersMap = new HashMap<String, Player>();
		this.creationTs = new Date(System.currentTimeMillis());
	}

	/**
	 * @return the gameSetParameters
	 */
	public GameSetParameters getGameSetParameters() {
		return this.gameSetParameters;
	}

	/**
	 * @param gameSetParameters the gameSetParameters to set
	 */
	public void setGameSetParameters(final GameSetParameters gameSetParameters) {
		this.gameSetParameters = gameSetParameters;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(final int index) {
		this.index = index;
	}
	
	/**
	 * @return the reverse index, being highest game index - index.
	 */
	public int getReverseIndex() {
		return this.highestGameIndex - this.index;
	}
	
	/**
	 * Returns the list of players.
	 * @return the list of players.
	 */
	public PlayerList getPlayers() {
		return this.players;
	}
	
	/**
	 * Returns the list of dead players.
	 * @return the list of players.
	 */
	public PlayerList getDeadPlayers() {
		return this.deadPlayers;
	}

	/**
	 * Returns the first dead player.
	 * @return the first dead player.
	 */
	public Player getDeadPlayer() {
		if (this.deadPlayers != null && this.deadPlayers.size() >= 1) {
			return this.deadPlayers.get(1);
		}
		return null;
	}

	/**
	 * Sets the list of players in the game.
	 * @param players the list of players in the game to set.
	 */
	public void setPlayers(final PlayerList players) {
		if (players == null) {
			throw new IllegalArgumentException("players=" + players);
		}
	
		this.players = players;
		for (Player player : this.players) {
			this.playersMap.put(player.getName().toLowerCase(), player);
		}
	}
	
	/**
	 * Sets the list of dead players in the game.
	 * @param players the list of players in the game to set.
	 */
	public final void setDeadPlayers(final PlayerList players) {
		if (players == null) {
			this.deadPlayers = new PlayerList();
		}
		else {
			this.deadPlayers = players;
		}
		
		for (Player player : this.players) {
			this.playersMap.put(player.getName().toLowerCase(), player);
		}
	}
	
	/**
	 * Sets a dead player in the game.
	 * @param player the dead player in the game to set.
	 */
	public void setDeadPlayer(final Player player) {
		if (player != null) {
			PlayerList players = new PlayerList();
			players.add(player);
			this.setDeadPlayers(players);
		}
	}

	/**
	 * Returns the player depending on the given name. 
	 * @param name the name of the player.
	 * @return the player depending on the given name.
	 */
	public Player getPlayerForName(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}
		return this.playersMap.get(name.toLowerCase());
	}

	/**
	 * Returns the dealer.
	 * @return the dealer
	 */
	public Player getDealer() {
		return this.dealer;
	}

	/**
	 * Sets the dealer.
	 * @param dealer the dealer to set
	 */
	public void setDealer(final Player dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the highestGameIndex
	 */
	public int getHighestGameIndex() {
		return this.highestGameIndex;
	}
	
	/**
	 * @return the highestGameIndex
	 */
	public boolean isLatestGame() {
		return this.highestGameIndex == this.index;
	}	

	/**
	 * @param highestGameIndex the highestGameIndex to set
	 */
	public void setHighestGameIndex(final int highestGameIndex) {
		if (highestGameIndex < 1) {
			throw new IllegalArgumentException("highestGameIndex<1=" + highestGameIndex);
		}
		this.highestGameIndex = highestGameIndex;
	}
	
	/**
	 * @return the creationTs
	 */
	public Date getCreationTs() {
		return this.creationTs;
	}

	/**
	 * @param creationTs the creationTs to set
	 */
	public void setCreationTs(final Date creationTs) {
		this.creationTs = creationTs;
	}
	
	/**
	 * @return the score computer
	 */
	public BaseGameScoresComputer getComputer() {
		return this.computer;
	}
	
	/**
	 * Indicates whether the specified player won this game.
	 * @param player
	 * @return
	 */
	public boolean isWinner(final Player player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		return this.getGameScores().isWinner(player);
	}

	/**
	 * Computes and returns the game scores, if they haven't yet been computed. Otherwise, returns the scores previously computed.
	 * @return the game scores.
	 */
	public abstract GameScores getGameScores();
}
