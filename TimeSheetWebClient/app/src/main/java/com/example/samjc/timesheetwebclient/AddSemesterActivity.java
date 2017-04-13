package com.example.samjc.timesheetwebclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddSemesterActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEmployeeDatabaseReference;
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);

        //set up Firebase properties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        //data listener
        mEmployeeDatabaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                data = snapshot.child("Data").getValue(Data.class);
            }
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });



//        how to create a new semester
//
//        change active field in data class
//
//        set non-active semester to new semester object containing the following
//
//        current week = 0
//        employees should be empty
//        set start and end dates
//        calculate number of weeks
//        create list of weeks each week needs it enddate set calculate that date based off start and enddate



    }
}
