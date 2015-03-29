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

import org.nla.tarotdroid.biz.enums.BetType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class Bet implements Serializable {

	/**
     * Serial version ID.
     */
    private static final long serialVersionUID = -5506014610970942678L;

    /** PETITE bet. */
	public static final Bet PETITE = new Bet();
	
    /** PRISE bet. */
	public static final Bet PRISE = new Bet();

	/** GARDE bet. */
	public static final Bet GARDE = new Bet();
	
	/** GARDE sans bet. */
	public static final Bet GARDESANS = new Bet();

	/** GARDE contre bet. */
	public static final Bet GARDECONTRE = new Bet();
	static
	{
		Bet.PETITE.setBetType(BetType.Petite);
	    Bet.PRISE.setBetType(BetType.Prise);
	    Bet.GARDE.setBetType(BetType.Garde);
	    Bet.GARDESANS.setBetType(BetType.GardeSans);
	    Bet.GARDECONTRE.setBetType(BetType.GardeContre);
	}
	
	/**
	 * @param betTypeAsString
	 * @return
	 */
	public static Bet valueOf(final String betTypeAsString) {
		if (betTypeAsString == null || betTypeAsString.equals("")) {
			return null;
		}
		BetType betType = BetType.valueOf(betTypeAsString);
		
		switch(betType) {
			case Petite:
				return Bet.PETITE;
    		case Prise:
    			return Bet.PRISE;
    		case Garde:
    			return Bet.GARDE;
    		case GardeSans:
    			return Bet.GARDESANS;
    		case GardeContre:
    			return Bet.GARDECONTRE;
    		default:
    		    return null;
		}
	}
	
	/**
	 * The bet.
	 */
	private BetType bet;

	/**
	 * The label.
	 */
	private String label;

	/**
	 * The image.
	 */
	private String image;
	
	/**
	 * @return the bet
	 */
	public BetType getBetType() {
		return this.bet;
	}

	/**
	 * @param betType
	 */
	public void setBetType(final BetType betType) {
		this.bet = betType;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return this.image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(final String image) {
		this.image = image;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.label != null ? this.label : this.bet.toString();
	}
}
