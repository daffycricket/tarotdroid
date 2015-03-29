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
public abstract class StandardBaseGame extends BaseGame {

	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
    private static final long serialVersionUID = -5265480278028929847L;

    /**
	 * Contract to win a game with no oudler.
	 */
	@CloudField(cloudify=false)
	private static final double ZERO_OUDLER_AIM = 56.0;

	/**
	 * Contract to win a game with one oudler.
	 */
	@CloudField(cloudify=false)
	private static final double ONE_OUDLER_AIM = 51.0;

	/**
	 * Contract to win a game with two oudlers.
	 */
	@CloudField(cloudify=false)
	private static final double TWO_OUDLER_AIM = 41.0;

	/**
	 * Contract to win a game with three oudlers.
	 */
	@CloudField(cloudify=false)
	private static final double THREE_OUDLER_AIM = 36.0;

	/**
	 * The leading player.
	 */
	protected Player leadingPlayer;

	/**
	 * The team with a poignee.
	 */
	@CloudField(targetType="String")
	private Team teamWithPoignee;

	/**
	 * The team with a double poignee.
	 */
	@CloudField(targetType="String")
	private Team teamWithDoublePoignee;

	/**
	 * The team with a triple poignee.
	 */
	@CloudField(targetType="String")
	private Team teamWithTriplePoignee;

	/**
	 * The team who brought the kid card to the end.
	 */
	@CloudField(targetType="String")
	private Team teamWithKidAtTheEnd;

	/**
	 * The list of players with a misery.
	 */
	private PlayerList playersWithMisery;

	/**
	 * The game bet.
	 */
	@CloudField(targetType="String")
	private Bet bet;

	/**
	 * The number of oudlers of the leading team.
	 */
	private int numberOfOudlers;

	/**
	 * The number of points earned by the leading team.
	 */
	private double points;

	/**
	 * The potential chelem.
	 */
	@CloudField(targetType="String")
	private Chelem chelem;

	/**
	 * Default constructor.
	 */
	protected StandardBaseGame() {
		super();
	}

	/**
	 * Returns the leading player.
	 * @return the leading player.
	 */
	public Player getLeadingPlayer() {
		return this.leadingPlayer;
	}

	/**
	 * Sets the leading player.
	 * @param leadingPlayer the leading player to set.
	 */
	public void setLeadingPlayer(final Player leadingPlayer) {
		if (leadingPlayer == null) {
			throw new IllegalArgumentException("leadingPlayer is null");
		}
//		if (!this.players.contains(leadingPlayer)) {
//			throw new IllegalArgumentException(leadingPlayer + " not playing");
//		}
		
		this.leadingPlayer = leadingPlayer;
	}
	
	/**
	 * Sets the team who got a poignee.
	 * @param teamWithPoignee
	 */
	public void setTeamWithPoignee(final Team teamWithPoignee) {
		this.teamWithPoignee = teamWithPoignee;
	}
	
	/**
	 * Returns the team who got a poignee.
	 */
	public Team getTeamWithPoignee() {
		return this.teamWithPoignee;
	}
	
	/**
	 * Sets one player with a poignee.
	 * @deprecated Kept for consistency with games created at the beginning of TarotDroid's development (before the 27/11/2011). At that time, the poignee was given to a player, but now it's given to the team. Use setTeamWithPoignee(Team) instead.
	 * @param playerWithPoignee a player with a poignee to set.
	 */
	@Deprecated
	public void setPlayerWithPoignee(final PersistableBusinessObject playerWithPoignee) {
		// first, make sure the user is in the game
		if (playerWithPoignee == null || !this.players.contains(playerWithPoignee)) {
			return;
		}

		// determines the team he's in with the defense team property  
		if (this.getDefenseTeam().contains(playerWithPoignee)) {
			this.setTeamWithPoignee(Team.DEFENSE_TEAM);
		}
		else {
			this.setTeamWithPoignee(Team.LEADING_TEAM);
		}
	}
	
	/**
	 * Sets the team who got a double poignee.
	 * @param teamWithDoublePoignee
	 */
	public void setTeamWithDoublePoignee(final Team teamWithDoublePoignee) {
		this.teamWithDoublePoignee = teamWithDoublePoignee;
	}
	
	/**
	 * Returns the team who got a double poignee.
	 */
	public Team getTeamWithDoublePoignee() {
		return this.teamWithDoublePoignee;
	}
	
	/**
	 * Sets a players with a double poignee.
	 * @deprecated Kept for consistency with games created at the beginning of TarotDroid's development (before the 27/11/2011). At that time, the double poignee was given to a player, but now it's given to the team. Use setTeamWithDoublePoignee(Team) instead.
	 * @param playerWithDoublePoignee a player with a double poignee to set.
	 */
	@Deprecated
	public void setPlayerWithDoublePoignee(final PersistableBusinessObject playerWithDoublePoignee) {
		// first, make sure the user is in the game
		if (playerWithDoublePoignee == null || !this.players.contains(playerWithDoublePoignee)) {
			return;
		}

		// determines the team he's in with the defense team property  
		if (this.getDefenseTeam().contains(playerWithDoublePoignee)) {
			this.setTeamWithDoublePoignee(Team.DEFENSE_TEAM);
		}
		else {
			this.setTeamWithDoublePoignee(Team.LEADING_TEAM);
		}
	}
	
	/**
	 * Sets the team who got a triple poignee.
	 * @param teamWithTriplePoignee
	 */
	public void setTeamWithTriplePoignee(final Team teamWithTriplePoignee) {
		this.teamWithTriplePoignee = teamWithTriplePoignee;
	}
	
	/**
	 * Returns the team who got a triple poignee.
	 */
	public Team getTeamWithTriplePoignee() {
		return this.teamWithTriplePoignee;
	}
	
	/**
	 * @deprecated Kept for consistency with games created at the beginning of TarotDroid's development (before the 27/11/2011). At that time, the triple poignee was given to a player, but now it's given to the team. Use setTeamWithTriplePoignee(Team) instead.  
	 * @param playerWithTriplePoignee the playerWithTriplePoignee to set
	 */
	@Deprecated
	public void setPlayerWithTriplePoignee(final PersistableBusinessObject playerWithTriplePoignee) {
		// first, make sure the user is in the game
		if (playerWithTriplePoignee == null || !this.players.contains(playerWithTriplePoignee)) {
			return;
		}

		// determines the team he's in with the defense team property  
		if (this.getDefenseTeam().contains(playerWithTriplePoignee)) {
			this.setTeamWithTriplePoignee(Team.DEFENSE_TEAM);
		}
		else {
			this.setTeamWithTriplePoignee(Team.LEADING_TEAM);
		}
	}

	/**
	 * Returns the list of players with a misery.
	 * @return the list of players with a misery.
	 */
	public PlayerList getPlayersWithMisery() {
		return this.playersWithMisery;
	}

	/**
	 * Returns the first player with a misery.
	 * @return the first player with a misery.
	 */
	public Player getPlayerWithMisery() {
		if(this.playersWithMisery != null && this.playersWithMisery.size() >= 1) {
			return this.playersWithMisery.get(1);
		}
		return null;
	}
	
	/**
	 * Sets the list of players with a misery.
	 * @param playersWithMisery the list of players with a misery to set.
	 */
	public void setPlayersWithMisery(final PlayerList playersWithMisery) {
		this.playersWithMisery = playersWithMisery;
	}
	
	/**
	 * Sets a player with a misery.
	 * @param playerWithMisery the player with a misery to set.
	 */
	public void setPlayerWithMisery(final Player playerWithMisery) {
		if (playerWithMisery != null) {
			this.playersWithMisery = new PlayerList();
			this.playersWithMisery.add(playerWithMisery);
		}
	}

	/**
	 * Indicates whether a team brought the kid card at the end.
	 * @return true if a team brought the kid card at the end; false otherwise.
	 */
	public boolean isKidAtTheEnd() {
		return this.teamWithKidAtTheEnd != null;
	}

	/**
	 * Returns the team who brought the kid card at the end.
	 * @return the team who brought the kid card at the end.
	 */
	public Team getTeamWithKidAtTheEnd() {
		return this.teamWithKidAtTheEnd;
	}

	/**
	 * Sets the team who brought the kid card at the end.
	 * @param teamWithKidAtTheEnd the team who brought the kid card at the end to set.
	 */
	public void setTeamWithKidAtTheEnd(final Team teamWithKidAtTheEnd) {
		this.teamWithKidAtTheEnd = teamWithKidAtTheEnd;
	}
	
	/**
	 * Sets the player who brought the kid card at the end.
	 * @deprecated Kept for consistency with games created at the beginning of TarotDroid's development (before the 27/11/2011). At that time, the kid at the end was given to a player, but now it's given to the team. Use setTeamWithKidAtTheEnd(Team) instead.  
	 * @param playerwithKidAtTheEnd
	 */
	@Deprecated
	public void setPlayerWithKidAtTheEnd(final PersistableBusinessObject playerwithKidAtTheEnd) {
		// first, make sure the user is in the game
		if (playerwithKidAtTheEnd == null || !this.players.contains(playerwithKidAtTheEnd)) {
			return;
		}

		// determines the team he's in with the defense team property  
		if (this.getDefenseTeam().contains(playerwithKidAtTheEnd)) {
			this.setTeamWithKidAtTheEnd(Team.DEFENSE_TEAM);
		}
		else {
			this.setTeamWithKidAtTheEnd(Team.LEADING_TEAM);
		}
	}

	/**
	 * Returns the bet.
	 * @return the bet.
	 */
	public Bet getBet() {
		return this.bet;
	}

	/**
	 * Sets the bet.
	 * @param bet the bet to set.
	 */
	public void setBet(final Bet bet) {
		if (bet == null) {
			throw new IllegalArgumentException("bet=" + bet);
		}
		this.bet = bet;
	}

	/**
	 * Returns the number of oudlers of the leading team.
	 * @return the number of oudlers of the leading team.
	 */
	public int getNumberOfOudlers() {
		return this.numberOfOudlers;
	}

	/**
	 * Sets the number of oudlers of the leading team.
	 * @param numberOfOudlers the number of oudlers of the leading team to set.
	 */
	public void setNumberOfOudlers(final int numberOfOudlers) {
		if (numberOfOudlers < 0 || numberOfOudlers > 3) {
			throw new IllegalArgumentException("incorrect numberOfOudlers (must be btw 0 and 3) =" + numberOfOudlers);
		}
		this.numberOfOudlers = numberOfOudlers;
	}

	/**
	 * Returns the points earned by the leading team.
	 * @return the points earned by the leading team.
	 */
	public double getPoints() {
		return this.points;
	}

	/**
	 * Sets the points earned by the leading team.
	 * @param points the points earned by the leading team to set.
	 */
	public void setPoints(final double points) {
		this.points = points;
	}

	/**
	 * @return the chelem
	 */
	public Chelem getChelem() {
		return this.chelem;
	}

	/**
	 * @param chelem the chelem to set
	 */
	public void setChelem(final Chelem chelem) {
		this.chelem = chelem;
	}

	/**
	 * Indicates whether the leading team won the game. 
	 * @return true if the leading team won the game; false otherwise.
	 */
	public boolean isGameWon() {
		switch(this.numberOfOudlers) {
		case 0 :
			return this.points >= StandardBaseGame.ZERO_OUDLER_AIM;
		case 1 :
			return this.points >= StandardBaseGame.ONE_OUDLER_AIM;
		case 2 :
			return this.points >= StandardBaseGame.TWO_OUDLER_AIM;
		case 3 :
			return this.points >= StandardBaseGame.THREE_OUDLER_AIM;
		default : 
			throw new IllegalStateException("incorrect number of oudlers=" + this.numberOfOudlers);
		}
	}

	/**
	 * Returns the difference between the contract and the actual points earned by the leading team.
	 * @return
	 */
	public double getDifferentialPoints() {
		switch(this.numberOfOudlers) {
		case 0 :
			return this.points - StandardBaseGame.ZERO_OUDLER_AIM;
		case 1 :
			return this.points - StandardBaseGame.ONE_OUDLER_AIM;
		case 2 :
			return this.points - StandardBaseGame.TWO_OUDLER_AIM;
		case 3 :
			return this.points - StandardBaseGame.THREE_OUDLER_AIM;
		default : 
			throw new IllegalStateException("incorrect number of oudlers=" + this.numberOfOudlers);
		}
	}
	
	/**
	 * Returns the defense team.
	 * @return
	 */
	public abstract PlayerList getDefenseTeam();
}