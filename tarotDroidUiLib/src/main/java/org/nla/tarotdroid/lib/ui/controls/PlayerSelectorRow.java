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

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.lib.helpers.UIHelper;
import org.nla.tarotdroid.lib.ui.constants.UIConstants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;

/**
 * Player selector row widget.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class PlayerSelectorRow extends LinearLayout {

	/**
	 * The player selector index. 
	 */
	private int playerIndex;
	
	/**
	 * The player index text view.
	 */
	private TextView txtPlayerIndex;
	
	/**
	 * The layout for displaying the texts.
	 */
	private LinearLayout textLayout;
	
	/**
	 * The player name text view.
	 */
	private PlayerSelectorAutoCompleteTextView txtPlayerName;
	
	/**
	 * The associated activity.
	 */
	private Activity activity;
	
	/**
	 * A buffer aimed to store a clicked player name.
	 */
	private String tempClickedName;
	
	/**
	 * Constructor using activity and player index.
	 * @param context
	 * @param playerIndex
	 */
	public PlayerSelectorRow(Activity context, int playerIndex) {
		super(context);
		this.activity = context;
		this.playerIndex = playerIndex;
		this.tempClickedName = "";
		
		this.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.setOrientation(HORIZONTAL);
		
		// player id
		this.txtPlayerIndex = new TextView(context);
		this.txtPlayerIndex.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.txtPlayerIndex.setText(Integer.toString(playerIndex));
		
		// player name
		this.txtPlayerName = new PlayerSelectorAutoCompleteTextView(context);
		this.txtPlayerName.setWidth(10);
		this.txtPlayerName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.txtPlayerName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
		this.txtPlayerName.setThreshold(1);

		// player id / name container
		this.textLayout = new LinearLayout(context);
		this.textLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.textLayout.setOrientation(VERTICAL);
		this.textLayout.setPadding(2, 2, 2, 2);
		this.textLayout.setGravity(Gravity.CENTER_VERTICAL);
		this.textLayout.addView(this.txtPlayerIndex);
		this.textLayout.addView(this.txtPlayerName);

		this.addView(this.textLayout);
		this.initializeViews();
	}
	
	/**
	 * Constructor using activity and attribute set.
	 * @param context
	 * @param attrs
	 */
	public PlayerSelectorRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.activity = (Activity)context;
		this.playerIndex = 0;
		this.tempClickedName = "";
		
		this.setOrientation(HORIZONTAL);
		
		// player id
		this.txtPlayerIndex = new TextView(context);
		this.txtPlayerIndex.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.txtPlayerIndex.setText(Integer.toString(playerIndex));
		
		// player name
		this.txtPlayerName = new PlayerSelectorAutoCompleteTextView(context);
		this.txtPlayerName.setWidth(10);
		this.txtPlayerName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.txtPlayerName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
		this.txtPlayerName.setThreshold(1);

		// player id / name container
		this.textLayout = new LinearLayout(context);
		this.textLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 15));
		this.textLayout.setOrientation(VERTICAL);
		this.textLayout.setPadding(2, 2, 2, 2);
		this.textLayout.setGravity(Gravity.CENTER_VERTICAL);
		this.textLayout.addView(this.txtPlayerIndex);
		this.textLayout.addView(this.txtPlayerName);

		this.addView(this.textLayout);
		this.initializeViews();
	}

	/**
	 * Initializes the internal views.
	 */
	private void initializeViews() {
		this.txtPlayerIndex.setText(this.getContext().getString(R.string.lblPlayerNumber, String.valueOf(this.playerIndex + 1)));
		this.txtPlayerName.setAdapter(this.buildAdapter());
				
		this.txtPlayerName.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View clickedView, int arg2, long arg3) {

				LinearLayout layout = (LinearLayout)clickedView;
				TextView txtName = (TextView)layout.getChildAt(1);
				tempClickedName = txtName.getText().toString();
				txtPlayerName.setText(tempClickedName);
			}
		});
	}
	
	/**
	 * Builds the internal adapter using 
	 * @return
	 */
	private PlayerAdapter buildAdapter() {
		List<HashMap<String,String>> friendList = new ArrayList<HashMap<String,String>>();
		for (Player player : AppContext.getApplication().getDalService().getAllPlayers()) {
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put(UIConstants.PLAYER_FACEBOOK_ID, player.getFacebookId());
            hm.put(UIConstants.PLAYER_PICTURE_URI, player.getPictureUri());
            hm.put(UIConstants.PLAYER_NAME, player.getName());
            friendList.add(hm);
		}
		
		return new PlayerAdapter(this.getContext(), friendList);
	}

	/**
	 * @return the playerIndex
	 */
	public int getPlayerIndex() {
		return this.playerIndex;
	}

	/**
	 * @param playerIndex the playerIndex to set
	 */
	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName() {
		return this.txtPlayerName.getText().toString();
	}

	/**
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName) {
		this.txtPlayerName.setText(playerName);
	}
	
	/**
	 * Player adapter for autocomplete text view.
	 */
    private class PlayerAdapter extends BaseAdapter implements Filterable {
    	
    	/**
    	 * The filter.
    	 */
    	private AlphabeticalPlayerNameFilter alphabeticalPlayerNameFilter;
    	
    	/**
    	 * The list of filtered friends.
    	 */
    	private List<HashMap<String,String>> filteredFriendList;
    	
    	/**
    	 * The list of actual friends.
    	 */
    	private List<HashMap<String,String>> originalFriendList;
    	
    	/**
    	 * The context.
    	 */
    	private Context context;
    	
    	/**
    	 * Constructor.
    	 * @param friendList
    	 */
    	protected PlayerAdapter(Context context, List<HashMap<String,String>> friendList) {
    		this.context = context;
    		this.originalFriendList = newArrayList(friendList);
    		this.filteredFriendList = friendList;
    		this.alphabeticalPlayerNameFilter = new AlphabeticalPlayerNameFilter();
    	}
    	
		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return this.filteredFriendList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			return this.filteredFriendList.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

		   // TODO Improve for perf issues
			
//		   View view = convertView;
//		   if (view == null) {
//			   LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			   view = layoutInflater.inflate(R.layout.autocomplete_layout_pic, parent, false);
//		   }
		   
		   HashMap<String,String> playerMap = (HashMap<String,String>)this.getItem(position);
		   
		   View view = null;
		   LayoutInflater layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   
		   String playerPictureUri = playerMap.get(UIConstants.PLAYER_PICTURE_URI);
		   if (playerPictureUri != null && playerPictureUri.length() != 0) {
			   
			   Bitmap contactBitmap = null;
			   try {
				   String contactId = Uri.parse(playerMap.get(UIConstants.PLAYER_PICTURE_URI)).getLastPathSegment();
				   contactBitmap = UIHelper.getContactPicture(this.context, contactId);
			   }
			   catch(Exception e) {
				   contactBitmap = null;
			   }
			   
			   // no problem when retrieving image => set it as player image 
			   if (contactBitmap != null) {
				   view = layoutInflater.inflate(R.layout.autocomplete_layout_pic, parent, false);
				   ImageView playerPicture = (ImageView)view.findViewById(R.id.playerPicture);
				   playerPicture.setImageBitmap(contactBitmap);
			   }
			   // problem => set default facebook profile picture as player image
			   else {
//				   view = layoutInflater.inflate(R.layout.autocomplete_layout_pic, parent, false);
//				   ImageView playerPicture = (ImageView)view.findViewById(R.id.playerPicture);
//				   playerPicture.setImageResource(R.drawable.icon_android);

				   
				   view = layoutInflater.inflate(R.layout.autocomplete_layout_facebookpic, parent, false);
				   ProfilePictureView playerFacebookPicture = (ProfilePictureView)view.findViewById(R.id.playerFacebookPicture);
				   playerFacebookPicture.setProfileId(null);
			   }
		   }
		   else {
			   view = layoutInflater.inflate(R.layout.autocomplete_layout_facebookpic, parent, false);
			   
			   ProfilePictureView playerFacebookPicture = (ProfilePictureView)view.findViewById(R.id.playerFacebookPicture);
			   playerFacebookPicture.setProfileId(playerMap.get(UIConstants.PLAYER_FACEBOOK_ID));
		   }
		   
		   TextView textView = (TextView)view.findViewById(R.id.username);
		   textView.setText(playerMap.get(UIConstants.PLAYER_NAME));

		   
		   return view;
			
		}

		/* (non-Javadoc)
		 * @see android.widget.Filterable#getFilter()
		 */
		@Override
		public Filter getFilter() {
			return this.alphabeticalPlayerNameFilter;
		}
		
		/**
		 * Alphabetical filter on player adapter.
		 * @author Nico
		 */
		private class AlphabeticalPlayerNameFilter extends Filter {
			
		    /* (non-Javadoc)
		     * @see android.widget.Filter#performFiltering(java.lang.CharSequence)
		     */
		    @Override
		    protected FilterResults performFiltering(CharSequence filterString) {
		        FilterResults results = new FilterResults();
		        if (filterString == null || filterString.length() == 0) {
		            results.values = originalFriendList;
		            results.count = originalFriendList.size();
		        }
		        else {
		        	String name = filterString.toString().toLowerCase();
			        List<HashMap<String,String>> friendsMatching = new ArrayList<HashMap<String,String>>();
			        for (HashMap<String,String> friend : originalFriendList) {
			        	if (friend.get(UIConstants.PLAYER_NAME).toLowerCase().startsWith(name)) {
			        		friendsMatching.add(friend);
			        	}
			        }
			        results.values = friendsMatching;
			        results.count = friendsMatching.size();
		        }

		        return results;
		    }

		    /* (non-Javadoc)
		     * @see android.widget.Filter#publishResults(java.lang.CharSequence, android.widget.Filter.FilterResults)
		     */
		    @SuppressWarnings("unchecked")
			@Override
		    protected void publishResults(CharSequence constraint, FilterResults results) {
		        if (results.count == 0) {
		            notifyDataSetInvalidated();
		        }
		        else {
		        	filteredFriendList = (List<HashMap<String,String>>) results.values;
		            notifyDataSetChanged();
		        }
		    }
		}
    }
}