package org.nla.tarotdroid.lib.ui.cloud;


import java.util.List;

import org.nla.tarotdroid.biz.GameSetParameters;
import org.nla.tarotdroid.cloud.clientmodel.RestGameSetParameters;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class GameSetParametersConverter {
	
	/**
	 * Default constructor.
	 */
	private GameSetParametersConverter() {
	}
	
	/**
	 * Rest game set parameters to Model game set parameters function.
	 */
	private static final Function<RestGameSetParameters, GameSetParameters> restGameSetParametersToModelGameSetParametersFunction = new Function<RestGameSetParameters, GameSetParameters>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public GameSetParameters apply(RestGameSetParameters restGameSetParameters) {
			GameSetParameters toReturn = new GameSetParameters();
			toReturn.setPetiteRate(restGameSetParameters.getPetiteRate());
			toReturn.setPriseRate(restGameSetParameters.getPriseRate());
			toReturn.setGardeRate(restGameSetParameters.getGardeRate());
			toReturn.setGardeSansRate(restGameSetParameters.getGardeSansRate());
			toReturn.setGardeContreRate(restGameSetParameters.getGardeContreRate());
			toReturn.setPetiteBasePoints(restGameSetParameters.getPetiteBasePoints());
			toReturn.setPriseBasePoints(restGameSetParameters.getPriseBasePoints());
			toReturn.setGardeBasePoints(restGameSetParameters.getGardeBasePoints());
			toReturn.setGardeSansBasePoints(restGameSetParameters.getGardeSansBasePoints());
			toReturn.setGardeContreBasePoints(restGameSetParameters.getGardeContreBasePoints());
			toReturn.setMiseryPoints(restGameSetParameters.getMiseryPoints());
			toReturn.setPoigneePoints(restGameSetParameters.getPoigneePoints());
			toReturn.setDoublePoigneePoints(restGameSetParameters.getDoublePoigneePoints());
			toReturn.setTriplePoigneePoints(restGameSetParameters.getTriplePoigneePoints());
			toReturn.setAnnouncedAndSucceededChelemPoints(restGameSetParameters.getAnnouncedAndSucceededChelemPoints());
			toReturn.setNotAnnouncedButSucceededChelemPoints(restGameSetParameters.getNotAnnouncedButSucceededChelemPoints());
			toReturn.setAnnouncedAndFailedChelemPoints(restGameSetParameters.getAnnouncedAndFailedChelemPoints());
			toReturn.setKidAtTheEndPoints(restGameSetParameters.getKidAtTheEndPoints());
			toReturn.setBelgianBaseStepPoints(restGameSetParameters.getBelgianBaseStepPoints());
			
			return toReturn;
		}
	};
	
	/**
	 * Model game set parameters to Rest game set parameters function.
	 */
	private static final Function<GameSetParameters, RestGameSetParameters> modelGameSetParametersToRestGameSetParametersFunction = new Function<GameSetParameters, RestGameSetParameters>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestGameSetParameters apply(GameSetParameters gameSetParameters) {
			RestGameSetParameters toReturn = new RestGameSetParameters();
			toReturn.setPetiteRate(gameSetParameters.getPetiteRate());
			toReturn.setPriseRate(gameSetParameters.getPriseRate());
			toReturn.setGardeRate(gameSetParameters.getGardeRate());
			toReturn.setGardeSansRate(gameSetParameters.getGardeSansRate());
			toReturn.setGardeContreRate(gameSetParameters.getGardeContreRate());
			toReturn.setPetiteBasePoints(gameSetParameters.getPetiteBasePoints());
			toReturn.setPriseBasePoints(gameSetParameters.getPriseBasePoints());
			toReturn.setGardeBasePoints(gameSetParameters.getGardeBasePoints());
			toReturn.setGardeSansBasePoints(gameSetParameters.getGardeSansBasePoints());
			toReturn.setGardeContreBasePoints(gameSetParameters.getGardeContreBasePoints());
			toReturn.setMiseryPoints(gameSetParameters.getMiseryPoints());
			toReturn.setPoigneePoints(gameSetParameters.getPoigneePoints());
			toReturn.setDoublePoigneePoints(gameSetParameters.getDoublePoigneePoints());
			toReturn.setTriplePoigneePoints(gameSetParameters.getTriplePoigneePoints());
			toReturn.setAnnouncedAndSucceededChelemPoints(gameSetParameters.getAnnouncedAndSucceededChelemPoints());
			toReturn.setNotAnnouncedButSucceededChelemPoints(gameSetParameters.getNotAnnouncedButSucceededChelemPoints());
			toReturn.setAnnouncedAndFailedChelemPoints(gameSetParameters.getAnnouncedAndFailedChelemPoints());
			toReturn.setKidAtTheEndPoints(gameSetParameters.getKidAtTheEndPoints());
			toReturn.setBelgianBaseStepPoints(gameSetParameters.getBelgianBaseStepPoints());

			return toReturn;
		}
	};
	
	/**
	 * Converts a Rest GameSetParameters to a GameSetParameters.
	 * @param restGameSetParameters
	 * @return
	 */
	public static GameSetParameters convertFromRest(RestGameSetParameters restGameSetParameters) {
		if (restGameSetParameters == null) {
			return null;
		}
		return restGameSetParametersToModelGameSetParametersFunction.apply(restGameSetParameters);
	}
	
	/**
	 * Converts a GameSetParameters to a Rest GameSetParameters.
	 * @param gameSetParameters
	 * @return
	 */
	public static RestGameSetParameters convertToRest(GameSetParameters gameSetParameters) {
		if (gameSetParameters == null) {
			return null;
		}
		return modelGameSetParametersToRestGameSetParametersFunction.apply(gameSetParameters);
	}
	
	/**
	 * Converts a list of Rest GameSetParameters to a list of GameSetParameters.
	 * @param restGameSetParameterss
	 * @return
	 */
	public static List<GameSetParameters> convertFromRest(List<RestGameSetParameters> restGameSetParameterss) {
		if (restGameSetParameterss == null) {
			return null;
		}
		return Lists.transform(restGameSetParameterss, restGameSetParametersToModelGameSetParametersFunction);
	}
	
	/**
	 * Converts a list of GameSetParameters to a list of Rest GameSetParameters.
	 * @param gameSetParameterss
	 * @return
	 */
	public static List<RestGameSetParameters> convertToRest(List<GameSetParameters> gameSetParameterss) {
		if (gameSetParameterss == null) {
			return null;
		}
		return Lists.transform(gameSetParameterss, modelGameSetParametersToRestGameSetParametersFunction);
	}
}
