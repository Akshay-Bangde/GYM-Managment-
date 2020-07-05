package com.example.smile.dataObjects;

import java.io.Serializable;

public class MemberGroupListModel  implements Serializable {
    String id;
    String groupName;
    String contactPersonName;
    String mobileNo;
    String email;
    String address;
    String state;
    String city;
    String totalMembers;

    String packId;
    String packageName;
    String packageCharge;
    String trainerCheck;
    String trainersID;
    String trainerName;
    String trainerCharge;
    String serialNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(String totalMembers) {
        this.totalMembers = totalMembers;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageCharge() {
        return packageCharge;
    }

    public void setPackageCharge(String packageCharge) {
        this.packageCharge = packageCharge;
    }

    public String getTrainerCheck() {
        return trainerCheck;
    }

    public void setTrainerCheck(String trainerCheck) {
        this.trainerCheck = trainerCheck;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainersID() {
        return trainersID;
    }

    public void setTrainersID(String trainersID) {
        this.trainersID = trainersID;
    }

    public String getTrainerCharge() {
        return trainerCharge;
    }

    public void setTrainerCharge(String trainerCharge) {
        this.trainerCharge = trainerCharge;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}


