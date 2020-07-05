package com.example.smile.dataObjects;

import java.io.Serializable;

public class Product_List_Model implements Serializable {



    String id;
    String  srno;

    String proname;
    String ppNo;
    String sName;
    String description;
    String unitRate;
    String proQty;
    String proSubtotal;
    String selectTax;
    String taxPercent;
    String taxAmount;
    String grandTotal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getPpNo() {
        return ppNo;
    }

    public void setPpNo(String ppNo) {
        this.ppNo = ppNo;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(String unitRate) {
        this.unitRate = unitRate;
    }

    public String getProQty() {
        return proQty;
    }

    public void setProQty(String proQty) {
        this.proQty = proQty;
    }

    public String getProSubtotal() {
        return proSubtotal;
    }

    public void setProSubtotal(String proSubtotal) {
        this.proSubtotal = proSubtotal;
    }

    public String getSelectTax() {
        return selectTax;
    }

    public void setSelectTax(String selectTax) {
        this.selectTax = selectTax;
    }

    public String getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        this.taxPercent = taxPercent;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }
}
