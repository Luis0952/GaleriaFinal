package com.app.jhon.galeriafinal.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.jhon.galeriafinal.Utilities.Constants;

/**
 * Created by David on 28/11/2017.
 */


public class SqliteHelper extends SQLiteOpenHelper {

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE_USERS);
        db.execSQL(Constants.CREATE_TABLE_PHOTOS);
        db.execSQL(Constants.CREATE_TABLE_FAVORITE);
        db.execSQL(Constants.CREATE_TABLE_COMMENTS);
        //db.execSQL("INSERT INTO users (name, phone, email, password) VALUES ('aaaaa', '11111', '1@gmail.com','1234')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLA_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLA_NAME_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLA_NAME_FAVORITE);
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLA_NAME_COMMENTS);
        onCreate(db);
    }
}
