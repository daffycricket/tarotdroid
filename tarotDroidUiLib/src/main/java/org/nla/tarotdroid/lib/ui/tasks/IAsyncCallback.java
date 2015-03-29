/**
 * 
 */
package org.nla.tarotdroid.lib.ui.tasks;

/**
 * Callback for AsyncTask.
 */
public interface IAsyncCallback<T> {
	
	/**
	 * Executes the callback.
	 * @param object
	 * @param exception
	 */
	void execute(T object, Exception exception);
}
