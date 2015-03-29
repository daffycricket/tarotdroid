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

import com.google.common.base.Objects;

/**
 * RestGameSetParameters entity.
 */
public class RestGameSetParameters {

	/**
	 * PetiteRate property.
	 */
	private int petiteRate;
	
	/**
	 * PriseRate property.
	 */
	private int priseRate;
	
	/**
	 * GardeRate property.
	 */
	private int gardeRate;
	
	/**
	 * GardeSansRate property.
	 */
	private int gardeSansRate;
	
	/**
	 * GardeContreRate property.
	 */
	private int gardeContreRate;
	
	/**
	 * PetiteBasePoints property.
	 */
	private int petiteBasePoints;
	
	/**
	 * PriseBasePoints property.
	 */
	private int priseBasePoints;
	
	/**
	 * GardeBasePoints property.
	 */
	private int gardeBasePoints;
	
	/**
	 * GardeSansBasePoints property.
	 */
	private int gardeSansBasePoints;
	
	/**
	 * GardeContreBasePoints property.
	 */
	private int gardeContreBasePoints;
	
	/**
	 * MiseryPoints property.
	 */
	private int miseryPoints;
	
	/**
	 * PoigneePoints property.
	 */
	private int poigneePoints;
	
	/**
	 * DoublePoigneePoints property.
	 */
	private int doublePoigneePoints;
	
	/**
	 * TriplePoigneePoints property.
	 */
	private int triplePoigneePoints;
	
	/**
	 * AnnouncedAndSucceededChelemPoints property.
	 */
	private int announcedAndSucceededChelemPoints;
	
	/**
	 * NotAnnouncedButSucceededChelemPoints property.
	 */
	private int notAnnouncedButSucceededChelemPoints;
	
	/**
	 * AnnouncedAndFailedChelemPoints property.
	 */
	private int announcedAndFailedChelemPoints;
	
	/**
	 * KidAtTheEndPoints property.
	 */
	private int kidAtTheEndPoints;
	
	/**
	 * BelgianBaseStepPoints property.
	 */
	private int belgianBaseStepPoints;
	
	/**
	 * Default constructor.
	 */
	public RestGameSetParameters() {
	}

	/**
	 * @return petiteRate property.
	 */
	public int getPetiteRate() {
		return this.petiteRate;
	}

	/**
	 * @param petiteRate
	 */
	public void setPetiteRate(int petiteRate) {
		this.petiteRate = petiteRate;
	}
	
	/**
	 * @return priseRate property.
	 */
	public int getPriseRate() {
		return this.priseRate;
	}

	/**
	 * @param priseRate
	 */
	public void setPriseRate(int priseRate) {
		this.priseRate = priseRate;
	}
	
	/**
	 * @return gardeRate property.
	 */
	public int getGardeRate() {
		return this.gardeRate;
	}

	/**
	 * @param gardeRate
	 */
	public void setGardeRate(int gardeRate) {
		this.gardeRate = gardeRate;
	}
	
	/**
	 * @return gardeSansRate property.
	 */
	public int getGardeSansRate() {
		return this.gardeSansRate;
	}

	/**
	 * @param gardeSansRate
	 */
	public void setGardeSansRate(int gardeSansRate) {
		this.gardeSansRate = gardeSansRate;
	}
	
	/**
	 * @return gardeContreRate property.
	 */
	public int getGardeContreRate() {
		return this.gardeContreRate;
	}

	/**
	 * @param gardeContreRate
	 */
	public void setGardeContreRate(int gardeContreRate) {
		this.gardeContreRate = gardeContreRate;
	}
	
	/**
	 * @return petiteBasePoints property.
	 */
	public int getPetiteBasePoints() {
		return this.petiteBasePoints;
	}

	/**
	 * @param petiteBasePoints
	 */
	public void setPetiteBasePoints(int petiteBasePoints) {
		this.petiteBasePoints = petiteBasePoints;
	}
	
	/**
	 * @return priseBasePoints property.
	 */
	public int getPriseBasePoints() {
		return this.priseBasePoints;
	}

	/**
	 * @param priseBasePoints
	 */
	public void setPriseBasePoints(int priseBasePoints) {
		this.priseBasePoints = priseBasePoints;
	}
	
	/**
	 * @return gardeBasePoints property.
	 */
	public int getGardeBasePoints() {
		return this.gardeBasePoints;
	}

	/**
	 * @param gardeBasePoints
	 */
	public void setGardeBasePoints(int gardeBasePoints) {
		this.gardeBasePoints = gardeBasePoints;
	}
	
	/**
	 * @return gardeSansBasePoints property.
	 */
	public int getGardeSansBasePoints() {
		return this.gardeSansBasePoints;
	}

	/**
	 * @param gardeSansBasePoints
	 */
	public void setGardeSansBasePoints(int gardeSansBasePoints) {
		this.gardeSansBasePoints = gardeSansBasePoints;
	}
	
	/**
	 * @return gardeContreBasePoints property.
	 */
	public int getGardeContreBasePoints() {
		return this.gardeContreBasePoints;
	}

	/**
	 * @param gardeContreBasePoints
	 */
	public void setGardeContreBasePoints(int gardeContreBasePoints) {
		this.gardeContreBasePoints = gardeContreBasePoints;
	}
	
	/**
	 * @return miseryPoints property.
	 */
	public int getMiseryPoints() {
		return this.miseryPoints;
	}

	/**
	 * @param miseryPoints
	 */
	public void setMiseryPoints(int miseryPoints) {
		this.miseryPoints = miseryPoints;
	}
	
	/**
	 * @return poigneePoints property.
	 */
	public int getPoigneePoints() {
		return this.poigneePoints;
	}

	/**
	 * @param poigneePoints
	 */
	public void setPoigneePoints(int poigneePoints) {
		this.poigneePoints = poigneePoints;
	}
	
	/**
	 * @return doublePoigneePoints property.
	 */
	public int getDoublePoigneePoints() {
		return this.doublePoigneePoints;
	}

	/**
	 * @param doublePoigneePoints
	 */
	public void setDoublePoigneePoints(int doublePoigneePoints) {
		this.doublePoigneePoints = doublePoigneePoints;
	}
	
	/**
	 * @return triplePoigneePoints property.
	 */
	public int getTriplePoigneePoints() {
		return this.triplePoigneePoints;
	}

	/**
	 * @param triplePoigneePoints
	 */
	public void setTriplePoigneePoints(int triplePoigneePoints) {
		this.triplePoigneePoints = triplePoigneePoints;
	}
	
	/**
	 * @return announcedAndSucceededChelemPoints property.
	 */
	public int getAnnouncedAndSucceededChelemPoints() {
		return this.announcedAndSucceededChelemPoints;
	}

	/**
	 * @param announcedAndSucceededChelemPoints
	 */
	public void setAnnouncedAndSucceededChelemPoints(int announcedAndSucceededChelemPoints) {
		this.announcedAndSucceededChelemPoints = announcedAndSucceededChelemPoints;
	}
	
	/**
	 * @return notAnnouncedButSucceededChelemPoints property.
	 */
	public int getNotAnnouncedButSucceededChelemPoints() {
		return this.notAnnouncedButSucceededChelemPoints;
	}

	/**
	 * @param notAnnouncedButSucceededChelemPoints
	 */
	public void setNotAnnouncedButSucceededChelemPoints(int notAnnouncedButSucceededChelemPoints) {
		this.notAnnouncedButSucceededChelemPoints = notAnnouncedButSucceededChelemPoints;
	}
	
	/**
	 * @return announcedAndFailedChelemPoints property.
	 */
	public int getAnnouncedAndFailedChelemPoints() {
		return this.announcedAndFailedChelemPoints;
	}

	/**
	 * @param announcedAndFailedChelemPoints
	 */
	public void setAnnouncedAndFailedChelemPoints(int announcedAndFailedChelemPoints) {
		this.announcedAndFailedChelemPoints = announcedAndFailedChelemPoints;
	}
	
	/**
	 * @return kidAtTheEndPoints property.
	 */
	public int getKidAtTheEndPoints() {
		return this.kidAtTheEndPoints;
	}

	/**
	 * @param kidAtTheEndPoints
	 */
	public void setKidAtTheEndPoints(int kidAtTheEndPoints) {
		this.kidAtTheEndPoints = kidAtTheEndPoints;
	}
	
	/**
	 * @return belgianBaseStepPoints property.
	 */
	public int getBelgianBaseStepPoints() {
		return this.belgianBaseStepPoints;
	}

	/**
	 * @param belgianBaseStepPoints
	 */
	public void setBelgianBaseStepPoints(int belgianBaseStepPoints) {
		this.belgianBaseStepPoints = belgianBaseStepPoints;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("petiteRate", this.petiteRate)
				.add("priseRate", this.priseRate)
				.add("gardeRate", this.gardeRate)
				.add("gardeSansRate", this.gardeSansRate)
				.add("gardeContreRate", this.gardeContreRate)
				.add("petiteBasePoints", this.petiteBasePoints)
				.add("priseBasePoints", this.priseBasePoints)
				.add("gardeBasePoints", this.gardeBasePoints)
				.add("gardeSansBasePoints", this.gardeSansBasePoints)
				.add("gardeContreBasePoints", this.gardeContreBasePoints)
				.add("miseryPoints", this.miseryPoints)
				.add("poigneePoints", this.poigneePoints)
				.add("doublePoigneePoints", this.doublePoigneePoints)
				.add("triplePoigneePoints", this.triplePoigneePoints)
				.add("announcedAndSucceededChelemPoints", this.announcedAndSucceededChelemPoints)
				.add("notAnnouncedButSucceededChelemPoints", this.notAnnouncedButSucceededChelemPoints)
				.add("announcedAndFailedChelemPoints", this.announcedAndFailedChelemPoints)
				.add("kidAtTheEndPoints", this.kidAtTheEndPoints)
				.add("belgianBaseStepPoints", this.belgianBaseStepPoints)
				.toString();
	}
}