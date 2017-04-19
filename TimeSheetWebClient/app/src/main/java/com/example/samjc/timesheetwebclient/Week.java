package com.example.samjc.timesheetwebclient;

import java.util.ArrayList;

/**
 * Created by dpivonka on 3/23/17.
 */

public class Week {
    public String endDate;
    public ArrayList<User> employees = new ArrayList<>();
    public boolean signed;

    public Week(){

    }

    public Week(ArrayList<User> employees) {
        this.employees = employees;
    }

    public Week(String endDate) {
        this.endDate = endDate;
    }

    public Week(String startDate, ArrayList<User> employees, boolean signed) {
        this.endDate = startDate;
        this.employees = employees;
        this.signed = signed;
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

    public boolean isSigned() {
        return signed;
    }
}
