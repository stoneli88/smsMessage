package com.intemplate.smservice.models;

import java.io.Serializable;

public class Phone implements Serializable {
    private int phoneID;
    private String phoneNumber;

    public Phone(int phoneID, String phoneNumber) {
        this.phoneID = phoneID;
        this.phoneNumber = phoneNumber;
    }

    public int getPhoneID() {
        return phoneID;
    }

    public void setPhoneID(int phoneID) {
        this.phoneID = phoneID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
