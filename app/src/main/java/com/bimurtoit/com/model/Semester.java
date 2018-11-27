package com.bimurtoit.com.model;

public class Semester {

    private long mId;
    private String semester_name;
    private double total_gpa;
    private int total_course;
    private double total_credit;


    public Semester() {

    }
    public Semester(long mId, String semester_name, double total_gpa, int total_course, double total_credit) {
        this.mId = mId;
        this.semester_name = semester_name;
        this.total_gpa = total_gpa;
        this.total_course = total_course;
        this.total_credit = total_credit;
    }

    public long getmId() {
        return mId;
    }

    public void setmId(long mId) {
        this.mId = mId;
    }

    public String getSemester_name() {
        return semester_name;
    }

    public void setSemester_name(String semester_name) {
        this.semester_name = semester_name;
    }

    public double getTotal_gpa() {
        return total_gpa;
    }

    public void setTotal_gpa(double total_gpa) {
        this.total_gpa = total_gpa;
    }

    public int getTotal_course() {
        return total_course;
    }

    public void setTotal_course(int total_course) {
        this.total_course = total_course;
    }

    public double getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(double total_credit) {
        this.total_credit = total_credit;
    }
}
