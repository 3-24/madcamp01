package com.minus21.mainapp.ui.main;

public class ContactInfo {
    private int info_id;
    private String Name;
    private String phNumber;

    public int getInfo_id() {
        return info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public ContactInfo(int info_id, String name, String phNumber) {
        this.info_id = info_id;
        Name = name;
        this.phNumber = phNumber;
    }
}