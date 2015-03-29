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

/**
 * @author nicocaro
 *
 */
public interface IPersistable {
	
	/**
	 * Indicates whether object has already been persisted.
	 * @return true if object has already been persisted, false other wise.
	 */
	boolean isPersisted();
	
	/**
	 * Gets the persistance id.
	 * @return the persistance id.
	 */
	long getId();
	
	/**
	 * Sets the persistance id.
	 * @param id the persistance id.
	 */
	void setId(long id);

	/**
	 * Detach from the repository.
	 */
	void detach();
}
