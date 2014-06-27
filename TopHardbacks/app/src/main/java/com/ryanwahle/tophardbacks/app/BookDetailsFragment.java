/*
    Author:     Ryan Wahle
    Date:       26 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
    This loads the fragment_book_details and returns the view
    to be displayed on the screen.
 */
public class BookDetailsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false);
    }
}
