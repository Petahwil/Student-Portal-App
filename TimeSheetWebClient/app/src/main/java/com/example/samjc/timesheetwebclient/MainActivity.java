package com.example.samjc.timesheetwebclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    //firebase
    private DatabaseReference mEmployeeDatabaseReference;
    //firebase
    private void writeNewUser(String name, String email) {
        User user = new User(name, email);

        mEmployeeDatabaseReference.push().setValue(user);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //firebase
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference().child("employees");

        final ListView lvMain = (ListView) findViewById(R.id.lv_main);
        ArrayList<Person> fakePeople = new ArrayList<Person>();
        fakePeople.add(new Person("Person1", "email1@domain.com"));
        fakePeople.add(new Person("Person2", "email2@domain.com"));
        fakePeople.add(new Person("Person3", "email3@domain.com"));
        PersonAdapter adapter = new PersonAdapter(this, fakePeople);
        lvMain.setAdapter(adapter);
        //firebase
        writeNewUser("user name" , "email");
        //startActivity(new Intent(MainActivity.this, AddNewPersonActivity.class));
    }



}
