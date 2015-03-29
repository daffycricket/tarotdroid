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

import java.util.Map;

import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.biz.enums.ResultType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 * A class designed to compute the statistics of a Player.
 */
public interface IPlayerStatisticsComputer {

	public int getGameSetCountForPlayer();
	
	public Map<ResultType, Integer> getWonAndLostGameSetsForPlayer();
	
	public Map<GameStyleType, Integer> getGameSetCountForPlayerByGameStyleType();
	
	public Map<GameStyleType, Map<ResultType, Integer>> getWonAndLostGameSetsForPlayerByGameStyleType();

	
	public int getGameCountForPlayer();
	
	public Map<ResultType, Integer> getWonAndLostGamesForPlayer();
	
	public Map<BetType, Integer> getGameCountForPlayerByBetType();
	
	public Map<BetType, Map<ResultType, Integer>> getWonAndLostGamesForPlayerByBetType();
	
	public int getGameCountAsLeadingPlayer();
	
	public int getGameCountAsCalledPlayer();
}
