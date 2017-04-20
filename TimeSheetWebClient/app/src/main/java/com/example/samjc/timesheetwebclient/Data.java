package com.example.samjc.timesheetwebclient;

public class Data {

    /**
     * The {@link String} to determine which semester is active.
     */
    public String active;
    /**
     * The {@link Semester} holding data for the Fall semester.
     */
    public Semester fall;
    /**
     * The {@link Semester} holding data for the Spring semester.
     */
    public Semester spring;
    /**
     * The {@link String} containing the admin's email.
     */
    public String email;

    /**
     * Default constructor for {@link Data} object.
     */
    public Data(){

    }

    /**
     * Constructor for {@link Data} object with parameters.
     * @param active The {@link String} telling which semester is active.
     * @param fall The {@link Semester} containing information for Fall semester.
     * @param spring The {@link Semester} containing information for Spring semester.
     */
    public Data(String active, Semester fall, Semester spring) {
        this.active = active;
        this.fall = fall;
        this.spring = spring;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    public Semester getFall() {
        return fall;
    }

    public void setFall(Semester fall) {
        this.fall = fall;
    }

    public Semester getSpring() {
        return spring;
    }

    public void setSpring(Semester spring) {
        this.spring = spring;
    }

    public Semester ActiveSemester(){
        if(active.equals("fall")|| active.equals("Fall")||active.equals("FALL")){
            return fall;
        }else{
            return spring;
        }

    }

    public Semester NonActiveSemester(){
        if(active.equals("fall")|| active.equals("Fall")||active.equals("FALL")){
            return spring;
        }else{
            return fall;
        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
