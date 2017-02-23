package com.example.samjc.timesheetwebclient;

/**
 * Created by samjc on 2/23/2017.
 */

public class Person {
    private String name;
    private String email;
    private Position position;
    private String faculty;

    Person(String name, String email, Position position, String faculty) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.faculty = faculty;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() { return name; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setPosition(Position position) { this.position = position; }
    public Position getPosition() { return position; }

    public void setFaculty(String faculty) { this.faculty = faculty; }
    public String getFaculty() { return faculty; }
}