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
package org.nla.tarotdroid.cloud.clientmodel;

import java.util.List;
import java.util.UUID;

import com.google.common.base.Objects;

/**
 * RestGameSet entity.
 */
public class RestGameSet {

//	/**
//	 * The unique id.
//	 */
//	private Long id;
//	
//	/**
//	 * Device local id.
//	 */
//	private Long localId;
	
	/**
	 * Creation timestamp.
	 */
	private long creationTs;
	
	/**
	 * Is valid property.
	 */
	private boolean isValid;
	
	/**
	 * A UUID identifying the object.
	 */
	private String uuid;

	/**
	 * GameSetParameters property.
	 */
	private RestGameSetParameters gameSetParameters;

	/**
	 * GameStyleType property.
	 */
	private RestGameStyleTypes gameStyleType;

	/**
	 * Players property.
	 */
	private List<RestPlayer> players;
	
	/**
	 * Games property.
	 */
	private List<RestGame> games;
	
	/**
	 * Default constructor.
	 */
	public RestGameSet() {
		this.isValid = true;
		this.uuid = UUID.randomUUID().toString();
	}
	
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<RestGame> getGames() {
		return this.games;
	}
	
	public void setGames(List<RestGame> games) {
		this.games = games;
	}

	public List<RestPlayer> getPlayers() {
		return this.players;
	}

	public void setPlayers(List<RestPlayer> players) {
		this.players = players;
	}
	
	public RestGameSetParameters getGameSetParameters() {
		return this.gameSetParameters;
	}

	public void setGameSetParameters(RestGameSetParameters gameSetParameters) {
		this.gameSetParameters = gameSetParameters;
	}

	public long getCreationTs() {
		return this.creationTs;
	}

	public void setCreationTs(long creationTs) {
		this.creationTs = creationTs;
	}
	
	public RestGameStyleTypes getGameStyleType() {
		return this.gameStyleType;
	}

	public void setGameStyleType(RestGameStyleTypes gameStyleType) {
		this.gameStyleType = gameStyleType;
	}
	
	public boolean isValid() {
		return this.isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
//	public Long getLocalId() {
//		return this.localId;
//	}
//
//	public void setLocalId(Long localId) {
//		this.localId = localId;
//	}
//	
//	public Long getId() {
//		return this.id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("uuid", this.uuid)
				.add("creationTs", this.creationTs)
				.add("gameSetParameters", this.gameSetParameters)
				.add("gameStyleType", this.gameStyleType)
				.add("players", this.players)
				.add("games", this.games)
				.add("isValid", this.isValid)
				.toString();
	}
}