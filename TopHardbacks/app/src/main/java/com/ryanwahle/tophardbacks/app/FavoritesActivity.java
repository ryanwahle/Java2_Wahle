package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ryanwahle.tophardbacks.Utility.DataStorageSingleton;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoritesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ArrayList<HashMap<String, Float>> bookRatingsArray = DataStorageSingleton.getInstance().readBookRatingsFromDisk(this);
        Log.v("RATING ARRAY DATA", bookRatingsArray.toString());

        Float averageRating = 0.0f;

        for (HashMap<String, Float> bookRatingData : bookRatingsArray) {
            for (Float floatRating : bookRatingData.values()) {
                Log.v("float", floatRating.toString());
                averageRating = averageRating + floatRating;
            }
        }

        averageRating = averageRating / bookRatingsArray.size();

        TextView textViewAverageRating = (TextView) findViewById(R.id.textViewAverageRating);
        textViewAverageRating.setText(averageRating.toString() + " Stars!");
    }



}
