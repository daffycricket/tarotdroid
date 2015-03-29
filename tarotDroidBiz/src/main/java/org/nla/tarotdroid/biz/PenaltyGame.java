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

import static com.google.common.base.Preconditions.checkArgument;

import org.nla.tarotdroid.biz.computers.PenaltyGameGameScoresComputer;


/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class PenaltyGame extends BaseGame {

    /**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -4478609294064065429L;
	
	/**
	 * Player to be penalted.
	 */
	private Player penaltedPlayer;
	
	/**
	 * Penalty points.
	 */
	private int penaltyPoints;
	
	/**
	 * Type of penalty.
	 */
	private String penaltyType;

	/**
	 * @return the penaltedPlayer
	 */
	public Player getPenaltedPlayer() {
		return this.penaltedPlayer;
	}

	/**
	 * @param penaltedPlayer the penaltedPlayer to set
	 */
	public void setPenaltedPlayer(Player penaltedPlayer) {
		this.penaltedPlayer = penaltedPlayer;
	}

	/**
	 * @return the penaltyPoints
	 */
	public int getPenaltyPoints() {
		return this.penaltyPoints;
	}

	/**
	 * @param penaltyPoints the penaltyPoints to set
	 */
	public void setPenaltyPoints(int penaltyPoints) {
		checkArgument(penaltyPoints > 0, "penalty points must be over 0");
		this.penaltyPoints = penaltyPoints;
	}
	
	/**
	 * @return the penaltyGameType
	 */
	public String getPenaltyType() {
		return this.penaltyType;
	}

	/**
	 * @param penaltyGameType the penaltyGameType to set
	 */
	public void setPenaltyType(String penaltyType) {
		this.penaltyType = penaltyType;
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.BaseGame#getGameScores()
	 */
	@Override
	public GameScores getGameScores() {
		PenaltyGameGameScoresComputer gameScoresComputer = new PenaltyGameGameScoresComputer(this);
		gameScoresComputer.computeScore();
		return gameScoresComputer.getGameScores();
	}
}
