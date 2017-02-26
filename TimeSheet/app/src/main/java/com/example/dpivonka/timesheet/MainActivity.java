package com.example.dpivonka.timesheet;

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
import java.util.List;

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

        //test auto email system
        MailSender mailSender = new MailSender("timesheetautoemail@gmail.com", "AndroidPass6");
        Mail.MailBuilder builder = new Mail.MailBuilder();
        Mail mail = builder
                .setSender("timesheetautoemail@gmail.com")
                .addRecipient(new Recipient("dpivonka@comcast.net"))
                .setText("Hello")
                .build();

        mailSender.sendMail(mail);
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

    }

}
