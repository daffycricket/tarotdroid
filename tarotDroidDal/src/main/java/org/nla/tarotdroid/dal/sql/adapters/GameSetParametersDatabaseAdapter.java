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
package org.nla.tarotdroid.dal.sql.adapters;

import java.text.MessageFormat;

import org.nla.tarotdroid.biz.GameSetParameters;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public class GameSetParametersDatabaseAdapter extends BaseAdapter {

	/**
	 * SQL Query to get the game set parameters associated to a game set.
	 */
	private static final String GetParametersForGameSetQuery = MessageFormat.format(
			"select * from {0} where {1} = ?", 
			DatabaseV3Constants.TABLE_GAMESET_PARAMETERS, 
			DatabaseV3Constants.COL_GAMESET_PARAMETERS_ID
	);
	
	/**
	 * Constructs a GameSetParametersyDatabaseAdapter from an Android context.
	 * @param context
	 */
	public GameSetParametersDatabaseAdapter(final SQLiteDatabase database) {
		super(database);
	}

	/**
	 * Persists a new GameSetParameters.
	 * @param gameSetParameters The GameSetParameters to persist.
	 */
	public void storeGameSetParameters(final GameSetParameters gameSetParameters, final long gameSetId) {
		ContentValues initialValues = createContentValues(gameSetParameters);
		initialValues.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_ID, gameSetId);
		this.database.insertOrThrow(DatabaseV3Constants.TABLE_GAMESET_PARAMETERS, null, initialValues);
		gameSetParameters.setId(gameSetId);
	}

	/**
	 * Deletes an existing GameSetParameters. 
	 * @param gameSetParametersId The GameSetParameters to delete.
	 * @return True if the GameSetParameters is successfully deleted, false otherwise.
	 */
	public boolean deleteGameSetParameters(final long gameSetParametersId) {
		return this.database.delete(DatabaseV3Constants.TABLE_GAMESET_PARAMETERS, DatabaseV3Constants.COL_GAMESET_PARAMETERS_ID + "=?", new String[]{new Long(gameSetParametersId).toString()}) > 0;
	}
	
	/**
	 * Returns a GameSetParameters object representing the 
	 * @param gameSetParametersId The identifier of the GameSetParameters to delete.
	 * @return a GameSetParameters
	 */
	public GameSetParameters fetchGameSetParametersForGameSetId(final long gameSetParametersId) {

		GameSetParameters gameSetParameters = null;
		Cursor dataSource = this.database.rawQuery(GetParametersForGameSetQuery, new String[]{new Long(gameSetParametersId).toString()});
		if (dataSource.moveToFirst()) {
			gameSetParameters = new GameSetParameters();
			gameSetParameters.setId(dataSource.getLong(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_ID)));
			gameSetParameters.setUuid(dataSource.getString(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_UUID)));
			gameSetParameters.setPetiteRate(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PETITERATE)));
			gameSetParameters.setPriseRate(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PRISERATE)));
			gameSetParameters.setGardeRate(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDERATE)));
			gameSetParameters.setGardeSansRate(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDESANSRATE)));
			gameSetParameters.setGardeContreRate(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDECONTRERATE)));
			gameSetParameters.setPetiteBasePoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PETITEBASEPOINTS)));
			gameSetParameters.setPriseBasePoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PRISEBASEPOINTS)));
			gameSetParameters.setGardeBasePoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDEBASEPOINTS)));
			gameSetParameters.setGardeSansBasePoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDESANSBASEPOINTS)));
			gameSetParameters.setGardeContreBasePoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDECONTREBASEPOINTS)));
			gameSetParameters.setMiseryPoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_MISERYPOINTS)));
			gameSetParameters.setPoigneePoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_HANDFULPOINTS)));
			gameSetParameters.setDoublePoigneePoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_DOUBLEHANDFULPOINTS)));
			gameSetParameters.setTriplePoigneePoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_TRIPLEHANDFULPOINTS)));
			gameSetParameters.setKidAtTheEndPoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_KIDATTHEENDPOINTS)));
			gameSetParameters.setBelgianBaseStepPoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_BELGIANBASESTEPPOINTS)));
			gameSetParameters.setAnnouncedAndSucceededChelemPoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDSUCCEEDEDPOINTS)));
			gameSetParameters.setAnnouncedAndFailedChelemPoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDFAILEDPOINTS)));
			gameSetParameters.setNotAnnouncedButSucceededChelemPoints(dataSource.getInt(dataSource.getColumnIndex(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMNOTANNOUNCEDANDSUCCEEDEDPOINTS)));
		}
		dataSource.close();
		
		return gameSetParameters;
	}

	/**
	 * Returns a ContentValues object representing the specified GameSetParameters.
	 * @param gameSetParameters The GameSetParameters to convert into a ContentValues.
	 * @return The ContentValues object representing the specified GameSetParameters.
	 */
	private ContentValues createContentValues(final GameSetParameters gameSetParameters) {
		ContentValues values = new ContentValues();
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_UUID, gameSetParameters.getUuid());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PETITERATE, gameSetParameters.getPetiteRate());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PRISERATE, gameSetParameters.getPriseRate());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDERATE, gameSetParameters.getGardeRate());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDESANSRATE, gameSetParameters.getGardeSansRate());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDECONTRERATE, gameSetParameters.getGardeContreRate());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PETITEBASEPOINTS, gameSetParameters.getPetiteBasePoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_PRISEBASEPOINTS, gameSetParameters.getPriseBasePoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDEBASEPOINTS, gameSetParameters.getGardeBasePoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDESANSBASEPOINTS, gameSetParameters.getGardeSansBasePoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_GARDECONTREBASEPOINTS, gameSetParameters.getGardeContreBasePoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_MISERYPOINTS, gameSetParameters.getMiseryPoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_HANDFULPOINTS, gameSetParameters.getPoigneePoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_DOUBLEHANDFULPOINTS, gameSetParameters.getDoublePoigneePoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_TRIPLEHANDFULPOINTS, gameSetParameters.getTriplePoigneePoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_KIDATTHEENDPOINTS, gameSetParameters.getKidAtTheEndPoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_BELGIANBASESTEPPOINTS, gameSetParameters.getBelgianBaseStepPoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDSUCCEEDEDPOINTS, gameSetParameters.getAnnouncedAndSucceededChelemPoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMANNOUNCEDANDFAILEDPOINTS, gameSetParameters.getAnnouncedAndFailedChelemPoints());
		values.put(DatabaseV3Constants.COL_GAMESET_PARAMETERS_SLAMNOTANNOUNCEDANDSUCCEEDEDPOINTS, gameSetParameters.getNotAnnouncedButSucceededChelemPoints());
		return values;
	}
}
