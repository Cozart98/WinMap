package com.example.macbook.winmap;

/**
 * Created by macbook on 03/05/2018.
 */

public class StoreModel {
    private String companyName;
    private String address;
    private String activity;
    private String subActivity;
    private String schedule;
    private String storeLatitude;
    private String storeLongitude;

    public StoreModel (String companyName, String address, String activity, String subActivity, String schedule, String storeLatitude, String storeLongitude){
        this.companyName = companyName;
        this.address = address;
        this.activity = activity;
        this.subActivity = subActivity;
        this.schedule = schedule;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
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
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
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

    public String getStoreLatitude() {
        return storeLatitude;
    }

    public void setStoreLatitude(String storeLatitude) {
        this.storeLatitude = storeLatitude;
    }

    public String getStoreLongitude() {
        return storeLongitude;
    }

    public void setStoreLongitude(String storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}