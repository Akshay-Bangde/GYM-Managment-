package com.example.smile.dataObjects;

import java.io.Serializable;

public class StaffListModel implements Serializable {

    String id;
    String serialNo;

    String employeeType;
    String name;
    String punchId;
    String contactno;
    String email;
    String state;
    String city;
    String address;
    String salary;
    String bioMetricId;
    String trainingCharge;
    String dob;
    String doj;
    String gender;

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

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPunchId() {
        return punchId;
    }

    public void setPunchId(String punchId) {
        this.punchId = punchId;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getBioMetricId() {
        return bioMetricId;
    }

    public void setBioMetricId(String bioMetricId) {
        this.bioMetricId = bioMetricId;
    }

    public String getTrainingCharge() {
        return trainingCharge;
    }

    public void setTrainingCharge(String trainingCharge) {
        this.trainingCharge = trainingCharge;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
