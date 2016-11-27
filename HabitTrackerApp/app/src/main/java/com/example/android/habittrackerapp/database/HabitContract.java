package com.example.android.habittrackerapp.database;

import android.provider.BaseColumns;

/**
 * Created by john on 10.11.2016.
 */

public final class HabitContract  {

    public static abstract class HabitEntry implements BaseColumns{

        //Names of table and columns
        public static final String TABLE_NAME = "habits";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_WORKING_TIME = "working_time";//hours spent at work (aprox.)
        public static final String COLUMN_LEARNING_TIME = "learning_time";//hours spent learning (aprox.)
        public static final String COLUMN_LEARNING_SUBJECT = "learning_subject";
        public static final String COLUMN_SPORT_TIME = "sport_time";//hours spent practicing sport (aprox.)
        public static final String COLUMN_SPORT_TYPE = "sport_type";
        public static final String COLUMN_RELAXING_TIME = "relaxing_time";//hours spent relaxing (aprox.)
        public static final String COLUMN_RELAXING_TYPE = "relaxing_type";
        public static final String COLUMN_MEALS_NUMBER = "meals";//number of meals taken
        public static final String COLUMN_SLEEPING_TIME = "sleeping_time";//hours of sleep
    }
}
