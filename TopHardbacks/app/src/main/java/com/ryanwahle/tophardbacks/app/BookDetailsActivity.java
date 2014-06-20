/*
    Author:     Ryan Wahle
    Date:       12 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

/*
    This activity displays more book details to the user
    and allows them to buy the book on Amazon and rate
    the book.
 */
public class BookDetailsActivity extends Activity {

    TextView bookNameTextView = null;
    TextView bookAuthorTextView = null;
    TextView bookPublisherTextView = null;
    TextView bookISBNTextView = null;
    TextView bookDescriptionTextView = null;

    RatingBar bookRatingBar = null;

    Bundle sentData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        setContentView(R.layout.activity_book_details);

        /*
            Get the passed in data from the other activity
            and display it on screen.
        */
        sentData = getIntent().getExtras();
        if (sentData != null) {
            String bookName = sentData.getString("bookName");
            String bookAuthor = sentData.getString("bookAuthor");
            String bookPublisher = sentData.getString("bookPublisher");
            String bookISBN = sentData.getString("bookISBN");
            String bookDescription = sentData.getString("bookDescription");

            bookNameTextView = (TextView) findViewById(R.id.bookNameTextView);
            bookAuthorTextView = (TextView) findViewById(R.id.bookAuthorTextView);
            bookPublisherTextView = (TextView) findViewById(R.id.bookPublisherTextView);
            bookISBNTextView = (TextView) findViewById(R.id.bookISBNTextView);
            bookDescriptionTextView = (TextView) findViewById(R.id.bookDescriptionTextView);

            bookNameTextView.setText(bookName);
            bookAuthorTextView.setText(bookAuthor);
            bookPublisherTextView.setText("Published by " + bookPublisher);
            bookISBNTextView.setText("ISBN: " + bookISBN);
            bookDescriptionTextView.setText("\"" + bookDescription + "\"");
        }
    }

    /*
        Pass back the book rating to the calling activity.
    */
    @Override
    public void finish() {
        Log.v("BOOKS", "STARTING FINISH");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent bookRatingReturnDataIntent = new Intent();

            bookRatingBar = (RatingBar) findViewById(R.id.bookratingBar);
            bookRatingReturnDataIntent.putExtra("bookRating", bookRatingBar.getRating());

            bookRatingReturnDataIntent.putExtra("bookISBN", sentData.getString("bookISBN"));

            setResult(RESULT_OK, bookRatingReturnDataIntent);
        }
        Log.v("BOOKS", "FINISHED");
        super.finish();
    }

    /*
        Load up Amazon with the book via the ISBN number.
    */
    public void loadAmazonBookWebpage (View view) {
        String amazonBookURLString = "http://www.amazon.com/gp/search/ref=sr_adv_b/?search-alias=stripbooks&unfiltered=1&field-keywords=&field-author=&field-title=&field-isbn=" + bookISBNTextView.getText() + "&field-publisher=&node=&field-p_n_condition-type=&field-feature_browse-bin=&field-subject=&field-language=&field-dateop=During&field-datemod=&field-dateyear=&sort=relevanceexprank&Adv-Srch-Books-Submit.x=28&Adv-Srch-Books-Submit.y=8";
        Uri amazonBookUri = Uri.parse(amazonBookURLString);

        Intent amazonBookIntent = new Intent(Intent.ACTION_VIEW, amazonBookUri);

        startActivityForResult(amazonBookIntent, 0);

    }


}

/*
http://www.amazon.com/gp/search/ref=sr_adv_b/?search-alias=stripbooks&unfiltered=1&field-keywords=&field-author=&field-title=&field-isbn=9780399167317&field-publisher=&node=&field-p_n_condition-type=&field-feature_browse-bin=&field-subject=&field-language=&field-dateop=During&field-datemod=&field-dateyear=&sort=relevanceexprank&Adv-Srch-Books-Submit.x=28&Adv-Srch-Books-Submit.y=8
 */