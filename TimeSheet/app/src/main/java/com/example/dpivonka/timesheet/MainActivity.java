package com.example.dpivonka.timesheet;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

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

        SignaturePad mSignaturePad;
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });

        final Button button = (Button) findViewById(R.id.button);

        //if button is clicked
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "Signature has been submitted.", Toast.LENGTH_LONG).show();
            }
        });

    }
}
