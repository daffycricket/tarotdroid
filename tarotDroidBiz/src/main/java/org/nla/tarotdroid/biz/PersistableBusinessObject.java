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

import java.util.Date;
import java.util.Observable;
import java.util.UUID;

/**
 * @author Nico
 *
 */
public abstract class PersistableBusinessObject extends Observable implements IPersistable {

	/**
	 * The serial UID necessary for serialization.
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 6754502139545250543L;

	/**
	 * Id to be considered the null id.
	 */
	private static final long NULL_ID = Long.MIN_VALUE;
	
	/**
	 * Persistance id.
	 */
	protected transient long id;
	
	/**
	 * Global unique identifier.
	 */
	protected String uuid;

	/**
	 * Synchronization account.
	 */
	private String syncAccount;

	/**
	 * Synchronization timestamp.
	 */
	private Date syncTimestamp;
	
	/**
	 * Default constructor.
	 */
	protected PersistableBusinessObject() {
		this.id = PersistableBusinessObject.NULL_ID;
		this.uuid = UUID.randomUUID().toString();
	}
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.IPersistable#isPersisted()
	 */
	@Override
	public boolean isPersisted() {
		return this.id != PersistableBusinessObject.NULL_ID;
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.IPersistable#getId()
	 */
	@Override
	public long getId() {
		return this.id;
	}
	
	/**
	 * Returns the global unique identifier.
	 * @return
	 */
	public String getUuid() {
		return this.uuid;
	}
	
	/**
	 * Sets  the global unique identifier.
	 */
	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}
	
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.IPersistable#setId(java.util.UUID)
	 */
	@Override
	public void setId(final long id) {
		if (id == PersistableBusinessObject.NULL_ID) {
			throw new IllegalArgumentException("id is null");
		}

		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.IPersistable#detach()
	 */
	@Override
	public void detach() {
		if (this.isPersisted()) {
			this.id = PersistableBusinessObject.NULL_ID;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object arg0) {
		if (arg0 == null) {
			return false;
		}
		if (!(arg0 instanceof IPersistable)) {
			return false;
		}
		return this.getId() == ((IPersistable)arg0).getId();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getClass().toString().hashCode() + new Long(this.id).hashCode();
	}

	public String getSyncAccount() {
		return this.syncAccount;
	}

	public void setSyncAccount(String syncAccount) {
		this.syncAccount = syncAccount;
	}

	public Date getSyncTimestamp() {
		return this.syncTimestamp;
	}

	public void setSyncTimestamp(Date syncTimestamp) {
		this.syncTimestamp = syncTimestamp;
	}
}
