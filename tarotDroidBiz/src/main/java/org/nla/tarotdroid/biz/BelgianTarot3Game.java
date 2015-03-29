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

import org.nla.tarotdroid.biz.computers.BelgianTarot3GameScoresComputer;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class BelgianTarot3Game extends BelgianBaseGame {
	
	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
	private static final long serialVersionUID = 8142834822634267543L;
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.BaseGame#getGameScores()
	 */
	@Override
	public GameScores getGameScores() {
		BelgianTarot3GameScoresComputer gameScoresComputer = new BelgianTarot3GameScoresComputer(this, this.gameSetParameters);
		gameScoresComputer.computeScore();
		return gameScoresComputer.getGameScores();
	}
}
