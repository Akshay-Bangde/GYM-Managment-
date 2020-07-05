package com.example.smile.dataObjects;

import java.io.Serializable;

public class FoodCategoryListModel implements Serializable {

    String id;
    String serial_No;
    String Category;
    String Name;
    String Total_Calorie;
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTotal_Calorie() {
        return Total_Calorie;
    }

    public void setTotal_Calorie(String total_Calorie) {
        Total_Calorie = total_Calorie;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
