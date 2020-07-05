package com.example.smile.dataObjects;

import java.io.Serializable;

public class MemberRenewalListModel implements Serializable {

    String id;
    String serialNo;
    String name;
    String doj;
    String bookingDate;
    String expiryDate;
    String packages;
    String duration;

    public MemberRenewalListModel( String serialNo, String name, String doj, String bookingDate, String expiryDate, String packages, String duration) {

        this.serialNo = serialNo;
        this.name = name;
        this.doj = doj;
        this.bookingDate = bookingDate;
        this.expiryDate = expiryDate;
        this.packages = packages;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
