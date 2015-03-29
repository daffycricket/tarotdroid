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
package org.nla.tarotdroid.biz.computers;

import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.biz.enums.TeamType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *	Helper class to compute the scores base upon a 3 or 4 player style game.
 */
public abstract class StandardTarotGameScoresComputer extends BaseGameScoresComputer {
	
	/**
	 * The game on which to base the calculations. 
	 */
	private StandardBaseGame game;
	
	/**
	 * Multiplication rate for a Petite.
	 */
	private int petiteRate;

	/**
	 * Multiplication rate for a Prise.
	 */
	private int priseRate;
	
	/**
	 * Multiplication rate for a Garde.
	 */
	private int gardeRate;
	
	/**
	 * Multiplication rate for a Garde Sans.
	 */
	private int gardeSansRate;
	
	/**
	 * Multiplication rate for a Garde Contre.
	 */
	private int gardeContreRate;
	
	/**
	 * Base points for a Petite.
	 */
	private int petiteBasePoints;
	
	/**
	 * Base points for a Prise.
	 */
	private int priseBasePoints;
	
	/**
	 * Base points for a Garde.
	 */
	private int gardeBasePoints;
	
	/**
	 * Base points for a Garde Sans.
	 */
	private int gardeSansBasePoints;
	
	/**
	 * Base points for a Garde Contre.
	 */
	private int gardeContreBasePoints;
	
	/**
	 * Bonus points for a misery.
	 */
	private int miseryPoints;

	/**
	 * Bonus points for a poignee.
	 */
	private int poigneePoints;

	/**
	 * Bonus points for a double poignee.
	 */
	private int doublePoigneePoints;
	
	/**
	 * Bonus points for a triple poignee.
	 */
	private int triplePoigneePoints;	

	/**
	 * Bonus points for a kid at the end.
	 */
	private int kidAtTheEndPoints;
	
	/**
	 * Bonus points for an announced and succeeded chelem.
	 */
	private int announcedAndSuccededChelemPoints;

	/**
	 * Malus points for an announced and failed chelem.
	 */
	private int announcedAndFailedChelemPoints;

	/**
	 * Bonus points for a not announced but succeeded chelem.
	 */
	private int notAnnouncedButSuccededChelemPoints;
	
	/**
	 * Number of players in the game: it represents the type of game (3 or 4 player style).
	 */
	private int nbPlayers;

	/**
	 * Constructor using game set parameters.
	 * @param gameSetParameters The game set parameters.
	 */
	protected StandardTarotGameScoresComputer(final StandardBaseGame game, final GameSetParameters gameSetParameters, final int nbPlayers) {
		super();
		if (gameSetParameters == null) {
			throw new IllegalArgumentException("gameSetParameters is null");
		}
		if (game == null) {
			throw new IllegalArgumentException("game is null");
		}
		if (nbPlayers != 3 && nbPlayers != 4) {
			throw new IllegalArgumentException("nbPlayer must be equals to 3 or 4 =" + nbPlayers);
		}
		this.nbPlayers = nbPlayers;
		this.game = game;
		this.petiteRate = gameSetParameters.getPetiteRate();
		this.priseRate = gameSetParameters.getPriseRate();
		this.gardeRate = gameSetParameters.getGardeRate();
		this.gardeSansRate = gameSetParameters.getGardeSansRate();
		this.gardeContreRate = gameSetParameters.getGardeContreRate();
		this.petiteBasePoints = gameSetParameters.getPetiteBasePoints();
		this.priseBasePoints = gameSetParameters.getPriseBasePoints();
		this.gardeBasePoints = gameSetParameters.getGardeBasePoints();
		this.gardeSansBasePoints = gameSetParameters.getGardeSansBasePoints();
		this.gardeContreBasePoints = gameSetParameters.getGardeContreBasePoints();
		this.miseryPoints = gameSetParameters.getMiseryPoints();
		this.poigneePoints = gameSetParameters.getPoigneePoints();
		this.doublePoigneePoints = gameSetParameters.getDoublePoigneePoints();
		this.triplePoigneePoints = gameSetParameters.getTriplePoigneePoints();
		this.kidAtTheEndPoints = gameSetParameters.getKidAtTheEndPoints();
		this.announcedAndSuccededChelemPoints = gameSetParameters.getAnnouncedAndSucceededChelemPoints();
		this.announcedAndFailedChelemPoints = gameSetParameters.getAnnouncedAndFailedChelemPoints();
		this.notAnnouncedButSuccededChelemPoints = gameSetParameters.getNotAnnouncedButSucceededChelemPoints();
	}
	
	/**
	 * Default constructor.
	 */
	protected StandardTarotGameScoresComputer(final StandardBaseGame game, final int nbPlayers) {
		this(game, new GameSetParameters(), nbPlayers);
	}

	@Override
	public void computeScore() {
		if (this.game == null) {
			throw new IllegalArgumentException("game is null"); 
		}
		if (this.game.getBet() == null) {
			throw new IllegalArgumentException("bet is null");
		}
		
		this.computeBasePoints();
		this.computeEachIndividualResult();
		this.takeInAccountMiseries();
		this.takeInAccountPoignees();
		this.takeInAccountDoublePoignees();
		this.takeInAccountTriplePoignees();
		this.takeInAccountKidAtTheEnd();
		this.takeInAccountChelemPoints();
	}
	
	/**
	 * Computes the base points, depending on the points of the game and the bet.
	 */
	public void computeBasePoints() {
		this.basePoints = (this.game.getDifferentialPoints() >= 0 ? this.getBetPoints() : -this.getBetPoints()) + (int)(this.game.getDifferentialPoints() * this.getBetRate());
	}
	
	/**
	 * Returns the bet rate to use. 
	 */
	public int getBetRate() {
		switch(this.game.getBet().getBetType()) {
			case Petite :
				return this.petiteRate;	
			case Prise :
				return this.priseRate;	
			case Garde :
				return this.gardeRate;
			case GardeSans :
				return this.gardeSansRate;
			case GardeContre :
				return this.gardeContreRate;
			default:
			    throw new IllegalStateException("unknown betType=" + this.game.getBet().getBetType());
		} 
	}
	
	/**
	 * Returns the bet points to use. 
	 */
	public int getBetPoints() {
		switch(this.game.getBet().getBetType()) {
			case Petite :
				return this.petiteBasePoints;	
			case Prise :
				return this.priseBasePoints;	
			case Garde :
				return this.gardeBasePoints;
			case GardeSans :
				return this.gardeSansBasePoints;
			case GardeContre :
				return this.gardeContreBasePoints;
			default:
			    throw new IllegalStateException("unknown betType=" + this.game.getBet().getBetType());
		} 
	}
	
	/**
	 * Computes the score of each player individually.
	 */
	public void computeEachIndividualResult() {

		// leader teamed with another player => 
		//		- leader player gets (nbPlayers-1) times the base points 
		this.scores.addScore(this.game.getLeadingPlayer(), this.basePoints * (this.nbPlayers - 1));

		// in any case, every defense player gets the opposite of the base points 
		for (Player player : this.game.getDefenseTeam()) {
			this.scores.addScore(player, this.basePoints * -1);
		}
	}
	
	/**
	 * Alters the score of each player with misery announcements points. 
	 */
	public void takeInAccountMiseries() {
		if (this.game.getPlayersWithMisery() != null) {
			for (Player playerWithMisery : this.game.getPlayersWithMisery()) {
				this.scores.updateScore(playerWithMisery, this.miseryPoints * (this.nbPlayers - 1));
				for (Player otherPlayer : this.game.getPlayers()) {
					if (otherPlayer != playerWithMisery) {
						this.scores.updateScore(otherPlayer, -this.miseryPoints);
					}
				}
			}
		}
	}
	
	/**
	 * Alters the score of each player with poignee announcements points.
	 */
	public void takeInAccountPoignees() {
		Team teamWithPoignee = this.game.getTeamWithPoignee(); 
		if (teamWithPoignee != null) {
			int attackPoigneePoints = (this.game.isGameWon() ? this.poigneePoints : this.poigneePoints * -1);
			attackPoigneePoints = (teamWithPoignee.getTeamType() == TeamType.BothTeams ? attackPoigneePoints * 2 : attackPoigneePoints); 
			
			// if attack won: 
			//  - attack takes all attackPoigneePoints x (nbPlayers - 1)
			//  - each defense player loses attackPoigneePoints
			// if defense won: 
			//  - attack loses all attackPoigneePoints x (nbPlayers - 1)
			//  - each defense player takes attackPoigneePoints
			this.scores.updateScore(this.game.getLeadingPlayer(), attackPoigneePoints * (this.nbPlayers - 1));
			for (Player defensePlayer : this.game.getDefenseTeam()) {
				this.scores.updateScore(defensePlayer, attackPoigneePoints * -1);
			}
		}
	}
	
	/**
	 * Alters the score of each player with double poignee announcements points.
	 */
	public void takeInAccountDoublePoignees() {
		Team teamWithDoublePoignee = this.game.getTeamWithDoublePoignee();
		// make sure no both teams got a double poignee
		if (teamWithDoublePoignee == Team.BOTH_TEAMS) {
			throw new IllegalStateException("it's not possible for both teams to have a double poignee");
		}
		
		if (teamWithDoublePoignee != null) {
			int attackDoublePoigneePoints = (this.game.isGameWon() ? this.doublePoigneePoints : this.doublePoigneePoints * -1);
			
			// if attack won: 
			//  - attack takes all attackDoublePoigneePoints x (nbPlayers - 1)
			//  - each defense player loses attackDoublePoigneePoints
			// if defense won: 
			//  - attack loses all attackDoublePoigneePoints x (nbPlayers - 1)
			//  - each defense player takes attackDoublePoigneePoints
			this.scores.updateScore(this.game.getLeadingPlayer(), attackDoublePoigneePoints * (this.nbPlayers - 1));
			for (Player defensePlayer : this.game.getDefenseTeam()) {
				this.scores.updateScore(defensePlayer, attackDoublePoigneePoints * -1);
			}
		}
	}
	
	/**
	 * Alters the score of each player with triple poignee announcements points.
	 */
	public void takeInAccountTriplePoignees() {
		Team teamWithTriplePoignee = this.game.getTeamWithTriplePoignee();
		// make sure no both teams got a double poignee
		if (teamWithTriplePoignee == Team.BOTH_TEAMS) {
			throw new IllegalStateException("it's not possible for both teams to have a triple poignee");
		}
		
		if (teamWithTriplePoignee != null) {
			int attackTriplePoigneePoints = (this.game.isGameWon() ? this.triplePoigneePoints : this.triplePoigneePoints * -1);
			
			// if attack won: 
			//  - attack takes all attackTriplePoigneePoints x (nbPlayers - 1)
			//  - each defense player loses attackTriplePoigneePoints
			// if defense won: 
			//  - attack loses all attackTriplePoigneePoints x (nbPlayers - 1)
			//  - each defense player takes attackTriplePoigneePoints
			this.scores.updateScore(this.game.getLeadingPlayer(), attackTriplePoigneePoints * (this.nbPlayers - 1));
			for (Player defensePlayer : this.game.getDefenseTeam()) {
				this.scores.updateScore(defensePlayer, attackTriplePoigneePoints * -1);
			}
		}
	}
	
	/**
	 * Alters the score of each player with kid at the end points.
	 */
	public void takeInAccountKidAtTheEnd() {
		Team kidAtTheEndTeam = this.game.getTeamWithKidAtTheEnd(); 
		if (kidAtTheEndTeam != null) {
			
			// base kid at the end points
			int baseKidAtTheEndPoints = this.kidAtTheEndPoints * this.getBetRate();
			
			// attack got the kid at the end => attack takes (nbPlayers - 1) x baseKidAtTheEndPoints, each defense player takes -baseKidAtTheEndPoints
			// defense got the kid at the end => attack takes (nbPlayers - 1) x -baseKidAtTheEndPoints, each defense player takes baseKidAtTheEndPoints
			int attackKidAtTheEndPoints = ( kidAtTheEndTeam == Team.LEADING_TEAM ? baseKidAtTheEndPoints : -baseKidAtTheEndPoints );
			
			// change scores accordingly
			this.scores.updateScore(this.game.getLeadingPlayer(), attackKidAtTheEndPoints * (this.nbPlayers - 1));
			for (Player defensePlayer : this.game.getDefenseTeam()) {
				this.scores.updateScore(defensePlayer, attackKidAtTheEndPoints * -1);
			}

		}
	}
	
	/**
	 * Alters the score of each player with chelem points.
	 */
	public void takeInAccountChelemPoints() {
		Chelem chelem = this.game.getChelem();
		if (chelem != null) {
			int chelemPoints = 0;
			switch(chelem.getChelemType()) {
    			case AnnouncedAndSucceeded:
    				chelemPoints = this.announcedAndSuccededChelemPoints;
    				break;		
    			case AnnouncedAndFailed:
    				chelemPoints = this.announcedAndFailedChelemPoints;
    				break;
    			case NotAnnouncedButSucceeded:
    			default:
    				chelemPoints = this.notAnnouncedButSuccededChelemPoints;
    				break;
			}
			
			this.scores.updateScore(this.game.getLeadingPlayer(), chelemPoints * (this.nbPlayers - 1));
			for (Player otherPlayer : this.game.getPlayers()) {
				if (otherPlayer != this.game.getLeadingPlayer()) {
					this.scores.updateScore(otherPlayer, -chelemPoints);
				}
			}
		}
	}
}













