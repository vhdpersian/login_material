package com.example.administrator.login1;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 6/8/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 30;

    // Database Name
    private static final String DATABASE_NAME = "test1.db";

    // Table Names
    private static final String TABLE_USER = "user";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // User column names
    private static final String User_Name = "name";
    private static final String User_Email = "email";
    private static final String User_Pass = "password";


    private static final String CREATE_TABLE_User = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + User_Name
            + " TEXT," + User_Email + " Text," + User_Pass + " Text," + KEY_CREATED_AT
            + " DATETIME" + ")";

    private SQLiteDatabase database;
    public DatabaseHelper( final Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);

        //  String ROOT_DIR = context.getA getAbsolutePath();
        //String s = context.getDatabasePath(DATABASE_NAME).getPath();

      //  database=SQLiteDatabase.openOrCreateDatabase("/sdcard/"+DATABASE_NAME,null);
        //onCreate(database);
       // onCreate()
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables

        try
        {
            db.execSQL(CREATE_TABLE_User);
        }
         catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // create new tables
        onCreate(db);
    }

    public User getUser(String email, String pass) {

        // SQLiteDatabase  db= SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null );
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                KEY_ID,
                User_Email,
                User_Pass,
                KEY_CREATED_AT};


        Cursor c = db.query(
                TABLE_USER
                , projection
                , User_Email + "=? and " + User_Pass + "=?"
                , new String[]{String.valueOf(email), String.valueOf(pass)}
                , null, null, null, null);


        if (c.moveToFirst()) {

            User user = new User();
            user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            user.setName(c.getString(c.getColumnIndex(User_Name)));
            user.setEmail(c.getString(c.getColumnIndex(User_Name)));
            user.setPass(c.getString(c.getColumnIndex(User_Pass)));
            user.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

            return user;

        } else {
            return null;
        }

    }

    public long addUser(String email, String name, String pass) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User_Email, email);
        values.put(User_Name, name);
        values.put(User_Pass, pass);
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long user_id = db.insert(TABLE_USER, null, values);

        return user_id;


    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}



