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
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class TextItem extends LinearLayout {
	
	/**
	 * 
	 */
	private CharSequence title;
	
	/**
	 * 
	 */
	private CharSequence content;
	
	
	/**
	 * @param context
	 */
	public TextItem(final Context context, int titleResourceId, int contentResourceId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.thumbnail_item, this);
    	this.title = context.getString(titleResourceId);
    	this.content = context.getString(contentResourceId);
    	this.initializeViews();
	}
	
	/**
	 * @param context
	 */
	public TextItem(final Context context, CharSequence title, CharSequence content) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.text_item, this);
    	this.title = title;
    	this.content = content;
    	this.initializeViews();
	}
	
	/**
	 * 
	 */
	private void initializeViews() {
		TextView txtTitle = (TextView) this.findViewById(R.id.txtTitle);
		TextView txtContent = (TextView) this.findViewById(R.id.txtContent);
		
		txtTitle.setText(this.title);
		txtContent.setText(this.content);
	}
}
