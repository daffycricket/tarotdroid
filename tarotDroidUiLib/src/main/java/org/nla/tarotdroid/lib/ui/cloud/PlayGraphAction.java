package org.nla.tarotdroid.lib.ui.cloud;

import com.facebook.model.OpenGraphAction;

public interface PlayGraphAction extends OpenGraphAction {
	    // The meal object
	    public GameSetGraphObject getGame();
	    public void setGame(GameSetGraphObject meal);
	}