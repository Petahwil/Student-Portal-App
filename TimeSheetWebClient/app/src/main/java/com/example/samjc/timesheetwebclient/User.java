package com.example.samjc.timesheetwebclient;

public class User extends Object {

    /**
     * The {@link String} containing the user's name.
     */
    public String username;
    /**
     * The {@link String} containing the user's email address.
     */
    public String email;
    /**
     * The {@link String} used to determine whether the user is a TA or an RA.
     */
    public String ta_ra;
    /**
     * The {@link String} containing the user's faculty advisor.
     */
    public String advisor;
    /**
     * The {@link String} containing the user's account code.
     */
    public String code;
    /**
     * True if user has signed for the week, otherwise false.
     */
    public boolean signed;


    /**
     * Default constructor for {@link User} object.
     */
    public User() {}

    /**
     * Constructor for {@link User} object with parameters.
     * @param username The {@link String} containing the user's name.
     * @param email The {@link String} containing the user's email address.
     */
    public User(String username, String email) {
        signed = false;
        this.username = username;
        this.email = email;
    }


    /**
     * Constructor for {@link User} object with parameters.
     * @param username The {@link String} containing the user's name.
     * @param signed True if user has signed for the week, otherwise false.
     */
    public User(String username, boolean signed) {
        this.signed = signed;
        this.username = username;

    }

    /**
     * Constructor for {@link User} object with parameters.
     * @param username The {@link String} containing the user's name.
     * @param email The {@link String} containing the user's email address.
     * @param ta_ra The {@link String} used to determine whether the user is a TA or an RA.
     * @param advisor The {@link String} containing the user's account code.
     * @param code The {@link String} containing the user's account code.
     */
    public User(String username, String email, String ta_ra, String advisor, String code) {
        this.username = username;
        this.email = email;
        this.ta_ra = ta_ra;
        this.advisor = advisor;
        this.code = code;
    }

    /**
     * Constructor for {@link User} object with parameters.
     * @param username The {@link String} containing the user's name.
     * @param email The {@link String} containing the user's email address.
     * @param ta_ra The {@link String} used to determine whether the user is a TA or an RA.
     * @param advisor The {@link String} containing the user's account code.
     * @param code The {@link String} containing the user's account code.
     * @param signed True if user has signed for the week, otherwise false.
     */
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