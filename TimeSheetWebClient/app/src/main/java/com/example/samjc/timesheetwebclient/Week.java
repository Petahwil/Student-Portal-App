package com.example.samjc.timesheetwebclient;

import java.util.ArrayList;

/**
 * Created by dpivonka on 3/23/17.
 */

public class Week {
    public String endDate;
    public ArrayList<User> employees = new ArrayList<>();


    public Week(){

    }

    public Week(ArrayList<User> employees) {
        this.employees = employees;
    }

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
