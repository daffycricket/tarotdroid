package org.nla.tarotdroid.dal.sql.adapters;

//import android.content.Context;
//import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseAdapter {
	
	/**
	 * SQLiteDatabase instance.
	 */
	protected SQLiteDatabase database;	

	/**
	 * Constructor.
	 */
	protected BaseAdapter(final SQLiteDatabase database) {
		if(database == null) {
			throw new IllegalArgumentException("database is null");
		}
		this.database = database;
	}
	
	/**
	 * Clears potential cached data.
	 */
	public void clear() {
	}
}
