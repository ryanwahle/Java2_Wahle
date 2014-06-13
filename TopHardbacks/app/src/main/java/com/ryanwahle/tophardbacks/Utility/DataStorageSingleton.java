/*
    Author:     Ryan Wahle
    Date:       12 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.Utility;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/*
    This call reads and writes the JSON data to a local file for future
    retrieval.
 */
public class DataStorageSingleton {
    private static DataStorageSingleton instance = null;
    private static String TAG = "DataStorageSingleton";
    private static String localFilenameString = "hardbackBooksTop20.dat";
    private static String localRatingFilnameString = "bookRatings.dat";

    protected DataStorageSingleton() {
        // Method is placeholder to make sure only one instance is ever created.
    }

    public static DataStorageSingleton getInstance() {
        if (instance == null) {
            instance = new DataStorageSingleton();
        }

        return instance;
    }

    /*
        Save a string of data to disk.
     */
    public void saveJSONDataToDisk(Context context, String dataToSave) {
        try {
            FileOutputStream jsonFileOutputStream = context.openFileOutput(localFilenameString, Context.MODE_PRIVATE);
            jsonFileOutputStream.write(dataToSave.getBytes());
            jsonFileOutputStream.close();
            Log.v(TAG, "JSON Data Written to Disk");
        } catch (FileNotFoundException ex) {
            Log.v(TAG, "[Error writing file to disk] " + ex);
        } catch (IOException ex) {
            Log.e(TAG, "[Error writing data to file]" + ex);
        }
    }

    /*
        Read data from a local file and return the contents in a String.
     */
    public String readSavedJSONDataFromDisk(Context context) {
        StringBuilder jsonDataStringBuilder = new StringBuilder();

        try {
            FileInputStream jsonFileInputStream = context.openFileInput(localFilenameString);
            BufferedInputStream jsonFileBufferedInputStream = new BufferedInputStream(jsonFileInputStream);

            byte[] jsonDataBytes = new byte[1024];
            int dataBytesRead = 0;

            while ((dataBytesRead = jsonFileBufferedInputStream.read(jsonDataBytes)) != -1) {
                jsonDataStringBuilder.append(new String(jsonDataBytes, 0, dataBytesRead));
            }
        } catch (FileNotFoundException ex) {
            Log.v(TAG, "[Local JSON Data Not Found on Disk] " + ex);
        } catch (IOException ex) {
            Log.e(TAG, "[Error reading local JSON Data] " + ex);
        }

        return jsonDataStringBuilder.toString();
    }

    /*
        This class saves the book ratings to disk.
    */
    public void saveBookRatingsToDisk(Context context, ArrayList<HashMap<String, Float>> bookRatingsDataArrayList) {
        try {
            FileOutputStream bookRatingsOutputStream = context.openFileOutput(localRatingFilnameString, Context.MODE_PRIVATE);
            ObjectOutputStream bookRatingsObjectOutputStream = new ObjectOutputStream(bookRatingsOutputStream);

            bookRatingsObjectOutputStream.writeObject(bookRatingsDataArrayList);
            bookRatingsOutputStream.close();

            Log.v(TAG, "Book Ratings written to disk");
        } catch (FileNotFoundException ex) {
            Log.v(TAG, "[Error writing file to disk] " + ex);
        } catch (IOException ex) {
            Log.e(TAG, "[Error writing data to file]" + ex);
        }
    }

    /*
        This class retrieves the book ratings from disk.
    */
    public ArrayList<HashMap<String, Float>> readBookRatingsFromDisk(Context context) {
        ArrayList<HashMap<String, Float>> bookRatingsDataArrayList = null;

        try {
            FileInputStream fileInputStream = context.openFileInput(localRatingFilnameString);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);

            bookRatingsDataArrayList = (ArrayList<HashMap<String, Float>>) objectInputStream.readObject();

        } catch (FileNotFoundException ex) {
            Log.v(TAG, "[Local Book Ratings Not Found on Disk] " + ex);
        } catch (IOException ex) {
            Log.e(TAG, "[Error reading local Book Ratings Data] " + ex);
        } catch (ClassNotFoundException ex) {
            Log.e(TAG, "Book Ratings Object not found");
        }

        return bookRatingsDataArrayList;
    }


}