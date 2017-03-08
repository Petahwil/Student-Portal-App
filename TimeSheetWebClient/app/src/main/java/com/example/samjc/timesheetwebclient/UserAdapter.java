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

    public UserAdapter(MainActivity context, ArrayList<User> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_info, parent, false);
        }

        Object currentObject = getItem(position);

        User currentUser = (User) currentObject;

        TextView nameView = (TextView) listItemView.findViewById(R.id.name_view);
        nameView.setText(currentUser.username);

        TextView emailView = (TextView) listItemView.findViewById(R.id.email_view);
        emailView.setText(currentUser.email);

        return listItemView;
    }
}
