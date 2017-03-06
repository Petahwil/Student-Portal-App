package com.example.samjc.timesheetwebclient;

/**
 * Created by samjc on 3/6/2017.
 */

public class User {

    public String username;
    public String email;
    public boolean signed;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        signed = false;
        this.username = username;
        this.email = email;
    }

}