package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ryanwahle.tophardbacks.NYTJSONService.NYTJSONService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    static String TAG = "MainActivity";

    final Handler nytJSONServiceHandler = new Handler() {
        public void handleMessage(Message message) {
            String nytJSONDataString = (String) message.obj;

            if (message.arg1 == RESULT_OK && nytJSONDataString != null) {
                Log.v(TAG, "Service Finished Retrieving Data.");

                saveJSONDataToDisk(nytJSONDataString);

                populateListView();
            }
        }
    };

    private void populateListView() {
        String localJSONDataString = readSavedJSONDataFromDisk();
        ArrayList<HashMap<String, String>> booksArrayList= new ArrayList<HashMap<String, String>>();

        try {
            JSONObject nytJSONObject = new JSONObject(localJSONDataString);
            JSONArray bookResultsJSONArray = nytJSONObject.getJSONArray("results");

            for (int arrayIndex = 0; arrayIndex < bookResultsJSONArray.length(); arrayIndex++) {
                String bookNameString = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("title");
                String bookAuthorString = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("author");
                String bookPublisher = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("publisher");

                HashMap<String, String> bookHashMap = new HashMap<String, String>();
                bookHashMap.put("bookName", bookNameString);
                bookHashMap.put("bookAuthor", bookAuthorString);
                bookHashMap.put("bookPublisher", bookPublisher);

                booksArrayList.add(bookHashMap);
            }

            //Log.v(TAG, "" + bookResultsJSONArray.toString());
        } catch (JSONException ex) {
            Log.e(TAG, "Error Parsing JSON Data: ", ex);
        }

        SimpleAdapter booksListViewAdaptor = new SimpleAdapter(this, booksArrayList, R.layout.activity_main_listview_row, new String[] {"bookName", "bookAuthor", "bookPublisher"}, new int[] {R.id.bookName, R.id.bookAuthor, R.id.bookPublisher});

        ListView booksListView = (ListView) findViewById(R.id.booksListView);
        booksListView.setAdapter(booksListViewAdaptor);
    }

    private String readSavedJSONDataFromDisk() {
        StringBuilder jsonDataStringBuilder = new StringBuilder();

        try {
            FileInputStream jsonFileInputStream = openFileInput("hardbackBooksTop20.dat");
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

    private void saveJSONDataToDisk(String dataToSave) {
        try {
            FileOutputStream jsonFileOutputStream = openFileOutput("hardbackBooksTop20.dat", Context.MODE_PRIVATE);
            jsonFileOutputStream.write(dataToSave.getBytes());
            jsonFileOutputStream.close();
            Log.v(TAG, "JSON Data Written to Disk");
        } catch (FileNotFoundException ex) {
            Log.e(TAG, "[Error writing file to disk] " + ex);
        } catch (IOException ex) {
            Log.e(TAG, "[Error writing data to file]" + ex);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Messenger nytJSONServiceMessenger = new Messenger(nytJSONServiceHandler);

        Intent nytJSONServiceIntent = new Intent(this, NYTJSONService.class);
        nytJSONServiceIntent.putExtra("messenger", nytJSONServiceMessenger);

        startService(nytJSONServiceIntent);
    }




}
