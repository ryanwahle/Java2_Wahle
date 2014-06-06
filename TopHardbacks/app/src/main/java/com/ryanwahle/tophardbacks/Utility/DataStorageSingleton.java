package com.ryanwahle.tophardbacks.Utility;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataStorageSingleton {
    private static DataStorageSingleton instance = null;
    private static String TAG = "DataStorageSingleton";
    private static String localFilenameString = "hardbackBooksTop20.dat";

    protected DataStorageSingleton() {
        // Method is placeholder to make sure only one instance is ever created.
    }

    public static DataStorageSingleton getInstance() {
        if (instance == null) {
            instance = new DataStorageSingleton();
        }

        return instance;
    }

    public void saveJSONDataToDisk(Context context, String dataToSave) {
        try {
            FileOutputStream jsonFileOutputStream = context.openFileOutput(localFilenameString, Context.MODE_PRIVATE);
            jsonFileOutputStream.write(dataToSave.getBytes());
            jsonFileOutputStream.close();
            Log.v(TAG, "JSON Data Written to Disk");
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "[Error writing file to disk] " + ex);
        } catch (IOException ex) {
            Log.e(TAG, "[Error writing data to file]" + ex);
        }
    }

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
            Log.e(TAG, "[Local JSON Data Not Found on Disk] " + ex);
        } catch (IOException ex) {
            Log.e(TAG, "[Error reading local JSON Data] " + ex);
        }

        return jsonDataStringBuilder.toString();
    }
}