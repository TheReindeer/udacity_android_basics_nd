package com.example.android.habittrackerapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.habittrackerapp.database.HabitContract.HabitEntry;

/**
 * Created by john on 10.11.2016.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    //Database name
    private static final String DATABASE_NAME = "habits.db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    public HabitDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create the String that contains the SQL statement to create the habits table
        String SQL_CREATE_HABITS_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + "(" +
                HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HabitEntry.COLUMN_WORKING_TIME + " INTEGER NOT NULL DEFAULT 8, " +
                HabitEntry.COLUMN_LEARNING_TIME + " INTEGER NOT NULL DEFAULT 0, " +
                HabitEntry.COLUMN_LEARNING_SUBJECT + " TEXT, " +
                HabitEntry.COLUMN_SPORT_TIME + " INTEGER NOT NULL DEFAULT 0, " +
                HabitEntry.COLUMN_SPORT_TYPE + " TEXT, " +
                HabitEntry.COLUMN_RELAXING_TIME + " INTEGER NOT NULL DEFAULT 0, " +
                HabitEntry.COLUMN_RELAXING_TYPE + " TEXT, " +
                HabitEntry.COLUMN_MEALS_NUMBER + " INTEGER NOT NULL DEFAULT 3, " +
                HabitEntry.COLUMN_SLEEPING_TIME + " INTEGER NOT NULL DEFAULT 8);";

        //Create the habits table
        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
