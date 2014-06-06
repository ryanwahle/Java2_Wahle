/*
    Author:     Ryan Wahle
    Date:       5 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.NYTJSONService;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.ryanwahle.tophardbacks.Utility.DataStorageSingleton;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/*
    This class grabs the JSON data from the NYTimes Top 20 Hardback Book RESTful web
    service that they provide.
 */
public class NYTJSONService extends IntentService {

    static String TAG = "NYTJSONService";

    public NYTJSONService() {
        super("NYTJSONService");
        Log.v(TAG, "Started Service");
    }

    @Override
    protected void onHandleIntent(Intent arg0) {
        URL dataURL = null;
        try {
            dataURL = new URL("http://api.nytimes.com/svc/books/v2/lists.json?list=hardcover-fiction&api-key=1134b67452889b1b00a9dfa4fd5345f1:6:69454330");
        } catch (MalformedURLException ex) {
            Log.e(TAG, "MalformedURLException: " + ex);
        }

        /*
            Grab the JSON data from the web service.
         */
        URLConnection remoteURLConnection = null;
        BufferedInputStream remoteDataBufferedInputStream = null;
        StringBuilder bufferStringBuilder = new StringBuilder();
        try {
            remoteURLConnection = dataURL.openConnection();
            remoteDataBufferedInputStream = new BufferedInputStream(remoteURLConnection.getInputStream());

            byte[] remoteDataBytes = new byte[1024];
            int bytesRead = 0;

            while ((bytesRead = remoteDataBufferedInputStream.read(remoteDataBytes)) != -1) {
                bufferStringBuilder.append(new String(remoteDataBytes, 0, bytesRead));
            }

            //Log.v(TAG, "JSON Data" + bufferStringBuilder);


        } catch (IOException ex) {
            Log.e(TAG, "IOException" + ex);
        }

        /*
            Store the JSON data using the DataStorageSingleton and then alert the
            messenger that we are done getting the data and the calling Activity can
            use it how they wish.
         */
        DataStorageSingleton.getInstance().saveJSONDataToDisk(this, bufferStringBuilder.toString());

        Bundle intentExtras = arg0.getExtras();
        Messenger intentMessenger = (Messenger) intentExtras.get("messenger");

        Message returnMessage = Message.obtain();
        returnMessage.arg1 = Activity.RESULT_OK;

        try {
            intentMessenger.send(returnMessage);
        } catch (RemoteException ex) {
            Log.e(TAG, "Messenger [RemoteException]: " + ex.getMessage());
        }

    }
}
