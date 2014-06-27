/*
    Author:     Ryan Wahle
    Date:       26 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ryanwahle.tophardbacks.Utility.DataStorageSingleton;

import java.util.ArrayList;
import java.util.HashMap;

/*
    This activity shows the average rating for books
 */
public class FavoritesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ArrayList<HashMap<String, Float>> bookRatingsArray = DataStorageSingleton.getInstance().readBookRatingsFromDisk(this);
        Log.v("RATING ARRAY DATA", bookRatingsArray.toString());

        Float averageRating = 0.0f;

        /*
            Add all the ratings together
         */
        for (HashMap<String, Float> bookRatingData : bookRatingsArray) {
            for (Float floatRating : bookRatingData.values()) {
                Log.v("float", floatRating.toString());
                averageRating = averageRating + floatRating;
            }
        }

        /*
            Get the average of all the ratings
         */
        averageRating = averageRating / bookRatingsArray.size();

        /*
            Show the average rating to the user.
         */
        TextView textViewAverageRating = (TextView) findViewById(R.id.textViewAverageRating);
        textViewAverageRating.setText(averageRating.toString() + " Stars!");
    }



}
