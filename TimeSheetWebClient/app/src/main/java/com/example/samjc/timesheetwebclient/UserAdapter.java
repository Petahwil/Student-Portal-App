package com.example.samjc.timesheetwebclient;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class UserAdapter extends ArrayAdapter<User>{

    //region MEMBER VARIABLES
    /**
     * The {@link View} for the list item.
     */
    private View listItemView;
    /**
     * The instance of {@link User} that holds the information for the current user.
     */
    private User currentUser;

    //region UI FIELDS
    /**
     * The {@link TextView} that will display the current user's name.
     */
    private TextView nameView;
    /**
     * The {@link TextView} that will display the current user's email address.
     */
    private TextView emailView;
    //endregion
    //endregion

    /**
     * Constructor for {@link UserAdapter}.
     * @param context The {@link MainActivity} context for the MainActivity.
     * @param items The {@link ArrayList<User>} holding the list of users to be adapted into the ListView in MainActivity.
     */
    public UserAdapter(MainActivity context, ArrayList<User> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_info, parent, false);
        }

        // Get the item from the list and cast it to type User.
        currentUser = (User) getItem(position);

        // Initialize UI fields.
        nameView = (TextView) listItemView.findViewById(R.id.name_view);
        emailView = (TextView) listItemView.findViewById(R.id.email_view);

        // Set text in UI fields.
        nameView.setText(currentUser.username);
        emailView.setText(currentUser.email);

        return listItemView;
    }
}
