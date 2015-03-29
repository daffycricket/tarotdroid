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

import org.nla.tarotdroid.biz.enums.ResultType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class Result implements Serializable {

	/**
     * Serial version ID.
     */
	private static final long serialVersionUID = -5398872372392923877L;
	

    /** 
     * Success.
     */
	public static final Result SUCCESS = new Result();

	/** 
	 * Failure.
	 */
	public static final Result FAILURE = new Result();

	static {
	    Result.FAILURE.setResultType(ResultType.Failure);
	    Result.SUCCESS.setResultType(ResultType.Success);
	}

	/**
	 * @param 
	 * @return
	 */
	public static Result valueOf(final String resultTypeAsString) {
		ResultType resultType = ResultType.valueOf(resultTypeAsString);
		
		switch(resultType) {
    		case Success:
    			return Result.SUCCESS;
    		case Failure:
    			return Result.FAILURE;
            default:
                return null;
		}
	}
	
	/**
	 * @param 
	 * @return
	 */
	public static Result valueOf(final ResultType resultType) {
		switch(resultType) {
    		case Success:
    			return Result.SUCCESS;
    		case Failure:
    			return Result.FAILURE;
            default:
                return null;
		}
	}
	
	/**
	 * The result type.
	 */
	private ResultType resultType;

	/**
	 * The label.
	 */
	private String label;

	/**
	 * 
	 * @return
	 */
	public ResultType getResultType() {
		return this.resultType;
	}

	/**
	 * 
	 * @param resultType
	 */
	public void setResultType(final ResultType resultType) {
		this.resultType = resultType;
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
		return this.label != null ? this.label : this.resultType.toString();
	}
}
