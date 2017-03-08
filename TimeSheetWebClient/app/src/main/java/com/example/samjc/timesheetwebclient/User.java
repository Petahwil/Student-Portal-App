package com.example.samjc.timesheetwebclient;

/**
 * Created by wilsop4 on 3/3/2017.
 */

public class User {

    public String username;
    public String email;
    public boolean signed;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        signed = true;
        this.username = username;
        this.email = email;
    }

}