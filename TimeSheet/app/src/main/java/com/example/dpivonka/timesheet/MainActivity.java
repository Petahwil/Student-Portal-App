package com.example.dpivonka.timesheet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.enricocandino.androidmail.MailSender;
import it.enricocandino.androidmail.model.Mail;
import it.enricocandino.androidmail.model.Recipient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test auto email system
        MailSender mailSender = new MailSender("timesheetautoemail@gmail.com", "AndroidPass6");
        Mail.MailBuilder builder = new Mail.MailBuilder();
        Mail mail = builder
                .setSender("timesheetautoemail@gmail.com")
                .addRecipient(new Recipient("dpivonka@comcast.net"))
                .setText("Hello")
                .build();

        mailSender.sendMail(mail);

    }
}
