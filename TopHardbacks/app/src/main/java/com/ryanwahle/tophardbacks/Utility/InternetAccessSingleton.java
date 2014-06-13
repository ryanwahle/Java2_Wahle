/*
    Author:     Ryan Wahle
    Date:       12 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
    This class provides a method to check to see if there is any internet
    service available.
 */
public class InternetAccessSingleton {
        private static InternetAccessSingleton instance = null;
        private static String TAG = "InternetAccessSingleton";

        protected InternetAccessSingleton() {
            // Method is placeholder to make sure only one instance is ever created.
        }

        public static InternetAccessSingleton getInstance() {
            if (instance == null) {
                instance = new InternetAccessSingleton();
            }

            return instance;
        }

        public boolean isInternetAvailable(Context context) {
            boolean returnCode = false;

            ConnectivityManager systemConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo systemNetworkInfo = systemConnectivityManager.getActiveNetworkInfo();

            if (systemNetworkInfo != null) {
                if (systemNetworkInfo.isConnected()) {
                    returnCode = true;
                    Log.v(TAG, "Internet is Available");
                } else {
                    Log.e(TAG, "Internet is not Available");
                }
            }

            return returnCode;
        }
}
