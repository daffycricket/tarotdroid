package org.nla.tarotdroid.lib.ui.controls;

import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
 
public class PlayerSelectorAutoCompleteTextView extends AutoCompleteTextView {
 
    /**
     * @param context
     * @param attrs
     */
    public PlayerSelectorAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    /**
     * @param context
     */
    public PlayerSelectorAutoCompleteTextView(Context context) {
        super(context);
    }
 
    /* (non-Javadoc)
     * @see android.widget.AutoCompleteTextView#convertSelectionToString(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
	@Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("txt");
    }
}