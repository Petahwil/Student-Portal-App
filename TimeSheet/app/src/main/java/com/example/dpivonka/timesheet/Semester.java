package com.example.dpivonka.timesheet;

import java.util.ArrayList;

/**
 * Created by dpivonka on 3/23/17.
 */

public class Semester {
    public String startdate;
    public String enddate;
    public ArrayList<User> employees = new ArrayList<>();
    public ArrayList<Week> weeks = new ArrayList<>();

    public Semester(){

    }

    public Semester(String startdate, String enddate, ArrayList<User> employees, ArrayList<Week> weeks) {
        this.startdate = startdate;
        this.enddate = enddate;
        this.employees = employees;
        this.weeks = weeks;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public ArrayList<User> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<User> employees) {
        this.employees = employees;
    }

    public ArrayList<Week> getWeeks() {
        return weeks;
    }

    public void setWeeks(ArrayList<Week> weeks) {
        this.weeks = weeks;
    }

    public ArrayList<String> getListOfUsernames(){
        ArrayList<String> usernames = new ArrayList<>();

        for(User u: employees){
            usernames.add(u.getUsername());
        }

        return usernames;
    }
}
