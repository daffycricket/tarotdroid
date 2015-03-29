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

import org.nla.tarotdroid.biz.enums.ChelemType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class Chelem implements Serializable {

	/**
     * Serial version ID.
     */
    private static final long serialVersionUID = 2071246466762538641L;

    /** 
     * Chelem announced and succeeded
     */
	public static final Chelem CHELEM_ANOUNCED_AND_SUCCEEDED = new Chelem();

	/**
	 * Chelem announced and succeeded 
	 */
	public static final Chelem CHELEM_ANOUNCED_AND_FAILED = new Chelem();

	/** 
	 * Chelem announced and succeeded 
	 */
	public static final Chelem CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED = new Chelem();

	static {
	    Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED.setChelemType(ChelemType.AnnouncedAndSucceeded);
	    Chelem.CHELEM_ANOUNCED_AND_FAILED.setChelemType(ChelemType.AnnouncedAndFailed);
	    Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED.setChelemType(ChelemType.NotAnnouncedButSucceeded);
	}
	
	/**
	 * @param chelemTypeAsString
	 * @return
	 */
	public static Chelem valueOf(final String chelemTypeAsString) {
		if (chelemTypeAsString == null || chelemTypeAsString.equals("")) {
			return null;
		}
		ChelemType chelemType = ChelemType.valueOf(chelemTypeAsString);
		
		switch(chelemType) {
    		case AnnouncedAndSucceeded:
    			return Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED;
    		case AnnouncedAndFailed:
    			return Chelem.CHELEM_ANOUNCED_AND_FAILED;
    		case NotAnnouncedButSucceeded:
    			return Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED;
    		default:
    		    return null;    
		}
	}
	
	/**
	 * The chelem type.
	 */
	private ChelemType chelemType;

	/**
	 * The label.
	 */
	private String label;

	/**
	 * @return the chelemType
	 */
	public ChelemType getChelemType() {
		return this.chelemType;
	}

	/**
	 * @param chelemType the chelemType to set
	 */
	public void setChelemType(final ChelemType chelemType) {
		this.chelemType = chelemType;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.label;
	}
}
