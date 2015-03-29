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

import static com.google.common.collect.Lists.newArrayList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.primitives.Ints;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *	A global result set depending on all game results.
 */
public class GameSetScores implements Iterable<GameScores>, Serializable {

	/**
     * Serial version ID.
     */
    private static final long serialVersionUID = -2075922227236418157L;
	
    /**
	 * The list of game scores.
	 */
	private List<GameScores> gameScores;

	/**
	 * The list of each global results at the end of each corresponding game.
	 */
	private List<MapPlayersScores> globalResults;

	/**
	 * The list of players.
	 */
	private PlayerList players;

	/**
	 *	Default constructor.
	 */
	public GameSetScores() {
		this.gameScores = new ArrayList<GameScores>();
		this.players = new PlayerList();
		this.globalResults = new ArrayList<MapPlayersScores>();
	}
	
	/**
	 * Sets the list of players.
	 * @param players the list of players to set.
	 */
	public void setPlayers(final PlayerList players) {
		this.players = players;
	}

	/**
	 * Add a new game score and computes the new associated global results.
	 * @param gameScore the game score.
	 */
	public void addGameScore(final GameScores gameScore) {
		if (gameScore == null) {
			throw new IllegalArgumentException("gameScore is null");
		}
		// update list of game scores
		this.gameScores.add(gameScore);

		// update list of results
		if (this.gameScores.size() == 1) {
		    this.globalResults.add(this.createFirstResults(gameScore));
		}
		else {
		    this.globalResults.add(this.addResults(this.getLastResults(), gameScore.getResults()));
		}
	}

	/**
	 * Removes the game score of given index.
	 * @param index
	 */
	public void removeGameScore(final int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index can't be negative=" + index);
		}
		if (index > this.gameScores.size() - 1) {
			throw new IllegalArgumentException("index can't above gameScore count=" + index);
		}

		// update list of game scores
		this.gameScores.remove(index);
		this.globalResults.remove(index);
	}

	/**
	 * Builds the first global results using the first game score.
	 * @return the first global results.
	 */
	public MapPlayersScores createFirstResults(final GameScores firstGameScore) {
		MapPlayersScores firstResult = new MapPlayersScores();
		for (Player player : this.players) {
			int score = firstGameScore.getResults().containsKey(player) ? firstGameScore.getResults().get(player) : 0;
			firstResult.put(player, score);
		}
		return firstResult;
	}

	/**
	 * Returns the last global results.
	 * @return the last global results.
	 */
	public MapPlayersScores getLastResults() {
		return this.globalResults.get(this.globalResults.size() - 1);
	}

	/**
	 * Returns a newly instantiated results corresponding to the sum of two results.
	 * @param result1 the first resuls to add.
	 * @param result2 the second resuls to add.
	 * @return a newly instantiated results corresponding to the sum of two results.
	 */
	public MapPlayersScores addResults(final MapPlayersScores result1, final MapPlayersScores result2) {
		if (result1 == null) {
			throw new IllegalArgumentException("result1 is null");
		}
		if (result2 == null) {
			throw new IllegalArgumentException("result2 is null");
		}

		MapPlayersScores addition = new MapPlayersScores();

		for (Player player : this.players) {
			int score1 = result1.containsKey(player) ? result1.get(player) : 0;
			int score2 = result2.containsKey(player) ? result2.get(player) : 0;
			addition.put(player, score1 + score2);
		}

		return addition;
	}

	/**
	 * Returns the global results for a given game.
	 * @return the global results for a given game.
	 */
	public MapPlayersScores getResultsAtGameOfIndex(final int gameIndex) {
		if (gameIndex >= this.globalResults.size()) {
			throw new IllegalArgumentException("gameIndex too high=" + gameIndex);
		}
		if (gameIndex < 0) {
			throw new IllegalArgumentException("gameIndex can't be smaller than 0=" + gameIndex);
		}

		return this.globalResults.get(gameIndex);
	}

	/**
	 * Returns the global results after the last game.
	 * @return the global results after the last game.
	 */
	public MapPlayersScores getResultsAtLastGame() {
		// no game yet, return null
		if (this.globalResults.size() == 0) {
			return null;
		}
		return this.getResultsAtGameOfIndex(this.globalResults.size() - 1);
	}

	/**
	 * Returns the individual result of a player for a given game.
	 * @return the individual result of a player for a given game.
	 */
	public int getIndividualResultsAtGameOfIndex(final int gameIndex, final Player player) {
		if (gameIndex > this.globalResults.size()) {
			throw new IllegalArgumentException("gameIndex to high=" + gameIndex);
		}
		if (gameIndex <= 0) {
			throw new IllegalArgumentException("gameIndex can't be smaller than 1 =" + gameIndex);
		}
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}

		return this.globalResults.get(gameIndex - 1).get(player);
	}

	/**
	 * Returns the rank of the player after the last game.
	 * @param player
	 * @return the rank of the player after the last game.
	 */
	public int getPlayerRankAtLastGame(final Player player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}

		// no game yet, return non significative value
		if (this.globalResults.size() == 0) {
			return 0;
		}

		return this.getResultsAtLastGame().getPlayerRank(player);
	}
	
	/**
	 * @param player
	 * @return
	 */
	public int getMinScoreEverForPlayer(final Player player) {
       if (player == null) {
            throw new IllegalArgumentException("player is null");
       }
       
       if (this.gameScores.size() == 0) {
    	   return 0;
       }
       
       List<Integer> allPlayerScoreValues = newArrayList();
       for (int i = 0; i < this.gameScores.size(); ++i) {
    	   allPlayerScoreValues.add(this.getResultsAtGameOfIndex(i).get(player));
       }
       allPlayerScoreValues.add(0);
		
       return Ints.min(Ints.toArray(allPlayerScoreValues));
	}
	
	/**
     * @param player
     * @return
     */
    public int getMaxScoreEverForPlayer(final Player player) {
    	if (player == null) {
            throw new IllegalArgumentException("player is null");
        }
    	
        if (this.gameScores.size() == 0) {
     	   return 0;
        }
		
        List<Integer> allPlayerScoreValues = newArrayList();
		for (int i = 0; i < this.gameScores.size(); ++i) {
			allPlayerScoreValues.add(this.getResultsAtGameOfIndex(i).get(player));
		}
		allPlayerScoreValues.add(0);
		
		return Ints.max(Ints.toArray(allPlayerScoreValues));
    }
    
    /**
     * Returns the maximum score of all games.
     * @return the maximum score of all games.
     */
    public int getMaxScoreEver() {
		if (this.gameScores.size() == 0) {
			return 0;
		}
 		
        List<Integer> allScoreValues = newArrayList();
 		for (int i = 0; i < this.gameScores.size(); ++i) {
 			allScoreValues.addAll(this.getResultsAtGameOfIndex(i).values());
 		}
 		allScoreValues.add(0);
    	
    	return Ints.max(Ints.toArray(allScoreValues));
    }

    /**
     * Returns the minimum score of all games.
     * @return the minimum score of all games.
     */
    public int getMinScoreEver() {
		if (this.gameScores.size() == 0) {
			return 0;
		}
 		
        List<Integer> allScoreValues = newArrayList();
 		for (int i = 0; i < this.gameScores.size(); ++i) {
 			allScoreValues.addAll(this.getResultsAtGameOfIndex(i).values());
 		}
 		allScoreValues.add(0);
    	
    	return Ints.min(Ints.toArray(allScoreValues));
    }
    
    /**
     * Returns the maximum absolute score.
     * @return the maximum absolute score.
     */
    public int getMaxAbsoluteScore() {
        return Math.max(Math.abs(this.getMinScoreEver()), Math.abs(this.getMaxScoreEver()));
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<GameScores> iterator() {
        return this.gameScores.iterator();
    }
//    
//    /**
//     * A function to transform a GameScores into a List<Integer> containing the player value (only one element per GameScores).
//     */
//	private class GameScoresToListOfPlayerValuesFunction implements Function<GameScores, List<Integer>> {
//		/**
//		 * The player.
//		 */
//		private Player player;
//		
//		/**
//		 * Constructor. 
//		 * @param player
//		 */
//    	protected GameScoresToListOfPlayerValuesFunction(final Player player) {
//        	if (player == null) {
//                throw new IllegalArgumentException("player is null");
//            }
//    		this.player = player;
//    	}
//    	
//    	/* (non-Javadoc)
//    	 * @see com.google.common.base.Function#apply(java.lang.Object)
//    	 */
//    	@Override
//    	public List<Integer> apply(final GameScores input) {
//    		int playerScore = input.getIndividualResult(this.player);
//    		
//    		if (playerScore != 0) {
//    			return newArrayList(playerScore);
//    		}
//    		else {
//    			return newArrayList();
//    		}
//    	}
//    }
}
