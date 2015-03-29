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

import java.io.Serializable;

import org.nla.tarotdroid.biz.enums.TeamType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class Team implements Serializable {

    /**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 5268757896361890849L;

	/** 
     * Leading team.
     */
	public static final Team LEADING_TEAM = new Team();

	/** 
	 * Defense team.
	 */
	public static final Team DEFENSE_TEAM = new Team();
	
	/** 
	 * Both teams.
	 */
	public static final Team BOTH_TEAMS = new Team();
	

	static {
	    Team.LEADING_TEAM.setTeamType(TeamType.LeadingTeam);
	    Team.DEFENSE_TEAM.setTeamType(TeamType.DefenseTeam);
	    Team.BOTH_TEAMS.setTeamType(TeamType.BothTeams);
	}

	/**
	 * @param teamTypeAsString
	 * @return
	 */
	public static Team valueOf(final String teamTypeAsString) {
		if (teamTypeAsString == null  || teamTypeAsString.equals("")) {
			return null;
		}
		TeamType teamType = TeamType.valueOf(teamTypeAsString);
		
		switch(teamType) {
    		case LeadingTeam:
    			return Team.LEADING_TEAM;
    		case DefenseTeam:
    			return Team.DEFENSE_TEAM;
    		case BothTeams:
    			return Team.BOTH_TEAMS;
    			
            default:
                return null;
		}
	}
	
	/**
	 * The team type.
	 */
	private TeamType teamType;

	/**
	 * The label.
	 */
	private String label;

	/**
	 * @return
	 */
	public TeamType getTeamType() {
		return this.teamType;
	}

	/**
	 * @param teamType
	 */
	public void setTeamType(final TeamType teamType) {
		this.teamType = teamType;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.label;
	}

}
