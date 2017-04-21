package com.example.samjc.timesheetwebclient;

import java.util.ArrayList;

public class Semester {
    /**
     * The {@link String} holding the semester's start date.
     */
    public String startdate;
    /**
     * The {@link String} holding the semester's end date.
     */
    public String enddate;
    /**
     * The current week of the semester.
     */
    public int currentWeek;
    /**
     * The number of weeks in the semester.
     */
    public int numWeeks;
    /**
     * The {@link ArrayList<User>} holding a list of the employees for the semester.
     */
    public ArrayList<User> employees = new ArrayList<>();
    /**
     * The {@link ArrayList<Week>} holding a list of the weeks in the semester.
     */
    public ArrayList<Week> weeks = new ArrayList<>();

    /**
     * Default constructor for {@link Semester} object.
     */
    public Semester(){

    }

    /**
     * Constructor for {@link Semester} with parameters.
     * @param startdate The {@link String} containing the start date for the semester.
     * @param numWeeks The number of weeks in the semester.
     * @param employees The {@link ArrayList<User>} containing the list of employees for the semester.
     * @param weeks The {@link ArrayList<Week>} containing the list of weeks in the semester.
     */
    public Semester(String startdate, int numWeeks, ArrayList<User> employees, ArrayList<Week> weeks) {
        this.startdate = startdate;
        this.numWeeks = numWeeks;
        this.employees = employees;
        this.weeks = weeks;
    }

    /**
     * Constructor for {@link Semester} with parameters.
     * @param startdate The {@link String} containing the start date for the semester.
     * @param enddate The {@link String} containing the end date for the semester.
     * @param employees The {@link ArrayList<User>} containing the list of employees for the semester.
     * @param weeks The {@link ArrayList<Week>} containing the list of weeks in the semester.
     */
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

    /**
     * @return List of names in the current semester.
     */
    public ArrayList<String> ListOfUsernames(){
        ArrayList<String> usernames = new ArrayList<>();

        for(User u: employees){
            usernames.add(u.getUsername());
        }

        return usernames;
    }
}
