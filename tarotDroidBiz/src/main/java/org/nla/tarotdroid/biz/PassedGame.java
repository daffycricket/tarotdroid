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
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class PassedGame extends BaseGame {

    /**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -6421380137157481721L;
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.BaseGame#getGameScores()
	 */
	@Override
	public GameScores getGameScores() {
		GameScores toReturn = new GameScores();
		
		for (Player player : this.getPlayers().getPlayers()) {
			toReturn.addScore(player, 0);
		}
		
		return toReturn;
	}
}
