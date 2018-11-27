package com.bimurtoit.com.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {


    String path;
    //Course Schema Design
    public static final String TABLE_NAME_CGPA = "TABLE_GPA";
    public static final String UID_COURSE = "_id";
    public static final String COLUMN_CGPA_COURSE_NAME = "COURSE_NAME";
    public static final String COLUMN_CGPA_COURSE_CREDIT = "COURSE_CREDIT";
    public static final String COLUMN_CGPA_COURSE_GPA = "COURSE_GPA";
    public static final String COLUMN_CGPA_SEMESTER_ID = "SEMESTER_ID";
    private static final String TABLE_CREATE_CGPA = "CREATE TABLE "+TABLE_NAME_CGPA+" ("
            +UID_COURSE +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_CGPA_COURSE_NAME+" VARCHAR(255), "
            +COLUMN_CGPA_COURSE_CREDIT+" REAL NOT NULL, "
            +COLUMN_CGPA_COURSE_GPA+" REAL NOT NULL, "
            +COLUMN_CGPA_SEMESTER_ID+" INTEGER NOT NULL "
            +");";
    private static final String DROP_TABLE_CGPA = "DROP TABLE IF EXISTS "+TABLE_NAME_CGPA;

    //Semester Schema Design
    public static final String TABLE_NAME_SEMESTER = "TABLE_SEMESTER";
    public static final String UID_SEMESTER = "_id";
    public static final String COLUMN_SEMESTER_NAME = "SEMESTER_NAME";
    public static final String COLUMN_SEMESTER_GPA = "SEMESTER_GPA";
    public static final String COLUMN_SEMESTER_TOTAL_COURSE = "SEMESTER_TOTAL_COURSE";
    public static final String COLUMN_SEMESTER_TOTAL_CREDIT = "SEMESTER_TOTAL_CREDIT";
    private static final String TABLE_CREATE_SEMESTER = "CREATE TABLE "+TABLE_NAME_SEMESTER+" ("
            +UID_SEMESTER+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COLUMN_SEMESTER_NAME+" VARCHAR(255), "
            +COLUMN_SEMESTER_GPA+" REAL NOT NULL, "
            +COLUMN_SEMESTER_TOTAL_COURSE+" INTEGER NOT NULL, "
            +COLUMN_SEMESTER_TOTAL_CREDIT+" REAL NOT NULL "
            +");";
    private static final String DROP_TABLE_SEMESTER = "DROP TABLE IF EXISTS "+TABLE_NAME_SEMESTER;


    private static final String DATABASE_NAME = "myCgpaCalculation.db";
    private static final int DATA_BASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(TABLE_CREATE_SEMESTER);
            sqLiteDatabase.execSQL(TABLE_CREATE_CGPA);
            Log.d("Anik", "Database Created");
        } catch (SQLException e) {
            Log.d("Anik", ""+e);
            e.printStackTrace();
        }
        path = sqLiteDatabase.getPath();
        Log.d("Anik", path);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_SEMESTER);
        sqLiteDatabase.execSQL(DROP_TABLE_CGPA);

        sqLiteDatabase.execSQL(TABLE_CREATE_SEMESTER);
        sqLiteDatabase.execSQL(TABLE_CREATE_CGPA);
    }

}
