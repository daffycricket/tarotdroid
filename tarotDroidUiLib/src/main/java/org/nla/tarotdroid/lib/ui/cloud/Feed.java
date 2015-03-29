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
package org.nla.tarotdroid.lib.ui.cloud;

import java.util.Date;

/**
 * Feed item.
 */
public class Feed {

	/**
	 * Internal id.
	 */
	private Long id;
	
	/**
	 * Locale code.
	 */
	private String locale;
	
	/**
	 * Localized title. 
	 */
	private String title;
	
	/**
	 * Localized content. 
	 */
	private String content;
	
	/**
	 * Creation date.
	 */
	private Date creation;
	
	/**
	 * Indicates whether feed is active.
	 */
	private boolean isActive;
	
	/**
	 * Default constructor.
	 */
	public Feed() {
	}

	/**
	 * Constructor using all params.
	 * @param id
	 * @param locale
	 * @param title
	 * @param content
	 * @param creation
	 * @param isActive
	 */
	public Feed(Long id, String locale, String title, String content, Date creation, boolean isActive) {
		this.id = id;
		this.locale = locale;
		this.title = title;
		this.content = content;
		this.creation = creation;
		this.isActive = isActive;
	}
	
	/**
	 * Constructor using all params.
	 * @param locale
	 * @param title
	 * @param content
	 * @param creation
	 * @param isActive
	 */
	public Feed(String locale, String title, String content, Date creation, boolean isActive) {
		this.locale = locale;
		this.title = title;
		this.content = content;
		this.creation = creation;
		this.isActive = isActive;
	}

	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return
	 */
	public Date getCreation() {
		return creation;
	}

	/**
	 * @param creation
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}

	/**
	 * @return
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}