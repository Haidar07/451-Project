package com.example.test1451;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME="Login.db";
    public static final String TableName1="users";
    public static final String TableName2="DataSnapshots";
    private static final String COL1 = "Timestamp";
    private static final String COL2 = "Operator";
    private static final String COL3 = "SignalPower";
    private static final String COL4 = "SNR";
    private static final String COL5 = "NetworkType";
    private static final String COL6 = "FrequencyBand";
    private static final String COL7 = "CellID";

    public DBHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TableName1 + " (username TEXT primary key, password TEXT)");
        db.execSQL("create table " + TableName2 + " (ID INTEGER primary key autoincrement, " + COL1 + " TEXT, " + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 + " TEXT, " + COL7 + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists users");
    }

    public Boolean insertRegistrationData(String username, String password){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();

        values.put("username",username);
        values.put("password",password);

        long result=db.insert("users",null,values);
        if(result==-1) return false;
        else return true;

    }

    public Boolean checkusername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=?",new String[] {username});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?",new String[] {username,password});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    public boolean addData(String timestamp, String operator, String signalPower, String snr, String networkType, String frequencyBand, String cellID) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, timestamp);
        contentValues.put(COL2, operator);
        contentValues.put(COL3, signalPower);
        contentValues.put(COL4, snr);
        contentValues.put(COL5, networkType);
        contentValues.put(COL6, frequencyBand);
        contentValues.put(COL7, cellID);

        long result = db.insert(TableName2, null, contentValues);

        if (result == -1) return false;
        else return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TableName2;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getData(String Date) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TableName2 + " WHERE timestamp ='" + Date + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}

