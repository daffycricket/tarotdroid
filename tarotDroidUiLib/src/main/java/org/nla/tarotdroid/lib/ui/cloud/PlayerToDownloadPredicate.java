package org.nla.tarotdroid.lib.ui.cloud;

import java.util.List;

import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.cloud.clientmodel.RestPlayer;

import com.google.common.base.Predicate;

/**
 * Predicate to figure which players to download from the cloud.
 */
public class PlayerToDownloadPredicate implements Predicate<RestPlayer> {
	
	/**
	 * Player to which every RestPlayer must be compared.
	 */
	private List<Player> toCompare;
	
	/**
	 * @param toCompare
	 */
	public PlayerToDownloadPredicate(List<Player> toCompare) {
		this.toCompare = toCompare;
	}
	
	/* (non-Javadoc)
	 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(RestPlayer restPlayer) {
		
		for(Player player : this.toCompare) {
			if (restPlayer.getUuid().equals(player.getUuid())) {
				return false;
			}
		}
		
		return true;
	}
}