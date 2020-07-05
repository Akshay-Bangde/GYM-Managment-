package com.example.smile.dataObjects;

import java.io.Serializable;

public class DietModel implements Serializable {

    String serial_id;
    String planName;
    String preWorkout;
    String postWorkout;
    String breakFast;
    String MornSnk;
    String EveSnk;
    String lunchh;
    String dinnerr;
    String meall;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPreWorkout() {
        return preWorkout;
    }

    public void setPreWorkout(String preWorkout) {
        this.preWorkout = preWorkout;
    }

    public String getPostWorkout() {
        return postWorkout;
    }

    public void setPostWorkout(String postWorkout) {
        this.postWorkout = postWorkout;
    }

    public String getBreakFast() {
        return breakFast;
    }

    public void setBreakFast(String breakFast) {
        this.breakFast = breakFast;
    }

    public String getMornSnk() {
        return MornSnk;
    }

    public void setMornSnk(String mornSnk) {
        MornSnk = mornSnk;
    }

    public String getEveSnk() {
        return EveSnk;
    }

    public void setEveSnk(String eveSnk) {
        EveSnk = eveSnk;
    }

    public String getLunchh() {
        return lunchh;
    }

    public void setLunchh(String lunchh) {
        this.lunchh = lunchh;
    }

    public String getDinnerr() {
        return dinnerr;
    }

    public void setDinnerr(String dinnerr) {
        this.dinnerr = dinnerr;
    }

    public String getMeall() {
        return meall;
    }

    public void setMeall(String meall) {
        this.meall = meall;
    }
}
