package com.example.dpivonka.timesheet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


import it.enricocandino.androidmail.MailSender;
import it.enricocandino.androidmail.model.Mail;
import it.enricocandino.androidmail.model.Recipient;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by dpivonka on 3/9/17.
 */

public class EmailSystem {

    static void SetAlarm(Context context){
        Calendar calendar = Calendar.getInstance();
        //alarm occurs at 6PM
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        //create intent and set alarm manager
        Intent intent = new Intent(context,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
    }

    static void EmailAdmin(ArrayList<User> userList){
        ArrayList<String> signed = new ArrayList<>();
        ArrayList<String> notsigned = new ArrayList<>();

        for (User user : userList) {
            if(user.signed==true){
                signed.add(user.getUsername());
            }else{//false
                notsigned.add(user.getUsername());
            }
        }

        String email = new String();

        email = "Not Signed\n\n";


        for (String s : notsigned) {
            email += s+"\n";
        }

        email += "\n\nSigned\n\n";

        for (String s : signed) {
            email += s+"\n";
        }

        MailSender mailSender = new MailSender("timesheetautoemail@gmail.com", "AndroidPass7");
        Mail.MailBuilder builder = new Mail.MailBuilder();
        Mail mail = builder
                .setSender("timesheetautoemail@gmail.com")
                .addRecipient(new Recipient("dpivonka@comcast.net"))
                .setSubject("Weekly Signatures")
                .setText(email)
                .build();
        mailSender.sendMail(mail);

        //reset all signed values for this week now that the email has sent
        if(!userList.isEmpty()){
            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mEmployeeDatabaseReference = mFirebaseDatabase.getReference().child("employees");
            mEmployeeDatabaseReference.removeValue();
            mEmployeeDatabaseReference.setValue("employees");
            mEmployeeDatabaseReference.child("employees");
            for (User user : userList) {
                user.signed=false;
                mEmployeeDatabaseReference.push().setValue(user);
            }
        }
    }

    static void EmailReminder(ArrayList<User> userList){

        for (User user : userList) {
            if(user.signed==false){
                MailSender mailSender = new MailSender("timesheetautoemail@gmail.com", "AndroidPass7");
                Mail.MailBuilder builder = new Mail.MailBuilder();
                Mail mail = builder
                        .setSender("timesheetautoemail@gmail.com")
                        .addRecipient(new Recipient(user.email))
                        .setSubject("TimeSheet Reminder")
                        .setText("You have not signed your timesheet for this week. Tomorrow is the last day to sign for this week.\n")
                        .build();
                mailSender.sendMail(mail);
            }
        }


    }

}
