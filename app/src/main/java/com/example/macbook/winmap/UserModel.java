package com.example.macbook.winmap;

/**
 * Created by macbook on 12/02/2018.
 */

public class UserModel {

    private String firstname;
    private String lastname;
    private String email;
    private boolean profil;
    private String companyName;
    private String adressCompany;

    public UserModel(String firstname, String lastname, String email, boolean profil, String companyName, String adressCompany) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.profil = profil;
        this.companyName = companyName;
        this.adressCompany = adressCompany;
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

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isProfil() {
        return profil;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setProfil(boolean profil) {
        this.profil = profil;
    }

    public String getAdressCompany() {
        return adressCompany;
    }

    public void setAdressCompany(String adressCompany) {
        this.adressCompany = adressCompany;
    }
}
