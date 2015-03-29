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

import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.biz.enums.ResultType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 * A class designed to compute the statistics of a GameSet.
 */
public interface IGameSetStatisticsComputer {
    
    /**
     * Returns an array corresponding to successes and failures COLORS.
     * @return an array corresponding to successes and failures COLORS.
     */
    public int[] getResultsColors();
    
    /**
     * Returns the count of successes and failures.   
     * @return the count of successes and failures as a Map<GameResultTypes, Integer>.
     */
    public Map<ResultType, Integer> getResultsCount();    
    /**
     * Returns for each player the number of games he has been a leading player.   
     * @return for each player the number of games he has been a leading player as a Map<Player, Integer>.
     */
    public Map<Player, Integer> getLeadingCount();    
    /**
     * Returns an array corresponding to leading user COLORS.
     * @return an array corresponding to leading user COLORS.
     */
    public int[] getLeadingCountColors();
    
    /**
     * Returns for each player the number of games he won as a leading player.   
     * @return for each player the number of games he won as a leading player as a Map<Player, Integer>.
     */
    public Map<Player, Integer> getLeadingSuccessCount();
    
    /**
     * Returns for each player the number of games he has been called.   
     * @return for each player the number of games he has been called as a Map<Player, Integer>.
     */
    public Map<Player, Integer> getCalledCount();
    
    /**
     * Returns an array corresponding to called players.
     * @return an array corresponding to called players.
     */
    public int[] getCalledCountColors();
    
    /**
     * Returns for each bet the number of games this bet has been called.   
     * @return for each bet the number of games this bet has been called as a Map<BetType, Double>.
     */
    public Map<BetType, Integer> getBetCount();
    
    /**
     * Returns an array corresponding to called bet COLORS.
     * @return an array corresponding to called bet COLORS.
     */
    public int[] getBetCountColors();
    
    /**
     *    
     * @return 
     */
    public Map<String, Integer> getFullBetCount();
    
    /**
     * 
     * @return 
     */
    public int[] getFullBetCountColors();
    
    /**
     * Returns for each bet the number of games this king has been called.   
     * @return for each bet the number of games this king has been called as a Map<KingType, Double>.
     */
    public Map<KingType, Integer> getKingCount();
    
    /**
     * Returns an array corresponding to called king COLORS.
     * @return an array corresponding to called king COLORS.
     */
    public int[] getKingCountColors();
    
    /**
     * Returns an array containing all the scores per game per player. 
     * @return an array containing all the scores per game per player as a List<double[]>. 
     */
    public List<double[]> getScores();
    
    /**
     * Returns the maximum score ever.
     * @return the maximum score ever.
     */
    public int getMaxScoreEver();
    
    /**
     * Returns the minimum score ever.
     * @return the minimum score ever.
     */
    public int getMinScoreEver();
    
    /**
     * Returns the minimum score ever for the given player..
     * @return the minimum score ever for the given player.
     */
    public int getMinScoreEverForPlayer(final Player player);
    
    /**
     * Returns the maximum score ever for the given player.
     * @return the maximum score ever for the given player.
     */
    public int getMaxScoreEverForPlayer(final Player player);
    
    /**
     * Returns the maximum absolute score.
     * @return the maximum absolute score.
     */
    public int getMaxAbsoluteScore();
    
    /**
     * Returns the player count.
     * @return the player count.
     */
    public int getPlayerCount();
    
    /**
     * Returns the game count.
     * @return the game count.
     */
    public int getGameCount();
    
    /**
     * Returns the player names as a String[].
     * @return the player names as a String[].
     */
    public String[] getPlayerNames();
    
    /**
     * Returns an array corresponding to the scores COLORS.
     * @return an array corresponding to the scores COLORS. 
     */
    public int[] getScoresColors();
}
