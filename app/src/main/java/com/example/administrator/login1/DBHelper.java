package com.example.administrator.login1;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by V_Karimi on 6/23/2016.
 */
public class DBHelper {

    private static final String TAG                  = "DatabaseHelper";

    public static final String  DATABASE_FILE_PATH = "/sdcard/";

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


    public DBHelper()
    {
        try
        {
            database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH
                     +DATABASE_NAME, null,SQLiteDatabase.OPEN_READWRITE);
        }
        catch (SQLiteException ex)
        {
            Log.e(TAG, "error -- " + ex.getMessage(), ex);
            // error means tables does not exits
            createTables();
        }
        finally
        {
            //close();
            //DBUtil.safeCloseDataBase(database);
        }
    }

    private void createTables()
    {
        try
        {
            database.execSQL(CREATE_TABLE_User);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void close()
    {
        database.close();
        // DBUtil.safeCloseDataBase(database);
    }

    public SQLiteDatabase getReadableDatabase()
    {
        database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH
                        + File.separator + DATABASE_NAME, null,
                SQLiteDatabase.OPEN_READONLY);
        return database;
    }

    public SQLiteDatabase getWritableDatabase()
    {
        database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH
                        + File.separator + DATABASE_NAME, null,
                SQLiteDatabase.OPEN_READWRITE);
        return database;
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

}
