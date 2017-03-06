package com.example.samjc.timesheetwebclient;

import android.content.Context;
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

public class PersonAdapter extends ArrayAdapter<Person>{

    public PersonAdapter(Context context, ArrayList<Person> items) {
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
        Log.d(getClass().getSimpleName(), currentObject.getClass().toString());

        Person currentPerson = (Person) currentObject;

        TextView nameView = (TextView) listItemView.findViewById(R.id.name_view);
        nameView.setText(currentPerson.getName());

        TextView emailView = (TextView) listItemView.findViewById(R.id.email_view);
        emailView.setText(currentPerson.getEmail());

        return listItemView;
    }
}
