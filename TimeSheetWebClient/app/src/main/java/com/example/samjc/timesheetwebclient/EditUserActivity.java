package com.example.samjc.timesheetwebclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by dpivonka on 4/25/17.
 */

public class EditUserActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mEmployeeDatabaseReference;
    private TinyDB tinydb;
    Data data;

    String userName;

    EditText nameEdit;
    AutoCompleteTextView emailEdit;
    RadioGroup positionGroup;
    AutoCompleteTextView facultyEdit;
    AutoCompleteTextView accountEdit;

    ArrayAdapter<String> emailAdapter;
    ArrayAdapter<String> facultyAdapter;
    ArrayAdapter<String> accountAdapter;

    ArrayList<String> emailList;
    ArrayList<String> facultyList;
    ArrayList<String> accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        //set up Firebase properties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference();

        // Initialize tinyDB
        tinydb = new TinyDB(getApplicationContext());

        // Get data object
        data = (Data) tinydb.getObject("data", Data.class);

        emailList = new ArrayList<String>();
        facultyList = new ArrayList<String>();
        accountList = new ArrayList<String>();

        for (User u:data.ActiveSemester().getEmployees()) {
            emailList.add(u.getEmail());
            facultyList.add(u.getAdvisor());
            accountList.add(u.getCode());
        }

        //set up UI Element fields
        nameEdit = (EditText) findViewById(R.id.name_edit);
        emailEdit = (AutoCompleteTextView) findViewById(R.id.email_edit);
        positionGroup = (RadioGroup) findViewById(R.id.rgroup_position);
        facultyEdit = (AutoCompleteTextView) findViewById(R.id.faculty_edit);
        accountEdit = (AutoCompleteTextView) findViewById(R.id.account_edit);

        //autocomplete adapters
        emailAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emailList);
        facultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, facultyList);
        accountAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountList);

        //init autocomplete textedits
        emailEdit.setAdapter(emailAdapter);
        facultyEdit.setAdapter(facultyAdapter);
        accountEdit.setAdapter(accountAdapter);

        Bundle bundle = getIntent().getExtras();
        //get username
        if (bundle != null)
            userName = bundle.getString("editusername");


        nameEdit.setText(userName.toString());

        nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Users name can not be modified", Toast.LENGTH_SHORT).show();
                nameEdit.setText(userName.toString());
            }


        });


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
                    startActivity(new Intent(EditUserActivity.this, MainActivity.class));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void writeNewUser(String name, String email, String position, String faculty, String account) {
        User user = new User(name, email, position, faculty, account);
        for(User u: data.ActiveSemester().getEmployees()){
            if(u.getUsername().equals(userName)){
                u.setEmail(email);
                u.setTa_ra(position);
                u.setAdvisor(faculty);
                u.setCode(account);
            }
        }

        mEmployeeDatabaseReference.child("Data").setValue(data);
    }

}
