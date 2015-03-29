package org.nla.tarotdroid.lib.ui.cloud;

import org.nla.tarotdroid.biz.GameSet;

import com.google.common.base.Predicate;

public class GameSetToUploadForCreationPredicate implements Predicate<GameSet> {

	@Override
	public boolean apply(GameSet gameSet) {
		return gameSet.getSyncTimestamp() == null && gameSet.getSyncAccount() == null;
	}
}