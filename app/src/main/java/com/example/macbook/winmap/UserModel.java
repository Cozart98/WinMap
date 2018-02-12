package com.example.macbook.winmap;

/**
 * Created by macbook on 12/02/2018.
 */

public class UserModel {

    private String firstname;
    private String lastname;
    private String email;
    private String refId;

    public UserModel(String firstname, String lastname, String email, String refId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;

        this.refId = refId;
    }

    public UserModel(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.refId = "";
    }

    public UserModel() {
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getRefId() {
        return refId;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }
}
