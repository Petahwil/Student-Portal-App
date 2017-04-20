package com.example.samjc.timesheetwebclient;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


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

    TextView startDateText;
    TextView endDateText;
    TextView numWeeksText;

    EditText startDateEditMonth;
    EditText startDateEditDay;
    EditText startDateEditYear;
    EditText endDateEditMonth;
    EditText endDateEditDay;
    EditText endDateEditYear;

    Button semseterDatesButton;
    Button newSemesterButton;

    EditText emailEdit;
    Button emailChange;

    String startDate;
    String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);

        //set up Firebase properties.
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        startDateEditMonth = (EditText) findViewById(R.id.start_date_month_edit);
        startDateEditDay = (EditText) findViewById(R.id.start_date_day_edit);
        startDateEditYear = (EditText) findViewById(R.id.start_date_year_edit);
        endDateEditMonth = (EditText) findViewById(R.id.end_date_month_edit);
        endDateEditDay = (EditText) findViewById(R.id.end_date_day_edit);
        endDateEditYear = (EditText) findViewById(R.id.end_date_year_edit);

        emailEdit = (EditText) findViewById(R.id.email_change);
        emailChange = (Button) findViewById(R.id.email_button);

        startDateText = (TextView) findViewById(R.id.start_date);
        endDateText = (TextView) findViewById(R.id.end_date);
        numWeeksText = (TextView) findViewById(R.id.num_weeks);

        semseterDatesButton = (Button) findViewById(R.id.edit_semester_button);
        newSemesterButton = (Button) findViewById(R.id.new_semester_button);

        //initalize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        //get data object
        data = (Data) tinydb.getObject("data", Data.class);

        //init semester data
        startDateText.setText("Start Date: " + data.ActiveSemester().getStartdate());
        endDateText.setText("End Date: " + data.ActiveSemester().getEnddate());
        numWeeksText.setText("Number of Weeks: " + data.ActiveSemester().numWeeks);

        //init email
        emailEdit.setHint("Enter Email - Current Email: "+data.getEmail());

        //email change listener
        emailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setEmail(emailEdit.getText().toString());

                mEmployeeDatabaseReference.child("Data").setValue(data);

                emailEdit.setText("");
                emailEdit.setHint("Enter Email - Current Email: "+data.getEmail());
            }
        });

        //create new semseter listener
        newSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewSemester();

                startDateText.setText("Start Date: " + data.ActiveSemester().getStartdate());
                endDateText.setText("End Date: " + data.ActiveSemester().getEnddate());
                numWeeksText.setText("Number of Weeks: " + data.ActiveSemester().numWeeks);
            }
        });

        //change current semseter dates
        semseterDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                Date start;
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

                try {
                    start = sdf.parse(data.ActiveSemester().getStartdate());

                    if(now.before(start)){
                        changeSemesterDates();
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "The semester you are trying to modify has already started. Please create a new semester", Toast.LENGTH_LONG).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                startDateText.setText("Start Date: " + data.ActiveSemester().getStartdate());
                endDateText.setText("End Date: " + data.ActiveSemester().getEnddate());
                numWeeksText.setText("Number of Weeks: " + data.ActiveSemester().numWeeks);
            }
        });

    }

    private void changeSemesterDates() {

        int startMonth = Integer.parseInt(startDateEditMonth.getText().toString());
        int startDay = Integer.parseInt(startDateEditDay.getText().toString());
        int startYear = Integer.parseInt(startDateEditYear.getText().toString());
        int endMonth = Integer.parseInt(endDateEditMonth.getText().toString());
        int endDay = Integer.parseInt(endDateEditDay.getText().toString());
        int endYear = Integer.parseInt(endDateEditYear.getText().toString());

        switch (startMonth) {
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
        switch (endMonth) {
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
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                start.setTime(sdf.parse(startDate));
                end.setTime(sdf.parse(endDate));
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Invalid date.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            ArrayList<Week> weeks = new ArrayList<Week>();
            Calendar tempCal = start;
            tempCal.add(Calendar.DATE, Calendar.FRIDAY - tempCal.get(Calendar.DAY_OF_WEEK) - 7);
            while (tempCal.before(end)) {
                start.add(Calendar.DATE, 7);
                weeks.add(new Week(sdf.format(tempCal.getTime())));
            }

            int numWeeks = weeks.size();

            data.ActiveSemester().setStartdate(startDate);
            data.ActiveSemester().setEnddate(endDate);
            data.ActiveSemester().setWeeks(weeks);
            data.ActiveSemester().setCurrentWeek(0);
            data.ActiveSemester().numWeeks = numWeeks;

            mEmployeeDatabaseReference.child("Data").setValue(data);

        }
    }

    void createNewSemester(){
        if (data.getActive().equals("Fall")) {
            data.setActive("Spring");
        } else {
            data.setActive("Fall");
        }

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
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            try {
                start.setTime(sdf.parse(startDate));
                end.setTime(sdf.parse(endDate));
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Invalid date.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


            ArrayList<Week> weeks = new ArrayList<Week>();
            Calendar tempCal = start;
            tempCal.add(Calendar.DATE, Calendar.FRIDAY - tempCal.get(Calendar.DAY_OF_WEEK) - 7);
            while (tempCal.before(end)) {
                start.add(Calendar.DATE, 7);
                weeks.add(new Week(sdf.format(tempCal.getTime())));
            }

            int numWeeks = weeks.size();

            data.ActiveSemester().setStartdate(startDate);
            data.ActiveSemester().setEnddate(endDate);
            data.ActiveSemester().setWeeks(weeks);
            data.ActiveSemester().setCurrentWeek(0);
            data.ActiveSemester().employees.clear();
            data.ActiveSemester().numWeeks = numWeeks;

            mEmployeeDatabaseReference.child("Data").setValue(data);
        }
    }
}
