package com.shwetadsa.bets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by Shweta on 02-04-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    public DatabaseHelper(Context context) {
        super(context, "usersdb", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user (fname text, lname text, email text, contact text, location text, age integer, uname text primary key, pass text, amount integer)";
        String sql1 = "create table books (bid integer PRIMARY KEY AUTOINCREMENT, uname text, bname text, bauthname text, genre text, image BLOB)";
        db.execSQL(sql);
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists books");
        onCreate(db);
    }


    public boolean insert(String fname, String lname, String email, String contact, String location, int age, String uname, String pass, int amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("fname", fname);
        cv.put("lname", lname);
        cv.put("email", email);
        cv.put("contact", contact);
        cv.put("location", location);
        cv.put("age", age);
        cv.put("uname", uname);
        cv.put("pass", pass);
        cv.put("amount", amount);
        long ins = db.insert("user", null, cv);
        if (ins == -1)  return false;
        else    return true;
    }

    public boolean chkemail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from user where email=?", new String[]{email});
        if (c.getCount()>0) return false;
        else return true;
    }

    public boolean chkuname(String uname){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from user where uname=?", new String[]{uname});
        if (cur.getCount()>0) return false;
        else return true;
    }

    public boolean chkunamepass(String uname,String pass){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from user where uname=? and pass=?", new String[]{uname,pass});
        if (c.getCount()>0) return true;
        else return false;
    }

    public String getfname(String uname){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select fname from user where uname=?", new String[]{uname});
        c.moveToFirst();
        return c.getString(0);
    }

    public String getlname(String uname){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select lname from user where uname=?", new String[]{uname});
        c.moveToFirst();
        return c.getString(0);
    }

    public boolean insertData(String uname, String bname, String bauthname, String genre, byte[] image){
        SQLiteDatabase db = this.getWritableDatabase();

        /*SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(0, bname);
        statement.bindString(1, bauthname);
        statement.bindString(2, genre);
        statement.bindBlob(3, image);
        statement.executeInsert(); */

        ContentValues cv1 = new ContentValues();
        cv1.put("uname", uname);
        cv1.put("bname", bname);
        cv1.put("bauthname", bauthname);
        cv1.put("genre", genre);
        cv1.put("image", image);
        long ins1 = db.insert("books", null, cv1);
        if (ins1 == -1)  return false;
        else    return true;

    }
}
