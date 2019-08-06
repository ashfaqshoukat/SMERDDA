package com.example.fyp.Extras;

import android.content.SharedPreferences;

public class PreferanceFile
{
    private static PreferanceFile preferanceFile=null;
    private static boolean isCompany;

    public static PreferanceFile getInstance(){
        if(preferanceFile==null){
            preferanceFile=new PreferanceFile();

        }
        return preferanceFile;
    }

    public   void setIsCompany(boolean status){
        isCompany=status;
    }

    public  boolean isIsCompany(){
        return isCompany;
    }


}
