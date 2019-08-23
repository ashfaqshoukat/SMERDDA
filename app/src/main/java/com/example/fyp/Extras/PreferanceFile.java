package com.example.fyp.Extras;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fyp.Models.COMPANYINFO;
import com.example.fyp.Models.CUSTOMERINFO;
import com.google.gson.Gson;

import java.util.Calendar;

public class PreferanceFile {
    private static PreferanceFile preferanceFile = null;
    private static SharedPreferences preferences = null;
    private static SharedPreferences.Editor editor = null;
    private boolean isCompany;

    public static PreferanceFile getInstance(Context context) {
        if (preferanceFile == null || editor == null || preferences == null) {
            preferences = context.getSharedPreferences("user", 0);
            editor = preferences.edit();
            preferanceFile = new PreferanceFile();

        }
        return preferanceFile;
    }

    public void setIsCompany(boolean status) {
        isCompany = status;
    }

    public boolean isIsCompany() {
        return isCompany;
    }

    public void setCustomerinfo(CUSTOMERINFO customerinfo) {
        editor.putString("user", new Gson().toJson(customerinfo));
        save();
    }

    public CUSTOMERINFO getCustomer() {
        return new Gson().fromJson(preferences.getString("user", null), CUSTOMERINFO.class);
    }


    public void setCompanyinfo(COMPANYINFO companyinfo) {
        editor.putString("user", new Gson().toJson(companyinfo));
        save();
    }

    public COMPANYINFO getCompany() {
        return new Gson().fromJson(preferences.getString("user", null), COMPANYINFO.class);
    }
    public void deletePreferance() {
        preferences.edit().clear();
    }

    private void save() {
        editor.apply();
    }


    public   String miliToDate(String timeStamp){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timeStamp));

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        return mDay+"-"+mMonth+"-"+mYear;
    }
}
