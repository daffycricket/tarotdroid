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

import com.facebook.widget.ProfilePictureView;

/**
 * A view aimed to display an facebook picture, a title and a content.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class FacebookThumbnailItem extends LinearLayout {
	
	/**
	 * The title string.
	 */
	private CharSequence title;
	
	/**
	 * The content string.
	 */
	private CharSequence content;
		
	/**
	 * The facebook id.
	 */
	private String facebookId;
	
	/**
	 * Constructs a FacebookThumbnailItem.
	 * @param context
	 * @param facebookId
	 * @param title
	 * @param content
	 */
	public FacebookThumbnailItem(final Context context, String facebookId, CharSequence title, CharSequence content) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.thumbnail_item_facebook, this);
        this.facebookId = facebookId;
    	this.title = title;
    	this.content = content;
    	this.initializeViews();
	}
	
	/**
	 * Initializes the views.
	 */
	private void initializeViews() {
        
		TextView txtTitle = (TextView) this.findViewById(R.id.txtTitle);
		TextView txtContent = (TextView) this.findViewById(R.id.txtContent);
		txtTitle.setText(this.title);
		txtContent.setText(this.content);
		
		if (this.facebookId != null) {
			ProfilePictureView pictureView = (ProfilePictureView) this.findViewById(R.id.imgThumbnail);
			pictureView.setProfileId(this.facebookId);
		}
    }
}
