package com.example.fyp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperProfile extends SQLiteOpenHelper
{

    static String sDatabaseName = "StoreCompnay";
    static String sTableName = "CompnayTBL";

    String sQuery = "";

    private ContentValues cValues;
    private SQLiteDatabase dataBase = null;
    private Cursor cursor;


    public static String sCompnayName = "CompnayName";



    public HelperProfile(Context context)
    {
        super(context, context.getExternalFilesDir(null).getAbsolutePath()
                + "/" + sDatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        sQuery = "CREATE TABLE " + sTableName + "(" + sCompnayName
                + " TEXT)";


        db.execSQL(sQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void InsertCompnayName(String Name)
    {
        dataBase = getWritableDatabase();
        cValues = new ContentValues();

        cValues.put(sCompnayName, Name);



        dataBase.insert(HelperProfile.sTableName, null, cValues);
        dataBase.close();
    }

    public Cursor SelectName() {

        dataBase = getReadableDatabase();

        cursor = dataBase.rawQuery("SELECT * FROM " + sTableName, null);
        return cursor;
    }

    public void DeleteName() {

        dataBase = getReadableDatabase();
        dataBase.execSQL("DELETE FROM CompnayTBL ");

    }
}
