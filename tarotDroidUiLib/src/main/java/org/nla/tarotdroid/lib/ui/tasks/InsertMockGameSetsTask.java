package org.nla.tarotdroid.lib.ui.tasks;

//import org.nla.tarotdroid.lib.R;
import org.nla.tarotdroid.lib.app.AppContext;
import org.nla.tarotdroid.lib.helpers.RandomHelper;
import org.nla.tarotdroid.biz.BaseGame;
import org.nla.tarotdroid.biz.GameSet;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
//import android.app.ProgressDialog;

/**
 *	An AsyncTask aimed to populate the DAL.
 */
public class InsertMockGameSetsTask extends AsyncTask<Void, String, Void> {
	
	/**
	 * The context.
	 */
	private Context context;
	
	/**
	 * The progress dialog.
	 */
	private ProgressDialog dialog;
	
	/**
	 * Flag indicating whether an error occured in the background. 
	 */
	private boolean backroundErrorHappened;

	/**
	 * Potential exception that happened in the background.
	 */
	private Exception backgroundException;
	
	/**
	 * The game set count to create.
	 */
	private int gameSetCount; 
	
	/**
	 * The max game count to create for each game set.
	 */
	private int maxGameCount;
	
	/**
	 * A flag indicating the execution must go on.
	 */
	private boolean goOn;
		
	/**
	 * Constructor using a context.
	 * @param context
	 * @param nbGameSetsToCreate
	 */
	public InsertMockGameSetsTask(final Context context, final int gameSetCount, final int maxGameCount) {
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		}
		
		this.context = context;
		this.backroundErrorHappened = false;
		this.gameSetCount = gameSetCount;
		this.maxGameCount = maxGameCount;
		this.goOn = true;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		this.dialog = new ProgressDialog(this.context);
		this.dialog.setMessage("Statrting game creation...");
		this.dialog.show();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Void doInBackground(final Void... params) {
		try {			
			for (int i = 0; i < this.gameSetCount && this.goOn; ++i){
				GameSet gameSet = RandomHelper.createRandomStandardTarotGameSet();
				AppContext.getApplication().getDalService().saveGameSet(gameSet);
				
				int nbGamesToCreate = RandomHelper.nextInt(this.maxGameCount);
				for (int j = 0; j < nbGamesToCreate && this.goOn; ++j ) {
					this.publishProgress("Creating game set " + i + " on " + this.gameSetCount + " (game " + j + "/" + nbGamesToCreate + ")");
					BaseGame game = RandomHelper.createRandomTarotGame(gameSet);
					gameSet.addGame(game);
					AppContext.getApplication().getDalService().saveGame(game, gameSet);
				}
			}
		}
		catch (Exception e) {
			this.backroundErrorHappened = true;
			this.backgroundException = e;
		}
		return null;
	}
	
    /* (non-Javadoc)
     * @see android.os.AsyncTask#onProgressUpdate(Progress[])
     */
    protected void onProgressUpdate(final String... messages) {
    	if (messages.length > 0) {
    		this.dialog.setMessage(messages[0]);
    	}
    }

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(final Void unused) {

		// hide busy idicator
		if (this.dialog.isShowing()) {
			this.dialog.dismiss();
		}

		// display toast if error happened
		if (this.backroundErrorHappened) {
            Toast.makeText(
            		this.context,
            		"Error :" + this.backgroundException,
                    Toast.LENGTH_LONG
            ).show();
		}
		
		// display toast if task was stopped
		if (!this.goOn) {
            Toast.makeText(
            		this.context,
            		"Games creation stopped...",
                    Toast.LENGTH_LONG
            ).show();
		}
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override
	protected void onCancelled() {
		this.goOn = false;
		super.onCancelled();
	}
}
