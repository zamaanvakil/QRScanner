package com.example.android.qrscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zamaan on 16-02-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "qrscanner.db";
    public static final String TABLE_NAME = "scans_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "date";
    public static final String COL_3 = "type";
    public static final String COL_4 = "scan_result";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase
                .execSQL("create table "+ TABLE_NAME + "(id integer primary key autoincrement, date date, type text, scan_result text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String date,String type,String scan_result){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,date);
        contentValues.put(COL_3,type);
        contentValues.put(COL_4,scan_result);
        long result =sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }
    public  Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" order by id desc",null);
        return res;
    }
    public int deleteData(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,"ID = ?",new String[]{id});
    }
    public int getithID(int i){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select id from "+TABLE_NAME+" order by id desc limit 1",null);
        res.moveToFirst();
        res.moveToPosition(i);
        return res.getInt(0);
    }
}

