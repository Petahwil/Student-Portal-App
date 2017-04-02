package com.example.dpivonka.timesheet;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEmployeeDatabaseReference;

    private TinyDB tinydb;

    private EditText mNameEditText;
    private Button mNextButton;
    private AutoCompleteTextView actv;

    private ArrayList<String> userNames = new ArrayList<>();
    private Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initalize firebase reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        //initalize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        //array adaptor for autocomplete view
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,userNames);

        //layout features
        mNameEditText = (EditText)findViewById(R.id.editText);
        mNextButton = (Button) findViewById(R.id.nextButton);
        actv = (AutoCompleteTextView) findViewById(R.id.editText);
        actv.setAdapter(adapter);

        //data listener
        mEmployeeDatabaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                userNames.clear();
                data = snapshot.child("Data").getValue(Data.class);

                for(String s:data.ActiveSemester().ListOfUsernames()){
                    System.out.println(s);
                    userNames.add(s);
                }
                //adapter.notifyDataSetChanged();

                tinydb.putObject("data", data);


                mEmployeeDatabaseReference.child("Data").setValue(data);

            }
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });











        //weekly email system activation
        EmailSystem.SetAlarm(getApplicationContext());

        //listener for next button
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(userNames.contains(mNameEditText.getText().toString())){
                        Intent ii = new Intent(MainActivity.this, WeekActivity.class);
                        ii.putExtra("userName", mNameEditText.getText().toString());
                        startActivity(ii);
                    }else{
                        Toast.makeText(getApplicationContext(), "Invalid Name Use Autocomplete", Toast.LENGTH_SHORT).show();
                    }
            }
        });






    }
}


