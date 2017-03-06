package com.example.samjc.timesheetwebclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lvMain = (ListView) findViewById(R.id.lv_main);

        ArrayList<Person> fakePeople = new ArrayList<Person>();
        fakePeople.add(new Person("Person1", "email1@domain.com"));
        fakePeople.add(new Person("Person2", "email2@domain.com"));
        fakePeople.add(new Person("Person3", "email3@domain.com"));
        PersonAdapter adapter = new PersonAdapter(this, fakePeople);
        lvMain.setAdapter(adapter);

        //startActivity(new Intent(MainActivity.this, AddNewPersonActivity.class));
    }



}
