package com.ryanwahle.tophardbacks.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class BookListFragment extends Fragment implements ListView.OnItemClickListener {

    public interface BookSelectedListener {
        public void BookSelected(int row);
    }

    private BookSelectedListener bookSelectedListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof BookSelectedListener) {
            bookSelectedListener = (BookSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + "must implement BookListFragment.BookSelectedListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        ListView booksListView = (ListView) view.findViewById(R.id.booksListView);
        booksListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        bookSelectedListener.BookSelected(i - 1);
    }
}