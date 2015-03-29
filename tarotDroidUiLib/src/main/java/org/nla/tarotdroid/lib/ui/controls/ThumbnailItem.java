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
import org.nla.tarotdroid.lib.helpers.UIHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A view aimed to display an image, a title and a content.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class ThumbnailItem extends LinearLayout {
	
	/**
	 * The identifier of the drawable to use as an image.
	 */
	private int drawableId;
	
	/**
	 * The title string.
	 */
	private CharSequence title;
	
	/**
	 * The content string.
	 */
	private CharSequence content;
	
    /**
     * A Uri used to retrieve a picture from device
     */
	private Uri pictureUri;
	
	/**
	 * Constructs a ThumbnailItem.
	 * @param context
	 * @param drawableId
	 * @param titleResourceId
	 * @param contentResourceId
	 */
	public ThumbnailItem(final Context context, int drawableId, int titleResourceId, int contentResourceId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.thumbnail_item, this);
    	this.drawableId = drawableId; 
    	this.title = context.getString(titleResourceId);
    	this.content = context.getString(contentResourceId);
    	this.initializeViews();
	}
	
	/**
	 * Constructs a ThumbnailItem.
	 * @param context
	 * @param pictureUri
	 * @param defaultDrawableId
	 * @param titleResourceId
	 * @param contentResourceId
	 */
	public ThumbnailItem(final Context context, Uri pictureUri, int defaultDrawableId, int titleResourceId, int contentResourceId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.thumbnail_item, this);
    	this.drawableId = defaultDrawableId;
    	this.pictureUri = pictureUri;
    	this.title = context.getString(titleResourceId);
    	this.content = context.getString(contentResourceId);
    	this.initializeViews();
	}
	
	/**
	 * Constructs a ThumbnailItem.
	 * @param context
	 * @param drawableId
	 * @param title
	 * @param content
	 */
	public ThumbnailItem(final Context context, int drawableId, CharSequence title, CharSequence content) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.thumbnail_item, this);
    	this.drawableId = drawableId; 
    	this.title = title;
    	this.content = content;
    	this.initializeViews();
	}
	
	/**
	 * Constructs a ThumbnailItem.
	 * @param context
	 * @param pictureUri
	 * @param defaultDrawableId
	 * @param title
	 * @param content
	 */
	public ThumbnailItem(final Context context, Uri pictureUri, int defaultDrawableId, CharSequence title, CharSequence content) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.thumbnail_item, this);
        this.drawableId = defaultDrawableId;
    	this.pictureUri = pictureUri;
    	this.title = title;
    	this.content = content;
    	this.initializeViews();
	}
	
	/**
	 * Initializes the views of the item.
	 */
	private void initializeViews() {
        
		TextView txtTitle = (TextView) this.findViewById(R.id.txtTitle);
		TextView txtContent = (TextView) this.findViewById(R.id.txtContent);
		txtTitle.setText(this.title);
		txtContent.setText(this.content);
		
		ImageView imageView = (ImageView) this.findViewById(R.id.imgThumbnail);
        if (this.pictureUri == null)
        {
        	imageView.setImageResource(this.drawableId);
        }
        else {
            try {
                    Bitmap selectedImage = null;
                    if (this.pictureUri.toString().contains("contact")) {
                    	selectedImage = UIHelper.getContactPicture(this.getContext(), this.pictureUri.getLastPathSegment());
                    }

                    if (selectedImage != null) { 
                    	imageView.setImageBitmap(selectedImage);
                    }
                    else {
                    	imageView.setImageResource(this.drawableId);
                    }
            }
            catch(Exception e) {
            	imageView.setImageResource(this.drawableId);  
            }               
        }
    }
}
