package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class BookDetailsActivity extends Activity {

    TextView bookNameTextView = null;
    TextView bookAuthorTextView = null;
    TextView bookPublisherTextView = null;
    TextView bookISBNTextView = null;
    TextView bookDescriptionTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Bundle sentData = getIntent().getExtras();
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

    public void loadAmazonBookWebpage (View view) {
        String amazonBookURLString = "http://www.amazon.com/gp/search/ref=sr_adv_b/?search-alias=stripbooks&unfiltered=1&field-keywords=&field-author=&field-title=&field-isbn=9780399167317&field-publisher=&node=&field-p_n_condition-type=&field-feature_browse-bin=&field-subject=&field-language=&field-dateop=During&field-datemod=&field-dateyear=&sort=relevanceexprank&Adv-Srch-Books-Submit.x=28&Adv-Srch-Books-Submit.y=8";
        Uri amazonBookUri = Uri.parse(amazonBookURLString);

        Intent amazonBookIntent = new Intent(Intent.ACTION_VIEW, amazonBookUri);
        if (amazonBookIntent.resolveActivity(getPackageManager()) != null) {
            startService(amazonBookIntent);
        }
    }
}

/*
http://www.amazon.com/gp/search/ref=sr_adv_b/?search-alias=stripbooks&unfiltered=1&field-keywords=&field-author=&field-title=&field-isbn=9780399167317&field-publisher=&node=&field-p_n_condition-type=&field-feature_browse-bin=&field-subject=&field-language=&field-dateop=During&field-datemod=&field-dateyear=&sort=relevanceexprank&Adv-Srch-Books-Submit.x=28&Adv-Srch-Books-Submit.y=8
 */