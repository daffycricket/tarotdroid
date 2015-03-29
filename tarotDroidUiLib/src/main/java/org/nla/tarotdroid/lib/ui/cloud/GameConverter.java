package org.nla.tarotdroid.lib.ui.cloud;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.BelgianBaseGame;
import org.nla.tarotdroid.biz.BelgianTarot3Game;
import org.nla.tarotdroid.biz.BelgianTarot4Game;
import org.nla.tarotdroid.biz.BelgianTarot5Game;
import org.nla.tarotdroid.biz.Bet;
import org.nla.tarotdroid.biz.Chelem;
import org.nla.tarotdroid.biz.King;
import org.nla.tarotdroid.biz.PassedGame;
import org.nla.tarotdroid.biz.PenaltyGame;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.StandardBaseGame;
import org.nla.tarotdroid.biz.StandardTarot3Game;
import org.nla.tarotdroid.biz.StandardTarot4Game;
import org.nla.tarotdroid.biz.StandardTarot5Game;
import org.nla.tarotdroid.biz.Team;
import org.nla.tarotdroid.cloud.clientmodel.RestBetTypes;
import org.nla.tarotdroid.cloud.clientmodel.RestColorTypes;
import org.nla.tarotdroid.cloud.clientmodel.RestGame;
import org.nla.tarotdroid.cloud.clientmodel.RestGameTypes;
import org.nla.tarotdroid.cloud.clientmodel.RestPlayer;
import org.nla.tarotdroid.cloud.clientmodel.RestSlamTypes;
import org.nla.tarotdroid.cloud.clientmodel.RestTeamTypes;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class GameConverter {
	
	/**
	 * Default constructor.
	 */
	private GameConverter() {
	}
	
	/**
	 * Rest ColorTypes to King function.
	 */
	private static final Function<RestColorTypes, King> restColorTypesToBizKingFunction = new Function<RestColorTypes, King>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public King apply(RestColorTypes restColorType) {
			if (restColorType == null) {
				return null;
			}
			
			King colorType = null; 
			switch (restColorType) {
				case coeur:
					colorType = King.HEART;
					break;
				case carreau:
					colorType = King.DIAMOND;
					break;
				case pic:
					colorType = King.SPADE;
					break;
				case trefle:
					colorType = King.CLUB;
					break;
				default:
					colorType = null;
					break;
			}
			return colorType;
		}
	};
	
	/**
	 * King to Rest ColorTypes function.
	 */
	private static final Function<King, RestColorTypes> bizKingToRestColorTypesFunction = new Function<King, RestColorTypes>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestColorTypes apply(King colorType) {
			if (colorType == null) {
				return null;
			}

			RestColorTypes restcolorType = null; 
			if (colorType == King.HEART) {
					restcolorType = RestColorTypes.coeur;
			}
			else if (colorType == King.DIAMOND) {
				restcolorType = RestColorTypes.carreau;
			}
			else if (colorType == King.SPADE) {
				restcolorType = RestColorTypes.pic;
			}
			else if (colorType == King.CLUB) {
				restcolorType = RestColorTypes.trefle;
			}
			else {
				restcolorType = null;
			}
			return restcolorType;
		}
	};
	
	/**
	 * Rest SlamTypes to Chelem function.
	 */
	private static final Function<RestSlamTypes, Chelem> restSlamTypesToBizChelemFunction = new Function<RestSlamTypes, Chelem>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public Chelem apply(RestSlamTypes restSlamType) {
			if (restSlamType == null) {
				return null;
			}

			Chelem slamType = null; 
			switch (restSlamType) {
				case annoncePasse:
					slamType = Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED;
					break;
				case annonceChute:
					slamType = Chelem.CHELEM_ANOUNCED_AND_FAILED;
					break;
				case nonAnnoncePasse:
					slamType = Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED;
					break;
				default:
					slamType = null;
					break;
			}
			return slamType;
		}
	};
	
	/**
	 * Chelem to Rest SlamTypes function.
	 */
	private static final Function<Chelem, RestSlamTypes> bizChelemToRestSlamTypesFunction = new Function<Chelem, RestSlamTypes>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestSlamTypes apply(Chelem slamType) {
			if (slamType == null) {
				return null;
			}
			
			RestSlamTypes restslamType = null; 
			if (slamType == Chelem.CHELEM_ANOUNCED_AND_SUCCEEDED) {
				restslamType = RestSlamTypes.annoncePasse;
			}
			else if (slamType == Chelem.CHELEM_ANOUNCED_AND_FAILED) {
				restslamType = RestSlamTypes.annonceChute;
			}
			else if (slamType == Chelem.CHELEM_NOT_ANOUNCED_BUT_SUCCEEDED) {
				restslamType = RestSlamTypes.nonAnnoncePasse;
			}
			else {
				restslamType = null;
			}
			return restslamType;
		}
	};
	
	/**
	 * Rest BetTypes to Bet function.
	 */
	private static final Function<RestBetTypes, Bet> restBetTypesToBizBetFunction = new Function<RestBetTypes, Bet>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public Bet apply(RestBetTypes restBetTypes) {
			if (restBetTypes == null) {
				return null;
			}
			
			Bet bet = null; 
			switch (restBetTypes) {
				case petite:
					bet = Bet.PETITE;
					break;
				case prise:
					bet = Bet.PRISE;
					break;
				case garde:
					bet = Bet.GARDE;
					break;
				case gardeSans:
					bet = Bet.GARDESANS;
					break;
				case gardeContre:
					bet = Bet.GARDECONTRE;
					break;
				default:
					bet = null;
					break;
			}
			return bet;
		}
	};
	
	/**
	 * Bet to Rest BetTypes function.
	 */
	private static final Function<Bet, RestBetTypes> bizBetToRestBetTypesFunction = new Function<Bet, RestBetTypes>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestBetTypes apply(Bet bet) {
			if (bet == null) {
				return null;
			}
			
			RestBetTypes restBetTypes = null; 
			if (bet == Bet.PETITE) {
				restBetTypes = RestBetTypes.petite;
			}
			else if (bet == Bet.PRISE) {
				restBetTypes = RestBetTypes.prise;
			}
			else if (bet == Bet.GARDE) {
				restBetTypes = RestBetTypes.garde;
			}
			else if (bet == Bet.GARDESANS) {
				restBetTypes = RestBetTypes.gardeSans;
			}
			else if (bet == Bet.GARDECONTRE) {
				restBetTypes = RestBetTypes.gardeContre;
			}
			else {
				restBetTypes = null;
			}
			return restBetTypes;
		}
	};
	
	/**
	 * Rest TeamTypes to Team function.
	 */
	private static final Function<RestTeamTypes, Team> restTeamTypesToBizTeamFunction = new Function<RestTeamTypes, Team>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public Team apply(RestTeamTypes restTeamTypes) {
			if (restTeamTypes == null) {
				return null;
			}
			
			Team teamType = null; 
			switch (restTeamTypes) {
				case attack:
					teamType = Team.LEADING_TEAM;
					break;
				case defense:
					teamType = Team.DEFENSE_TEAM;
					break;
				default:
					teamType = null;
					break;
			}
			return teamType;
		}
	};
	
	/**
	 * Team to Rest TeamTypes function.
	 */
	private static final Function<Team, RestTeamTypes> bizTeamToRestTeamTypesFunction = new Function<Team, RestTeamTypes>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestTeamTypes apply(Team teamType) {
			if (teamType == null) {
				return null;
			}
			
			RestTeamTypes restTeamTypes = null; 
			if (teamType == Team.LEADING_TEAM) {
				restTeamTypes = RestTeamTypes.attack;
			}
			else if (teamType == Team.DEFENSE_TEAM) {
				restTeamTypes = RestTeamTypes.defense;
			}
			else {
				restTeamTypes = null;
			}
			
			return restTeamTypes;
		}
	};
	
	/**
	 * RestGame to BaseGame function.
	 */
	private static final Function<RestGame, BaseGame> restGameToBizGameFunction = new Function<RestGame, BaseGame>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public BaseGame apply(RestGame restGame) {
			BaseGame toReturn = null;
			
			try {
				if (restGame.getGameType() == RestGameTypes.standard3) {
					toReturn = new StandardTarot3Game();
				}
				else if (restGame.getGameType() == RestGameTypes.standard4) {
					toReturn = new StandardTarot4Game();
				}
				else if (restGame.getGameType() == RestGameTypes.standard5) {
					toReturn = new StandardTarot5Game();
				}
				else if (restGame.getGameType() == RestGameTypes.belge3) {
					toReturn = new BelgianTarot3Game();
				}
				else if (restGame.getGameType() == RestGameTypes.belge4) {
					toReturn = new BelgianTarot4Game();
				}
				else if (restGame.getGameType() == RestGameTypes.belge5) {
					toReturn = new BelgianTarot5Game();
				}
				else if (restGame.getGameType() == RestGameTypes.passe) {
					toReturn = new PassedGame();
				}
				else if (restGame.getGameType() == RestGameTypes.penalite) {
					toReturn = new PenaltyGame();
				}
				else {
					throw new IllegalArgumentException(MessageFormat.format("Unknown game type {0}", restGame.getClass().getName()));
				}

				
				// set common properties
				toReturn.setIndex(restGame.getIndex());
				toReturn.setUuid(restGame.getUuid());
				toReturn.setDealer(PlayerConverter.convertFromRest(restGame.getDealer(), true));
				
				List<RestPlayer> listOfRestPlayers = restGame.getPlayers();
				List<Player> listOfPlayers = PlayerConverter.convertFromRest(listOfRestPlayers, true);
				PlayerList playerList = new PlayerList(listOfPlayers);
				
				toReturn.setPlayers(playerList);
				toReturn.setDeadPlayers(new PlayerList(PlayerConverter.convertFromRest(restGame.getDeadPlayers(), true)));
				toReturn.setCreationTs(new Date(restGame.getCreationTs()));

				// set standard common properties
				if (toReturn instanceof StandardBaseGame) {
					StandardBaseGame standardGame = (StandardBaseGame)toReturn;
					standardGame.setLeadingPlayer(PlayerConverter.convertFromRest(restGame.getLeadingPlayer(), true));
					standardGame.setBet(restBetTypesToBizBetFunction.apply(restGame.getBet()));
					standardGame.setNumberOfOudlers(restGame.getNumberOfOudlers());
					standardGame.setPoints(restGame.getPoints());
					standardGame.setTeamWithPoignee(restTeamTypesToBizTeamFunction.apply(restGame.getTeamWithPoignee()));
					standardGame.setTeamWithDoublePoignee(restTeamTypesToBizTeamFunction.apply(restGame.getTeamWithDoublePoignee()));
					standardGame.setTeamWithTriplePoignee(restTeamTypesToBizTeamFunction.apply(restGame.getTeamWithTriplePoignee()));
					standardGame.setTeamWithKidAtTheEnd(restTeamTypesToBizTeamFunction.apply(restGame.getTeamWithKidAtTheEnd()));
					standardGame.setPlayersWithMisery(new PlayerList(PlayerConverter.convertFromRest(restGame.getPlayersWithMisery(), true)));
					standardGame.setChelem(restSlamTypesToBizChelemFunction.apply(restGame.getChelem()));
				}

				// set standard 5 properties
				if (toReturn instanceof StandardTarot5Game) {
					StandardTarot5Game standard5Game = (StandardTarot5Game)toReturn;
					standard5Game.setCalledPlayer(PlayerConverter.convertFromRest(restGame.getCalledPlayer(), true));
					standard5Game.setCalledKing(restColorTypesToBizKingFunction.apply(restGame.getCalledKing()));
				}
				
				// set belgian common properties
				if (toReturn instanceof BelgianBaseGame) {
					BelgianBaseGame belgianGame = (BelgianBaseGame)toReturn;
					belgianGame.setFirst(PlayerConverter.convertFromRest(restGame.getFirst(), true));
					belgianGame.setSecond(PlayerConverter.convertFromRest(restGame.getSecond(), true));
					belgianGame.setThird(PlayerConverter.convertFromRest(restGame.getThird(), true));			
				}

				// set belgian 4 properties
				if (toReturn instanceof BelgianTarot4Game) {
					BelgianTarot4Game belgian4Game = (BelgianTarot4Game)toReturn;
					belgian4Game.setFourth(PlayerConverter.convertFromRest(restGame.getFourth(), true));
				}
				
				// set belgian 5 properties
				if (toReturn instanceof BelgianTarot5Game) {
					BelgianTarot5Game belgian5Game = (BelgianTarot5Game)toReturn;
					belgian5Game.setFourth(PlayerConverter.convertFromRest(restGame.getFourth(), true));
					belgian5Game.setFifth(PlayerConverter.convertFromRest(restGame.getFifth(), true));
				}
				
				// set penalty properties
				if (toReturn instanceof PenaltyGame) {
					PenaltyGame penaltyGame = (PenaltyGame)toReturn;
					penaltyGame.setPenaltyPoints(restGame.getPenaltyPoints());
					penaltyGame.setPenaltyType(restGame.getPenaltyType());
					penaltyGame.setPenaltedPlayer(PlayerConverter.convertFromRest(restGame.getPenalted(), true));
				}
			}
			catch (Exception e) {
				throw new ConvertException(RestGame.class, BaseGame.class, e);
			}			
			
			return toReturn;
		}
	};

	/**
	 * BaseGame to RestGame function.
	 */
	private static final Function<BaseGame, RestGame> bizGameToRestGameFunction = new Function<BaseGame, RestGame>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestGame apply(BaseGame game) {
			RestGame toReturn = new RestGame();
			try {
				toReturn.setUuid(game.getUuid());
				
				if (game instanceof StandardTarot3Game) {
					toReturn.setGameType(RestGameTypes.standard3);
				}
				else if (game instanceof StandardTarot4Game) {
					toReturn.setGameType(RestGameTypes.standard4);
				}
				else if (game instanceof StandardTarot5Game) {
					toReturn.setGameType(RestGameTypes.standard5);
				}
				else if (game instanceof BelgianTarot3Game) {
					toReturn.setGameType(RestGameTypes.belge3);
				}
				else if (game instanceof BelgianTarot4Game) {
					toReturn.setGameType(RestGameTypes.belge4);
				}
				else if (game instanceof BelgianTarot5Game) {
					toReturn.setGameType(RestGameTypes.belge5);
				}
				else if (game instanceof PenaltyGame) {
					toReturn.setGameType(RestGameTypes.penalite);
				}
				else if (game instanceof PassedGame) {
					toReturn.setGameType(RestGameTypes.passe);
				}
				else {
					throw new IllegalArgumentException(MessageFormat.format("Unknown game type {0}", game.getClass().getName()));
				}
				
				// set common properties
				toReturn.setIndex(game.getIndex());
				toReturn.setDealer(PlayerConverter.convertToRest(game.getDealer()));
				toReturn.setPlayers(PlayerConverter.convertToRest(game.getPlayers().getPlayers()));
				toReturn.setDeadPlayers(PlayerConverter.convertToRest(game.getDeadPlayers().getPlayers()));
				toReturn.setCreationTs(game.getCreationTs().getTime());
				
				// set common standard properties
				if (game instanceof StandardBaseGame) {
					StandardBaseGame stdGame = (StandardBaseGame)game;
					toReturn.setLeadingPlayer(PlayerConverter.convertToRest(stdGame.getLeadingPlayer()));
					toReturn.setBet(bizBetToRestBetTypesFunction.apply(stdGame.getBet()));
					toReturn.setNumberOfOudlers(stdGame.getNumberOfOudlers());
					toReturn.setPoints(stdGame.getPoints());
					toReturn.setTeamWithPoignee(bizTeamToRestTeamTypesFunction.apply(stdGame.getTeamWithPoignee()));
					toReturn.setTeamWithDoublePoignee(bizTeamToRestTeamTypesFunction.apply(stdGame.getTeamWithDoublePoignee()));
					toReturn.setTeamWithTriplePoignee(bizTeamToRestTeamTypesFunction.apply(stdGame.getTeamWithTriplePoignee()));
					toReturn.setTeamWithKidAtTheEnd(bizTeamToRestTeamTypesFunction.apply(stdGame.getTeamWithKidAtTheEnd()));
					if (stdGame.getPlayersWithMisery() != null && stdGame.getPlayersWithMisery().getPlayers() != null) {
						toReturn.setPlayersWithMisery(PlayerConverter.convertToRest(stdGame.getPlayersWithMisery().getPlayers()));
					}
					toReturn.setChelem(bizChelemToRestSlamTypesFunction.apply(stdGame.getChelem()));
				}
				
				// set standard 5 properties
				if (game instanceof StandardTarot5Game) {
					StandardTarot5Game std5Game = (StandardTarot5Game)game;
					toReturn.setCalledPlayer(PlayerConverter.convertToRest(std5Game.getCalledPlayer()));
					toReturn.setCalledKing(bizKingToRestColorTypesFunction.apply(std5Game.getCalledKing()));
				}
				
				// set belgian common properties
				if (game instanceof BelgianBaseGame) {
					BelgianBaseGame belgianGame = (BelgianBaseGame)game;
					toReturn.setFirst(PlayerConverter.convertToRest(belgianGame.getFirst()));
					toReturn.setSecond(PlayerConverter.convertToRest(belgianGame.getSecond()));
					toReturn.setThird(PlayerConverter.convertToRest(belgianGame.getThird()));			
				}
				
				// set belgian 4 properties
				if (game instanceof BelgianTarot4Game) {
					BelgianTarot4Game belgian4Game = (BelgianTarot4Game)game;
					toReturn.setFourth(PlayerConverter.convertToRest(belgian4Game.getFourth()));
				}
				
				// set belgian 5 properties
				if (game instanceof BelgianTarot5Game) {
					BelgianTarot5Game belgian5Game = (BelgianTarot5Game)game;
					toReturn.setFourth(PlayerConverter.convertToRest(belgian5Game.getFourth()));
					toReturn.setFifth(PlayerConverter.convertToRest(belgian5Game.getFifth()));
				}
				
				// set penalty properties
				if (game instanceof PenaltyGame) {
					PenaltyGame penaltyGame = (PenaltyGame)game;
					toReturn.setPenaltyPoints(penaltyGame.getPenaltyPoints());
					toReturn.setPenaltyType(penaltyGame.getPenaltyType());
					toReturn.setPenalted(PlayerConverter.convertToRest(penaltyGame.getPenaltedPlayer()));
				}
			}
			catch (Exception re) {
				throw new ConvertException(BaseGame.class, RestGame.class, re);
			}
			
			return toReturn;
		}
	};
	
	/**
	 * Converts a Rest Game to a BaseGame.
	 * @param convertToRest
	 * @return
	 */
	public static BaseGame convertFromRest(RestGame restGame) {
		if (restGame == null) {
			return null;
		}
		
		return restGameToBizGameFunction.apply(restGame);
	}
	
	/**
	 * Converts a BaseGame to a Rest Game.
	 * @param game
	 * @return
	 */
	public static RestGame convertToRest(BaseGame game) {
		if (game == null) {
			return null;
		}
		
		return bizGameToRestGameFunction.apply(game);
	}
	
	/**
	 * Converts a list of RestGame to a list of BaseGame.
	 * @param restGames
	 * @return
	 */
	public static List<BaseGame> convertFromRest(List<RestGame> restGames) {
		if (restGames == null) {
			return null;
		}
		
		return Lists.transform(restGames, restGameToBizGameFunction);
	}
	
	/**
	 * Converts a list of BaseGame to a list of RestGame.
	 * @param games
	 * @return
	 */
	public static List<RestGame> convertToRest(List<BaseGame> games) {
		if (games == null) {
			return null;
		}
		
		return Lists.transform(games, bizGameToRestGameFunction);
	}
}
