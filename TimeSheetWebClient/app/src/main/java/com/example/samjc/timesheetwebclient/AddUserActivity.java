package com.example.samjc.timesheetwebclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    //endregion
    //region UI Elements
    /**
     * The {@link EditText} where user enters name of the new user.
     */
    EditText nameEdit;
    /**
     * The {@link EditText} where user enters email address of the new user.
     */
    EditText emailEdit;
    /**
     * The {@link Button} that user presses to save the new user to the database.
     */
    Button saveButton;
    //endregion
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        //set up Firebase properties
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference().child("employees");

        //set up UI Element fields
        nameEdit = (EditText) findViewById(R.id.name_edit);
        emailEdit = (EditText) findViewById(R.id.email_edit);
        saveButton = (Button) findViewById(R.id.save_button);

        //handle event where saveButton is pressed.
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get name and email from text fields in activity.
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    //if name field is empty make Toast to inform user.
                    Toast.makeText(getApplicationContext(), "Empty \"Name\" field.", Toast.LENGTH_LONG).show();
                } else if ((TextUtils.isEmpty(email)) ||
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //else if email is invalid or empty make Toast to inform user.
                    Toast.makeText(getApplicationContext(), "Invalid or empty \"Email Address\" field.", Toast.LENGTH_LONG).show();
                } else {
                    //else add user to database and go back to MainActivity.
                    writeNewUser(name, email);
                    Toast.makeText(getApplicationContext(), "User \"" + name + "\" added to database.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddUserActivity.this, MainActivity.class));
                }
            }
        });
    }


    //region HELPER FUNCTIONS
    /**
     * This function takes a name and an email address,
     * creates a User object, then adds it to Firebase.
     * @param name The {@link String} holding the name of the new user.
     * @param email The {@link String} holding the email address of the new user.
     */
    private void writeNewUser(String name, String email) {
        User user = new User(name, email);
        mEmployeeDatabaseReference.push().setValue(user);
    }
    //endregion
}
