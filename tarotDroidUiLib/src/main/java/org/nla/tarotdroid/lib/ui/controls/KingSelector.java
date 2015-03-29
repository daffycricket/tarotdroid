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

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.King;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class KingSelector extends Selector<King> {

	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public KingSelector(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    /**
     * Builds a View associated to an instance of T.
     * @param text
     * @return
     */
	@Override
    protected View buildObjectView(final King king) {
		
		// otherwise, get 
		ImageView toReturn = new ImageView(getContext());
		
		// retrieve king object
		switch (king.getKingType()) {
			case Clubs:
				toReturn.setImageResource(R.drawable.icon_club);
				break;
			case Diamonds:
				toReturn.setImageResource(R.drawable.icon_diamond_old);
				break;					
			case Hearts:
				toReturn.setImageResource(R.drawable.icon_heart_old);
				break;
			case Spades:
				toReturn.setImageResource(R.drawable.icon_spade);
				break;
			default:
				throw new IllegalStateException("unknow king type");
		}
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 64, 1);
		params.setMargins(2, 0, 2, 0);
		toReturn.setLayoutParams(params);
		
		return toReturn;
    }

}
