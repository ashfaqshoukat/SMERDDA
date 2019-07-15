package com.example.fyp;

import io.realm.RealmObject;

public class CompnayNameRealm extends RealmObject
{
    public String getsCompnayName()
    {
        return sCompnayName;
    }

    public void setsCompnayName(String sCompnayName)
    {
        this.sCompnayName = sCompnayName;
    }

    String sCompnayName;
}
