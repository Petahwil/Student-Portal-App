package com.example.samjc.timesheetwebclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private ArrayList<User> userList = new ArrayList<>();
    private ListView lvMain;
    private UserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //firebase
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference().child("employees");


        //get users data everytime it changes
        mEmployeeDatabaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userList.add(user);
                }
            }
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        final ArrayList<User> fakePeople = new ArrayList<User>();
        fakePeople.add(new User("User1", "email1@domain.com"));
        fakePeople.add(new User("User7", "email2@domain.com"));
        fakePeople.add(new User("User3", "email3@domain.com"));

        Collections.sort(fakePeople, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.username.compareTo(o2.username);
            }
        });
        lvMain = (ListView) findViewById(R.id.lv_main);
        adapter = new UserAdapter(this, fakePeople);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final User user = (User) lvMain.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete user: \"" + user.username + "\" from database?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete user from database
                        adapter = new UserAdapter(MainActivity.this, fakePeople);
                        lvMain.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "User: \"" + user.username + "\" deleted from database.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


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
