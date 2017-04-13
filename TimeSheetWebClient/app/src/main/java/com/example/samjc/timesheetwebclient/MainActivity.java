package com.example.samjc.timesheetwebclient;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import it.enricocandino.androidmail.MailSender;
import it.enricocandino.androidmail.model.Mail;
import it.enricocandino.androidmail.model.Recipient;

public class MainActivity extends AppCompatActivity {

    //region MEMBER VARIABLES
    //region FIREBASE
    /**
     * The {@link FirebaseDatabase} holding the Firebase database.
     */
    private FirebaseDatabase mFirebaseDatabase;
    /**
     * The {@link DatabaseReference} to the employees in the Firebase database.
     */
    private DatabaseReference mEmployeeDatabaseReference;
    Data data;
    //endregion
    /**
     * The {@link ArrayList<User>} that holds all users in database.
     */
    private ArrayList<User> userList = new ArrayList<>();
    /**
     * The {@link ListView} that displays all of the users.
     */
    private ListView lvMain;
    /**
     * The {@link UserAdapter} that will populate the {@link ListView} with the users in userList.
     */
    private UserAdapter adapter;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up Firebase properties.
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        //read list of users into userList every time the database is updated.
        mEmployeeDatabaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                userList.clear();
                data = snapshot.child("Data").getValue(Data.class);

                for(User u:data.ActiveSemester().getEmployees()){
                    userList.add(u);
                }
                sortUsers();
                adapter.notifyDataSetChanged();
            }
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        lvMain = (ListView) findViewById(R.id.lv_main);
        adapter = new UserAdapter(this, userList);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final User user = (User) lvMain.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete user \"" + user.username + "\" from database?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete user from database

                        //reset adapter and apply to lvMain to update list.
                        adapter.notifyDataSetChanged();

                        int x;
                        for(x = 0; x < data.ActiveSemester().employees.size(); x++){
                            if(data.ActiveSemester().employees.get(x).getUsername().equals(user.getUsername())){
                                break;
                            }
                        }

                        data.ActiveSemester().employees.remove(x);

                        for(Week w: data.ActiveSemester().getWeeks()){
                            w.employees.remove(x);
                        }
                        
                        mEmployeeDatabaseReference.child("Data").setValue(data);


                        //adapter = new UserAdapter(MainActivity.this, userList);
                        //lvMain.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "User \"" + user.username + "\" deleted from database.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing.
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    //region MENU FUNCTIONS

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
            case R.id.action_email:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("What information do you need?")
                .setItems(R.array.email_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                System.out.println("0");
                                //send this semester
                                break;
                            case 1:
                                System.out.println("1");
                                //send last semester
                                break;
                            default:
                                System.out.println("-1");
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                //EmailAdmin();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    //region HELPER FUNCTIONS

    /**
     * Sorts users in {@link ArrayList<User>} userList alphabetically.
     */
    private void sortUsers() {
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.username.compareTo(o2.username);
            }
        });
    }

    /**
     * Sends email to admin showing which users have signed in for the
     * week and which users have not.
     */
    private void EmailAdmin(){
        ArrayList<String> signed = new ArrayList<>();
        ArrayList<String> notsigned = new ArrayList<>();

        //filter users who signed from users who didn't.
        for (User user : userList) {
            if(user.signed){
                signed.add(user.getUsername());
            }else{
                notsigned.add(user.getUsername());
            }
        }

        //create and populate body of email.
        String email = "Not Signed\n\n";

        for (String s : notsigned) {
            email += s+"\n";
        }

        email += "\n\nSigned\n\n";

        for (String s : signed) {
            email += s+"\n";
        }

        //build and send email.
        MailSender mailSender = new MailSender("timesheetautoemail@gmail.com", "AndroidPass7");
        Mail.MailBuilder builder = new Mail.MailBuilder();
        Mail mail = builder
                .setSender("timesheetautoemail@gmail.com")
                .addRecipient(new Recipient("dpivonka@comcast.net"))
                .setSubject("Weekly Signatures")
                .setText("testing auto email system\n\n"+email)
                .build();
        mailSender.sendMail(mail);

        //reset all signed values for this week now that the email has sent.
        mEmployeeDatabaseReference.removeValue();
        mEmployeeDatabaseReference.setValue("employees");
        mEmployeeDatabaseReference.child("employees");
        for (User user : userList) {
            user.signed=false;
            mEmployeeDatabaseReference.push().setValue(user);
        }

        Toast.makeText(getApplicationContext(),
                "Email Sent", Toast.LENGTH_SHORT).show();
    }

    User findUserByName(String name) {
        for (User u:data.ActiveSemester().getEmployees()) {
            if (name.equals(u.getUsername())) {
                return u;
            }
        }
        return null;
    }

    //endregion
}
