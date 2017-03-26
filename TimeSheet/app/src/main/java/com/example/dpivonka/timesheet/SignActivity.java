package com.example.dpivonka.timesheet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by dpivonka on 3/25/17.
 */

public class SignActivity extends AppCompatActivity {

    private TinyDB tinydb;
    private Data data;

    private Button mSendButton;
    private Button mClearButton;
    private SignaturePad mSignaturePad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        //initalize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        //get data object
        data = (Data) tinydb.getObject("data", Data.class);

        //get username and weeks signing for
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("userName");
        ArrayList<Integer> missedwWeeks = bundle.getIntegerArrayList("weeks");
        boolean curentWeek = bundle.getBoolean("current");

        //tis will be useful
        ArrayList<Week> weeks = data.getActiveSemester().getWeeks();

        //set up views
        mSendButton = (Button) findViewById(R.id.sigButton);
        mClearButton = (Button) findViewById(R.id.clear_button);
        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);

        //signature
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {}

            @Override
            public void onSigned() {

                //submit button
                mSendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //todo push signature data to firebase

                        //                User u = new User("dan pivonka", "dpivonka@comcast.net", "TA", "Mr.Johnson", "ABC123");
                        //
                        //                data.getSpring().employees.add(u);
                        //
                        //                mEmployeeDatabaseReference.child("Data").child("spring").child("employees").setValue(data.spring.employees);

                    }
                });
            }
            @Override
            public void onClear() {}
        });

        //clear button
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





