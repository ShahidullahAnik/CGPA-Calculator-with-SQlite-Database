package com.bimurtoit.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bimurtoit.com.model.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseOperation {

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] mAllColumns = {DBHelper.UID_COURSE,
            DBHelper.COLUMN_CGPA_COURSE_NAME,
            DBHelper.COLUMN_CGPA_COURSE_CREDIT,
            DBHelper.COLUMN_CGPA_COURSE_GPA,
            DBHelper.COLUMN_CGPA_SEMESTER_ID };
    public CourseOperation(Context context) {
        dbHelper = new DBHelper(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void open() throws SQLException {
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }
    /*public void close() {
        dbHelper.close();
        Log.d("Anik", "cgpa db close");
    }*/
    public Course createCourse(String course_name, double course_credit, double gpa, Long semesterId) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_CGPA_COURSE_NAME, course_name);
        values.put(DBHelper.COLUMN_CGPA_COURSE_CREDIT, course_credit);
        values.put(DBHelper.COLUMN_CGPA_COURSE_GPA, gpa);
        values.put(DBHelper.COLUMN_CGPA_SEMESTER_ID, semesterId);
        long insertId = sqLiteDatabase.insert(DBHelper.TABLE_NAME_CGPA,null,values);
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_CGPA, mAllColumns,
                DBHelper.UID_COURSE + " = " + insertId, null, null,
                null, null);
        if(cursor == null){
            Log.d("Anik", "null "+cursor);
        }
        else{
            Log.d("Anik", "not null");
        }
        cursor.moveToFirst();
        Course newCourse = cursorToCourse(cursor);
        cursor.close();
        return newCourse;
    }
    public List<Course> getCoursesBySemesterId(Long semesterId){
        List<Course> coursesBySemesterId = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_NAME_CGPA, mAllColumns,
                DBHelper.COLUMN_CGPA_SEMESTER_ID + " = ?",
                new String[] { String.valueOf(semesterId) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Course courses = cursorToCourse(cursor);
                coursesBySemesterId.add(courses);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return coursesBySemesterId;
    }
    public List<Course> getAllCourses(){
        List<Course> allCourse = new ArrayList<>();
        Cursor cursor =
                sqLiteDatabase.query(DBHelper.TABLE_NAME_CGPA,mAllColumns,null,null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Course courses = cursorToCourse(cursor);
                allCourse.add(courses);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return allCourse;
    }
    public void deleteCourse(Course course) {
        long id = course.getmId();
        sqLiteDatabase.delete(DBHelper.TABLE_NAME_CGPA, DBHelper.UID_COURSE
                + " = " + id, null);
    }

    public void updateCourse(Course course, String mCourseName, double mCredit, double mGpa){
        long id = course.getmId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_CGPA_COURSE_NAME,mCourseName);
        contentValues.put(DBHelper.COLUMN_CGPA_COURSE_CREDIT,mCredit);
        contentValues.put(DBHelper.COLUMN_CGPA_COURSE_GPA,mGpa);
        String[] whereArgs = {String.valueOf(id)};
        sqLiteDatabase.update(DBHelper.TABLE_NAME_CGPA,contentValues, DBHelper.UID_COURSE+" =? ",whereArgs);
    }
    private Course cursorToCourse(Cursor cursor) {
        Course course = new Course();
        course.setmId(cursor.getLong(0));
        course.setCourse_name(cursor.getString(1));
        course.setCourse_credit(cursor.getDouble(2));
        course.setObtain_gpa(cursor.getDouble(3));
        course.setSemester_id(cursor.getInt(4));
        return course;
    }
}
