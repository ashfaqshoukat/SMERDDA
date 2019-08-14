package com.example.fyp.Models;

public class COMPANYINFO {
    private String FCMToken="",compName, email, password,companyId,profileimage,phonenbr,about;

    public COMPANYINFO() {
    }

    public COMPANYINFO(String compName, String email, String password) {
        this.compName = compName;
        this.email = email;
        this.password = password;
    }

    public String getFCMToken() {
        return FCMToken;
    }

    public void setFCMToken(String FCMToken) {
        this.FCMToken = FCMToken;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getPhonenbr() {
        return phonenbr;
    }

    public void setPhonenbr(String phonenbr) {
        this.phonenbr = phonenbr;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}