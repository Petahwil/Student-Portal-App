package com.example.dpivonka.timesheet;

/**
 * Created by wilsop4 on 3/3/2017.
 */

public class User extends Object{

    public String username;
    public String email;
    public String ta_ra;
    public String advisor;
    public String code;
    public boolean signed;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        signed = false;
        this.username = username;
        this.email = email;
    }


    public User(String username, boolean signed) {
        this.signed = signed;
        this.username = username;

    }

    public User(String username, String email, boolean signed) {
        this.username = username;
        this.email = email;
        this.signed = signed;
    }

    public User(String username, String email, String ta_ra, String advisor, String code) {
        this.username = username;
        this.email = email;
        this.ta_ra = ta_ra;
        this.advisor = advisor;
        this.code = code;
    }

    public User(String username, String email, String ta_ra, String advisor, String code, boolean signed) {
        this.username = username;
        this.email = email;
        this.ta_ra = ta_ra;
        this.advisor = advisor;
        this.code = code;
        this.signed = signed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTa_ra() {
        return ta_ra;
    }

    public void setTa_ra(String ta_ra) {
        this.ta_ra = ta_ra;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }
}