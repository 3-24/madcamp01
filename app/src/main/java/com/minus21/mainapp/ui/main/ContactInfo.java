package com.minus21.mainapp.ui.main;

public class ContactInfo {
    private String Name;
    private String phNumber;
    private String email;
    private String note;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ContactInfo(String name, String phNumber, String email, String note) {
        Name = name;
        this.phNumber = phNumber;
        this.email = email;
        this.note = note;
    }
}