package com.example.samjc.timesheetwebclient;

/**
 * Created by samjc on 3/6/2017.
 */

public class User {

    /**
     * The {@link String} holding the name of the employee.
     */
    public String username;
    /**
     * The {@link String} holding the email address of the employee.
     */
    public String email;
    /**
     * The {@link Boolean} determining whether or not the employee has entered his/her hours for the week.
     */
    public boolean signed;


    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public User() {}

    /**
     * Constructor for {@link User} class.
     * @param username The {@link String} used to initiate the "username" member variable of the {@link User} class.
     * @param email The {@link String} used to initiate the "email" member variable of the {@link User} class.
     */
    public User(String username, String email) {
        signed = false;
        this.username = username;
        this.email = email;
    }

    public String getUsername (){
        return username;
    }
}