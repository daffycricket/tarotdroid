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
package org.nla.tarotdroid.lib.ui.controls;

import static com.google.common.base.Preconditions.checkArgument;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public final class RowFactory {
	
	/**
	 * Prevents the construction of a RowFactory.
	 */
	private RowFactory() {
	}
	
	/**
	 * Builds and returns a view depending on Game type.
	 * @param context
	 * @param dialog
	 * @param game
	 * @param weight
	 * @param refreshableGameSetContainer
	 * @return
	 */
	public static View buildGameRow(final Context context, final ProgressDialog dialog, final BaseGame game, final float weight, final GameSet gameSet) {
		checkArgument(gameSet != null, "gameSet is null");
		checkArgument(game != null, "game is null");
		
		// the row to return
		BaseGameRow gameRow = null;

		// 3 or 4 standard player style
		if (game instanceof StandardTarot3Game || game instanceof StandardTarot4Game) {
			gameRow = new StandardTarotGameRow(context, dialog, (StandardBaseGame)game, weight, gameSet);
		}
		// 5 standard player style
		else if (game instanceof StandardTarot5Game) {
			gameRow = new StandardTarot5GameRow(context, dialog, (StandardBaseGame)game, weight, gameSet);
		}
		// 3, 4 or 5 belgian player style
		else if (game instanceof BelgianTarot3Game || game instanceof BelgianTarot4Game || game instanceof BelgianTarot5Game) {
			gameRow = new BelgianTarotGameRow(context, dialog, game, weight, gameSet);
		}
		// penalty game
		else if (game instanceof PenaltyGame) {
			gameRow = new PenaltyGameRow(context, dialog, game, weight, gameSet);
		}
		// passed game
		else if (game instanceof PassedGame) {
			gameRow = new PassedGameRow(context, dialog, game, weight, gameSet);
		}

		else {
			throw new IllegalArgumentException("game is of unknown type:" + game);
		}

		return gameRow; 
	}
}