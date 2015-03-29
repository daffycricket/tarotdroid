package org.nla.tarotdroid.lib.ui.tasks;


/**
 * Task aimed to retrieve a session cooki from GAE.
 */
public class GetAppEngineTokenTask/* extends AsyncTask<String, Void, Boolean>*/ {
    
//	public GetAppEngineTokenTask() {
//		
//	}
//	
//	/* (non-Javadoc)
//	 * @see android.os.AsyncTask#doInBackground(Params[])
//	 */
//	@Override
//	protected Boolean doInBackground(String... tokens) {
//            try {
//                // Don't follow redirects
//            	httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
//                
//                HttpGet httpGet = new HttpGet("http://" + AppContext.getApplication().getCloudUrl() + "/_ah/login?continue=http://localhost/&auth=" + tokens[0]);
//                HttpResponse response = httpClient.execute(httpGet);
//                if(response.getStatusLine().getStatusCode() != 302)
//                        // Response should be a redirect
//                        return false;
//                
//                for(Cookie cookie : httpClient.getCookieStore().getCookies()) {
//                        if(cookie.getName().equals("ACSID")) {
//                        	tokenCookie = cookie.getValue();
//                        	//httpRequest.addHeader("Cookie", cookie.getValue());
//                        	return true;
//                        }
//                }
//            }
//            catch (ClientProtocolException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//            }
//            catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//            }
//            httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
//            return false;
//    }
//    
//    /* (non-Javadoc)
//     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//     */
//	@Override
//    protected void onPostExecute(Boolean result) {
//		progressDialog.setMessage("Synchronisation des joueurs...");
//        new CloudPlayerSyncTask().execute();
//    }
}