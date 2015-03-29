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

import java.util.List;

import org.nla.tarotdroid.lib.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class Selector<T> extends LinearLayout {
	
	/**
	 * The backend objects represented by the Gallery.
	 */
	private List<T> objects;
	
	/**
	 * The selected object.
	 */
	private T selected;
	
	/**
	 * The mode : read-only or editable;
	 */
	private boolean isReadOnly;
	
	/**
	 * The selection/deselection event listener.
	 */
	private OnObjectSelectedListener<T> onObjectSelectedListener;
	
	/**
	 * Constructor.
	 * @param context
	 * @param attrs
	 */
	public Selector(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER);
		this.selected = null;
		this.onObjectSelectedListener = null;
		this.isReadOnly = false;
		
		// retrieve custom attributes from xml
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Selector);
		final int N = array.getIndexCount();
		for (int i = 0; i < N; ++i)
		{
		    int attr = array.getIndex(i);
		    switch (attr)
		    {

                // TODO : fix this...
                case 5: //R.styleable.Selector_readOnly:
		        	this.isReadOnly = array.getBoolean(attr, false);
		            break;
		    }
		}
		array.recycle();		
	}

	/**
	 * Constructor.
	 * @param context
	 */
	public Selector(final Context context) {
		this(context, null);
	}
	
    /**
	 * @return the objects
	 */
	public List<T> getObjects() {
		return ImmutableList.copyOf(this.objects);
	}

	/**
	 * @param objects the objects to set
	 */
	public void setObjects(final List<T> objects) {
		this.objects = objects;
		if (this.objects != null) {
			this.initializeViews();
		}
	}
	
	/**
	 * Selection/deselection event listener.
	 */
	private OnClickListener clickListener = new OnClickListener() {
		
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void onClick(final View v) {
			if (v.getTag() == null) {
				return;
			}
			
			else if (v.getTag() == selected) {
				setSelected(null);
			}
			
			else {
				setSelected((T)v.getTag());
			}
			updateViews();
		}
	};
	
	/**
	 * Updates the views. 
	 */
	private void updateViews() {
		for (int i = 0; i < this.getChildCount(); ++i ) {
			View view = this.getChildAt(i);
			if (view.getTag() == this.selected) {
				//view.setBackgroundColor(Color.LTGRAY);
				view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_selected_item));
			}
			else {
				view.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_unselected_text_item));
				//view.setBackgroundColor(Color.GRAY);
			}
		}
	}

	/**
     * Initializes graphical views with model.
     */
    private void initializeViews() {
    	this.removeAllViews();
    	for(T object : this.objects) {
    		View view = this.buildObjectView(object);
    		view.setTag(object);
    		if (!this.isReadOnly) {
    			view.setOnClickListener(this.clickListener);
    		}
    		this.addView(view);
    	}
//    	ViewGroup.LayoutParams oldParams = this.getLayoutParams();
//    	LinearLayout.LayoutParams newParams = new LayoutParams(oldParams.width, oldParams.height);
    	this.setWeightSum(this.objects.size());
//    	this.setLayoutParams(newParams);
    	this.updateViews();
    }
    
    /**
     * Sets the widget read only.
     * @param isReadOnly
     */
    public void setReadOnly(boolean isReadOnly) {
    	if (this.isReadOnly != isReadOnly) {
    		this.isReadOnly = isReadOnly;
    		initializeViews();
    	}
    }
    
    /**
     * Returns the selected object.
     * @return the selected object.
     */
	public T getSelected() {
		return this.selected;
    }
	
    /**
     * Sets the specified object.
     */
	public void setSelected(final T toSelect) {
		
		// object already selected, do nothing.
		if (toSelect == this.selected) {
			return;
		}
		
		// deselect everything 
		else if (toSelect == null) {
			this.selected = null;
			this.updateViews();
			if (this.onObjectSelectedListener != null) {
				this.onObjectSelectedListener.onNothingSelected();
			}
		}
		
		// object not present in list, do nothing
		else if (!this.objects.contains(toSelect)) {
			return;
		}
		
		// select given object
		else  {
			this.selected = toSelect;
			this.updateViews();
			if (this.onObjectSelectedListener != null) {
				this.onObjectSelectedListener.onItemSelected(toSelect);
			}
		}
    }
    
    /**
     * Indicates whether an object has been selected.
     * @return true if an object has been selected, false otherwise.
     */
    public boolean isSelected() {
    	return this.selected != null;
    }
    
    /**
     * Builds a View associated to an instance of T.
     * @param text
     * @return
     */
    protected View buildObjectView(final T object) {
		TextView toreturn = new TextView(this.getContext());
		toreturn.setGravity(Gravity.CENTER);
		
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 64, 1);
		params.setMargins(2, 0, 2, 0);
		toreturn.setLayoutParams(params);
		
		toreturn.setSingleLine(false);
		toreturn.setTypeface(null, Typeface.BOLD);
		toreturn.setTextColor(Color.WHITE);
		toreturn.setEllipsize(TruncateAt.END);
		toreturn.setBackgroundColor(Color.GRAY);
		toreturn.setText(object.toString());
		return toreturn;
    }
    
    /**
     * Sets an object selection/deselection events listener.
     * @param listener
     */
    public void setObjectSelectedListener(final OnObjectSelectedListener<T> onObjectSelectedListener) {
    	this.onObjectSelectedListener = onObjectSelectedListener;
    }
    
    /**
     * Object selection listener.
     * @param <T>
     */
    public interface OnObjectSelectedListener<T> {
    	/**
    	 * Event thrown when an object is selected in the selector.
    	 * @param selected the selected object.
    	 */
    	public void onItemSelected(final T selected);
    	
    	/**
    	 * Event thrown when no object is selected any longer.
    	 * @param selected the selected object.
    	 */
    	public void onNothingSelected();
    }
}
