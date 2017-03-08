package com.example.samjc.timesheetwebclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUserActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add_user);
        //firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        //firebase
        mEmployeeDatabaseReference = mFirebaseDatabase.getReference().child("employees");

        final EditText nameEdit = (EditText) findViewById(R.id.name_edit);
        final EditText emailEdit = (EditText) findViewById(R.id.email_edit);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                writeNewUser(nameEdit.getText().toString(), emailEdit.getText().toString());
                startActivity(new Intent(AddUserActivity.this, MainActivity.class));
            }
        });
    }
}
