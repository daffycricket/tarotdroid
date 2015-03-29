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
package org.nla.tarotdroid.cloud.clientmodel;

import java.io.Serializable;
import java.util.UUID;

import com.google.common.base.Objects;

/**
 * RestPlayer entity.
 */
@SuppressWarnings("serial")
public class RestPlayer implements Serializable{
	
//	/**
//	 * Device local id.
//	 */
//	private Long localId;
//	
	/**
	 * Creation timestamp.
	 */
	private long creationTs;
	
	/**
	 * Name property.
	 */
	private String name;
	
	/**
	 * PictureUri property.
	 */
	private String pictureUri;
	
	/**
	 * Email property.
	 */
	private String email;
	
	/**
	 * The facebook id.
	 */
	private String facebookId;

	/**
	 * Is valid property.
	 */
	private boolean isValid;

//	/**
//	 * The id.
//	 */
//	private Long id;
//	
//	/**
//	 * The cloud creator.
//	 */
//	private String cloudCreator;
//	
//	/**
//	 * The cloud last timestamp.
//	 */
//	private Date cloudCreationTs;
//
//	/**
//	 * The cloud last modifier.
//	 */
//	private String cloudLastModifier;
//	
//	/**
//	 * The cloud last timestamp.
//	 */
//	private Date cloudLastTs;
	
	/**
	 * A UUID identifying the object.
	 */
	private String uuid;
	
	/**
	 * Default constructor.
	 */
	public RestPlayer() {
		this.isValid = true;
		this.uuid = UUID.randomUUID().toString();
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

//	public Long getLocalId() {
//		return this.localId;
//	}
//
//	public void setLocalId(Long localId) {
//		this.localId = localId;
//	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getCreationTs() {
		return this.creationTs;
	}

	public void setCreationTs(long creationTs) {
		this.creationTs = creationTs;
	}
	
	public String getPictureUri() {
		return this.pictureUri;
	}

	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isValid() {
		return this.isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	/**
	 * @return the facebookId
	 */
	public String getFacebookId() {
		return this.facebookId;
	}

	/**
	 * @param facebookId the facebookId to set
	 */
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

//	public Long getId() {
//		return this.id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

//	public String getCloudCreator() {
//		return this.cloudCreator;
//	}
//
//	public void setCloudCreator(String cloudCreator) {
//		this.cloudCreator = cloudCreator;
//	}
//
//	public Date getCloudCreationTs() {
//		return this.cloudCreationTs;
//	}
//
//	public void setCloudCreationTs(Date cloudCreationTs) {
//		this.cloudCreationTs = cloudCreationTs;
//	}
//
//	public String getCloudLastModifier() {
//		return this.cloudLastModifier;
//	}
//
//	public void setCloudLastModifier(String cloudLastModifier) {
//		this.cloudLastModifier = cloudLastModifier;
//	}
//
//	public Date getCloudLastModificationTs() {
//		return this.cloudLastTs;
//	}
//
//	public void setCloudLastModificationTs(Date cloudLastTs) {
//		this.cloudLastTs = cloudLastTs;
//	}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("uuid", this.uuid)
//				.add("id", this.id)
//				.add("localId", this.localId)
				.add("creationTs", this.creationTs)
				.add("name", this.name)
				.add("email", this.email)
				.add("facebookId", this.facebookId)
				.add("isValid", this.isValid)
				.toString();
	}
}