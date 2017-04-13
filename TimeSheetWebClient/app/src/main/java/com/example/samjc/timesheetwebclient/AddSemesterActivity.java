package com.example.samjc.timesheetwebclient;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddSemesterActivity extends AppCompatActivity{

    private Data data;
    private TinyDB tinydb;

    EditText startDateEditMonth;
    EditText startDateEditDay;
    EditText startDateEditYear;
    EditText endDateEditMonth;
    EditText endDateEditDay;
    EditText endDateEditYear;

    String startDate;
    String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);

        startDateEditMonth = (EditText) findViewById(R.id.start_date_month_edit);
        startDateEditDay = (EditText) findViewById(R.id.start_date_day_edit);
        startDateEditYear = (EditText) findViewById(R.id.start_date_year_edit);
        endDateEditMonth = (EditText) findViewById(R.id.end_date_month_edit);
        endDateEditDay = (EditText) findViewById(R.id.end_date_day_edit);
        endDateEditYear = (EditText) findViewById(R.id.end_date_year_edit);

        //initalize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        //get data object
        data = (Data) tinydb.getObject("data", Data.class);

        if (data.getActive().equals("Fall")) {
            data.setActive("Spring");
        } else {
            data.setActive("Fall");
        }

//        how to create a new semester
//
//        change active field in data class
//
//        set non-active semester to new semester object containing the following
//
//        current week = 0
//        employees should be empty
//        set start and end dates
//        calculate number of weeks
//        create list of weeks each week needs it enddate set calculate that date based off start and enddate


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                int startMonth = Integer.parseInt(startDateEditMonth.getText().toString());
                int startDay = Integer.parseInt(startDateEditDay.getText().toString());
                int startYear = Integer.parseInt(startDateEditYear.getText().toString());
                int endMonth = Integer.parseInt(endDateEditMonth.getText().toString());
                int endDay = Integer.parseInt(endDateEditDay.getText().toString());
                int endYear = Integer.parseInt(endDateEditYear.getText().toString());

                switch(startMonth) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        if (startDay < 1 || startDay > 31) {
                            startDate = "ERROR";
                        } else {
                            startDate = startMonth + "/" + startDay + "/" + startYear;
                        }
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        if (startDay < 1 || startDay > 30) {
                            startDate = "ERROR";
                        } else {
                            startDate = startMonth + "/" + startDay + "/" + startYear;
                        }
                        break;
                    case 2:
                        if (startDay < 1 || startDay > 29) {
                            startDate = "ERROR";
                        } else {
                            startDate = startMonth + "/" + startDay + "/" + startYear;
                        }
                        break;
                    default:
                        startDate = "ERROR";
                        break;
                }
                switch(endMonth) {
                    case 1:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 10:
                    case 12:
                        if (endDay < 1 || endDay > 31) {
                            endDate = "ERROR";
                        } else {
                            endDate = endMonth + "/" + endDay + "/" + endYear;
                        }
                        break;
                    case 4:
                    case 6:
                    case 9:
                    case 11:
                        if (endDay < 1 || endDay > 30) {
                            endDate = "ERROR";
                        } else {
                            endDate = endMonth + "/" + endDay + "/" + endYear;
                        }
                        break;
                    case 2:
                        if (endDay < 1 || endDay > 29) {
                            endDate = "ERROR";
                        } else {
                            endDate = endMonth + "/" + endDay + "/" + endYear;
                        }
                        break;
                    default:
                        endDate = "ERROR";
                        break;
                }

                if (startDate.equals("ERROR") || endDate.equals("ERROR")) {
                    Toast.makeText(getApplicationContext(), "Invalid date.", Toast.LENGTH_LONG).show();
                } else {
                    //add semester to database
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
