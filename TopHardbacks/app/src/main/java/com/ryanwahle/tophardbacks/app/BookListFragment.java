/*
    Author:     Ryan Wahle
    Date:       19 June 2014
    School:     Full Sail University
    Class:      Java 2 1406
*/

package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/*
    This is the fragment class that lists the book data.
 */
public class BookListFragment extends Fragment implements ListView.OnItemClickListener {

    /*
        Require parent classes to implement a BookSelected method.
     */
    public interface BookSelectedListener {
        public void BookSelected(int row);
    }

    private BookSelectedListener bookSelectedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        /*
            If the parent activity implements this class' interface then create
            a variable pointing back to it, otherwise generate an error.
         */
        if (activity instanceof BookSelectedListener) {
            bookSelectedListener = (BookSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + "must implement BookListFragment.BookSelectedListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        ListView booksListView = (ListView) view.findViewById(R.id.booksListView);

        /*
            Set the onItemClick to this class which we implemented.
         */
        booksListView.setOnItemClickListener(this);

        return view;
    }

    /*
        If a book is selected by a user, pass back the row to the parent
        activity.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        bookSelectedListener.BookSelected(i - 1);
    }
}