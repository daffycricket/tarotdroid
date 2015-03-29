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
package org.nla.tarotdroid.biz;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 * A tarot player.
 */
public class Player extends PersistableBusinessObject implements Serializable {
	
	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
    private static final long serialVersionUID = -296470676847901802L;

	
    /**
	 * Creates a Player.
	 * @param name
	 */
	public Player(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}
		this.name = name;
		this.creationTs = new Date(System.currentTimeMillis());
	}
	
	/**
	 * Creates a Player.
	 */
	public Player() {
		this.name = UUID.randomUUID().toString();
		this.creationTs = new Date(System.currentTimeMillis());
	}

	/**
	 * The name of the player.
	 */
	private String name;
	
	/**
	 * Creation timestamp.
	 */
	private Date creationTs;
	
	/**
	 * Picture uri.
	 */
	private transient String pictureUri;
	
	/**
	 * Email.
	 */
	private String email;
	
	/**
	 * Facebook id.
	 */
	private String facebookId;
	
	/**
	 * Returns the name of the player.
	 * @return the name of the player.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the player.
	 * @param name	the name of the player.
	 */
	public void setName(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}
		this.name = name;
	}
	
	/**
	 * @return the creationTs
	 */
	public Date getCreationTs() {
		return this.creationTs;
	}

	/**
	 * @param creationTs the creationTs to set
	 */
	public void setCreationTs(final Date creationTs) {
		this.creationTs = creationTs;
	}
	
	/**
	 * @return
	 */
	public String getPictureUri() {
		return this.pictureUri;
	}

	/**
	 * @param uri
	 */
	public void setPictureUri(final String uri) {
		this.pictureUri = uri;
	}
	
	/**
	 * @return
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the facebookId
	 */
	public String getFacebookId() {
		return this.facebookId == null || this.facebookId.equals("") ? null : this.facebookId ;
	}

	/**
	 * @param facebookId the facebookId to set
	 */
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			throw new IllegalArgumentException("o is null");
		}
		if (((Player)o).getName() == null && this.name == null) {
			return o == this;
		}
		if (((Player)o).getName() == null || this.name == null) {
			return false;
		}
		return ((Player)o).getName().equalsIgnoreCase(this.name);
	}

	/* (non-Javadoc)
     * @see org.nla.tarotdroid.biz.PersistableBusinessObject#hashCode()
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}
}
