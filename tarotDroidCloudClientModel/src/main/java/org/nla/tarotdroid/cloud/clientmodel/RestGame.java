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

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Objects;

/**
 * RestGame entity.
 */
/**
 * @author Nico
 *
 */
@SuppressWarnings("serial")
public class RestGame implements Serializable{
	
	/**
	 * Creation Timestamp property.
	 */
	private long creationTs;
	
	/**
	 * Game type.
	 */
	private RestGameTypes gameType;
	
	/**
	 * Players property.
	 */
	private List<RestPlayer> players;
	
	/**
	 * DeadPlayers property.
	 */
	private List<RestPlayer> deadPlayers;
	
	/**
	 * Dealer property.
	 */
	private RestPlayer dealer;
	
	/**
	 * Index property.
	 */
	private int index;
	
	/**
	 * LeadingPlayer property.
	 */
	private RestPlayer leadingPlayer;

	/**
	 * CalledPlayer property.
	 */
	private RestPlayer calledPlayer;
	
	/**
	 * CalledKing property.
	 */
	private RestColorTypes calledKing;
	
	/**
	 * Bet property.
	 */
	private RestBetTypes bet;
	
	/**
	 * NumberOfOudlers property.
	 */
	private int numberOfOudlers;
	
	/**
	 * Points property.
	 */
	private double points;
	
	/**
	 * TeamWithPoignee property.
	 */
	private RestTeamTypes teamWithPoignee;
	
	/**
	 * TeamWithDoublePoignee property.
	 */
	private RestTeamTypes teamWithDoublePoignee;
	
	/**
	 * TeamWithTriplePoignee property.
	 */
	private RestTeamTypes teamWithTriplePoignee;
	
	/**
	 * TeamWithKidAtTheEnd property.
	 */
	private RestTeamTypes teamWithKidAtTheEnd;
	
	/**
	 * PlayersWithMisery property.
	 */
	private List<RestPlayer> playersWithMisery;
	
	/**
	 * Chelem property.
	 */
	private RestSlamTypes chelem;
	
	/**
	 * First property.
	 */
	private RestPlayer first;
	
	/**
	 * Second property.
	 */
	private RestPlayer second;
	
	/**
	 * Third property.
	 */
	private RestPlayer third;
	
	/**
	 * Fourth property.
	 */
	private RestPlayer fourth;
	
	/**
	 * Fifth property.
	 */
	private RestPlayer fifth;
	
	/**
	 * Penalted property.
	 */
	private RestPlayer penalted;
	
	/**
	 * Penalty points property.
	 */
	private int penaltyPoints;
	
	/**
	 * Penalty type property.
	 */
	private String penaltyType;
	
	/**
	 * A UUID identifying the object.
	 */
	private String uuid;
	
	/**
	 * Default constructor.
	 */
	public RestGame() {
		this.uuid = UUID.randomUUID().toString();
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public long getCreationTs() {
		return this.creationTs;
	}

	public void setCreationTs(long creationTs) {
		this.creationTs = creationTs;
	}
	
	public RestGameTypes getGameType() {
		return this.gameType;
	}

	public void setGameType(RestGameTypes gameType) {
		this.gameType = gameType;
	}

	/**
	 * @return players property.
	 */
	public java.util.List<RestPlayer> getPlayers() {
		return this.players;
	}

	/**
	 * @param players
	 */
	public void setPlayers(java.util.List<RestPlayer> players) {
		this.players = players;
	}
	
	/**
	 * @return deadPlayers property.
	 */
	public java.util.List<RestPlayer> getDeadPlayers() {
		return this.deadPlayers;
	}

	/**
	 * @param deadPlayers
	 */
	public void setDeadPlayers(java.util.List<RestPlayer> deadPlayers) {
		this.deadPlayers = deadPlayers;
	}
	
	/**
	 * @return dealer property.
	 */
	public RestPlayer getDealer() {
		return this.dealer;
	}

	/**
	 * @param dealer
	 */
	public void setDealer(RestPlayer dealer) {
		this.dealer = dealer;
	}
	
	/**
	 * @return index property.
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/**
	 * @return leadingPlayer property.
	 */
	public RestPlayer getLeadingPlayer() {
		return this.leadingPlayer;
	}

	/**
	 * @param leadingPlayer
	 */
	public void setLeadingPlayer(RestPlayer leadingPlayer) {
		this.leadingPlayer = leadingPlayer;
	}
	
	/**
	 * @return calledPlayer property.
	 */
	public RestPlayer getCalledPlayer() {
		return this.calledPlayer;
	}

	/**
	 * @param calledPlayer
	 */
	public void setCalledPlayer(RestPlayer calledPlayer) {
		this.calledPlayer = calledPlayer;
	}
	
	/**
	 * @return calledKing property.
	 */
	public RestColorTypes getCalledKing() {
		return this.calledKing;
	}

	/**
	 * @param calledKing
	 */
	public void setCalledKing(RestColorTypes calledKing) {
		this.calledKing = calledKing;
	}
	
	/**
	 * @return teamWithPoignee property.
	 */
	public RestTeamTypes getTeamWithPoignee() {
		return this.teamWithPoignee;
	}

	/**
	 * @param teamWithPoignee
	 */
	public void setTeamWithPoignee(RestTeamTypes teamWithPoignee) {
		this.teamWithPoignee = teamWithPoignee;
	}
	
	/**
	 * @return teamWithDoublePoignee property.
	 */
	public RestTeamTypes getTeamWithDoublePoignee() {
		return this.teamWithDoublePoignee;
	}

	/**
	 * @param teamWithDoublePoignee
	 */
	public void setTeamWithDoublePoignee(RestTeamTypes teamWithDoublePoignee) {
		this.teamWithDoublePoignee = teamWithDoublePoignee;
	}
	
	/**
	 * @return teamWithTriplePoignee property.
	 */
	public RestTeamTypes getTeamWithTriplePoignee() {
		return this.teamWithTriplePoignee;
	}

	/**
	 * @param teamWithTriplePoignee
	 */
	public void setTeamWithTriplePoignee(RestTeamTypes teamWithTriplePoignee) {
		this.teamWithTriplePoignee = teamWithTriplePoignee;
	}
	
	/**
	 * @return teamWithKidAtTheEnd property.
	 */
	public RestTeamTypes getTeamWithKidAtTheEnd() {
		return this.teamWithKidAtTheEnd;
	}

	/**
	 * @param teamWithKidAtTheEnd
	 */
	public void setTeamWithKidAtTheEnd(RestTeamTypes teamWithKidAtTheEnd) {
		this.teamWithKidAtTheEnd = teamWithKidAtTheEnd;
	}
	
	/**
	 * @return playersWithMisery property.
	 */
	public java.util.List<RestPlayer> getPlayersWithMisery() {
		return this.playersWithMisery;
	}

	/**
	 * @param playersWithMisery
	 */
	public void setPlayersWithMisery(java.util.List<RestPlayer> playersWithMisery) {
		this.playersWithMisery = playersWithMisery;
	}
	
	/**
	 * @return bet property.
	 */
	public RestBetTypes getBet() {
		return this.bet;
	}

	/**
	 * @param bet
	 */
	public void setBet(RestBetTypes bet) {
		this.bet = bet;
	}
	
	/**
	 * @return numberOfOudlers property.
	 */
	public int getNumberOfOudlers() {
		return this.numberOfOudlers;
	}

	/**
	 * @param numberOfOudlers
	 */
	public void setNumberOfOudlers(int numberOfOudlers) {
		this.numberOfOudlers = numberOfOudlers;
	}
	
	/**
	 * @return points property.
	 */
	public double getPoints() {
		return this.points;
	}

	/**
	 * @param points
	 */
	public void setPoints(double points) {
		this.points = points;
	}
	
	/**
	 * @return chelem property.
	 */
	public RestSlamTypes getChelem() {
		return this.chelem;
	}

	/**
	 * @param chelem
	 */
	public void setChelem(RestSlamTypes chelem) {
		this.chelem = chelem;
	}
	
	/**
	 * @return first property.
	 */
	public RestPlayer getFirst() {
		return this.first;
	}

	/**
	 * @param first
	 */
	public void setFirst(RestPlayer first) {
		this.first = first;
	}
	
	/**
	 * @return second property.
	 */
	public RestPlayer getSecond() {
		return this.second;
	}

	/**
	 * @param second
	 */
	public void setSecond(RestPlayer second) {
		this.second = second;
	}
	
	/**
	 * @return third property.
	 */
	public RestPlayer getThird() {
		return this.third;
	}

	/**
	 * @param third
	 */
	public void setThird(RestPlayer third) {
		this.third = third;
	}

	/**
	 * @return fourth property.
	 */
	public RestPlayer getFourth() {
		return this.fourth;
	}

	/**
	 * @param fourth
	 */
	public void setFourth(RestPlayer fourth) {
		this.fourth = fourth;
	}
	
	/**
	 * @return fifth property.
	 */
	public RestPlayer getFifth() {
		return this.fifth;
	}

	/**
	 * @param fifth
	 */
	public void setFifth(RestPlayer fifth) {
		this.fifth = fifth;
	}

	/**
	 * @return the penalted
	 */
	public RestPlayer getPenalted() {
		return penalted;
	}

	/**
	 * @param penalted the penalted to set
	 */
	public void setPenalted(RestPlayer penalted) {
		this.penalted = penalted;
	}

	/**
	 * @return the penaltyPoints
	 */
	public int getPenaltyPoints() {
		return penaltyPoints;
	}

	/**
	 * @param penaltyPoints the penaltyPoints to set
	 */
	public void setPenaltyPoints(int penaltyPoints) {
		this.penaltyPoints = penaltyPoints;
	}

	/**
	 * @return the penaltyType
	 */
	public String getPenaltyType() {
		return penaltyType;
	}

	/**
	 * @param penaltyType the penaltyType to set
	 */
	public void setPenaltyType(String penaltyType) {
		this.penaltyType = penaltyType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("uuid", this.index)
				.add("index", this.index)
				.add("dealer", this.dealer)
				.add("players", this.players)
				.add("deadPlayers", this.deadPlayers)
				.add("leadingPlayer", this.leadingPlayer)
				.add("bet", this.bet)
				.add("calledPlayer", this.calledPlayer)
				.add("calledKing", this.calledKing)
				.add("numberOfOudlers", this.numberOfOudlers)
				.add("points", this.points)
				.add("teamWithPoignee", this.teamWithPoignee)
				.add("teamWithDoublePoignee", this.teamWithDoublePoignee)
				.add("teamWithTriplePoignee", this.teamWithTriplePoignee)
				.add("teamWithKidAtTheEnd", this.teamWithKidAtTheEnd)
				.add("playersWithMisery", this.playersWithMisery)
				.add("chelem", this.chelem)
				.add("first", this.first)
				.add("second", this.second)
				.add("third", this.third)
				.add("fourth", this.fourth)
				.add("fifth", this.fifth)
				.add("penalted", this.penalted)
				.add("penaltyPoints", this.penaltyPoints)
				.add("penaltyType", this.penaltyType)
				.toString();
	}
}