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
package org.nla.tarotdroid.dal;

public class DalException extends Exception {

	/**
     * Serial version ID.
     */
    private static final long serialVersionUID = -5704149453668857236L;

    /**
	 * Default constructor.
	 */
	public DalException() {
		super();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public DalException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public DalException(final String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public DalException(final Throwable arg0) {
		super(arg0);
	}
}
