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
package org.nla.tarotdroid.lib.ui.constants;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public final class UIConstants {
	
	/**
	 * Default constructor.
	 */
	private UIConstants() {
	}
	
	/**
	 * Player view width.
	 */
	public static final int PLAYER_VIEW_WIDTH = 120;
	
	/**
	 * Player view height.
	 */
	public static final int PLAYER_VIEW_HEIGHT = 50;

	/**
	 * Layout params for the player row items. 
	 */
	public static final LayoutParams PLAYERS_LAYOUT_PARAMS = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1);
	static {
	    UIConstants.PLAYERS_LAYOUT_PARAMS.setMargins(3, 0, 3, 0);
	}
	
	/**
	 * Layout params for the tab gameset view. 
	 */
	public static final LayoutParams TABGAMESET_LAYOUT_PARAMS = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1);
	static {
	    UIConstants.TABGAMESET_LAYOUT_PARAMS.setMargins(3, 3, 3, 3);
	}
	
	/**
	 * Layout params for the called color ImageView. 
	 */
	public static final LayoutParams CALLED_COLOR_LAYOUT_PARAMS = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, (float)0.7);
	static {
	    UIConstants.CALLED_COLOR_LAYOUT_PARAMS.setMargins(0, 0, 4, 0);		
	}
	
	/**
	 * Layout params for TextView associated to the ImageView of the called color. 
	 */
	public static final LayoutParams TXT_SCORE_LAYOUT_PARAMS = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, (float)0.3);
	
	/**
	 * Picture uri constant.
	 */
	public static final String PLAYER_PICTURE_URI = "playerPictureUri";
	
	/**
	 * Facebook id constant.
	 */
	public static final String PLAYER_FACEBOOK_ID = "facebookId";

	/**
	 * Player name constant.
	 */
	public static final String PLAYER_NAME = "playerName";
}
