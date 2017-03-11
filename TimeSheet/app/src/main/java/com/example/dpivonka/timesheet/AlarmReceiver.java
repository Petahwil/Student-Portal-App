package com.example.dpivonka.timesheet;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import it.enricocandino.androidmail.MailSender;
import it.enricocandino.androidmail.model.Attachment;
import it.enricocandino.androidmail.model.Mail;
import it.enricocandino.androidmail.model.Recipient;

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
        int day = calendar.get(Calendar.DAY_OF_WEEK);
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
