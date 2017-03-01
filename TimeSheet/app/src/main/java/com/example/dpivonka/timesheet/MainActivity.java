package com.example.dpivonka.timesheet;

<<<<<<< HEAD
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
=======
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
>>>>>>> dbbe6aac3bdfa7c04e068e9330e06da958c50f3e

import it.enricocandino.androidmail.MailSender;
import it.enricocandino.androidmail.model.Mail;
import it.enricocandino.androidmail.model.Recipient;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private EditText mNameEditText;
    private DatabaseReference mMessagesDatabaseReference;
    private Button mSendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mNameEditText = (EditText)findViewById(R.id.nameText);
        mSendButton = (Button) findViewById(R.id.sigButton);
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Signature");


        //weekly email system
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
        calendar.set(Calendar.HOUR_OF_DAY,18);
        Intent intent = new Intent(getApplicationContext(),Notification_reciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, pendingIntent);






        // Enable Send button when there's text to send
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send messages on click to DB
                FriendlyMessage friendlyMessage = new FriendlyMessage(mNameEditText.getText().toString());
                mMessagesDatabaseReference.push().setValue(friendlyMessage);
                // Clear input box
                mNameEditText.setText("");
            }
        });

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
