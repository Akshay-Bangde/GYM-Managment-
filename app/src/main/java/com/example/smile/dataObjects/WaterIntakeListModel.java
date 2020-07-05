package com.example.smile.dataObjects;

import java.io.Serializable;

public class WaterIntakeListModel implements Serializable {

    public String id;
    public String serial_no;
    public String glasssNo;
    public String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getGlasssNo() {
        return glasssNo;
    }

    public void setGlasssNo(String glasssNo) {
        this.glasssNo = glasssNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
