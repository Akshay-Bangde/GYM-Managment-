package com.example.smile.dataObjects;

import java.io.Serializable;

public class Equipment_Model implements Serializable {

    String eSrno;
    String etransaction_id;
    String eName;
    String eDescription;
    String eRate;
    String eQty;
    String eNetamt;
    String eSerialid;
    String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String geteSerialid() {
        return eSerialid;
    }

    public void seteSerialid(String eSerialid) {
        this.eSerialid = eSerialid;
    }

    int edtimg;
    int deleteimg;


    public String geteSrno() {
        return eSrno;
    }

    public void seteSrno(String eSrno) {
        this.eSrno = eSrno;
    }

    public String getEtransaction_id() {
        return etransaction_id;
    }

    public void setEtransaction_id(String etransaction_id) {
        this.etransaction_id = etransaction_id;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteDescription() {
        return eDescription;
    }

    public void seteDescription(String eDescription) {
        this.eDescription = eDescription;
    }

    public String geteRate() {
        return eRate;
    }

    public void seteRate(String eRate) {
        this.eRate = eRate;
    }

    public String geteQty() {
        return eQty;
    }

    public void seteQty(String eQty) {
        this.eQty = eQty;
    }

    public String geteNetamt() {
        return eNetamt;
    }

    public void seteNetamt(String eNetamt) {
        this.eNetamt = eNetamt;
    }

    public int getEdtimg() {
        return edtimg;
    }

    public void setEdtimg(int edtimg) {
        this.edtimg = edtimg;
    }

    public int getDeleteimg() {
        return deleteimg;
    }

    public void setDeleteimg(int deleteimg) {
        this.deleteimg = deleteimg;
    }
}
