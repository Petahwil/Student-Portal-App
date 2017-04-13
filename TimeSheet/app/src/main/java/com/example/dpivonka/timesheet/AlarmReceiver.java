package com.example.dpivonka.timesheet;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by dpivonka on 3/1/17.
 */
public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //get tinyDB reference
        TinyDB tinydb = new TinyDB(context);

        Data data = (Data) tinydb.getObject("data", Data.class);

        ArrayList<User> userList = new ArrayList<>();

        //fill userList with data from tinyDB
        for (User u : data.ActiveSemester().employees) {
            userList.add(u);
        }

        Calendar calendar = Calendar.getInstance();

        //get day and hour
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        //if the hour is 6pm
        if(hour == 18){
            //check day
            switch (day){
                case Calendar.FRIDAY:
                    EmailSystem.EmailAdmin(userList);
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
