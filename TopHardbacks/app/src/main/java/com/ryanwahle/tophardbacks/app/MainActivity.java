package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ryanwahle.tophardbacks.Utility.DataStorageSingleton;
import com.ryanwahle.tophardbacks.NYTJSONService.NYTJSONService;
import com.ryanwahle.tophardbacks.Utility.InternetAccessSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    static String TAG = "MainActivity";

    final Handler nytJSONServiceHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.arg1 == RESULT_OK) {
                Log.v(TAG, "Service Finished Retrieving Data.");
                populateListView();
            }
        }
    };

    private void populateListView() {
        String localJSONDataString = DataStorageSingleton.getInstance().readSavedJSONDataFromDisk(this);
        ArrayList<HashMap<String, String>> booksArrayList = new ArrayList<HashMap<String, String>>();

        try {
            JSONObject nytJSONObject = new JSONObject(localJSONDataString);
            JSONArray bookResultsJSONArray = nytJSONObject.getJSONArray("results");

            for (int arrayIndex = 0; arrayIndex < bookResultsJSONArray.length(); arrayIndex++) {
                String bookNameString = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("title");
                String bookAuthorString = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("author");
                String bookPublisher = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("publisher");

                HashMap<String, String> bookHashMap = new HashMap<String, String>();
                bookHashMap.put("bookName", bookNameString);
                bookHashMap.put("bookAuthor", "by " + bookAuthorString);
                bookHashMap.put("bookPublisher", bookPublisher);

                booksArrayList.add(bookHashMap);
            }

            //Log.v(TAG, "" + bookResultsJSONArray.toString());
        } catch (JSONException ex) {
            Log.e(TAG, "Error Parsing JSON Data: ", ex);
        }

        SimpleAdapter booksListViewAdaptor = new SimpleAdapter(this, booksArrayList, R.layout.activity_main_listview_row, new String[] {"bookName", "bookAuthor", "bookPublisher"}, new int[] {R.id.bookName, R.id.bookAuthor, R.id.bookPublisher});

        ListView booksListView = (ListView) findViewById(R.id.booksListView);

        booksListView.addHeaderView(getLayoutInflater().inflate(R.layout.activity_listview_header, null));
        booksListView.setAdapter(booksListViewAdaptor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Messenger nytJSONServiceMessenger = new Messenger(nytJSONServiceHandler);

        Intent nytJSONServiceIntent = new Intent(this, NYTJSONService.class);
        nytJSONServiceIntent.putExtra("messenger", nytJSONServiceMessenger);

        if (InternetAccessSingleton.getInstance().isInternetAvailable(this)) {
            Log.v(TAG, "Internet Access Available");
            startService(nytJSONServiceIntent);
        }
    }




}
