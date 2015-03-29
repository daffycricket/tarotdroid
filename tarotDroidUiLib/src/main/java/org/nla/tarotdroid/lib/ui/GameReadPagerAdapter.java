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

import java.util.List;

import org.nla.tarotdroid.biz.GameSet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Adapter to back up game view pager fragments.
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public class GameReadPagerAdapter extends FragmentPagerAdapter {

	/**
	 * The list of fragments to display.
	 */
	private final List<Fragment> fragments;

	/**
	 * The current game set.
	 */
	private GameSet gameSet;
	
	/**
	 * Constructs a GameReadPagerAdapter. 
	 * @param fm
	 * @param fragments
	 */
	public GameReadPagerAdapter(FragmentManager fm, List<Fragment> fragments, GameSet gameSet) {
		super(fm);
		this.fragments = fragments;
		this.gameSet = gameSet;
	}

	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.fragments.size();
	}
	
    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
     */
    @Override
    public CharSequence getPageTitle(int position) {
    	return (position + 1) + "/" + this.gameSet.getGameCount();
    }
}