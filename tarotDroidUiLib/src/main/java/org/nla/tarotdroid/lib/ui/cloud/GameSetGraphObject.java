package org.nla.tarotdroid.lib.ui.cloud;

import com.facebook.model.GraphObject;

public interface GameSetGraphObject extends GraphObject {
	    // A URL
	    public String getUrl();
	    public void setUrl(String url);

	    // An ID
	    public String getId();
	    public void setId(String id);
}
