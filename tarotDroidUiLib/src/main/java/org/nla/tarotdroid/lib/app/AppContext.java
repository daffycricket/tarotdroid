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

public class AppContext {
	
	/**
	 * Unique instance of the application.
	 */
	private static ITarotDroidApp app;
	
	/**
	 * Default constructor.
	 */
	private AppContext() {
	}

	/**
	 * Returns the unique instance of the application.
	 * @return
	 */
	public static ITarotDroidApp getApplication() {
		return app;
	}
	
	/**
	 * Sets the unique instance of the application.
	 * @param application
	 */
	public static void setApplication(ITarotDroidApp application) {
		app = application;
	}	
}
