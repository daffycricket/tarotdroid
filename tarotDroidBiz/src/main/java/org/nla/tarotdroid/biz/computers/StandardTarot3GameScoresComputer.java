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
import org.nla.tarotdroid.biz.StandardTarot3Game;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *	Helper class to compute the scores base upon a 3 player style game.
 */
public class StandardTarot3GameScoresComputer extends StandardTarotGameScoresComputer {

	/**
	 * Constructor using game set parameters.
	 * @param gameSetParameters The game set parameters.
	 */
	public StandardTarot3GameScoresComputer(final StandardTarot3Game game, final GameSetParameters gameSetParameters) {
		super(game, gameSetParameters, 3);
	}
	
	/**
	 * Default constructor.
	 */
	public StandardTarot3GameScoresComputer(final StandardTarot3Game game) {
		this(game, new GameSetParameters());
	}
}













