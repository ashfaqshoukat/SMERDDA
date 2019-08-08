package com.example.fyp.Models;

public class NewOrderModel {
    String cname,email,address,phoneNum,state;

    public NewOrderModel() {
    }

    public NewOrderModel(String cname, String email, String address, String phoneNum, String state) {
        this.cname = cname;
        this.email = email;
        this.address = address;
        this.phoneNum = phoneNum;
        this.state = state;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
