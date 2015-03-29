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
package org.nla.tarotdroid.biz.computers;

import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.StandardTarot4Game;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *	Helper class to compute the scores base upon a 4 player style game.
 */
public class StandardTarot4GameScoresComputer extends StandardTarotGameScoresComputer {

	/**
	 * Constructor using game set parameters.
	 * @param gameSetParameters The game set parameters.
	 */
	public StandardTarot4GameScoresComputer(final StandardTarot4Game game, final GameSetParameters gameSetParameters) {
		super(game, gameSetParameters, 4);
	}
	
	/**
	 * Default constructor.
	 */
	public StandardTarot4GameScoresComputer(final StandardTarot4Game game) {
		this(game, new GameSetParameters());
	}
}













