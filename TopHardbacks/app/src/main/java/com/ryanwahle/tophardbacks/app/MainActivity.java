/*
    Author:     Ryan Wahle
    Date:       12 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ryanwahle.tophardbacks.NYTJSONService.NYTJSONService;
import com.ryanwahle.tophardbacks.Utility.DataStorageSingleton;
import com.ryanwahle.tophardbacks.Utility.InternetAccessSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity implements BookListFragment.BookSelectedListener {

    static String TAG = "MainActivity";
    ArrayList<HashMap<String, String>> booksArrayList = null;
    ArrayList<HashMap<String, Float>> bookRatingsArrayList = null;

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
    }

    @Override
    public void BookSelected(int row) {
        loadBookDetails(row);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Messenger nytJSONServiceMessenger = new Messenger(nytJSONServiceHandler);

        Intent nytJSONServiceIntent = new Intent(this, NYTJSONService.class);
        nytJSONServiceIntent.putExtra("messenger", nytJSONServiceMessenger);

        /*
            If there is saved state instance then use that data instead of
            getting it from disk or the internet.
        */
        if (savedInstanceState != null) {
            booksArrayList = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("savedInstanceData");
            Log.v(TAG, "POPULATING WITH SAVED DATA");
            populateListView();
        } else {
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

    }

    /*
        Save any data needed to a saved state instance.
    */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (booksArrayList != null && !booksArrayList.isEmpty()) {
            savedInstanceState.putSerializable("savedInstanceData", (Serializable) booksArrayList);
        }
    }

    /*
        Load up the Book Details Activity and send the book data.
    */
    public void loadBookDetails(int row) {
        HashMap<String, String> bookItemHashMap = booksArrayList.get(row);

        String bookName = bookItemHashMap.get("bookName");
        String bookAuthor = bookItemHashMap.get("bookAuthor");
        String bookPublisher = bookItemHashMap.get("bookPublisher");
        String bookDescription = bookItemHashMap.get("bookDescription");
        String bookISBN = bookItemHashMap.get("bookISBN");

        Intent bookDetailsIntent = new Intent(this, BookDetailsActivity.class);

        bookDetailsIntent.putExtra("bookName", bookName);
        bookDetailsIntent.putExtra("bookAuthor", bookAuthor);
        bookDetailsIntent.putExtra("bookPublisher", bookPublisher);
        bookDetailsIntent.putExtra("bookDescription", bookDescription);
        bookDetailsIntent.putExtra("bookISBN", bookISBN);

        startActivityForResult(bookDetailsIntent, 0);
    }

    /*
        This is called after the Book Details Activity is finished. Find the book
        rating and save it to disk, then show an alert to the user stating it has
        been saved.
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 0) {
            Bundle bookRatingResultBundle = data.getExtras();

            if (bookRatingsArrayList == null) {
                bookRatingsArrayList = new ArrayList<HashMap<String, Float>>();
            }

            HashMap<String, Float> bookRatingHashMap = new HashMap<String, Float>();
            bookRatingHashMap.put(bookRatingResultBundle.getString("bookISBN"), bookRatingResultBundle.getFloat("bookRating"));

            bookRatingsArrayList.add(bookRatingHashMap);

            DataStorageSingleton.getInstance().saveBookRatingsToDisk(this, bookRatingsArrayList);
            showAlert("You rated this book " + bookRatingResultBundle.getFloat("bookRating") + " Stars!");
        }
    }

    /*
        Show an alert to the user based on the string inputted.
    */
    private void showAlert (String alertText) {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle("Rating Saved To Disk");
        alertBox.setMessage(alertText);
        alertBox.setPositiveButton("OK", null);
        alertBox.setCancelable(false);
        alertBox.create().show();
    }
}
