package org.nla.tarotdroid.lib.ui.cloud;


import java.util.Date;
import java.util.List;

import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;
import org.nla.tarotdroid.biz.PlayerList;
import org.nla.tarotdroid.biz.enums.GameStyleType;
import org.nla.tarotdroid.cloud.clientmodel.RestGame;
import org.nla.tarotdroid.cloud.clientmodel.RestGameSet;
import org.nla.tarotdroid.cloud.clientmodel.RestGameStyleTypes;

import com.google.common.base.Function;
import com.google.common.collect.Lists;


public class GameSetConverter {
		
	/**
	 * Default constructor.
	 */
	private GameSetConverter() {
	}
	
	/**
	 * Rest GameStyleTypes to Model GameStyleTypest function.
	 */
	private static final Function<RestGameStyleTypes, GameStyleType> restGameStyleTypesToModelGameStyleTypesFunction = new Function<RestGameStyleTypes, GameStyleType>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public GameStyleType apply(RestGameStyleTypes restGameStyleTypes) {
			if (restGameStyleTypes == null){
				return null;
			}
			
			GameStyleType gameStyleType = null; 
			switch (restGameStyleTypes) {
				case tarot3:
					gameStyleType = GameStyleType.Tarot3;
					break;
				case tarot4:
					gameStyleType = GameStyleType.Tarot4;
					break;
				case tarot5:
					gameStyleType = GameStyleType.Tarot5;
					break;
				default:
					gameStyleType = null;
					break;
			}
			return gameStyleType;
		}
	};
	
	/**
	 * Model GameStyleTypes to Rest GameStyleTypest function.
	 */
	private static final Function<GameStyleType, RestGameStyleTypes> modelGameStyleTypesToRestGameStyleTypesFunction = new Function<GameStyleType, RestGameStyleTypes>() {

		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestGameStyleTypes apply(GameStyleType gameStyleType) {
			if (gameStyleType == null){
				return null;
			}
			
			RestGameStyleTypes restGameStyleTypes = null; 
			switch (gameStyleType) {
				case Tarot3:
					restGameStyleTypes = RestGameStyleTypes.tarot3;
					break;
				case Tarot4:
					restGameStyleTypes = RestGameStyleTypes.tarot4;
					break;
				case Tarot5:
					restGameStyleTypes = RestGameStyleTypes.tarot5;
					break;
				default:
					restGameStyleTypes = null;
					break;
			}
			return restGameStyleTypes;
		}
	};
	
	/**
	 * Rest game set to Model game set function.
	 */
	private static final Function<RestGameSet, GameSet> restGameSetToModelGameSetFunction = new Function<RestGameSet, GameSet>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public GameSet apply(RestGameSet restGameSet) {
			GameSet toReturn = new GameSet();
			try {
				toReturn.setCreationTs(new Date(restGameSet.getCreationTs()));
				toReturn.setUuid(restGameSet.getUuid());
				
				toReturn.setGameStyleType(restGameStyleTypesToModelGameStyleTypesFunction.apply(restGameSet.getGameStyleType()));
				toReturn.setPlayers(new PlayerList(PlayerConverter.convertFromRest(restGameSet.getPlayers(), true)));
				toReturn.setGameSetParameters(GameSetParametersConverter.convertFromRest(restGameSet.getGameSetParameters()));
				
				List<RestGame> restGames = restGameSet.getGames();
				List<BaseGame> games = GameConverter.convertFromRest(restGames);
				
				for(BaseGame game : games) {
					toReturn.addGame(game);
				}
			}
			catch (Exception e) {
				throw new ConvertException(RestGameSet.class, GameSet.class, e);
			}
			return toReturn;
		}
	};

	/**
	 * Model game set to Rest game set function.
	 */
	private static final Function<GameSet, RestGameSet> modelGameSetToRestGameSetFunction = new Function<GameSet, RestGameSet>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestGameSet apply(GameSet gameSet) {
			RestGameSet toReturn = new RestGameSet();
			try {
				toReturn.setCreationTs(gameSet.getCreationTs().getTime());
				toReturn.setUuid(gameSet.getUuid());
				
				toReturn.setGameStyleType(modelGameStyleTypesToRestGameStyleTypesFunction.apply(gameSet.getGameStyleType()));
				toReturn.setGameSetParameters(GameSetParametersConverter.convertToRest(gameSet.getGameSetParameters()));
				toReturn.setPlayers(PlayerConverter.convertToRest(gameSet.getPlayers().getPlayers()));
				toReturn.setGames(GameConverter.convertToRest(gameSet.getGames()));
			}
			catch (Exception e) {
				throw new ConvertException(GameSet.class, RestGameSet.class, e);
			}
			return toReturn;
		}
	};
	
	/**
	 * Cloud id to Rest gameset function.
	 */
	private static final Function<String, RestGameSet> cloudIdToRestGameSetFunction = new Function<String, RestGameSet>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestGameSet apply(String gameSetId) {
			RestGameSet toReturn = new RestGameSet();
			toReturn.setUuid(gameSetId);
			toReturn.setValid(false);
			return toReturn;
		}
	};
	
	/**
	 * Converts a Rest GameSet to a Model GameSet.
	 * @param restGameSet
	 * @return
	 */
	public static GameSet convertFromRest(RestGameSet restGameSet) {
		if (restGameSet == null) {
			return null;
		}
		
		return restGameSetToModelGameSetFunction.apply(restGameSet);
	}
	
	/**
	 * Converts a Model GameSet to a Rest GameSet.
	 * @param gameSet
	 * @return
	 */
	public static RestGameSet convertToRest(GameSet gameSet) {
		if (gameSet == null) {
			return null;
		}

		return modelGameSetToRestGameSetFunction.apply(gameSet);
	}
	
	/**
	 * Converts a list of Rest GameSet to a list of Model GameSet.
	 * @param restGameSets
	 * @return
	 */
	public static List<GameSet> convertFromRest(List<RestGameSet> restGameSets) {
		if (restGameSets == null) {
			return null;
		}

		return Lists.transform(restGameSets, restGameSetToModelGameSetFunction);
	}
	
	/**
	 * Converts a list of Model GameSet to a list of Rest GameSet.
	 * @param gameSets
	 * @return
	 */
	public static List<RestGameSet> convertToRest(List<GameSet> gameSets) {
		if (gameSets == null) {
			return null;
		}

		return Lists.transform(gameSets, modelGameSetToRestGameSetFunction);
	}

	/**
	 * Converts a list of Model GameSets. 
	 * @param idsOfGameSetsToInvalidate
	 * @return
	 */
	public static List<RestGameSet> convertToRestForInvalidation(List<String> idsOfGameSetsToInvalidate) {
		if (idsOfGameSetsToInvalidate == null) {
			return null;
		}
		
		return Lists.transform(idsOfGameSetsToInvalidate, cloudIdToRestGameSetFunction);
	}
}
