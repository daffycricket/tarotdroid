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

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 * http://code.google.com/p/android-eco-tools/source/browse/trunk/ecoLamp/src/com/android/custom/EditIntegerPreference.java?spec=svn83&r=83
 */
public class EditIntegerPreference extends EditTextPreference {
	/**
	 * Constructs an EditIntegerPreference object using a Context.
	 * @param context
	 */
	public EditIntegerPreference(final Context context) {
		super(context);
	}

	/**
	 * Constructs an EditIntegerPreference object using a Context and an AttributeSet.
	 * @param context
	 * @param AttributeSet
	 */
	public EditIntegerPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructs an EditIntegerPreference object using a Context, an AttributeSet and an int.
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public EditIntegerPreference(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	/* (non-Javadoc)
	 * @see android.preference.EditTextPreference#getText()
	 */
	@Override
	public String getText() {
		try {
			return String.valueOf(getSharedPreferences().getInt(getKey(), 0));
		}
		catch (Exception e) {
			return "0";
		}
	}

	/* (non-Javadoc)
	 * @see android.preference.EditTextPreference#setText(java.lang.String)
	 */
	@Override
	public void setText(final String text) {
		getSharedPreferences().edit().putInt(getKey(), Integer.parseInt(text)).commit();
	}

	@Override
	protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
		if (restoreValue) {
			getEditText().setText(this.getText());
		}
		else {
			super.onSetInitialValue(restoreValue, defaultValue);
		}
	}
}
