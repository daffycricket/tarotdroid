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

import org.nla.tarotdroid.lib.ui.TabGameSetActivity;
import org.nla.tarotdroid.biz.GameSet;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public abstract class BaseRow extends LinearLayout {
	
	/**
	 * The progress dialog.
	 */
	protected ProgressDialog progressDialog;
	
//	/**
//	 * The current game set.
//	 */
//	protected GameSet gameSet;

	/**
	 * Returns the game set on which activity has to work.
	 * @return
	 */
	protected GameSet getGameSet() {
		return TabGameSetActivity.getInstance().getGameSet();
	}
	
	/**
	 * @param gameSet
	 * @param context
	 * @param dialog
	 * @param attrs
	 * @param weight
	 */
	protected BaseRow(final Context context, final ProgressDialog dialog, final AttributeSet attrs, final float weight) {
		super(context, attrs);
		checkArgument(dialog != null, "dialog is null");
		this.progressDialog = dialog;
		this.setOrientation(HORIZONTAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, weight));
		this.setWeightSum(this.getGameSet().getPlayers().size() + 1);
	}
}
