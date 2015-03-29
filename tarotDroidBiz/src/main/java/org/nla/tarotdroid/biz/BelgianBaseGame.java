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

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public abstract class BelgianBaseGame extends BaseGame {
	
    /**
	 * Serial version ID.
	 */
	@CloudField(cloudify=false)
	private static final long serialVersionUID = 5663881475225952069L;

	/**
	 * First player.
	 */
	protected Player first;
	
	/**
	 * Second player.
	 */
	protected Player second;

	/**
	 * Third player.
	 */
	protected Player third;

	/**
	 * @return the first
	 */
	public Player getFirst() {
		return this.first;
	}

	/**
	 * @param first the first to set
	 */
	public void setFirst(final Player first) {
		this.first = first;
	}

	/**
	 * @return the second
	 */
	public Player getSecond() {
		return this.second;
	}

	/**
	 * @param second the second to set
	 */
	public void setSecond(final Player second) {
		this.second = second;
	}

	/**
	 * @return the third
	 */
	public Player getThird() {
		return this.third;
	}

	/**
	 * @param third the third to set
	 */
	public void setThird(final Player third) {
		this.third = third;
	}
}
