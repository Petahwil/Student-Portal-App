package com.example.dpivonka.timesheet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dpivonka on 3/25/17.
 */

public class SignActivity extends AppCompatActivity {

    private TinyDB tinydb;
    private Data data;

    private Button mSendButton;
    private Button mClearButton;
    private SignaturePad mSignaturePad;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEmployeeDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        //initalize firebase reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        //initalize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        //get data object
        data = (Data) tinydb.getObject("data", Data.class);

        //get username and weeks signing for
        Bundle bundle = getIntent().getExtras();
        final String userName = bundle.getString("userName");
        final ArrayList<Integer> missedwWeeks = bundle.getIntegerArrayList("weeks");
        final boolean curentWeek = bundle.getBoolean("current");

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

                        //check what week it is and update if nessasary
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date endDate = null;
                        try {
                            endDate = sdf.parse(data.ActiveSemester().weeks.get(data.ActiveSemester().currentWeek).getEndDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (new Date().after(endDate)) {
                            for(int x = data.ActiveSemester().currentWeek; x < data.ActiveSemester().weeks.size(); x++){
                                try {
                                    endDate = sdf.parse(data.ActiveSemester().weeks.get(x).getEndDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(new Date().before(endDate)){
                                    data.ActiveSemester().setCurrentWeek(x);
                                    break;
                                }
                            }
                        }

                        //sign for current week
                        if(curentWeek){
                            for(User u: data.ActiveSemester().weeks.get(data.ActiveSemester().currentWeek).employees){
                                if(u.getUsername().equals(userName)){
                                    u.setSigned(true);
                                    break;
                                }
                            }
                        }

                        //sign for missed weeks
                        for(int i: missedwWeeks){
                            for(User u: data.ActiveSemester().weeks.get(i-1).employees){
                                if(u.getUsername().equals(userName)){
                                    u.setSigned(true);
                                    break;
                                }
                            }
                        }

                        //push changes to firebase
                        mEmployeeDatabaseReference.child("Data").setValue(data);

                        //return to main activity
                        Intent intent = new Intent(SignActivity.this, MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Signature has been submitted.", Toast.LENGTH_LONG).show();
                        startActivity(intent);
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





