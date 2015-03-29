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

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class GameSetParameters extends PersistableBusinessObject implements Serializable {

	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
    private static final long serialVersionUID = -5271599980040089644L;

    /**
	 * Multiplication rate for a Petite.
	 */
	private int petiteRate = 1;
	
    /**
	 * Multiplication rate for a Prise.
	 */
	private int priseRate = 1;

	/**
	 * Multiplication rate for a Garde.
	 */
	private int gardeRate = 2;

	/**
	 * Multiplication rate for a Garde Sans.
	 */
	private int gardeSansRate = 4;
	
	/**
	 * Multiplication rate for a Garde Contre.
	 */
	private int gardeContreRate = 6;

	/**
	 * Base points for a Petite.
	 */
	private int petiteBasePoints = 10;
	
	/**
	 * Base points for a Prise.
	 */
	private int priseBasePoints = 25;
	
	/**
	 * Base points for a Garde.
	 */
	private int gardeBasePoints = 25;
	
	/**
	 * Base points for a Garde Sans.
	 */
	private int gardeSansBasePoints = 25;
	
	/**
	 * Base points for a Garde Contre.
	 */
	private int gardeContreBasePoints = 25;
	
	/**
	 * Bonus points for a misery.
	 */
	private int miseryPoints = 10;
	
	/**
	 * Bonus points for a poignee.
	 */
	private int poigneePoints = 20;

	/**
	 * Bonus points for a double poignee.
	 */
	private int doublePoigneePoints = 30;
	
	/**
	 * Bonus points for a triple poignee.
	 */
	private int triplePoigneePoints = 40;
	
	/**
	 * Bonus points for an announced and succeeded chelem.
	 */
	private int announcedAndSucceededChelemPoints = 400;

	/**
	 * Bonus points for a not announced but succeeded chelem.
	 */
	private int notAnnouncedButSucceededChelemPoints = 200;

	/**
	 * Malus points for an announced and failed chelem.
	 */
	private int announcedAndFailedChelemPoints = -200;

	/**
	 * Bonus points for a kid at the end.
	 */
	private int kidAtTheEndPoints = 10;
	
	/**
	 * Base step points of a belgian game.
	 */
	private int belgianBaseStepPoints = 100;
	
//	/**
//	 * Flag indicating in which order to display the games.
//	 */
//	private transient boolean displayGamesInReverseOrder = true;
//	
//	/**
//	 * Flag indicating whether to display the global scores for each game, or the scores of the game.
//	 */
//	private transient boolean displayGlobalScoresForEachGame = true;
//
//	/**
//	 * Flag indicating whether to display the next dealer.
//	 */
//	private transient boolean displayNextDealer = false;	
//
//	/**
//	 * Flag indicating whether Petite bet is authorized.
//	 */
//	private transient boolean isPetiteAuthorized = false;
//	
//	/**
//	 * Flag indicating whether Prise bet is authorized.
//	 */
//	private transient boolean isPriseAuthorized = true;
//	
//	/**
//	 * Flag indicating whether the misery annoucement is authorized at 3 or 4 player style.
//	 */
//	private transient boolean isMiseryAuthorized = false;
//	
//	/**
//	 * Flag indicating whether to allow passed games.
//	 */
//	private transient boolean passedGamesAllowed = false;
//	
//	/**
//	 * Flag indicating whether to allow penalty games.
//	 */
//	private transient boolean penaltyGamesAllowed = false;
//
//	/**
//	 * Flag indicating whether to allow belgian games.
//	 */
//	private transient boolean belgianGamesAllowed = false;
//	
//	/**
//	 * Flag indicating whether to allow dead players.
//	 */
//	private transient boolean deadPlayerAuthorized = true;
//	
//	/**
//	 * Flag indicating whether to automatically select the dead player.
//	 */
//	private transient boolean deadPlayerAutomaticallySelected = true;
//
//	/**
//	 * Flag indicating whether the game is in simulation mode.
//	 */
//	private transient boolean devSimulationMode = false;
//
//	/**
//	 * Flag indicating the game set count (dev property).
//	 */
//	private transient int devGameSetCount = 50;
//	
//	/**
//	 * Flag indicating the maximum game count (dev property).
//	 */
//	private transient int devMaxGameCount = 50;
//	
//	/**
//	 * Flag indicating whether the scrren must be kept on.
//	 */
//	private transient boolean keepScreenOn = true;

	/**
	 * @return the petiteRate
	 */
	public final int getPetiteRate() {
		return this.petiteRate;
	}

	/**
	 * @param petiteRate the petiteRate to set
	 */
	public final void setPetiteRate(final int petiteRate) {
		if (this.petiteRate != petiteRate) {
			this.petiteRate = petiteRate;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the petiteBasePoints
	 */
	public final int getPetiteBasePoints() {
		return this.petiteBasePoints;
	}

	/**
	 * @param petiteBasePoints the petiteBasePoints to set
	 */
	public final void setPetiteBasePoints(final int petiteBasePoints) {
		if (this.petiteBasePoints != petiteBasePoints) {
			this.petiteBasePoints = petiteBasePoints;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
	/**
	 * @return the priseRate
	 */
	public final int getPriseRate() {
		return this.priseRate;
	}

	/**
	 * @param priseRate the priseRate to set
	 */
	public final void setPriseRate(final int priseRate) {
		if (this.priseRate != priseRate) {
			this.priseRate = priseRate;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the priseBasePoints
	 */
	public final int getPriseBasePoints() {
		return this.priseBasePoints;
	}

	/**
	 * @param priseBasePoints the priseBasePoints to set
	 */
	public final void setPriseBasePoints(final int priseBasePoints) {
		if (this.priseBasePoints != priseBasePoints) {
			this.priseBasePoints = priseBasePoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the gardeRate
	 */
	public final int getGardeRate() {
		return this.gardeRate;
	}

	/**
	 * @param gardeRate the gardeRate to set
	 */
	public final void setGardeRate(final int gardeRate) {
		if (this.gardeRate != gardeRate) {
			this.gardeRate = gardeRate;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the gardeSansRate
	 */
	public final int getGardeSansRate() {
		return this.gardeSansRate;
	}

	/**
	 * @param gardeSansRate the gardeSansRate to set
	 */
	public final void setGardeSansRate(final int gardeSansRate) {
		if (this.gardeSansRate != gardeSansRate) {
			this.gardeSansRate = gardeSansRate;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the gardeContreRate
	 */
	public final int getGardeContreRate() {
		return this.gardeContreRate;
	}

	/**
	 * @param gardeContreRate the gardeContreRate to set
	 */
	public final void setGardeContreRate(final int gardeContreRate) {
		if (this.gardeContreRate != gardeContreRate) {
			this.gardeContreRate = gardeContreRate;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the gardeBasePoints
	 */
	public final int getGardeBasePoints() {
		return this.gardeBasePoints;
	}

	/**
	 * @param gardeBasePoints the gardeBasePoints to set
	 */
	public final void setGardeBasePoints(final int gardeBasePoints) {
		if (this.gardeBasePoints != gardeBasePoints) {
			this.gardeBasePoints = gardeBasePoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the gardeSansBasePoints
	 */
	public final int getGardeSansBasePoints() {
		return this.gardeSansBasePoints;
	}

	/**
	 * @param gardeSansBasePoints the gardeSansBasePoints to set
	 */
	public final void setGardeSansBasePoints(final int gardeSansBasePoints) {
		if (this.gardeSansBasePoints != gardeSansBasePoints) {
			this.gardeSansBasePoints = gardeSansBasePoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the gardeContreBasePoints
	 */
	public final int getGardeContreBasePoints() {
		return this.gardeContreBasePoints;
	}

	/**
	 * @param gardeContreBasePoints the gardeContreBasePoints to set
	 */
	public final void setGardeContreBasePoints(final int gardeContreBasePoints) {
		if (this.gardeContreBasePoints != gardeContreBasePoints) {
			this.gardeContreBasePoints = gardeContreBasePoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the miseryPoints
	 */
	public final int getMiseryPoints() {
		return this.miseryPoints;
	}

	/**
	 * @param miseryPoints the miseryPoints to set
	 */
	public void setMiseryPoints(final int miseryPoints) {
		if (this.miseryPoints != miseryPoints) {
			this.miseryPoints = miseryPoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the poigneePoints
	 */
	public int getPoigneePoints() {
		return this.poigneePoints;
	}

	/**
	 * @param poigneePoints the poigneePoints to set
	 */
	public void setPoigneePoints(final int poigneePoints) {
		if (this.poigneePoints != poigneePoints) {
			this.poigneePoints = poigneePoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the doublePoigneePoints
	 */
	public int getDoublePoigneePoints() {
		return this.doublePoigneePoints;
	}

	/**
	 * @param doublePoigneePoints the doublePoigneePoints to set
	 */
	public void setDoublePoigneePoints(final int doublePoigneePoints) {
		if (this.doublePoigneePoints != doublePoigneePoints) {
			this.doublePoigneePoints = doublePoigneePoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the triplePoigneePoints
	 */
	public int getTriplePoigneePoints() {
		return this.triplePoigneePoints;
	}

	/**
	 * @param triplePoigneePoints the triplePoigneePoints to set
	 */
	public void setTriplePoigneePoints(final int triplePoigneePoints) {
		if (this.triplePoigneePoints != triplePoigneePoints) {
			this.triplePoigneePoints = triplePoigneePoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the announcedAndSucceededChelemPoints
	 */
	public int getAnnouncedAndSucceededChelemPoints() {
		return this.announcedAndSucceededChelemPoints;
	}

	/**
	 * @param announcedAndSucceededChelemPoints the announcedAndSucceededChelemPoints to set
	 */
	public void setAnnouncedAndSucceededChelemPoints(final int announcedAndSucceededChelemPoints) {
		if (this.announcedAndSucceededChelemPoints != announcedAndSucceededChelemPoints) {
			this.announcedAndSucceededChelemPoints = announcedAndSucceededChelemPoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the notAnnouncedButSucceededChelemPoints
	 */
	public int getNotAnnouncedButSucceededChelemPoints() {
		return this.notAnnouncedButSucceededChelemPoints;
	}

	/**
	 * @param notAnnouncedButSucceededChelemPoints the notAnnouncedButSucceededChelemPoints to set
	 */
	public void setNotAnnouncedButSucceededChelemPoints(final int notAnnouncedButSucceededChelemPoints) {
		if (this.notAnnouncedButSucceededChelemPoints != notAnnouncedButSucceededChelemPoints) {
			this.notAnnouncedButSucceededChelemPoints = notAnnouncedButSucceededChelemPoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the announcedAndFailedChelemPoints
	 */
	public int getAnnouncedAndFailedChelemPoints() {
		return this.announcedAndFailedChelemPoints;
	}

	/**
	 * @param announcedAndFailedChelemPoints the announcedAndFailedChelemPoints to set
	 */
	public void setAnnouncedAndFailedChelemPoints(final int announcedAndFailedChelemPoints) {
		if (this.announcedAndFailedChelemPoints != announcedAndFailedChelemPoints) {
			this.announcedAndFailedChelemPoints = announcedAndFailedChelemPoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the kidAtTheEndPoints
	 */
	public int getKidAtTheEndPoints() {
		return this.kidAtTheEndPoints;
	}

	/**
	 * @param kidAtTheEndPoints the kidAtTheEndPoints to set
	 */
	public void setKidAtTheEndPoints(final int kidAtTheEndPoints) {
		if (this.kidAtTheEndPoints != kidAtTheEndPoints) {
			this.kidAtTheEndPoints = kidAtTheEndPoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * @return the belgianBasePoints
	 */
	public int getBelgianBaseStepPoints() {
		return this.belgianBaseStepPoints;
	}

	/**
	 * @param belgianBaseStepPoints the belgianBaseStepPoints to set
	 */
	public void setBelgianBaseStepPoints(final int belgianBaseStepPoints) {
		if (this.belgianBaseStepPoints != belgianBaseStepPoints) {
			this.belgianBaseStepPoints = belgianBaseStepPoints;
			this.setChanged();
			this.notifyObservers();
		}
	}

//	/**
//	 * @return the displayGamesInReverseOrder
//	 */
//	public boolean isDisplayGamesInReverseOrder() {
//		return this.displayGamesInReverseOrder;
//	}
//
//	/**
//	 * @param displayGamesInReverseOrder the displayGamesInReverseOrder to set
//	 */
//	public void setDisplayGamesInReverseOrder(final boolean displayGamesInReverseOrder) {
//		if (this.displayGamesInReverseOrder != displayGamesInReverseOrder) {
//			this.displayGamesInReverseOrder = displayGamesInReverseOrder;
//			this.setChanged();
//			this.notifyObservers();
//		}
//	}
//
//	/**
//	 * @return the displayGlobalScoresForEachGame
//	 */
//	public boolean isDisplayGlobalScoresForEachGame() {
//		return this.displayGlobalScoresForEachGame;
//	}
//
//	/**
//	 * @param displayGlobalScoresForEachGame the displayGlobalScoresForEachGame to set
//	 */
//	public void setDisplayGlobalScoresForEachGame(final boolean displayGlobalScoresForEachGame) {
//		if (this.displayGlobalScoresForEachGame != displayGlobalScoresForEachGame) {
//			this.displayGlobalScoresForEachGame = displayGlobalScoresForEachGame;
//			this.setChanged();
//			this.notifyObservers();
//		}
//	}
//	
//	/**
//	 * @return
//	 */
//	public boolean isDisplayNextDealer() {
//		return this.displayNextDealer;
//	}
//
//	/**
//	 * @param displayNextDealer
//	 */
//	public void setDisplayNextDealer(final boolean displayNextDealer) {
//		this.displayNextDealer = displayNextDealer;
//	}
//
//	/**
//	 * @return the belgianGamesAllowed
//	 */
//	public boolean areBelgianGamesAllowed() {
//		return this.belgianGamesAllowed;
//	}
//
//	/**
//	 * @param belgianGamesAllowed the belgianGamesAllowed to set
//	 */
//	public void setBelgianGamesAllowed(final boolean belgianGamesAllowed) {
//		if (this.belgianGamesAllowed != belgianGamesAllowed) {
//			this.belgianGamesAllowed = belgianGamesAllowed;
//			this.setChanged();
//			this.notifyObservers();
//		}
//	}
//
//	/**
//	 * @return the passedGamesAllowed
//	 */
//	public boolean arePassedGamesAllowed() {
//		return this.passedGamesAllowed;
//	}
//
//	/**
//	 * @param passedGamesAllowed the passedGamesAllowed to set
//	 */
//	public void setPassedGamesAllowed(boolean passedGamesAllowed) {
//		this.passedGamesAllowed = passedGamesAllowed;
//	}
//
//	/**
//	 * @return the penaltyGamesAllowed
//	 */
//	public boolean arePenaltyGamesAllowed() {
//		return this.penaltyGamesAllowed;
//	}
//
//	/**
//	 * @param penaltyGamesAllowed the penaltyGamesAllowed to set
//	 */
//	public void setPenaltyGamesAllowed(boolean penaltyGamesAllowed) {
//		this.penaltyGamesAllowed = penaltyGamesAllowed;
//	}
//
//	/**
//	 * @return the isPetiteAuthorized
//	 */
//	public boolean isPetiteAuthorized() {
//		return this.isPetiteAuthorized;
//	}
//
//	/**
//	 * @param isPetiteAuthorized the isPetiteAuthorized to set
//	 */
//	public void setPetiteAuthorized(final boolean isPetiteAuthorized) {
//		this.isPetiteAuthorized = isPetiteAuthorized;
//	}
//	
//	/**
//	 * @return the isPriseAuthorized
//	 */
//	public boolean isPriseAuthorized() {
//		return this.isPriseAuthorized;
//	}
//
//	/**
//	 * @param isPriseAuthorized the isPriseAuthorized to set
//	 */
//	public void setPriseAuthorized(final boolean isPriseAuthorized) {
//		this.isPriseAuthorized = isPriseAuthorized;
//	}
//
//	/**
//	 * @return the isMiseryAuthorized
//	 */
//	public boolean isMiseryAuthorized() {
//		return this.isMiseryAuthorized;
//	}
//
//	/**
//	 * @param isMiseryAuthorized the isMiseryAuthorized to set
//	 */
//	public void setMiseryAuthorized(final boolean isMiseryAuthorized) {
//		this.isMiseryAuthorized = isMiseryAuthorized;
//	}
//
//	/**
//	 * @return the deadPlayerAuthorized
//	 */
//	public boolean isDeadPlayerAuthorized() {
//		return this.deadPlayerAuthorized;
//	}
//
//	/**
//	 * @param deadPlayerAuthorized the deadPlayerAuthorized to set
//	 */
//	public void setDeadPlayerAuthorized(final boolean deadPlayerAuthorized) {
//		if (this.deadPlayerAuthorized != deadPlayerAuthorized) {
//			this.deadPlayerAuthorized = deadPlayerAuthorized;
//			this.setChanged();
//			this.notifyObservers();
//		}
//	}
//
//	/**
//	 * @return the deadPlayerAutomaticallySelected
//	 */
//	public boolean isDeadPlayerAutomaticallySelected() {
//		return this.deadPlayerAutomaticallySelected;
//	}
//
//	/**
//	 * @param deadPlayerAutomaticallySelected the deadPlayerAutomaticallySelected to set
//	 */
//	public void setDeadPlayerAutomaticallySelected(final boolean deadPlayerAutomaticallySelected) {
//		if (this.deadPlayerAutomaticallySelected != deadPlayerAutomaticallySelected) {
//			this.deadPlayerAutomaticallySelected = deadPlayerAutomaticallySelected;
//			this.setChanged();
//			this.notifyObservers();
//		}
//	}
//
//	/**
//	 * @return the devSimulationMode
//	 */
//	public boolean isDevSimulationMode() {
//		return this.devSimulationMode;
//	}
//
//	/**
//	 * @param devSimulationMode the devSimulationMode to set
//	 */
//	public void setDevSimulationMode(final boolean devSimulationMode) {
//		this.devSimulationMode = devSimulationMode;
//	}
//	
//	/**
//	 * @return
//	 */
//	public int getDevGameSetCount() {
//		return this.devGameSetCount;
//	}
//
//	/**
//	 * @param devGameSetCount
//	 */
//	public void setDevGameSetCount(int devGameSetCount) {
//		this.devGameSetCount = devGameSetCount;
//	}
//
//	/**
//	 * @return
//	 */
//	public int getDevMaxGameCount() {
//		return this.devMaxGameCount;
//	}
//
//	/**
//	 * @param devMaxGameCount
//	 */
//	public void setDevMaxGameCount(int devMaxGameCount) {
//		this.devMaxGameCount = devMaxGameCount;
//	}
//
//	/**
//	 * @return the keepScreenOn
//	 */
//	public boolean isKeepScreenOn() {
//		return this.keepScreenOn;
//	}
//
//	/**
//	 * @param keepScreenOn the keepScreenOn to set
//	 */
//	public void setKeepScreenOn(final boolean keepScreenOn) {
//		if (this.keepScreenOn != keepScreenOn) {
//			this.keepScreenOn = keepScreenOn;
//			this.setChanged();
//			this.notifyObservers();
//		}
//	}

//	/**
//	 * @return the isStoringGameSets
//	 */
//	public boolean isStoringGameSets() {
//		return true;
//	}
}