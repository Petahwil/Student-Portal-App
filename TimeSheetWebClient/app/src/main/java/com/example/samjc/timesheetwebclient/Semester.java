package com.example.samjc.timesheetwebclient;



import java.util.ArrayList;

/**
 * Created by dpivonka on 3/23/17.
 */

public class Semester {
    public String startdate;
    public String enddate;
    public int currentWeek;
    public int numWeeks;
    public ArrayList<User> employees = new ArrayList<>();
    public ArrayList<Week> weeks = new ArrayList<>();

    public Semester(){

    }

    public Semester(String startdate, int numWeeks, ArrayList<User> employees, ArrayList<Week> weeks) {
        this.startdate = startdate;
        this.numWeeks = numWeeks;
        this.employees = employees;
        this.weeks = weeks;
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

    public int getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        this.currentWeek = currentWeek;
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

    public ArrayList<String> ListOfUsernames(){
        ArrayList<String> usernames = new ArrayList<>();

        for(User u: employees){
            usernames.add(u.getUsername());
        }

        return usernames;
    }
}
