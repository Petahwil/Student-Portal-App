package com.example.samjc.timesheetwebclient;

import java.util.ArrayList;

/**
 * Created by dpivonka on 3/23/17.
 */

public class Week {
    /**
     * The {@link String} containing the end date of the week.
     */
    public String endDate;
    /**
     * The {@link ArrayList<User>} containing the list of users.
     */
    public ArrayList<User> employees = new ArrayList<>();


    /**
     * Default constructor for {@link Week} object.
     */
    public Week(){

    }

    /**
     * Constructor for {@link Week} object with employees parameters.
     * @param employees The {@link ArrayList<User>} holding all the new employees.
     */
    public Week(ArrayList<User> employees) {
        this.employees = employees;
    }

    /**
     * Constructor for {@link Week} object with end date parameter.
     * @param endDate The {@link String} end date of the week.
     */
    public Week(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<User> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<User> employees) {
        this.employees = employees;
    }


}
