package com.example.samjc.timesheetwebclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddUserActivity extends AppCompatActivity {

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
    //region UI Elements
    /**
     * The {@link AutoCompleteTextView} where user enters name of the new user.
     */
    AutoCompleteTextView nameEdit;
    /**
     * The {@link AutoCompleteTextView} where user enters email address of the new user.
     */
    AutoCompleteTextView emailEdit;
    RadioGroup positionGroup;
    RadioButton positionButton;
    AutoCompleteTextView facultyEdit;
    EditText accountEdit;
    ArrayAdapter<String> nameAdapter;
    ArrayAdapter<String> emailAdapter;
    ArrayAdapter<String> facultyAdapter;
    ArrayList<String> nameList;
    ArrayList<String> emailList;
    ArrayList<String> facultyList;
    //endregion
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        //set up Firebase properties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        //data listener
        mEmployeeDatabaseReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                data = snapshot.child("Data").getValue(Data.class);

                nameList.clear();
                emailList.clear();
                facultyList.clear();

                for (User u:data.NonActiveSemester().getEmployees()) {
                    nameList.add(u.getUsername());
                    emailList.add(u.getEmail());
                    facultyList.add(u.getAdvisor());
                }

            }
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

        nameList = new ArrayList<String>();
        emailList = new ArrayList<String>();
        facultyList = new ArrayList<String>();

        //set up UI Element fields
        nameEdit = (AutoCompleteTextView) findViewById(R.id.name_edit);
        emailEdit = (AutoCompleteTextView) findViewById(R.id.email_edit);
        positionGroup = (RadioGroup) findViewById(R.id.rgroup_position);
        facultyEdit = (AutoCompleteTextView) findViewById(R.id.faculty_edit);
        accountEdit = (EditText) findViewById(R.id.account_edit);

        nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList);
        emailAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emailList);
        facultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, facultyList);

        nameEdit.setAdapter(nameAdapter);
        emailEdit.setAdapter(emailAdapter);
        facultyEdit.setAdapter(facultyAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                //get user fields from activity.
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String position;
                switch (positionGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_ta:
                        position = "TA";
                        break;
                    case R.id.radio_ra:
                        position = "RA";
                        break;
                    default:
                        position = "";
                        break;
                }
                String faculty = facultyEdit.getText().toString();
                String account = accountEdit.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    //if name field is empty make Toast to inform user.
                    Toast.makeText(getApplicationContext(), "Empty \"Name\" field.", Toast.LENGTH_LONG).show();
                } else if ((TextUtils.isEmpty(email)) ||
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //else if email is invalid or empty make Toast to inform user.
                    Toast.makeText(getApplicationContext(), "Invalid or empty \"Email Address\" field.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(position)) {
                    //else if no position is selected make Toast to inform user.
                    Toast.makeText(getApplicationContext(), "No \"Position\" selected.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(faculty)) {
                    //else if faculty field is empty make Toast to inform user.
                    Toast.makeText(getApplicationContext(), "Empty \"Faculty Advisor\" field.", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(account)) {
                    //else if account field is empty make Toast to inform user.
                    Toast.makeText(getApplicationContext(), "Empty \"HR Account\" field.", Toast.LENGTH_LONG).show();
                } else {
                    //else add user to database and go back to MainActivity.
                    writeNewUser(name, email, position, faculty, account);
                    Toast.makeText(getApplicationContext(), "User \"" + name + "\" added to database.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddUserActivity.this, MainActivity.class));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //region HELPER FUNCTIONS
    /**
     * This function takes a name and an email address,
     * creates a User object, then adds it to Firebase.
     * @param name The {@link String} holding the name of the new user.
     * @param email The {@link String} holding the email address of the new user.
     */
    private void writeNewUser(String name, String email, String position, String faculty, String account) {
        User user = new User(name, email, position, faculty, account);
        data.ActiveSemester().employees.add(user);
        for (Week w:data.ActiveSemester().getWeeks()) {
            w.getEmployees().add(new User(name, false));
        }
        mEmployeeDatabaseReference.child("Data").setValue(data);
    }
    //endregion
}
