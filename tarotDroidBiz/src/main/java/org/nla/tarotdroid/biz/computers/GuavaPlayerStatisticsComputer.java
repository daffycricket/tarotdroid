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

import static com.google.common.collect.Maps.newHashMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.enums.BetType;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.biz.enums.ResultType;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
//import com.google.common.collect.Iterables;
//
//import static com.google.common.collect.Lists.newArrayList;  
//import static com.google.common.base.Predicates.and;
//import static com.google.common.base.Predicates.or;
//import static com.google.common.base.Predicates.in;
//import static com.google.common.base.Predicates.not;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 * A class designed to compute the statistics of a player throughout a list of GameSet using the Guava library.
 * Check here for more info http://thierry-leriche-dessirier.developpez.com/tutoriels/java/tuto-google-collections.
 */
public final class GuavaPlayerStatisticsComputer implements IPlayerStatisticsComputer {

	/**
	 * 3 Player style game set predicate. 
	 */
	private static final Predicate<GameSet> tarot3GameSetPredicate = new Predicate<GameSet>() {
		
		@Override
		public boolean apply(final GameSet gameSet) {
			return gameSet != null && gameSet.getGameStyleType() == GameStyleType.Tarot3;
		}
	};
	
	/**
	 * 4 Player style game set predicate. 
	 */
	private static final Predicate<GameSet> tarot4GameSetPredicate = new Predicate<GameSet>() {
		
		@Override
		public boolean apply(final GameSet gameSet) {
			return gameSet != null && gameSet.getGameStyleType() == GameStyleType.Tarot4;
		}
	};
	
	/**
	 * 5 Player style game set predicate. 
	 */
	private static final Predicate<GameSet> tarot5GameSetPredicate = new Predicate<GameSet>() {
		
		@Override
		public boolean apply(final GameSet gameSet) {
			return gameSet != null && gameSet.getGameStyleType() == GameStyleType.Tarot5;
		}
	};
	
	/**
	 * Prise bet type predicate. 
	 */
	private static final Predicate<BaseGame> priseGamePredicate = new Predicate<BaseGame>() {
		
		@Override
		public boolean apply(final BaseGame game) {
			return game != null && game instanceof StandardBaseGame && ((StandardBaseGame)game).getBet().getBetType() == BetType.Prise;
		}
	};
	
	/**
	 * Garde bet type predicate. 
	 */
	private static final Predicate<BaseGame> gardeGamePredicate = new Predicate<BaseGame>() {
		
		@Override
		public boolean apply(final BaseGame game) {
			return game != null && game instanceof StandardBaseGame && ((StandardBaseGame)game).getBet().getBetType() == BetType.Garde;
		}
	};
	
	/**
	 * GardeSans bet type predicate. 
	 */
	private static final Predicate<BaseGame> gardeSansGamePredicate = new Predicate<BaseGame>() {
		
		@Override
		public boolean apply(final BaseGame game) {
			return game != null && game instanceof StandardBaseGame && ((StandardBaseGame)game).getBet().getBetType() == BetType.GardeSans;
		}
	};
	
	/**
	 * GardeContre bet type predicate. 
	 */
	private static final Predicate<BaseGame> gardeContreGamePredicate = new Predicate<BaseGame>() {
		
		@Override
		public boolean apply(final BaseGame game) {
			return game != null && game instanceof StandardBaseGame && ((StandardBaseGame)game).getBet().getBetType() == BetType.GardeContre;
		}
	};
	
    /**
     * The list of game sets on which to compute the statistics.
     */
    private List<GameSet> gameSets;
    
    /**
     * A sub collection of the game sets in which the player played. 
     */
    private Collection<GameSet> gameSetsWithPlayer;
    
    /**
     * A sub collection of the games in which the player played. 
     */    
    private List<BaseGame> gamesWithPlayer;
    
    /**
     * The player for whom we want to generate the statistics. 
     */
    private Player player;
        
    /**
     * Constructor using a List<GameSet> and a Player.
     * @param gameSets the lis of game sets.
     * @param player the player.
     */
    protected GuavaPlayerStatisticsComputer(final List<GameSet> gameSets, final Player player) {
        if (gameSets == null) {
            throw new IllegalArgumentException("gameSets is null");
        }
        if (player == null) {
            throw new IllegalArgumentException("player is null");
        }
        
        this.gameSets = gameSets;
        this.player = player;
        
        // compute list of game sets with player
		this.gameSetsWithPlayer = Collections2.filter(
				this.gameSets,
				new PlayerInGameSetPredicate()
		);

		// compute list of games with player
		this.gamesWithPlayer = Lists.newArrayList(
				Iterables.concat(
						Iterables.transform(
								this.gameSetsWithPlayer, 
								new GameSetToListOfBaseGamesFunction()
						)
				)
		);
		
    }
	
	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.computers.IPlayerStatisticsComputer#getGameSetCountForPlayer()
	 */
	@Override
	public int getGameSetCountForPlayer() {
		return gameSetsWithPlayer.size();
	}

	/* (non-Javadoc)
	 * @see org.nla.tarotdroid.biz.computers.IPlayerStatisticsComputer#getWonAndLostGameSetsForPlayer()
	 */
	@Override
	public Map<ResultType, Integer> getWonAndLostGameSetsForPlayer() {
		int successes = Collections2.filter(
				this.gameSetsWithPlayer,
				new WonOrLostGameSetPredicate(true)
		).size();

		int failures = Collections2.filter(
				this.gameSetsWithPlayer,
				new WonOrLostGameSetPredicate(false)
		).size();
		
		Map<ResultType, Integer> toReturn = new HashMap<ResultType, Integer>(); 
		toReturn.put(ResultType.Success, successes);
		toReturn.put(ResultType.Failure, failures);
		
		return toReturn;
	}

	@Override
	public Map<GameStyleType, Integer> getGameSetCountForPlayerByGameStyleType() {
		Collection<GameSet> tarot3GameSets = Collections2.filter(this.gameSetsWithPlayer, tarot3GameSetPredicate);
		Collection<GameSet> tarot4GameSets = Collections2.filter(this.gameSetsWithPlayer, tarot4GameSetPredicate);
		Collection<GameSet> tarot5GameSets = Collections2.filter(this.gameSetsWithPlayer, tarot5GameSetPredicate);

		Map<GameStyleType, Integer> toReturn = new HashMap<GameStyleType, Integer>();
		toReturn.put(GameStyleType.Tarot3, tarot3GameSets.size());
		toReturn.put(GameStyleType.Tarot4, tarot4GameSets.size());
		toReturn.put(GameStyleType.Tarot5, tarot5GameSets.size());
		
		return toReturn;
	}

	@Override
	public Map<GameStyleType, Map<ResultType, Integer>> getWonAndLostGameSetsForPlayerByGameStyleType() {
		// computing info about tarot 3 game style type
		int tarot3Successes = Collections2.filter(
				Collections2.filter(this.gameSetsWithPlayer, tarot3GameSetPredicate),
				new WonOrLostGameSetPredicate(true)
		).size();

		int tarot3Failures = Collections2.filter(
				Collections2.filter(this.gameSetsWithPlayer, tarot3GameSetPredicate),
				new WonOrLostGameSetPredicate(false)
		).size();
		
		Map<ResultType, Integer> tarot3ReturnMap = new HashMap<ResultType, Integer>();
		tarot3ReturnMap.put(ResultType.Success, tarot3Successes);
		tarot3ReturnMap.put(ResultType.Failure, tarot3Failures);
		
		// computing info about tarot 4 game style type
		int tarot4Successes = Collections2.filter(
				Collections2.filter(this.gameSetsWithPlayer, tarot4GameSetPredicate),
				new WonOrLostGameSetPredicate(true)
		).size();

		int tarot4Failures = Collections2.filter(
				Collections2.filter(this.gameSetsWithPlayer, tarot4GameSetPredicate),
				new WonOrLostGameSetPredicate(false)
		).size();
		
		Map<ResultType, Integer> tarot4ReturnMap = new HashMap<ResultType, Integer>();
		tarot4ReturnMap.put(ResultType.Success, tarot4Successes);
		tarot4ReturnMap.put(ResultType.Failure, tarot4Failures);

		// computing info about tarot 5 game style type
		int tarot5Successes = Collections2.filter(
				Collections2.filter(this.gameSetsWithPlayer, tarot5GameSetPredicate),
				new WonOrLostGameSetPredicate(true)
		).size();

		int tarot5Failures = Collections2.filter(
				Collections2.filter(this.gameSetsWithPlayer, tarot5GameSetPredicate),
				new WonOrLostGameSetPredicate(false)
		).size();
		
		Map<ResultType, Integer> tarot5ReturnMap = new HashMap<ResultType, Integer>();
		tarot5ReturnMap.put(ResultType.Success, tarot5Successes);
		tarot5ReturnMap.put(ResultType.Failure, tarot5Failures);
		
		Map<GameStyleType, Map<ResultType, Integer>> toReturn = new HashMap<GameStyleType, Map<ResultType, Integer>>();
		toReturn.put(GameStyleType.Tarot3, tarot3ReturnMap);
		toReturn.put(GameStyleType.Tarot4, tarot4ReturnMap);
		toReturn.put(GameStyleType.Tarot5, tarot5ReturnMap);
		
		return toReturn;
	}

	@Override
	public int getGameCountForPlayer() {
		return this.gamesWithPlayer.size();
	}

	@Override
	public Map<ResultType, Integer> getWonAndLostGamesForPlayer() {
		int successes = Collections2.filter(
				this.gamesWithPlayer,
				new WonOrLostGamePredicate(true)
		).size();

		int failures = Collections2.filter(
				this.gamesWithPlayer,
				new WonOrLostGamePredicate(false)
		).size();
		
		Map<ResultType, Integer> toReturn = new HashMap<ResultType, Integer>(); 
		toReturn.put(ResultType.Success, successes);
		toReturn.put(ResultType.Failure, failures);
		
		return toReturn;
	}

	@Override
	public Map<BetType, Integer> getGameCountForPlayerByBetType() {

		Collection<BaseGame> priseGames = Collections2.filter(this.gamesWithPlayer, priseGamePredicate);
		Collection<BaseGame> gardeGames = Collections2.filter(this.gamesWithPlayer, gardeGamePredicate);
		Collection<BaseGame> gardeSansGameSets = Collections2.filter(this.gamesWithPlayer, gardeSansGamePredicate);
		Collection<BaseGame> gardeContreGameSets = Collections2.filter(this.gamesWithPlayer, gardeContreGamePredicate);

		Map<BetType, Integer> toReturn = newHashMap();
		toReturn.put(BetType.Prise, priseGames.size());
		toReturn.put(BetType.Garde, gardeGames.size());
		toReturn.put(BetType.GardeSans, gardeSansGameSets.size());
		toReturn.put(BetType.GardeContre, gardeContreGameSets.size());
		
		return toReturn;
	}

	@Override
	public Map<BetType, Map<ResultType, Integer>> getWonAndLostGamesForPlayerByBetType() {
		
		// computing info about prise games
		int priseSuccesses = Collections2.filter(
				Collections2.filter(this.gamesWithPlayer, priseGamePredicate),
				new WonOrLostGamePredicate(true)
		).size();

		int priseFailures = Collections2.filter(
				Collections2.filter(this.gamesWithPlayer, priseGamePredicate),
				new WonOrLostGamePredicate(false)
		).size();
		
		Map<ResultType, Integer> priseReturnMap = newHashMap();
		priseReturnMap.put(ResultType.Success, priseSuccesses);
		priseReturnMap.put(ResultType.Failure, priseFailures);
		
		// computing info about garde games
		int gardeSuccesses = Collections2.filter(
				Collections2.filter(this.gamesWithPlayer, gardeGamePredicate),
				new WonOrLostGamePredicate(true)
		).size();

		int gardeFailures = Collections2.filter(
				Collections2.filter(this.gamesWithPlayer, gardeGamePredicate),
				new WonOrLostGamePredicate(false)
		).size();
		
		Map<ResultType, Integer> gardeReturnMap = newHashMap();
		gardeReturnMap.put(ResultType.Success, gardeSuccesses);
		gardeReturnMap.put(ResultType.Failure, gardeFailures);

		// computing info about garde sans games
		int gardeSansSuccesses = Collections2.filter(
				Collections2.filter(this.gamesWithPlayer, gardeSansGamePredicate),
				new WonOrLostGamePredicate(true)
		).size();

		int gardeSansFailures = Collections2.filter(
				Collections2.filter(this.gamesWithPlayer, gardeSansGamePredicate),
				new WonOrLostGamePredicate(false)
		).size();
		
		Map<ResultType, Integer> gardeSansReturnMap = newHashMap();
		gardeSansReturnMap.put(ResultType.Success, gardeSansSuccesses);
		gardeSansReturnMap.put(ResultType.Failure, gardeSansFailures);
		
		// computing info about garde contre games
		int gardeContreSuccesses = Collections2.filter(
				Collections2.filter(this.gamesWithPlayer, gardeContreGamePredicate),
				new WonOrLostGamePredicate(true)
		).size();

		int gardeContreFailures = Collections2.filter(
				Collections2.filter(this.gamesWithPlayer, gardeContreGamePredicate),
				new WonOrLostGamePredicate(false)
		).size();
		
		Map<ResultType, Integer> gardeContreReturnMap = newHashMap();
		gardeContreReturnMap.put(ResultType.Success, gardeContreSuccesses);
		gardeContreReturnMap.put(ResultType.Failure, gardeContreFailures);
		
		Map<BetType, Map<ResultType, Integer>> toReturn = newHashMap();
		toReturn.put(BetType.Prise, priseReturnMap);
		toReturn.put(BetType.Garde, gardeReturnMap);
		toReturn.put(BetType.GardeSans, gardeSansReturnMap);
		toReturn.put(BetType.GardeContre, gardeContreReturnMap);

		return toReturn;
	}
	
	
	
	@Override
	public int getGameCountAsLeadingPlayer() {
		return Collections2.filter(
				this.gamesWithPlayer, 
				new Predicate<BaseGame> (){

					@Override
					public boolean apply(final BaseGame game) {
						return game != null && game instanceof StandardBaseGame && ((StandardBaseGame)game).getLeadingPlayer() == GuavaPlayerStatisticsComputer.this.player;
					}}
		).size();
	}

	@Override
	public int getGameCountAsCalledPlayer() {
		return Collections2.filter(
				this.gamesWithPlayer, 
				new Predicate<BaseGame> (){

					@Override
					public boolean apply(final BaseGame game) {
						return game != null && game instanceof StandardTarot5Game && ((StandardTarot5Game)game).getCalledPlayer() == GuavaPlayerStatisticsComputer.this.player;
					}}
		).size();
	}

	/**
	 * A function to transform a GameSet to a List<BaseGame>.
	 */
	protected class GameSetToListOfBaseGamesFunction implements Function<GameSet, List<BaseGame>> {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public List<BaseGame> apply(final GameSet input) { 
	        return input.getGames();
	    }
	};
	
	/**
	 * A Predicate to find all GameSet in which a player played.
	 */
	protected class PlayerInGameSetPredicate implements Predicate<GameSet> {
		
		/* (non-Javadoc)
		 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(final GameSet gameSet) {
			return gameSet != null && gameSet.getPlayers().contains(GuavaPlayerStatisticsComputer.this.player);
		}
	}
	
	/**
	 * A Predicate to find all GameSet won or lost by a player.
	 * @author Nico
	 *
	 */
	protected class WonOrLostGameSetPredicate implements Predicate<GameSet> {
		
		/**
		 * Tells whether to check for victory or loss.
		 */
		private boolean checkVictory;
				
		/**
		 * Constructor.
		 * @param checkVictory true if the gameset must be checked against victory, false if the gameset must be checked against loss.
		 */
		protected WonOrLostGameSetPredicate(final boolean checkVictory) {
			this.checkVictory = checkVictory;
		}
		
		/* (non-Javadoc)
		 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(final GameSet gameSet) {
			if (gameSet == null) {
				return false;
			}
			
			if (this.checkVictory) {
				return gameSet.isWinner(GuavaPlayerStatisticsComputer.this.player);
			}
			else {
				return !gameSet.isWinner(GuavaPlayerStatisticsComputer.this.player);
			}
		}
	}
	
	/**
	 * A Predicate to find all games won or lost by a player.
	 * @author Nico
	 *
	 */
	protected class WonOrLostGamePredicate implements Predicate<BaseGame> {
		
		/**
		 * Tells whether to check for victory or loss.
		 */
		private boolean checkVictory;
				
		/**
		 * Constructor.
		 * @param checkVictory true if the game must be checked against victory, false if the game must be checked against loss.
		 */
		protected WonOrLostGamePredicate(final boolean checkVictory) {
			this.checkVictory = checkVictory;
		}
		
		/* (non-Javadoc)
		 * @see ch.lambdaj.function.matcher.Predicate#apply(java.lang.Object)
		 */
		@Override
		public boolean apply(final BaseGame game) {
			if (game == null) {
				return false;
			}
			
			if (this.checkVictory) {
				return game.isWinner(GuavaPlayerStatisticsComputer.this.player);
			}
			else {
				return !game.isWinner(GuavaPlayerStatisticsComputer.this.player);
			}
		}
	}
}
