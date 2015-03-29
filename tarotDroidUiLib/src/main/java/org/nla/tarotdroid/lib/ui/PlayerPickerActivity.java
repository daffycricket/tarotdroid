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
package org.nla.tarotdroid.lib.ui;

import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.FacebookException;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.PickerFragment;

/**
 * Activity that allows you to select friends.
 * @author Nico
 */
public class PlayerPickerActivity extends SherlockFragmentActivity {

	/**
	 * Friend picker uri.
	 */
	public static final Uri FRIEND_PICKER = Uri.parse("picker://friend");
	
	/**
	 * Facebook player picker. 
	 */
	private FriendPickerFragment friendPickerFragment;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pickers);

	    Bundle args = getIntent().getExtras();
	    FragmentManager manager = getSupportFragmentManager();
	    Fragment fragmentToShow = null;

        if (savedInstanceState == null) {
            this.friendPickerFragment = new FriendPickerFragment(args);
        }
        else {
        	this.friendPickerFragment = (FriendPickerFragment) manager.findFragmentById(R.id.picker_fragment);
        }
        
        // Set the listener to handle errors
        this.friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {

			@Override
			public void onError(PickerFragment<?> fragment, FacebookException error) {
				
			}
        });
        
        // Set the listener to handle button clicks
        this.friendPickerFragment.setOnDoneButtonClickedListener(
                new PickerFragment.OnDoneButtonClickedListener() {
		            @Override
		            public void onDoneButtonClicked(PickerFragment<?> fragment) {
		                finishActivity();
		            }
        });
        fragmentToShow = this.friendPickerFragment;

	    manager.beginTransaction()
	           .replace(R.id.picker_fragment, fragmentToShow)
	           .commit();
	}

	/**
	 * Called when an error occurs. 
	 * @param error
	 */
	private void onError(Exception error) {
	    this.onError(error.getLocalizedMessage(), false);
	}

	/**
	 * Called when an error occurs.
	 * @param error
	 * @param finishActivity
	 */
	private void onError(String error, final boolean finishActivity) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle(R.string.error_dialog_title).setMessage(error);
	    builder.setPositiveButton(R.string.error_dialog_button_text,  new DialogInterface.OnClickListener() {
            	@Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (finishActivity) {
                        finishActivity();
                    }
                }
            }
	    );
	    builder.show();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
	    super.onStart();
        try {
            this.friendPickerFragment.loadData(false);
        }
        catch (Exception ex) {
            onError(ex);
        }
	}

	/**
	 * Called when the activity is finished.
	 */
	private void finishActivity() {
	    if (this.friendPickerFragment != null && this.friendPickerFragment.getSelection().size() > 0) {
	    	AppContext.getApplication().setSelectedUser(friendPickerFragment.getSelection().get(0));
	    	AppContext.getApplication().setSelectedUsers(friendPickerFragment.getSelection());
	    }   
	    setResult(RESULT_OK, null);
	    this.finish();
	}
}
