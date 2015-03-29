package org.nla.tarotdroid.lib.ui.cloud;

import org.nla.tarotdroid.biz.Player;

import com.google.common.base.Predicate;

/**
 * Predicate to figure which players to upload t the cloud.
 */
public class PlayerToUploadForCreationPredicate implements Predicate<Player> {
	
	/* (non-Javadoc)
	 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(Player player) {
		
		if (player.getSyncTimestamp() == null && player.getSyncAccount() == null) {
			return true;
		}
		else {
			return false;
		}
	}
}