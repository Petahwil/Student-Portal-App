package com.example.dpivonka.timesheet;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import it.enricocandino.androidmail.MailSender;
import it.enricocandino.androidmail.model.Mail;
import it.enricocandino.androidmail.model.Recipient;

/**
 * Created by dpivonka on 3/1/17.
 */
public class Notification_reciver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {

        MailSender mailSender = new MailSender("timesheetautoemail@gmail.com", "AndroidPass7");
        Mail.MailBuilder builder = new Mail.MailBuilder();
        Mail mail = builder
                .setSender("timesheetautoemail@gmail.com")
                .addRecipient(new Recipient("dpivonka@comcast.net"))
                .setText("test")
                .build();

        mailSender.sendMail(mail);

    }
}
