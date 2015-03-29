package org.nla.tarotdroid.cloud.clientmodel;

import com.google.common.base.Objects;

/**
 * A Notification holding a localized message and a code.
 */
public class RestNotification {

	/**
	 * Error message.
	 */
	private String message;
	
	/**
	 * Error code.
	 */
	private String code;
	
	/**
	 * 
	 * @param code
	 * @param message
	 */
	public RestNotification(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	/**
	 * 
	 * @param message
	 */
	public RestNotification(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("code", this.code)
				.add("message", this.message)
				.toString();
	}
}
