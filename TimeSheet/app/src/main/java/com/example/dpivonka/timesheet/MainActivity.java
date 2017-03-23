package com.example.dpivonka.timesheet;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.github.gcacace.signaturepad.views.SignaturePad;


import android.text.Editable;
import android.text.TextWatcher;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private EditText mNameEditText;
    private DatabaseReference mEmployeeDatabaseReference;
    private Button mSendButton;
    private Button mClearButton;
    private SignaturePad mSignaturePad;
    private AutoCompleteTextView actv;

    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<String> userNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initalize firebase reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();



        Data data = new Data();
        Week week = new Week();
        User user = new User();
        user.username="steve";
        user.email="adsfdf@sfdg.com";
        user.Ta_Ra="TA";
        user.advisor="mr.smith";
        user.code="abc123";
        user.signed=false;

        week.employees.add(user);

        user.username="john";
        user.email="asdfgdffdf@sfdg.com";
        user.Ta_Ra="RA";
        user.advisor="mr.chen";
        user.code="bfg354";
        user.signed=true;

        week.employees.add(user);

        data.fall.weeks.add(week);
        data.fall.weeks.add(week);

        data.spring.weeks.add(week);



        data.active="fall";

        mEmployeeDatabaseReference.push().setValue(data);





        mEmployeeDatabaseReference = mEmployeeDatabaseReference.child("employees");

        //initalize tinyDB
        final TinyDB tinydb = new TinyDB(getApplicationContext());

        //get users data everytime it changes
        mEmployeeDatabaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userList.add(user);
                    userNames.add(user.getUsername());
                }
                tinydb.putListObject("MyUsers", userList);
            }
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        //weekly email system
        EmailSystem.SetAlarm(getApplicationContext());


        mNameEditText = (EditText)findViewById(R.id.editText);
        mSendButton = (Button) findViewById(R.id.sigButton);
        mClearButton = (Button) findViewById(R.id.clear_button);
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        actv = (AutoCompleteTextView) findViewById(R.id.editText);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,userNames);
        actv.setAdapter(adapter);

        // Enable Send button when there's text to send
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        // Send button sends a message and clears the EditText

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }
            @Override
            public void onSigned() {
                mSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userNames.contains(mNameEditText.getText().toString())) {
                            mEmployeeDatabaseReference = mFirebaseDatabase.getReference().child("employees");
                            mEmployeeDatabaseReference.removeValue();
                            mEmployeeDatabaseReference.setValue("employees");
                            mEmployeeDatabaseReference.child("employees");
                            for (User user : userList) {
                                if (user.getUsername().equals(mNameEditText.getText().toString())) {
                                    if (user.signed == true) {
                                        Toast.makeText(getApplicationContext(),
                                                "Already signed this week.", Toast.LENGTH_SHORT).show();
                                    }
                                    user.signed = true;
                                    mNameEditText.setText("");
                                    mSignaturePad.clear();
                                }
                                mEmployeeDatabaseReference.push().setValue(user);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Name could not be found.", Toast.LENGTH_SHORT).show();
                            mNameEditText.setText("");
                            mSignaturePad.clear();
                        }
                    }
                });

            }
            @Override
            public void onClear() {}
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
                Toast.makeText(getApplicationContext(),
                        "Signature has been cleared.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}


