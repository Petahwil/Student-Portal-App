package com.example.samjc.timesheetwebclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

        ArrayList<User> fakePeople = new ArrayList<User>();
        fakePeople.add(new User("User1", "email1@domain.com"));
        fakePeople.add(new User("User7", "email2@domain.com"));
        fakePeople.add(new User("User3", "email3@domain.com"));

        Collections.sort(fakePeople, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.username.compareTo(o2.username);
            }
        });
        UserAdapter adapter = new UserAdapter(this, fakePeople);
        lvMain.setAdapter(adapter);
        //firebase
        writeNewUser("user name" , "email");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                startActivity(new Intent(this, AddUserActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
