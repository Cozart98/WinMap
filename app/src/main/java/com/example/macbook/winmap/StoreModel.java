package com.example.macbook.winmap;

/**
 * Created by macbook on 03/05/2018.
 */

public class StoreModel {
    private String companyName;
    private String adress;
    private String activity;
    private String subActivity;
    private String schedule;

    public StoreModel (String companyName, String adress, String activity, String subActivity, String schedule){
        this.companyName = companyName;
        this.adress = adress;
        this.activity = activity;
        this.subActivity = subActivity;
        this.schedule = schedule;
    }

    public StoreModel() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(String subActivity) {
        this.subActivity = subActivity;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
