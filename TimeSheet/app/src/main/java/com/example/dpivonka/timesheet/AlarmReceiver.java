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

    private ArrayList<User> userList = new ArrayList<>();
    ArrayList<String> signed = new ArrayList<>();
    ArrayList<String> notsigned = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        TinyDB tinydb = new TinyDB(context);
        ArrayList<User> userList = new ArrayList<>();

        for (Object object : tinydb.getListObject("MyUsers", User.class)) {
            userList.add((User) object);
        }

        Calendar calendar = Calendar.getInstance();

        Calendar timer  = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);


        if(hour == 18){
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
