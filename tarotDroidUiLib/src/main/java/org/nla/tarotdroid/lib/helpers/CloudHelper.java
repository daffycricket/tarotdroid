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
package org.nla.tarotdroid.lib.helpers;

import static com.google.common.base.Preconditions.checkArgument;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * 
 * Class aimed to send an request that requires google authentication.
 */
public class CloudHelper {
	
	/**
	 * Find Google account in device for given email.  
	 * @param context
	 * @param email
	 * @return
	 */
	public static Account findAccount(Context context, String email) {
		checkArgument(context != null, "context is null");
		checkArgument(email != null, "email is null");
		AccountManager accountManager = AccountManager.get(context);
	    for(Account tempAccount : accountManager.getAccountsByType("com.google")) {
	    	if (tempAccount.name.equals(email)) {
	    		return tempAccount;
	    	}
	    }
		return null;
	}}
