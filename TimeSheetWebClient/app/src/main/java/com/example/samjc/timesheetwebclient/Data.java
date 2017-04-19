package com.example.samjc.timesheetwebclient;

/**
 * Created by dpivonka on 3/23/17.
 */

public class Data {

    public String active;
    public Semester fall;
    public Semester spring;
    public String email;

    public Data(){

    }

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
