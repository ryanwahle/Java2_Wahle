package com.ryanwahle.tophardbacks.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

/*
    This class loads up a dialog fragment.
 */
public class AlertDialogFragment extends DialogFragment {

    public static MainActivity.DialogType type;

    public AlertDialogFragment() {
        // Required empty public constructor
    }

    /*
        Set the dialog type and return a new class instance.
     */
    public static AlertDialogFragment newInstance(MainActivity.DialogType dialogType) {
        type = dialogType;

        return new AlertDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        /*
            Create a new dialog based on where the user clicked the preferences
            button or the search button.
         */
        switch (type) {
            case PREFERENCES:
                Log.v("DIALOG FRAGMENT", "PREFERENCES");
                builder.setView(inflater.inflate(R.layout.dialog_fragment_preferences, null))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText firstName = (EditText) getDialog().findViewById(R.id.editTextFirstName);
                                EditText lastName = (EditText) getDialog().findViewById(R.id.editTextLastName);

                                Log.v("FIRST NAME", firstName.getText().toString());

                                /*
                                    Set the first and last name to the Android Shared Preferences.
                                 */
                                MainActivity.editPreferences.putString("firstname", firstName.getText().toString());
                                MainActivity.editPreferences.putString("lastname", lastName.getText().toString());
                                MainActivity.editPreferences.apply();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialogFragment.this.getDialog().cancel();
                            }
                        });



                break;
            case SEARCH:
                Log.v("DIALOG FRAGMENT", "SEARCH");
                builder.setView(inflater.inflate(R.layout.dialog_fragment_search, null))
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText search = (EditText) getDialog().findViewById(R.id.editTextSearch);
                                        Log.v("SEARCH", "User searching for: " + search.getText().toString());

                                    }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialogFragment.this.getDialog().cancel();
                            }
                        });
                break;
        }

        return builder.create();
    }


}
