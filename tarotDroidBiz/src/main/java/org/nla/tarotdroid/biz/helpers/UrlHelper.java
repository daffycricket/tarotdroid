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
package org.nla.tarotdroid.biz.helpers;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.computers.GameSetStatisticsComputerFactory;
import org.nla.tarotdroid.biz.computers.IGameSetStatisticsComputer;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 */
public final class UrlHelper {
	
	/**
	 * Private constructor.
	 */
	private UrlHelper() {
	}
	
	/**
	 * Builds the gameSet link.
	 * @param gameSet
	 * @param baseUrl
	 * @return
	 */
	public static String buildLink(final GameSet gameSet, final String baseUrl) {
		return MessageFormat.format("{0}?gameSetId={1}", baseUrl, gameSet.getUuid());
	}
	
	/**
	 * Generate url of image on google charts.
	 * Check https://developers.google.com/chart/image/docs/chart_params to learn about the parameters 
	 * Check http://imagecharteditor.appspot.com/ to generate charts
	 * @param gameSet
	 * @return
	 */
	public static String buildPictureLink(final GameSet gameSet) {
		StringBuffer buf = new StringBuffer();
		
		Map<Integer, String> colorsForPlayerCount = newHashMap();
		colorsForPlayerCount.put(3, "3072F3,FF0000,FF9900");
		colorsForPlayerCount.put(4, "3072F3,FF0000,FF9900,15F34B");
		colorsForPlayerCount.put(5, "3072F3,FF0000,FF9900,15F34B,50AA66");
		colorsForPlayerCount.put(6, "3072F3,FF0000,FF9900,15F34B,50AA66,AA00FF");
		
		int minScore = -1 * gameSet.getGameSetScores().getMaxAbsoluteScore() - 100;
		int maxScore = gameSet.getGameSetScores().getMaxAbsoluteScore() + 100;
		int gameStyleType;
		switch(gameSet.getGameStyleType()) {
			case Tarot3:
				gameStyleType = 3;
				break;
			case Tarot4:
				gameStyleType = 4;
				break;
			case Tarot5:
				gameStyleType = 5;
				break;
			case None:
			default:
				gameStyleType = 0;
		}
		
		// this bit generates a List<String> containing each player data set (ex: -1|50,75,100,75,75)
		IGameSetStatisticsComputer gameSetStatisticsComputer = GameSetStatisticsComputerFactory.GetGameSetStatisticsComputer(gameSet, "guava");
		List<String> playerDataSets = newArrayList();
		String[] playerNames = gameSetStatisticsComputer.getPlayerNames();
		List<double[]> playerScores = gameSetStatisticsComputer.getScores();
		
		for (int playerIndex = 0; playerIndex < playerNames.length; ++playerIndex) {
			double[] nonProjectedPlayerScores = playerScores.get(playerIndex);
			List<Integer> projectedPlayerScores = newArrayList();
			for (Double nonProjectedScore : nonProjectedPlayerScores) {
				projectedPlayerScores.add(projectScoresOnChart(nonProjectedScore, minScore, maxScore));
			}

			StringBuffer playerDataSet = new StringBuffer();
			playerDataSet.append("-1|");
			playerDataSet.append(Joiner.on(",").join(projectedPlayerScores));
			playerDataSets.add(playerDataSet.toString());
		}
		
		// each parameter value generation
		String chxr = MessageFormat.format("chxr=0,0,{0}|1,{1},{2}", gameSet.getGameCount(), minScore + "", maxScore + "");
		String chco = MessageFormat.format("&chco={0}", colorsForPlayerCount.get(gameSet.getPlayers().size()));
		String chdl = MessageFormat.format("&chdl={0}", Joiner.on("|").join(playerNames));
		String chls = MessageFormat.format("&chls={0}", Strings.repeat("3", gameSet.getPlayers().size()).replace("", " ").trim().replace(" ", "|"));
		String chd = MessageFormat.format("&chd=t:{0}", Joiner.on("|").join(playerDataSets));
		String chtt = MessageFormat.format("&chtt=Tarot+a+{0}%2C+{1}+parties", gameStyleType, gameSet.getGameCount());
		
		// final url generation
		buf.append("http://chart.googleapis.com/chart?");
		buf.append(chxr); //chxr =>  x axis goes from 0 to {0} games, y axis goes from {1} to {2} points  
		buf.append("&chxs=0,676767,13,0,_,676767|1,676767,13,0,_,676767"); //chxs => x and y axis propoerties and styles 
		buf.append("&chxt=x,y"); //chs => x and y axis displayed
		buf.append("&chs=600x400"); //chs => image resolution
		buf.append("&cht=lxy"); // cht => chart type line xy
		buf.append(chco); // chco => colors of player lines
		//buf.append("&chds=-10,100,0,100,-20,100,0,100"); // chds => ? but apparently useless for lxy
		buf.append(chd); // chd => series of payer scores 
		buf.append(chdl); // chdl => legends
		buf.append("&chdlp=r"); // chdlp => legend at the right of the chart
		buf.append("&chg=0,10,5,2"); // chg => grid properties => only horizontal dashed lines
		buf.append(chls); // chls => line styles => no dash, 3 in thickness
		buf.append("&chma=5,5,5,25"); // chma => margins
		buf.append(chtt); // chtt => title, {0} is gameStyleType, {1} is game count
		
		return buf.toString();
	}
	
	/**
	 * Project the actual score onto the canvas of a chart of 100x100.
	 * @param score
	 * @param minScore
	 * @param maxScore
	 * @return
	 */
	private static int projectScoresOnChart(double score, int minScore, int maxScore) {
		double span = (double)(maxScore - minScore) / 100.0;
		double val0 = maxScore / span;
		double toReturn = Integer.MAX_VALUE;
		
		if (score >= 0) {
			toReturn = score / span + val0;
		}
		else {
			toReturn = val0 - (Math.abs(score) / span);
		}
		
		int truncated = (int)toReturn;
		
		if (toReturn > 100 || toReturn < 0) {
			throw new RuntimeException("Incorrect return value=" + toReturn);
		}

		return truncated;
	}
}
