package org.nla.tarotdroid.lib.model;

import com.google.gson.Gson;

/**
 * @author Nico
 * 
 */
public class TarotDroidUser {

	private String email;

	private String password;

	private String uuid;

	public TarotDroidUser(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
