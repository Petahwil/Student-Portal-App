package com.example.samjc.timesheetwebclient;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddSemesterActivity extends AppCompatActivity{

    /**
     * The {@link FirebaseDatabase} holding the Firebase database.
     */
    private FirebaseDatabase mFirebaseDatabase;
    /**
     * The {@link DatabaseReference} to the employees in the Firebase database.
     */
    private DatabaseReference mEmployeeDatabaseReference;

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

        //set up Firebase properties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

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

        if (data.getActive().equals("fall")) {
            data.setActive("spring");
        } else {
            data.setActive("fall");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy", Locale.US);
                    try {
                        start.setTime(sdf.parse(startDate));
                        end.setTime(sdf.parse(endDate));
                    } catch (ParseException e) {
                        Toast.makeText(getApplicationContext(), "Invalid date.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    int numWeeks = end.get(Calendar.WEEK_OF_YEAR) - start.get(Calendar.WEEK_OF_YEAR);

                    ArrayList<Week> weeks = new ArrayList<Week>();
                    Calendar tempCal = start;
                    tempCal.add(Calendar.DATE, Calendar.FRIDAY - tempCal.get(Calendar.DAY_OF_WEEK) - 7);
                    while (tempCal.before(end)) {
                        start.add(Calendar.DATE, 7);
                        weeks.add(new Week(sdf.format(tempCal.getTime())));
                    }

                    data.ActiveSemester().setStartdate(startDate);
                    data.ActiveSemester().setEnddate(endDate);
                    data.ActiveSemester().setWeeks(weeks);
                    data.ActiveSemester().setCurrentWeek(0);
                    data.ActiveSemester().employees.clear();
                    data.ActiveSemester().numWeeks = numWeeks;

                    mEmployeeDatabaseReference.child("Data").setValue(data);

                    startActivity(new Intent(this, MainActivity.class));
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
