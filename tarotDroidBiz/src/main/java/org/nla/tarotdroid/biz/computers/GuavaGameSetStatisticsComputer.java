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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Maps.newHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianBaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.KingType;
import org.nla.tarotdroid.biz.enums.ResultType;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 * A class designed to compute the statistics of a GameSet.
 */
public final class GuavaGameSetStatisticsComputer implements IGameSetStatisticsComputer {
    
    /**
     * Constant for GREEN Color. See http://developer.android.com/reference/android/graphics/Color.html#GREEN for code reference.
     */
    private static final int COLOR_GREEN = -16711936;

    /**
     * Constant for BLUE Color. See http://developer.android.com/reference/android/graphics/Color.html#BLUE for code reference.
     */
    private static final int COLOR_BLUE = -16776961;
    
    /**
     * Constant for CYAN Color. See http://developer.android.com/reference/android/graphics/Color.html#CYAN for code reference.
     */
    private static final int COLOR_CYAN = -16711681;
    
    /**
     * Constant for YELLOW Color. See http://developer.android.com/reference/android/graphics/Color.html#YELLOW for code reference.
     */
    private static final int COLOR_YELLOW = -256;
    
    /**
     * Constant for RED Color. See http://developer.android.com/reference/android/graphics/Color.html#RED for code reference.
     */
    private static final int COLOR_RED = -65536;
    
    /**
     * Constant for MAGENTA Color. See http://developer.android.com/reference/android/graphics/Color.html#MAGENTA for code reference.
     */
    private static final int COLOR_MAGENTA = -65281;
    
    /**
     * Array of display COLORS.
     */
    protected static final List<Integer> COLORS = new ArrayList<Integer>();
    static {
        GuavaGameSetStatisticsComputer.COLORS.add(GuavaGameSetStatisticsComputer.COLOR_GREEN);
        GuavaGameSetStatisticsComputer.COLORS.add(GuavaGameSetStatisticsComputer.COLOR_BLUE);
        GuavaGameSetStatisticsComputer.COLORS.add(GuavaGameSetStatisticsComputer.COLOR_CYAN);
        GuavaGameSetStatisticsComputer.COLORS.add(GuavaGameSetStatisticsComputer.COLOR_YELLOW);
        GuavaGameSetStatisticsComputer.COLORS.add(GuavaGameSetStatisticsComputer.COLOR_RED);
        GuavaGameSetStatisticsComputer.COLORS.add(GuavaGameSetStatisticsComputer.COLOR_MAGENTA);
    }
    
    /**
     * The game set on which to compute the statistics.
     */
    private GameSet gameSet;
    
    /**
     * Number of necessary leading player series. 
     */
    private int leadingPlayerSeriesCount;

    /**
     * Number of necessary called player series. 
     */
    private int calledPlayerSeriesCount;
    
    /**
     * Number of necessary bet series. 
     */
    private int betSeriesCount;

    /**
     * Number of necessary full bet series. 
     */
    private int fullBetSeriesCount;
    
    /**
     * Number of necessary king series. 
     */
    private int kingSeriesCount;
        
    /**
     * Constructor using a GameSet.
     * @param gameSet
     */
    protected GuavaGameSetStatisticsComputer(final GameSet gameSet) {
        if (gameSet == null) {
            throw new IllegalArgumentException("gameSet is null");
        }
        this.gameSet = gameSet;
        this.leadingPlayerSeriesCount = Integer.MIN_VALUE;
        this.calledPlayerSeriesCount = Integer.MIN_VALUE;
        this.betSeriesCount = Integer.MIN_VALUE;
        this.fullBetSeriesCount = Integer.MIN_VALUE;
        this.kingSeriesCount = Integer.MIN_VALUE;
    }
    
    /**
     * Returns an array corresponding to successes and failures COLORS.
     * @return an array corresponding to successes and failures COLORS.
     */
    public int[] getResultsColors() {
        return new int[] { GuavaGameSetStatisticsComputer.COLOR_RED, GuavaGameSetStatisticsComputer.COLOR_GREEN };
    }
    
    /**
     * Returns the count of successes and failures.   
     * @return the count of successes and failures as a Map<GameResultTypes, Integer>.
     */
    public Map<ResultType, Integer> getResultsCount() {
        // won predicate
        Predicate<BaseGame> successPredicate = new Predicate<BaseGame> () {
			@Override
			public boolean apply(final BaseGame game) {
				return game != null && game instanceof StandardBaseGame && ((StandardBaseGame)game).isGameWon();
			}
		};
		
		// lost predicate
        Predicate<BaseGame> failurePredicate = new Predicate<BaseGame> () {
			@Override
			public boolean apply(final BaseGame game) {
				return game != null && game instanceof StandardBaseGame && !((StandardBaseGame)game).isGameWon();
			}
		};
        
		// computing info about prise games
		int successCount = Collections2.filter(this.gameSet.getStandardGames(), successPredicate).size();
		int failureCount = Collections2.filter(this.gameSet.getStandardGames(), failurePredicate).size();
        
		Map<ResultType, Integer> toReturn = newHashMap();
        toReturn.put(ResultType.Success, successCount);
        toReturn.put(ResultType.Failure, failureCount);
        
        return toReturn;
    }
    
    /**
     * Returns for each player the number of games he has been a leading player.   
     * @return for each player the number of games he has been a leading player as a Map<Player, Integer>.
     */
    public Map<Player, Integer> getLeadingCount() {
    	// transform StandardBaseGame into Player
    	Function<StandardBaseGame, Player> gameSetToLeadingPlayerFunction = new Function<StandardBaseGame, Player>() {
			
			@Override
			public Player apply(final StandardBaseGame stdBaseGame) {
				return stdBaseGame.getLeadingPlayer();
			}
		};
		
		// group standard games by player 
		ImmutableListMultimap<Player, StandardBaseGame> leadingPlayerMultimap = Multimaps.index(this.gameSet.getStandardGames(), gameSetToLeadingPlayerFunction);
		
		// build return object
		Map<Player, Integer> toReturn = newHashMap();
		for (Player player : leadingPlayerMultimap.keys()) {
			toReturn.put(player, leadingPlayerMultimap.get(player).size());
		}
		this.leadingPlayerSeriesCount = toReturn.keySet().size();
        
        return toReturn;
    }
    
    /**
     * Returns an array corresponding to leading user COLORS.
     * @return an array corresponding to leading user COLORS.
     */
    public int[] getLeadingCountColors() {
        if (this.leadingPlayerSeriesCount == Integer.MIN_VALUE) {
            throw new IllegalStateException("you must call getLeadingCount() first");
        }
        return GuavaGameSetStatisticsComputer.convert(GuavaGameSetStatisticsComputer.COLORS.subList(0, this.leadingPlayerSeriesCount));
    }
    
    /**
     * Returns for each player the number of games he won as a leading player.   
     * @return for each player the number of games he won as a leading player as a Map<Player, Integer>.
     */
    public Map<Player, Integer> getLeadingSuccessCount() {
		Map<Player, Integer> toReturn = newHashMap();
		for (Player player : this.gameSet.getPlayers()) {
			toReturn.put(player, Collections2.filter(this.gameSet.getStandardGames(), new LeaderPlayerWinsPredicate(player)).size());
		}
        return toReturn;
    }
    
    /**
     * Returns for each player the number of games he has been called.   
     * @return for each player the number of games he has been called as a Map<Player, Integer>.
     */
    public Map<Player, Integer> getCalledCount() {
    	// transform StandardBaseGame into Player
    	Function<StandardTarot5Game, Player> gameSetToCalledPlayerFunction = new Function<StandardTarot5Game, Player>() {
			
			@Override
			public Player apply(final StandardTarot5Game stdBaseGame) {
				return stdBaseGame.getCalledPlayer();
			}
		};
		
		// group standard games by player 
		ImmutableListMultimap<Player, StandardTarot5Game> calledPlayerMultimap = Multimaps.index(this.gameSet.getStandard5Games(), gameSetToCalledPlayerFunction);
		
		// build return object
		Map<Player, Integer> toReturn = newHashMap();
		for (Player player : calledPlayerMultimap.keys()) {
			toReturn.put(player, calledPlayerMultimap.get(player).size());
		}
		this.calledPlayerSeriesCount = toReturn.keySet().size();
        
        return toReturn;
    }
    
    /**
     * Returns an array corresponding to called players.
     * @return an array corresponding to called players.
     */
    public int[] getCalledCountColors() {
        if (this.calledPlayerSeriesCount == Integer.MIN_VALUE) {
            throw new IllegalStateException("you must call getCalledCount() first");
        }
        return GuavaGameSetStatisticsComputer.convert(GuavaGameSetStatisticsComputer.COLORS.subList(0, this.calledPlayerSeriesCount));
    }
    
    /**
     * Returns for each bet the number of games this bet has been called.   
     * @return for each bet the number of games this bet has been called as a Map<BetType, Double>.
     */
    public Map<BetType, Integer> getBetCount() {
    	
    	// transform StandardBaseGame into BetType
    	Function<StandardBaseGame, BetType> gameToBetTypeFunction = new Function<StandardBaseGame, BetType>() {
			
			@Override
			public BetType apply(final StandardBaseGame stdBaseGame) {
				return stdBaseGame.getBet().getBetType();
			}
		};
		
		// group standard games by bet type 
		ImmutableListMultimap<BetType, StandardBaseGame> betTypeMultimap = Multimaps.index(this.gameSet.getStandardGames(), gameToBetTypeFunction);
		
		// build return object
		Map<BetType, Integer> toReturn = newHashMap();
		for (BetType betType : betTypeMultimap.keys()) {
			toReturn.put(betType, betTypeMultimap.get(betType).size());
		}
		this.betSeriesCount = toReturn.keySet().size();
        
        return toReturn;
    }
    
    /**
     * Returns an array corresponding to called bet COLORS.
     * @return an array corresponding to called bet COLORS.
     */
    public int[] getBetCountColors() {
        if (this.betSeriesCount == Integer.MIN_VALUE) {
            throw new IllegalStateException("you must call getBetCount() first");
        }
        return GuavaGameSetStatisticsComputer.convert(GuavaGameSetStatisticsComputer.COLORS.subList(0, this.betSeriesCount));
    }
    
    /**
     * TODO IMPROVE   
     * @return 
     */
    public Map<String, Integer> getFullBetCount() {
    	
    	// transform BaseGame into string description
    	Function<BaseGame, String> gameToStringFunction = new Function<BaseGame, String>() {
			
			@Override
			public String apply(final BaseGame baseGame) {
				if (baseGame instanceof StandardBaseGame) {
					return "Standard";
				}
				else if (baseGame instanceof BelgianBaseGame) {
					return "Belge";
				}
				else if (baseGame instanceof PassedGame) {
					return "Passe";
				}
				else if (baseGame instanceof PenaltyGame) {
					return "Fausse donne";
				}
				else {
					throw new IllegalArgumentException("GetFullBetCount() Unknow game type");
				}
				
				
			}
		};
		
		// group games by string description 
		ImmutableListMultimap<String, BaseGame> descriptionMultimap = Multimaps.index(this.gameSet.getGames(), gameToStringFunction);
		
		// build return object
		Map<String, Integer> toReturn = newHashMap();
		for (String description : descriptionMultimap.keys()) {
			toReturn.put(description, descriptionMultimap.get(description).size());
		}
		this.fullBetSeriesCount = toReturn.keySet().size();
        
        return toReturn;
    }
    
    /**
     * Returns an array corresponding to called bet COLORS.
     * @return an array corresponding to called bet COLORS.
     */
    public int[] getFullBetCountColors() {
    	checkArgument(this.fullBetSeriesCount != Integer.MIN_VALUE, "you must call getFullBetCount() first");
        return GuavaGameSetStatisticsComputer.convert(GuavaGameSetStatisticsComputer.COLORS.subList(0, this.fullBetSeriesCount));
    }
    
    /**
     * Returns for each bet the number of games this king has been called.   
     * @return for each bet the number of games this king has been called as a Map<KingType, Double>.
     */
    public Map<KingType, Integer> getKingCount() {
    	
    	// transform StandardTarot5Game into BetType
    	Function<StandardTarot5Game, KingType> gameToBetTypeFunction = new Function<StandardTarot5Game, KingType>() {
			
			@Override
			public KingType apply(final StandardTarot5Game stdBaseGame) {
				return stdBaseGame.getCalledKing().getKingType();
			}
		};
		
		// group standard games by bet type 
		ImmutableListMultimap<KingType, StandardTarot5Game> kingTypeMultimap = Multimaps.index(this.gameSet.getStandard5Games(), gameToBetTypeFunction);
		
		// build return object
		Map<KingType, Integer> toReturn = newHashMap();
		for (KingType kingType : kingTypeMultimap.keys()) {
			toReturn.put(kingType, kingTypeMultimap.get(kingType).size());
		}
		this.kingSeriesCount = toReturn.keySet().size();
		
        return toReturn;
    }
    
    /**
     * Returns an array corresponding to called king COLORS.
     * @return an array corresponding to called king COLORS.
     */
    public int[] getKingCountColors() {
        if (this.kingSeriesCount == Integer.MIN_VALUE) {
            throw new IllegalStateException("you must call getKingCount() first");
        }
        return GuavaGameSetStatisticsComputer.convert(GuavaGameSetStatisticsComputer.COLORS.subList(0, this.kingSeriesCount));
    }
    
    /**
     * Returns an array containing all the scores per game per player. 
     * @return an array containing all the scores per game per player as a List<double[]>. 
     */
    public List<double[]> getScores() {
        List<double[]> toReturn = new ArrayList<double[]>();
        
        // creates an array of all game scores for each player 
        for (Player player : this.gameSet.getPlayers()) {
            double[] playerScores = new double[this.gameSet.getGameCount() + 1];
            playerScores[0] = 0;
            for (int gameIndex = 0; gameIndex < this.gameSet.getGameCount(); ++gameIndex) {
                int score = this.gameSet.getGameSetScores().getIndividualResultsAtGameOfIndex(gameIndex + 1, player);
                playerScores[gameIndex + 1] = score;
            }
            toReturn.add(playerScores);
        }

        return toReturn;
    }
    
    /**
     * Returns the maximum score ever.
     * @return the maximum score ever.
     */
    public int getMaxScoreEver() {
        return this.gameSet.getGameSetScores().getMaxScoreEver();
    }
    
    /**
     * Returns the minimum score ever.
     * @return the minimum score ever.
     */
    public int getMinScoreEver() {
        return this.gameSet.getGameSetScores().getMinScoreEver();
    }
    
    /**
     * Returns the minimum score ever for the given player..
     * @return the minimum score ever for the given player.
     */
    public int getMinScoreEverForPlayer(final Player player) {
        return this.gameSet.getGameSetScores().getMinScoreEverForPlayer(player);
    }
    
    /**
     * Returns the maximum score ever for the given player.
     * @return the maximum score ever for the given player.
     */
    public int getMaxScoreEverForPlayer(final Player player) {
        return this.gameSet.getGameSetScores().getMaxScoreEverForPlayer(player);
    }
    
    /**
     * Returns the maximum absolute score.
     * @return the maximum absolute score.
     */
    public int getMaxAbsoluteScore() {
        return this.gameSet.getGameSetScores().getMaxAbsoluteScore();
    }
    
    /**
     * Returns the player count.
     * @return the player count.
     */
    public int getPlayerCount() {
        return this.gameSet.getPlayers().size();
    }
    
    /**
     * Returns the game count.
     * @return the game count.
     */
    public int getGameCount() {
        return this.gameSet.getGameCount();
    }
    
    /**
     * Returns the player names as a String[].
     * @return the player names as a String[].
     */
    public String[] getPlayerNames() {
        return this.gameSet.getPlayers().getPlayerNames();
    }
    
    /**
     * Returns an array corresponding to the scores COLORS.
     * @return an array corresponding to the scores COLORS. 
     */
    public int[] getScoresColors() {
        ArrayList<Integer> lstColors = new ArrayList<Integer>();
        
        
        switch(this.gameSet.getGameStyleType()) {
            case Tarot3:
                if (this.gameSet.getPlayers().size() == 3) {
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_GREEN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_BLUE);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_RED);
                }
                else {
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_GREEN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_BLUE);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_YELLOW);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_RED);                   
                }
                break;
            case Tarot4:
                if (this.gameSet.getPlayers().size() == 4) {
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_GREEN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_BLUE);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_YELLOW);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_RED);                   
                }
                else {
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_GREEN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_BLUE);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_CYAN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_YELLOW);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_RED);                   
                }
                break;
            case Tarot5:
                if (this.gameSet.getPlayers().size() == 5) {
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_GREEN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_BLUE);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_CYAN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_YELLOW);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_RED);               
                }
                else {
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_GREEN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_BLUE);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_CYAN);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_YELLOW);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_RED);
                    lstColors.add(GuavaGameSetStatisticsComputer.COLOR_MAGENTA);
                }
                break;
            case None:
            default:
                throw new IllegalArgumentException("Incorrect game style type:" + this.gameSet.getGameStyleType());
        }
        return GuavaGameSetStatisticsComputer.convert(lstColors);
    }
    
    /**
     * Method to convert a List<Integer> into a int[]. Ugly code but there's no way to do it natively with the sdk...
     * @param toConvert
     * @return
     */
    private static int[] convert(final List<Integer> toConvert) {
        int[] toReturn = new int[toConvert.size()];
        for (int i = 0; i < toConvert.size(); ++i) {
            toReturn[i] = toConvert.get(i);
        }
        return toReturn;
    }
    
	/**
	 * A Predicate to find all games where player is leader.
	 * @author Nico
	 *
	 */
	protected class LeaderPlayerWinsPredicate implements Predicate<StandardBaseGame> {
		
		/**
		 * The player to check.
		 */
		private Player player;
				

		/**
		 * @param player
		 */
		protected LeaderPlayerWinsPredicate(final Player player) {
			this.player = player;
		}
		
		/* (non-Javadoc)
		 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(final StandardBaseGame game) {
			if (game == null) {
				return false;
			}
			return game.getLeadingPlayer() == this.player && game.isWinner(this.player);
		}
	}
}
