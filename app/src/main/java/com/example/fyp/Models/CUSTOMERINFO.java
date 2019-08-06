package com.example.fyp.Models;

public class CUSTOMERINFO {
    public String username, email, phone, address, password, profileimage;

    public CUSTOMERINFO() {
    }

    public CUSTOMERINFO(String username, String email, String phone, String address, String password) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
        this.profileimage = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
