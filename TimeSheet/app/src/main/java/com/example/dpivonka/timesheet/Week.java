package com.example.dpivonka.timesheet;

import java.util.ArrayList;

/**
 * Created by dpivonka on 3/23/17.
 */

public class Week {
    public ArrayList<User> employees = new ArrayList<>();

    public Week(){

    }

    public Week(ArrayList<User> employees) {
        this.employees = employees;
    }

    public ArrayList<User> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<User> employees) {
        this.employees = employees;
    }
}
