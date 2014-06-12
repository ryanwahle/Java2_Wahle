/*
    Author:     Ryan Wahle
    Date:       5 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ryanwahle.tophardbacks.NYTJSONService.NYTJSONService;
import com.ryanwahle.tophardbacks.Utility.DataStorageSingleton;
import com.ryanwahle.tophardbacks.Utility.InternetAccessSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    static String TAG = "MainActivity";
    ArrayList<HashMap<String, String>> booksArrayList = null;

    /*
        This is called when the service has completed letting us know to load the data.
     */
    final Handler nytJSONServiceHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.arg1 == RESULT_OK) {
                Log.v(TAG, "Service Finished Retrieving Data.");
                populateListView();
            }
        }
    };

    /*
        Read the data from the local JSON file and populate the listView and also
        add a header to the listView.
     */
    private void populateListView() {
        String localJSONDataString = DataStorageSingleton.getInstance().readSavedJSONDataFromDisk(this);
        booksArrayList = new ArrayList<HashMap<String, String>>();

        try {
            JSONObject nytJSONObject = new JSONObject(localJSONDataString);
            JSONArray bookResultsJSONArray = nytJSONObject.getJSONArray("results");

            /*
                Loop through the JSONArray and grab a couple of fields from the JSONObject and
                put them in a HashMap.
             */
            for (int arrayIndex = 0; arrayIndex < bookResultsJSONArray.length(); arrayIndex++) {
                String bookNameString = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("title");
                String bookAuthorString = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("author");
                String bookPublisher = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("publisher");
                String bookDescription = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("description");
                String bookISBN = bookResultsJSONArray.getJSONObject(arrayIndex).getJSONArray("book_details").getJSONObject(0).getString("primary_isbn13");

                HashMap<String, String> bookHashMap = new HashMap<String, String>();
                bookHashMap.put("bookName", bookNameString);
                bookHashMap.put("bookAuthor", "by " + bookAuthorString);
                bookHashMap.put("bookPublisher", bookPublisher);
                bookHashMap.put("bookDescription", bookDescription);
                bookHashMap.put("bookISBN", bookISBN);

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

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loadBookDetails(i - 1);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Messenger nytJSONServiceMessenger = new Messenger(nytJSONServiceHandler);

        Intent nytJSONServiceIntent = new Intent(this, NYTJSONService.class);
        nytJSONServiceIntent.putExtra("messenger", nytJSONServiceMessenger);

        /*
            Check to see if the internet is available. If it is then start the service.
            If it's not available then try and get local file, and if there is no
            local file then display a message telling the user no internet is available.
         */

        if (InternetAccessSingleton.getInstance().isInternetAvailable(this)) {
            startService(nytJSONServiceIntent);
        } else {
            if (DataStorageSingleton.getInstance().readSavedJSONDataFromDisk(this).isEmpty()) {
                Toast.makeText(this, "Enable internet access to use this app!", Toast.LENGTH_LONG).show();
            } else {
                populateListView();


            }
        }
    }

    public void loadBookDetails(int row) {
        HashMap<String, String> bookItemHashMap = booksArrayList.get(row);

        String bookName = bookItemHashMap.get("bookName").toString();
        String bookAuthor = bookItemHashMap.get("bookAuthor").toString();
        String bookPublisher = bookItemHashMap.get("bookPublisher").toString();
        String bookDescription = bookItemHashMap.get("bookDescription").toString();
        String bookISBN = bookItemHashMap.get("bookISBN").toString();

        Intent bookDetailsIntent = new Intent(this, BookDetailsActivity.class);

        bookDetailsIntent.putExtra("bookName", bookName);
        bookDetailsIntent.putExtra("bookAuthor", bookAuthor);
        bookDetailsIntent.putExtra("bookPublisher", bookPublisher);
        bookDetailsIntent.putExtra("bookDescription", bookDescription);
        bookDetailsIntent.putExtra("bookISBN", bookISBN);

        startActivityForResult(bookDetailsIntent, 0);
    }



}
