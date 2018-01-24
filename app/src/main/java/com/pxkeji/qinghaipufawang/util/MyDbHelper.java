package com.pxkeji.qinghaipufawang.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/1/22.
 */

public class MyDbHelper extends SQLiteOpenHelper {

    public static final String CREATE_USER = "create table users ("
            + "id integer, "
            + "name text, "
            + "gender text, "
            + "intro text, "
            + "year integer, "
            + "month integer, "
            + "day integer)";

    private Context mContext;

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        LogUtil.w("MyDbHelper", "database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
        onCreate(db);
    }
}
