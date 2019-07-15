package com.example.fyp;

public class CompanyInfo {
    private String compName, email, password,companyId;

    public CompanyInfo() {
    }

    public CompanyInfo(String compName, String email, String password) {
        this.compName = compName;
        this.email = email;
        this.password = password;
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
}