package com.example.dpivonka.timesheet;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by dpivonka on 3/1/17.
 */
public class AlarmReceiver extends BroadcastReceiver{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEmployeeDatabaseReference;

    @Override
    public void onReceive(Context context, Intent intent) {

        //initalize firebase reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        //get tinyDB reference
        TinyDB tinydb = new TinyDB(context);
        Data data = (Data) tinydb.getObject("data", Data.class);

        //check what week it is and update if nessasary
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date endDate = null;
        try {
            endDate = sdf.parse(data.ActiveSemester().weeks.get(data.ActiveSemester().currentWeek).getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(endDate)) {
            for(int x = data.ActiveSemester().currentWeek; x < data.ActiveSemester().weeks.size(); x++){
                try {
                    endDate = sdf.parse(data.ActiveSemester().weeks.get(x).getEndDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(new Date().before(endDate)){
                    data.ActiveSemester().setCurrentWeek(x);

                    //update tiny db
                    tinydb.putObject("data", data);

                    //push changes to firebase
                    mEmployeeDatabaseReference.child("Data").setValue(data);

                    break;
                }
            }
        }

        //fill userList with data from tinyDB
        ArrayList<User> userList = new ArrayList<>();
        for (User u : data.ActiveSemester().employees) {

            boolean signed = false;
            for(User u1: data.ActiveSemester().getWeeks().get(data.ActiveSemester().currentWeek).employees){
                if(u1.getUsername().equals(u.getUsername())){
                    signed = u1.isSigned();
                }
            }

            userList.add(new User(u.getUsername(), u.getEmail(), signed));
        }

        //get day and hour
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        //if the hour is 6pm
        if(hour == 18){
            //check day
            switch (day){
                case Calendar.FRIDAY:
                    EmailSystem.EmailAdmin(userList, data.getEmail());
                    break;
                case Calendar.THURSDAY:
                    EmailSystem.EmailReminder(userList);
                    break;
                default:
                    //No task scheduled for this day
            }
        }
    }
}
