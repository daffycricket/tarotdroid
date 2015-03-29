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
package org.nla.tarotdroid.lib.app;

/**
 *	Application params.
 */
public class AppParams {
	
	/**
	 * Flag indicating in which order to display the games.
	 */
	private transient boolean isDisplayGamesInReverseOrder = true;
	
	/**
	 * Flag indicating whether to display the global scores for each game, or the scores of the game.
	 */
	private transient boolean isDisplayGlobalScoresForEachGame = true;

	/**
	 * Flag indicating whether to display the next dealer.
	 */
	private transient boolean isDisplayNextDealer = false;	

	/**
	 * Flag indicating whether Petite bet is authorized.
	 */
	private transient boolean isPetiteAuthorized = false;
	
	/**
	 * Flag indicating whether Prise bet is authorized.
	 */
	private transient boolean isPriseAuthorized = true;
	
	/**
	 * Flag indicating whether the misery annoucement is authorized at 3 or 4 player style.
	 */
	private transient boolean isMiseryAuthorized = false;
	
	/**
	 * Flag indicating whether to allow passed games.
	 */
	private transient boolean isPassedGamesAllowed = false;
	
	/**
	 * Flag indicating whether to allow penalty games.
	 */
	private transient boolean isPenaltyGamesAllowed = false;

	/**
	 * Flag indicating whether to allow belgian games.
	 */
	private transient boolean isBelgianGamesAllowed = false;
	
	/**
	 * Flag indicating whether to allow dead players.
	 */
	private transient boolean isDeadPlayerAuthorized = true;
	
	/**
	 * Flag indicating whether to automatically select the dead player.
	 */
	private transient boolean isDeadPlayerAutomaticallySelected = true;

	/**
	 * Flag indicating whether the game is in simulation mode.
	 */
	private transient boolean isDevSimulationMode = false;

	/**
	 * Flag indicating the game set count (dev property).
	 */
	private transient int devGameSetCount = 50;
	
	/**
	 * Flag indicating the maximum game count (dev property).
	 */
	private transient int devMaxGameCount = 50;
	
	/**
	 * Flag indicating whether the scrren must be kept on.
	 */
	private transient boolean isKeepScreenOn = true;

	/**
	 * @return the isDisplayGamesInReverseOrder
	 */
	public boolean isDisplayGamesInReverseOrder() {
		return this.isDisplayGamesInReverseOrder;
	}

	/**
	 * @param isDisplayGamesInReverseOrder the isDisplayGamesInReverseOrder to set
	 */
	public void setDisplayGamesInReverseOrder(
			boolean isDisplayGamesInReverseOrder) {
		this.isDisplayGamesInReverseOrder = isDisplayGamesInReverseOrder;
	}

	/**
	 * @return the isDisplayGlobalScoresForEachGame
	 */
	public boolean isDisplayGlobalScoresForEachGame() {
		return this.isDisplayGlobalScoresForEachGame;
	}

	/**
	 * @param isDisplayGlobalScoresForEachGame the isDisplayGlobalScoresForEachGame to set
	 */
	public void setDisplayGlobalScoresForEachGame(
			boolean isDisplayGlobalScoresForEachGame) {
		this.isDisplayGlobalScoresForEachGame = isDisplayGlobalScoresForEachGame;
	}

	/**
	 * @return the isDisplayNextDealer
	 */
	public boolean isDisplayNextDealer() {
		return this.isDisplayNextDealer;
	}

	/**
	 * @param isDisplayNextDealer the isDisplayNextDealer to set
	 */
	public void setDisplayNextDealer(boolean isDisplayNextDealer) {
		this.isDisplayNextDealer = isDisplayNextDealer;
	}

	/**
	 * @return the isPetiteAuthorized
	 */
	public boolean isPetiteAuthorized() {
		return this.isPetiteAuthorized;
	}

	/**
	 * @param isPetiteAuthorized the isPetiteAuthorized to set
	 */
	public void setPetiteAuthorized(boolean isPetiteAuthorized) {
		this.isPetiteAuthorized = isPetiteAuthorized;
	}

	/**
	 * @return the isPriseAuthorized
	 */
	public boolean isPriseAuthorized() {
		return this.isPriseAuthorized;
	}

	/**
	 * @param isPriseAuthorized the isPriseAuthorized to set
	 */
	public void setPriseAuthorized(boolean isPriseAuthorized) {
		this.isPriseAuthorized = isPriseAuthorized;
	}

	/**
	 * @return the isMiseryAuthorized
	 */
	public boolean isMiseryAuthorized() {
		return this.isMiseryAuthorized;
	}

	/**
	 * @param isMiseryAuthorized the isMiseryAuthorized to set
	 */
	public void setMiseryAuthorized(boolean isMiseryAuthorized) {
		this.isMiseryAuthorized = isMiseryAuthorized;
	}

	/**
	 * @return the isPassedGamesAllowed
	 */
	public boolean isPassedGamesAllowed() {
		return this.isPassedGamesAllowed;
	}

	/**
	 * @param isPassedGamesAllowed the isPassedGamesAllowed to set
	 */
	public void setPassedGamesAllowed(boolean isPassedGamesAllowed) {
		this.isPassedGamesAllowed = isPassedGamesAllowed;
	}

	/**
	 * @return the isPenaltyGamesAllowed
	 */
	public boolean isPenaltyGamesAllowed() {
		return this.isPenaltyGamesAllowed;
	}

	/**
	 * @param isPenaltyGamesAllowed the isPenaltyGamesAllowed to set
	 */
	public void setPenaltyGamesAllowed(boolean isPenaltyGamesAllowed) {
		this.isPenaltyGamesAllowed = isPenaltyGamesAllowed;
	}

	/**
	 * @return the isBelgianGamesAllowed
	 */
	public boolean isBelgianGamesAllowed() {
		return this.isBelgianGamesAllowed;
	}

	/**
	 * @param isBelgianGamesAllowed the isBelgianGamesAllowed to set
	 */
	public void setBelgianGamesAllowed(boolean isBelgianGamesAllowed) {
		this.isBelgianGamesAllowed = isBelgianGamesAllowed;
	}

	/**
	 * @return the isDeadPlayerAuthorized
	 */
	public boolean isDeadPlayerAuthorized() {
		return this.isDeadPlayerAuthorized;
	}

	/**
	 * @param isDeadPlayerAuthorized the isDeadPlayerAuthorized to set
	 */
	public void setDeadPlayerAuthorized(boolean isDeadPlayerAuthorized) {
		this.isDeadPlayerAuthorized = isDeadPlayerAuthorized;
	}

	/**
	 * @return the isDeadPlayerAutomaticallySelected
	 */
	public boolean isDeadPlayerAutomaticallySelected() {
		return this.isDeadPlayerAutomaticallySelected;
	}

	/**
	 * @param isDeadPlayerAutomaticallySelected the isDeadPlayerAutomaticallySelected to set
	 */
	public void setDeadPlayerAutomaticallySelected(
			boolean isDeadPlayerAutomaticallySelected) {
		this.isDeadPlayerAutomaticallySelected = isDeadPlayerAutomaticallySelected;
	}

	/**
	 * @return the isDevSimulationMode
	 */
	public boolean isDevSimulationMode() {
		return this.isDevSimulationMode;
	}

	/**
	 * @param isDevSimulationMode the isDevSimulationMode to set
	 */
	public void setDevSimulationMode(boolean isDevSimulationMode) {
		this.isDevSimulationMode = isDevSimulationMode;
	}

	/**
	 * @return the devGameSetCount
	 */
	public int getDevGameSetCount() {
		return this.devGameSetCount;
	}

	/**
	 * @param devGameSetCount the devGameSetCount to set
	 */
	public void setDevGameSetCount(int devGameSetCount) {
		this.devGameSetCount = devGameSetCount;
	}

	/**
	 * @return the devMaxGameCount
	 */
	public int getDevMaxGameCount() {
		return this.devMaxGameCount;
	}

	/**
	 * @param devMaxGameCount the devMaxGameCount to set
	 */
	public void setDevMaxGameCount(int devMaxGameCount) {
		this.devMaxGameCount = devMaxGameCount;
	}

	/**
	 * @return the isKeepScreenOn
	 */
	public boolean isKeepScreenOn() {
		return this.isKeepScreenOn;
	}

	/**
	 * @param isKeepScreenOn the isKeepScreenOn to set
	 */
	public void setKeepScreenOn(boolean isKeepScreenOn) {
		this.isKeepScreenOn = isKeepScreenOn;
	}
	
	
}
