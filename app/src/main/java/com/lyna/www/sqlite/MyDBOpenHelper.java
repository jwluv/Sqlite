package com.lyna.www.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    private static final String name = "awe.db";
    private static final SQLiteDatabase.CursorFactory factory = null;
    private static final int version = 1;

    public MyDBOpenHelper(Context context) {
        super(context, name, factory, version);
    }

    public MyDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("*************   CREATE TABLE awe_country");
        db.execSQL("CREATE TABLE awe_country (_id INTEGER PRIMARY KEY AUTOINCREMENT, country TEXT, capital TEXT);");
        System.out.println("*************   CREATE TABLE awe_country");
        for(int i=0; i<10; i++){
            //db.execSQL("INSERT INTO awe_country VALUES( null, '" + "Country"+ i + "', '" + "Capital" + i + "');");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE awe_country ;");
        onCreate(db);
//        Toast.makeText(this.,"onUpgrade", Toast.LENGTH_LONG).show();
    }

    public void deleteRecord(SQLiteDatabase mdb, String country) {
        mdb.execSQL("DELETE FROM awe_country WHERE country='" + country + "';");
    }

}
