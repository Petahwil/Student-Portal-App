package com.example.samjc.timesheetwebclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
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

    //region MEMBER VARIABLES
    //region FIREBASE
    /**
     * The {@link FirebaseDatabase} holding the Firebase database.
     */
    private FirebaseDatabase mFirebaseDatabase;
    /**
     * The {@link DatabaseReference} to the employees in the Firebase database.
     */
    private DatabaseReference mEmployeeDatabaseReference;
    /**
     * The {@link TinyDB} holding a reference to Firebase.
     */
    private TinyDB tinydb;
    /**
     * The {@link Data} holding all information for both semesters.
     */
    Data data;
    //endregion
    //region UI ELEMENTS
    /**
     * The {@link TextView} showing the start date of the current semester.
     */
    TextView startDateText;
    /**
     * The {@link TextView} showing the end date of the current semester.
     */
    TextView endDateText;
    /**
     * The {@link TextView} showing the number of weeks in the current semester.
     */
    TextView numWeeksText;
    TextView currentEmailText;

    /**
     * The {@link EditText} for the start date month.
     */
    EditText startDateEditMonth;
    /**
     * The {@link EditText} for the start date day.
     */
    EditText startDateEditDay;
    /**
     * The {@link EditText} for the start date year.
     */
    EditText startDateEditYear;
    /**
     * The {@link EditText} for the end date month.
     */
    EditText endDateEditMonth;
    /**
     * The {@link EditText} for the end date day.
     */
    EditText endDateEditDay;
    /**
     * The {@link EditText} for the end date year.
     */
    EditText endDateEditYear;

    /**
     * The {@link Button} to change current semester's date.
     */
    Button semesterDatesButton;
    /**
     * The {@link Button} to create a new semester with the given dates.
     */
    Button newSemesterButton;

    /**
     * The {@link EditText} where user enter's the admin's email address.
     */
    EditText emailEdit;
    /**
     * The {@link Button} that update's the admin's email address in Firebase.
     */
    Button emailChange;

    /**
     * The {@link String} holding the start date for the semester.
     */
    String startDate;
    /**
     * The {@link String} holding the end date for the semester.
     */
    String endDate;
    //endregion
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);

        // Set up Firebase properties.
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        // Set up UI elements.
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
        currentEmailText = (TextView) findViewById(R.id.curr_email);

        semesterDatesButton = (Button) findViewById(R.id.edit_semester_button);
        newSemesterButton = (Button) findViewById(R.id.new_semester_button);

        // Initialize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        // Get data object
        data = (Data) tinydb.getObject("data", Data.class);


        // Initialize information display.
        String sDate = "Start Date: " + data.ActiveSemester().getStartdate();
        String eDate = "End Date: " + data.ActiveSemester().getEnddate();
        String nWeeks = "Number of Weeks: " + data.ActiveSemester().numWeeks;
        String cEmail = "Admin Email: " + data.getEmail();
        startDateText.setText(sDate);
        endDateText.setText(eDate);
        numWeeksText.setText(nWeeks);
        currentEmailText.setText(cEmail);

        // Initialize email hint.
        emailEdit.setHint("Enter Email");

        // Email change listener
        emailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdit.getText().toString();
                if ((TextUtils.isEmpty(email)) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Invalid or empty \"Email Address\" field.", Toast.LENGTH_LONG).show();
                } else {
                    data.setEmail(emailEdit.getText().toString());

                    mEmployeeDatabaseReference.child("Data").setValue(data);

                    emailEdit.setText("");
                    emailEdit.setHint("Enter Email - Current Email: "+data.getEmail());
                }
            }
        });

        // Create new semester
        newSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddSemesterActivity.this);
                builder.setTitle("Create new semester?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createNewSemester();

                        // Update information display.
                        String sDate = "Start Date: " + data.ActiveSemester().getStartdate();
                        String eDate = "End Date: " + data.ActiveSemester().getEnddate();
                        String nWeeks = "Number of Weeks: " + data.ActiveSemester().numWeeks;
                        startDateText.setText(sDate);
                        endDateText.setText(eDate);
                        numWeeksText.setText(nWeeks);

                        startActivity(new Intent(AddSemesterActivity.this, MainActivity.class));
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        // Change current semester dates
        semesterDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddSemesterActivity.this);
                builder.setTitle("Change semester dates?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //check if semester has started
                        Calendar now = Calendar.getInstance();
                        Date start;
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
                        try {
                            start = sdf.parse(data.ActiveSemester().getStartdate());

                            if(now.before(start)){
                                changeSemesterDates();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "The semester you are trying to modify has already started." +
                                                "Please create a new semester.",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // Update information display.
                        String sDate = "Start Date: " + data.ActiveSemester().getStartdate();
                        String eDate = "End Date: " + data.ActiveSemester().getEnddate();
                        String nWeeks = "Number of Weeks: " + data.ActiveSemester().numWeeks;
                        startDateText.setText(sDate);
                        endDateText.setText(eDate);
                        numWeeksText.setText(nWeeks);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    /**
     * Change dates of current semester.
     */
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

    /**
     * Create a new semester with given dates.
     */
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
