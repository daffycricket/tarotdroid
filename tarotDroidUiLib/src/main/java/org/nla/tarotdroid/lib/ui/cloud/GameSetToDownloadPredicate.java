package org.nla.tarotdroid.lib.ui.cloud;

import java.util.List;

import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.cloud.clientmodel.RestGameSet;

import com.google.common.base.Predicate;

public class GameSetToDownloadPredicate implements Predicate<RestGameSet> {
	
	/**
	 * Player to which every RestPlayer must be compared.
	 */
	private List<GameSet> toCompare;
	
	/**
	 * @param toCompare
	 */
	public GameSetToDownloadPredicate(List<GameSet> toCompare) {
		this.toCompare = toCompare;
	}
	
	/* (non-Javadoc)
	 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
	 */
	@Override
	public boolean apply(RestGameSet restGameSet) {
		
		for(GameSet gameSet : this.toCompare) {
			if (restGameSet.getUuid().equals(gameSet.getUuid())) {
				return false;
			}
		}
		
		return true;
	}
}