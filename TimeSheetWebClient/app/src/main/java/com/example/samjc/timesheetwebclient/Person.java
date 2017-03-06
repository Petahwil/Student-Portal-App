package com.example.samjc.timesheetwebclient;

/**
 * Created by samjc on 2/23/2017.
 */

public class Person {
    private String name;
    private String email;

    Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}