package com.example.fyp.Models;

public class COMPANY {
    public String comp,about;

    public COMPANY() {
    }

    public COMPANY(String comp, String about) {
        this.comp = comp;
        this.about = about;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
