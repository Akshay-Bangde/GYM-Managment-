package com.example.smile.dataObjects;

import java.io.Serializable;

public class FoodEntryListModel implements Serializable {

    String id;
    String serial_No;
    String Profile;
    String Foodname;
    String uom;
    String Calorie;
    String description;
    String foodCatName;
    String Status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial_No() {
        return serial_No;
    }

    public void setSerial_No(String serial_No) {
        this.serial_No = serial_No;
    }

    public String getFoodname() {
        return Foodname;
    }

    public void setFoodname(String foodname) {
        Foodname = foodname;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getCalorie() {
        return Calorie;
    }

    public void setCalorie(String calorie) {
        Calorie = calorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoodCatName() {
        return foodCatName;
    }

    public void setFoodCatName(String foodCatName) {
        this.foodCatName = foodCatName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
