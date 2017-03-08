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
import java.util.Collections;
import java.util.Comparator;

import it.enricocandino.androidmail.MailSender;
import it.enricocandino.androidmail.model.Attachment;
import it.enricocandino.androidmail.model.Mail;
import it.enricocandino.androidmail.model.Recipient;

/**
 * Created by dpivonka on 3/1/17.
 */
public class Notification_reciver extends BroadcastReceiver{

    private ArrayList<User> userList = new ArrayList<>();
    ArrayList<String> signed = new ArrayList<>();
    ArrayList<String> notsigned = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mEmployeeDatabaseReference = mFirebaseDatabase.getReference().child("employees");
        //get users data everytime it changes
        mEmployeeDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userList.add(user);
                }

                if (!userList.isEmpty()) {
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User c1, User c2) {
                            //You should ensure that list doesn't contain null values!
                            return c1.getUsername().compareTo(c2.getUsername());
                        }
                    });
                }

                for (User user : userList) {
                    if(user.signed==true){
                        signed.add(user.getUsername());
                    }else{//false
                        notsigned.add(user.getUsername());
                    }
                }

                String email = new String();

                email = "Not Signed\n\n";


                for (String s : signed) {
                    email += s+"\n";
                }

                email += "\n\nSigned\n\n";

                for (String s : notsigned) {
                    email += s+"\n";
                }

                MailSender mailSender = new MailSender("timesheetautoemail@gmail.com", "AndroidPass7");
                Mail.MailBuilder builder = new Mail.MailBuilder();
                Mail mail = builder
                        .setSender("timesheetautoemail@gmail.com")
                        .addRecipient(new Recipient("dpivonka@comcast.net"))
                        .setText("testing auto email system\n\n"+email)
                        .build();
                mailSender.sendMail(mail);

            }

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }
}
