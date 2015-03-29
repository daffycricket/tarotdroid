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

import org.nla.tarotdroid.biz.enums.KingType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class King implements Serializable {

	/**
     * Serial version ID.
     */
    private static final long serialVersionUID = -2271742674453366926L;

    /** 
     * Heart king.
     */
	public static final King HEART = new King();

	/** 
	 * Diamond king.
	 */
	public static final King DIAMOND = new King();

	/**
	 * Spade king.
	 */
	public static final King SPADE = new King();

	/** 
	 * Club king.
	 */
	public static final King CLUB = new King();
	static {
	    King.HEART.setKingType(KingType.Hearts);
	    King.DIAMOND.setKingType(KingType.Diamonds);
	    King.SPADE.setKingType(KingType.Spades);
	    King.CLUB.setKingType(KingType.Clubs);
	}
	

	/**
	 * @param kingTypeAsString
	 * @return
	 */
	public static King valueOf(final String kingTypeAsString) {
		if (kingTypeAsString == null  || kingTypeAsString.equals("")) {
			return null;
		}
		
		KingType kingType = KingType.valueOf(kingTypeAsString);
		
		switch(kingType) {
    		case Hearts:
    			return King.HEART;
    		case Spades:
    			return King.SPADE;
    		case Clubs:
    			return King.CLUB;
    		case Diamonds:
    			return King.DIAMOND;
            default:
                return null;
		}
	}
	
	/**
	 * The king.
	 */
	private KingType king;

	/**
	 * The label.
	 */
	private String label;

	/**
	 * The image.
	 */
	private String image;

	/**
	 * @return the color
	 */
	public KingType getKingType() {
		return this.king;
	}
	
	/**
	 * @param kingType
	 */
	public void setKingType(final KingType kingType) {
		this.king = kingType;
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
		return this.label != null ? this.label : this.king.toString();
	}
}
