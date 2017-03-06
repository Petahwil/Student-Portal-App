package com.example.samjc.timesheetwebclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewPersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_person);

        final EditText nameEdit = (EditText) findViewById(R.id.name_edit);
        final EditText emailEdit = (EditText) findViewById(R.id.email_edit);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Person p = new Person(nameEdit.getText().toString(), emailEdit.getText().toString());
            }
        });
    }
}
