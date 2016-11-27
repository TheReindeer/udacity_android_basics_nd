package com.example.android.habittrackerapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.habittrackerapp.database.HabitContract.HabitEntry;

/**
 * Created by john on 10.11.2016.
 */

public class DatabaseManager {
    private HabitDbHelper mDbHelper;

    //Data to be inserted into database. Values can be taken from UI or from string xml values. Just null values in this project
    int workingTime;
    int learningTime;
    String learningSubject;
    int sportTime;
    String sportType;
    int relaxingTime;
    String relaxingType;
    int mealsNumber;
    int sleepingTime;

    //Insert method
    public void insertHabit(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_WORKING_TIME, workingTime);
        values.put(HabitEntry.COLUMN_LEARNING_TIME, learningTime);
        values.put(HabitEntry.COLUMN_LEARNING_SUBJECT, learningSubject);
        values.put(HabitEntry.COLUMN_SPORT_TIME, sportTime);
        values.put(HabitEntry.COLUMN_SPORT_TYPE, sportType);
        values.put(HabitEntry.COLUMN_RELAXING_TIME, relaxingTime);
        values.put(HabitEntry.COLUMN_RELAXING_TYPE, relaxingType);
        values.put(HabitEntry.COLUMN_MEALS_NUMBER, mealsNumber);
        values.put(HabitEntry.COLUMN_SLEEPING_TIME, sleepingTime);

        //Inserting values into database. Returned id not needed in this project
        db.insert(HabitEntry.TABLE_NAME, null, values);
    }

    //Reading method
    public Cursor readHabitsDatabase(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Define projection. It can be null when all the columns are returned
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_WORKING_TIME,
                HabitEntry.COLUMN_LEARNING_TIME,
                HabitEntry.COLUMN_LEARNING_SUBJECT,
                HabitEntry.COLUMN_SPORT_TIME,
                HabitEntry.COLUMN_SPORT_TYPE,
                HabitEntry.COLUMN_RELAXING_TIME,
                HabitEntry.COLUMN_RELAXING_TYPE,
                HabitEntry.COLUMN_MEALS_NUMBER,
                HabitEntry.COLUMN_SLEEPING_TIME
        };

        //Query for all the columns and rows in the database
        Cursor cursor = db.query(HabitEntry.TABLE_NAME, projection, null, null, null, null, null);

        //Return cursor. It will be needed when calling the read method, in order to read data from every/single column/row in the database.
        return cursor;
    }
}
