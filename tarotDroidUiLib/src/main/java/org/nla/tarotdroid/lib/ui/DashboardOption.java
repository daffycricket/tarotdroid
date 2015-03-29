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
package org.nla.tarotdroid.lib.ui;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 * A business object backing up the data of dashboard option.
 */
/**
 * @author Nico
 *
 */
public class DashboardOption {
	
	/**
	 * Resource id of the drawable to display.
	 */
	private int drawableId;
	
	/**
	 * Resource id of the string title.
	 */
	private int titleResourceId;
	
	/**
	 * Resource id of the string content.
	 */
	private int contentResourceId;
	
	/**
	 * String content.
	 */
	private String content;
	
	/**
	 * Tag object.
	 */
	private Object tag;
	
	/**
	 * Constructs a DashboardOption.
	 * @param drawableId
	 * @param titleResourceId
	 * @param contentResourceId
	 * @param tag
	 */
	public DashboardOption(int drawableId, int titleResourceId, String content, Object tag) {
		this.drawableId = drawableId;
		this.titleResourceId = titleResourceId;
		this.content = content;
		this.tag = tag;
	}
	
	/**
	 * Constructs a DashboardOption.
	 * @param drawableId
	 * @param titleResourceId
	 * @param contentResourceId
	 * @param tag
	 */
	public DashboardOption(int drawableId, int titleResourceId, int contentResourceId, Object tag) {
		this.drawableId = drawableId;
		this.titleResourceId = titleResourceId;
		this.contentResourceId = contentResourceId;
		this.tag = tag;
	}

	public int getDrawableId() {
		return this.drawableId;
	}

	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}

	public int getTitleResourceId() {
		return this.titleResourceId;
	}

	public void setTitleResourceId(int titleResourceId) {
		this.titleResourceId = titleResourceId;
	}

	public int getContentResourceId() {
		return this.contentResourceId;
	}

	public void setContentResourceId(int contentResourceId) {
		this.contentResourceId = contentResourceId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Object getTag() {
		return this.tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}
}
