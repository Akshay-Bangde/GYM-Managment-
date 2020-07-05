package com.example.smile.dataObjects;

import java.io.Serializable;

public class GYMPackageListModel implements Serializable {

    private String Id;
    public String serial_no;

    private String packagename;
    private String details;
    private String duration;
    private String packageamount;
    private String activityname;
    private String activityamount;
    private String totalamount;


    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPackageamount() {
        return packageamount;
    }


    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public void setPackageamount(String packageamount) {
        this.packageamount = packageamount;
    }

    public String getActivityamount() {
        return activityamount;
    }

    public void setActivityamount(String activityamount) {
        this.activityamount = activityamount;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }


}
