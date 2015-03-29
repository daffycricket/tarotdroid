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
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.nla.tarotdroid.biz.enums.GameStyleType;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 * A list of sequential games.
 */
public class GameSet extends PersistableBusinessObject implements Iterable<BaseGame>, Serializable {
	
	/**
     * Serial version ID.
     */
	@CloudField(cloudify=false)
    private static final long serialVersionUID = 7711940419321095883L;

    /**
	 * List of games in game set.
	 */
	private List<BaseGame> games;
	
    /**
     * List of standard base games in game set.
     */
    private List<StandardBaseGame> standardGames;

    /**
     * List of standard 5 player style games in game set.
     */
    private List<StandardTarot5Game> standard5Games;
	
	/**
	 * Global results at the end of each game.
	 */
    @CloudField(cloudify=false)
	private GameSetScores gameSetScores;
	
	/**
	 * List of all players, including dead players.
	 */
	private PlayerList players;

	/**
	 * Game set parameters.
	 */
	private GameSetParameters gameSetParameters;
	
	/**
	 * Creation timestamp.
	 */
	private Date creationTs;
	
	/**
	 * Game style type.
	 */
	@CloudField(targetType="String")
	private GameStyleType gameStyleType;
	
	/**
	 * Standard game count.
	 */
	private int stdGameCount;
	
    /**
     * Standard 5 player style game count.
     */
    private int std5GameCount;
		
	/**
	 * Convenient, miscellaneous unstructured properties to improve db access.
	 */
    @CloudField(cloudify=false)
	private String miscProperties;
    
    /**
     * Indicates if and when gameset was posted to Facebook.
     */
    private Date facebookPostTs;
	
	/**
	 * Default constructor.
	 */
	public GameSet() {
		this.games = new ArrayList<BaseGame>();
		this.standardGames = new ArrayList<StandardBaseGame>();
		this.standard5Games = new ArrayList<StandardTarot5Game>();
		this.gameSetScores = new GameSetScores();
		this.creationTs = new Date(System.currentTimeMillis());
		this.gameStyleType = GameStyleType.None;
		this.stdGameCount = 0;
		this.std5GameCount = 0;
		this.miscProperties = "0|";
	}

	/**
	 * @return the gameSetParameters
	 */
	public GameSetParameters getGameSetParameters() {
		return this.gameSetParameters;
	}

	/**
	 * @param gameSetParameters the gameSetParameters to set
	 */
	public void setGameSetParameters(final GameSetParameters gameSetParameters) {
		if (gameSetParameters != this.gameSetParameters) {
			this.gameSetParameters = gameSetParameters;
			this.setChanged();
			this.notifyObservers();
		}
	}
	
    /**
     * Returns a copy of the list containing the games.
     * @return a copy of the list containing the games
     */
    public List<BaseGame> getGames() {
        List<BaseGame> copy = new ArrayList<BaseGame>();
        copy.addAll(this.games);
        return copy;
    }
    
    /**
     * Returns a copy of the list containing the standard games.
     * @return a copy of the list containing the standard games
     */
    public List<StandardBaseGame> getStandardGames() {
        List<StandardBaseGame> copy = new ArrayList<StandardBaseGame>();
        copy.addAll(this.standardGames);
        return copy;
    }
    
    /**
     * Returns a copy of the list containing the standard 5 player style games.
     * @return a copy of the list containing the standard 5 player style games
     */
    public List<StandardTarot5Game> getStandard5Games() {
        List<StandardTarot5Game> copy = new ArrayList<StandardTarot5Game>();
        copy.addAll(this.standard5Games);
        return copy;
    }
	
	/**
	 * Sets the list of players.
	 * @param players the list of players.
	 */
	public void setPlayers(final PlayerList players) {
		if (players == null) {
			throw new IllegalArgumentException("players is null");
		}
		if (this.players != players) {
			this.players = players;
			this.gameSetScores.setPlayers(players);
			this.setChanged();
			this.notifyObservers();
		}
	}

	/**
	 * Returns the list of players in the game. 
	 * @return the list of players in the game.
	 */
	public PlayerList getPlayers() {
		return this.players;
	}

	/**
	 * Sets highest game index on all games.
	 * @param highestGameIndex
	 */
	private void setHighestGameIndex(final int highestGameIndex) {
		for (BaseGame game : this.games) {
			game.setHighestGameIndex(highestGameIndex);
		}
	}
	
	/**
	 * Returns highest game index.
	 * @return highest game index.
	 */
	private int getHighestGameIndex() {
		return this.games.size();
	}
	
	/**
	 * @return the creationTs
	 */
	public Date getCreationTs() {
		return this.creationTs;
	}

	/**
	 * @param creationTs the creationTs to set
	 */
	public void setCreationTs(final Date creationTs) {
		this.creationTs = creationTs;
	}
	
	/**
	 * @return the gameStyleType
	 */
	public GameStyleType getGameStyleType() {
		return this.gameStyleType;
	}

	/**
	 * @param gameStyleType the gameStyleType to set
	 */
	public void setGameStyleType(final GameStyleType gameStyleType) {
		this.gameStyleType = gameStyleType;
	}
	
//	/**
//	 * @param miscPropoerties
//	 */
//	public void setMiscProperties(final String miscProperties) {
//		if (miscProperties == null) {
//			this.miscProperties = "0|";
//		}
//		else {
//			this.miscProperties = miscProperties;
//		}
//	}
//
//	/**
//	 * @return the miscProperties
//	 */
//	public String getMiscProperties() {
//		return this.miscProperties;
//	}

	/**
	 * @return the facebookPostTs
	 */
	public Date getFacebookPostTs() {
		return this.facebookPostTs;
	}

	/**
	 * @param facebookPostTs the facebookPostTs to set
	 */
	public void setFacebookPostTs(Date facebookPostTs) {
		this.facebookPostTs = facebookPostTs;
	}

	/**
	 * Adds a new game to the set.
	 * @param game the games to set.
	 */
	public void addGame(final BaseGame game) {
		checkArgument(game != null, "game is null");
		checkArgument(this.gameSetParameters != null, "this.gameSetParameters null, can't compute scores");
		
		int highestIndex = this.games.size() + 1;
		game.setGameSetParameters(this.gameSetParameters);
		game.setIndex(highestIndex);
		this.games.add(game);
		this.gameSetScores.addGameScore(game.getGameScores());
		this.setHighestGameIndex(highestIndex);
		
		if (game instanceof StandardTarot5Game) {
		    this.standard5Games.add((StandardTarot5Game)game);
		    this.std5GameCount += 1;
		}
		if (game instanceof StandardBaseGame) {
		    this.standardGames.add((StandardBaseGame)game);
		    this.stdGameCount += 1;
		}
		
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Removes the last game.
	 */
	public BaseGame removeLastGame() {
		BaseGame removedGame = null;
		if (this.getGameCount() > 0) {
			int indexOfGameToRemove = this.getGameCount() - 1;
			removedGame = this.games.remove(indexOfGameToRemove);
			// TODO: Try/Catch to prevent a non-understood exception when trying to remove the only game in the game set
			try {
				this.gameSetScores.removeGameScore(indexOfGameToRemove);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			this.setHighestGameIndex(this.getHighestGameIndex());
			
		    if (removedGame instanceof StandardTarot5Game) {
		        this.standard5Games.remove((StandardTarot5Game)removedGame);
		        this.std5GameCount -= 1;
	        }
	        if (removedGame instanceof StandardBaseGame) {
	            this.standardGames.remove((StandardBaseGame)removedGame);
	            this.stdGameCount -= 1;
	        }

			this.setChanged();
			this.notifyObservers();
		}
		return removedGame;
	}
	
	/**
	 * Removes the given game and 
	 */
	public List<BaseGame> removeGameAndAllSubsequentGames(final BaseGame toRemove) {
		checkArgument(toRemove != null, "toRemove is null");
		//checkArgument(toRemove.getIndex() <= toRemove.getHighestGameIndex() , "toRemove is null");
		List<BaseGame> toReturn = newArrayList();
		
		if (this.getGameCount() > 0) {
			
			// if last game, use other method
			if (toRemove.isLatestGame()) {
				toReturn.add(this.removeLastGame());
			}
			
			// otherwise, loop
			else {
				for (int indexOfGameToRemove = this.getGameCount() - 1; indexOfGameToRemove >= toRemove.getIndex() - 1; indexOfGameToRemove--) {
					BaseGame removedGame = this.games.remove(indexOfGameToRemove);
					
					// TODO: Try/Catch to prevent a non-understood exception when trying to remove the only game in the game set
					try {
						this.gameSetScores.removeGameScore(indexOfGameToRemove);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					this.setHighestGameIndex(this.getHighestGameIndex());
					
				    if (removedGame instanceof StandardTarot5Game) {
				        this.standard5Games.remove((StandardTarot5Game)removedGame);
				        this.std5GameCount -= 1;
			        }
			        if (removedGame instanceof StandardBaseGame) {
			            this.standardGames.remove((StandardBaseGame)removedGame);
			            this.stdGameCount -= 1;
			        }
			        toReturn.add(removedGame);
				}
			}

			this.setChanged();
			this.notifyObservers();
		}
		
		return toReturn;
	}
	
	/**
	 * Returns the global results of the game set.
	 * @return the global results of the game set.
	 */
	public GameSetScores getGameSetScores() {
		return this.gameSetScores;
	}
	
	/**
	 * Returns the number of games in the set. 
	 * @return	the number of games in the set.
	 */
	public int getGameCount() {
		return this.games.size();
	}
	
    /**
     * Returns the number of standard games in the set. 
     * @return  the number of standard games in the set.
     */
    public int getStandardGameCount() {        
        return this.stdGameCount;
    }
    
    /**
     * Returns the number of standard 5 player style games in the set. 
     * @return  the number of standard 5 player style  games in the set.
     */
    public int getStandard5GameCount() {        
        return this.std5GameCount;
    }

    /**
     * Returns the number of belgian games in the set. 
     * @return  the number of belgian games in the set.
     */
    public int getBelgianGameCount() {
        return this.games.size() - this.stdGameCount;
    }
    
	/**
	 * Returns the game at given index.
	 * @return the game at given index.
	 */
	public BaseGame getGameOfIndex(final int index) {
		if (index < 0) {
			throw new IllegalArgumentException("index<0=" + index);
		}
		if (index > this.getGameCount()) {
			throw new IllegalArgumentException("index greater than game count=" + index);
		}
		
		return this.games.get(index - 1);
	}
	
	/**
	 * Returns the last game.
	 * @return the last game.
	 */
	public BaseGame getLastGame() {
		return this.games.get(this.games.size() - 1);
	}
	
	/**
	 * Indicates whether a standard game (opposed to a belgian game) has been played.
	 * @return true if a standard game has been played, false otherwise
	 */
	public boolean hasAStandardGameBeenPlayed() {
		for (BaseGame game : this.games) {
			if (game instanceof StandardBaseGame) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Indicates whether the given player is the winner, that is has the highest score of the game set. There can be several winners on the same game set, if they all have the same score.
	 * @param player
	 * @return
	 */
	public boolean isWinner(final Player player) {
		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}
		MapPlayersScores lastPlayerScore = this.getGameSetScores().getResultsAtLastGame();
		try {
			return lastPlayerScore.get(player) == lastPlayerScore.getMaxScore();
		} 
		catch (Exception e) {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<BaseGame> iterator() {
		return this.games.iterator();
	}
    
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
            String formatedDate = DateFormat.getInstance().format(this.getCreationTs());
            String gameSetShortDescription = "%1s %2s, %3s partie(s), joueurs: %4s";
            StringTokenizer sttk = new StringTokenizer(this.miscProperties, "|");
            String gameCountAsString = sttk.nextToken();
            String playersIdsAsString = sttk.nextToken();

            return String.format(
            	gameSetShortDescription,
            	formatedDate,
            	this.getGameStyleType().toString(),
            	gameCountAsString,
            	playersIdsAsString
            );
        }
        catch (Exception e) {
            return super.toString();
        }	
	}

	/**
	 * TODO : Externalize in some helper...
	 * @return
	 */
	public Map<Class<? extends BaseGame>, List<Long>> getGameIds() {
		Map<Class<? extends BaseGame>, List<Long>> gameIds = newHashMap();
		
		for (BaseGame game : this.games) {
			Class<? extends BaseGame> gameClass = game.getClass();
			
			List<Long> gameIdsForGivenClass = null;
			if (gameIds.containsKey(gameClass)) {
				gameIdsForGivenClass = gameIds.get(gameClass);
			}
			else {
				gameIdsForGivenClass = newArrayList();
				gameIds.put(gameClass, gameIdsForGivenClass);
			}
			gameIdsForGivenClass.add(game.getId());
		}
		
		return gameIds;
	}
}
