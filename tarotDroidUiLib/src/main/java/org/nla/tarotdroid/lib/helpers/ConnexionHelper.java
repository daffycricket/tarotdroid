package org.nla.tarotdroid.lib.helpers;

import static com.google.common.base.Preconditions.checkArgument;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Network and internet connexion helper class.
 */
public class ConnexionHelper {

    /**
     * Checking for all possible internet providers
     * **/
    public static boolean isConnectedToInternet(Context context) {
    	checkArgument(context != null, "context is null");
    	boolean toReturn = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                    	  toReturn = true;
                    	  break;
                      }

          }
          return toReturn;
    }
}