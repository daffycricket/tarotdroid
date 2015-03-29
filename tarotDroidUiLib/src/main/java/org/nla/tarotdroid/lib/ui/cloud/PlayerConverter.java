package org.nla.tarotdroid.lib.ui.cloud;

import static com.google.common.collect.Maps.newHashMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.biz.Player;
import org.nla.tarotdroid.cloud.clientmodel.RestPlayer;
import org.nla.tarotdroid.dal.DalException;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class PlayerConverter {
	
	/**
	 * Default constructor.
	 */
	private PlayerConverter() {
	}
	
	/**
	 * A cache of players.
	 */
	public static final Map<String, Player> cachedPlayers = newHashMap();
	
	/**
	 * Rest player to Model player function.
	 */
	private static final Function<RestPlayer, Player> restPlayerToModelPlayerUsingCacheFunction = new Function<RestPlayer, Player>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public Player apply(RestPlayer restPlayer) {
			Player toReturn = null;
			
			// try to get player from repo
			try {
				toReturn = AppContext.getApplication().getDalService().getPlayerByUuid(restPlayer.getUuid());
			}
			catch (DalException e) {
				toReturn = null;
			}
			
			// if not found, create player
			if (toReturn == null) {
				toReturn = new Player();
				toReturn.setUuid(restPlayer.getUuid());
				toReturn.setCreationTs(new Date(restPlayer.getCreationTs()));
				toReturn.setName(restPlayer.getName());
				//toReturn.setEmail(restPlayer.getEmail());
				toReturn.setFacebookId(restPlayer.getFacebookId());
				//toReturn.setPictureUri(restPlayer.getPictureUri());
			}
					
			return toReturn;
		}
	};
	
	/**
	 * Rest player to Model player function.
	 */
	private static final Function<RestPlayer, Player> restPlayerToModelPlayerWithoutCacheFunction = new Function<RestPlayer, Player>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public Player apply(RestPlayer restPlayer) {
			Player toReturn = new Player();
			toReturn.setUuid(restPlayer.getUuid());
			toReturn.setCreationTs(new Date(restPlayer.getCreationTs()));
			toReturn.setName(restPlayer.getName());
			//toReturn.setEmail(restPlayer.getEmail());
			toReturn.setFacebookId(restPlayer.getFacebookId());
			//toReturn.setPictureUri(restPlayer.getPictureUri());
					
			return toReturn;
		}
	};


	/**
	 * Model player to Rest player function.
	 */
	private static final Function<Player, RestPlayer> modelPlayerToRestPlayerFunction = new Function<Player, RestPlayer>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestPlayer apply(Player player) {
			RestPlayer toReturn = new RestPlayer();
//			toReturn.setId(player.getId());
			toReturn.setUuid(player.getUuid());
			toReturn.setCreationTs(player.getCreationTs().getTime());
			toReturn.setName(player.getName());
			toReturn.setEmail(player.getEmail());
			toReturn.setPictureUri(player.getPictureUri());
			toReturn.setValid(true);
			return toReturn;
		}
	};
	
	/**
	 * Id to Rest player function.
	 */
	private static final Function<String, RestPlayer> cloudIdToRestPlayerFunction = new Function<String, RestPlayer>() {
		
		/* (non-Javadoc)
		 * @see com.google.common.base.Function#apply(java.lang.Object)
		 */
		@Override
		public RestPlayer apply(String playerId) {
			RestPlayer toReturn = new RestPlayer();
			toReturn.setUuid(playerId);
			toReturn.setValid(false);
			return toReturn;
		}
	};
	
	/**
	 * Converts a Rest Player to a Model Player.
	 * @param restPlayer
	 * @return
	 */
	public static Player convertFromRest(RestPlayer restPlayer, boolean searchInCache) {
		if (restPlayer == null) {
			return null;
		}
		
		
		if (searchInCache) {
			return restPlayerToModelPlayerUsingCacheFunction.apply(restPlayer);
		}
		else {
			return restPlayerToModelPlayerWithoutCacheFunction.apply(restPlayer);
		}
		
	}
	
	/**
	 * Converts a Model Player to a Rest Player.
	 * @param player
	 * @return
	 */
	public static RestPlayer convertToRest(Player player) {
		if (player == null) {
			return null;
		}
		
		return modelPlayerToRestPlayerFunction.apply(player);
	}
	
	/**
	 * Converts a list of Rest Players to a list of Model Players.
	 * @param restPlayers
	 * @param searchInCache
	 * @return
	 */
	public static List<Player> convertFromRest(List<RestPlayer> restPlayers, boolean searchInCache) {
		if (restPlayers == null) {
			return null;
		}
		
		if (searchInCache) {
			return Lists.transform(restPlayers, restPlayerToModelPlayerUsingCacheFunction);
		}
		else {
			return Lists.transform(restPlayers, restPlayerToModelPlayerWithoutCacheFunction);
		}
	}
	
	/**
	 * Converts a list of Model Players to a list of Rest Players.
	 * @param players
	 * @return
	 */
	public static List<RestPlayer> convertToRest(List<Player> players) {
		if (players == null) {
			return null;
		}
		
		return Lists.transform(players, modelPlayerToRestPlayerFunction);
	}

	/**
	 * Converts a list of Model Players. 
	 * @param idsOfPlayersToInvalidate
	 * @return
	 */
	public static List<RestPlayer> convertToRestForInvalidation(ArrayList<String> idsOfPlayersToInvalidate) {
		if (idsOfPlayersToInvalidate == null) {
			return null;
		}
		
		return Lists.transform(idsOfPlayersToInvalidate, cloudIdToRestPlayerFunction);
	}
}
