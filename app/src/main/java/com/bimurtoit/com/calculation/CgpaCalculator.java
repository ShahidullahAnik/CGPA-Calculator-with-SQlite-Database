package com.bimurtoit.com.calculation;
import com.bimurtoit.com.model.Course;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

public class CgpaCalculator {

    private DecimalFormat precision = new DecimalFormat("0.00");

    public String calculatedCGPA(List<Course> courseList){
        double sum = 0;
        double total_credit = 0;
        double calculated_cgpa;
        for(int i=0; i<courseList.size(); i++){
            sum += courseList.get(i).getCourse_credit()*courseList.get(i).getObtain_gpa();
            total_credit += courseList.get(i).getCourse_credit();
        }
        calculated_cgpa = sum / total_credit;
        return courseList.isEmpty() ? "0.00":String.valueOf(precision.format(calculated_cgpa));
    }
    public Vector<Double> calculation(List<Course> courseList){
        Vector<Double> ret = new Vector<>();
        double sum = 0;
        double total_credit = 0;
        double calculated_cgpa;
        for(int i=0; i<courseList.size(); i++){
            sum += courseList.get(i).getCourse_credit()*courseList.get(i).getObtain_gpa();
            total_credit += courseList.get(i).getCourse_credit();
        }
        calculated_cgpa = sum / total_credit;
        ret.add(calculated_cgpa);
        ret.add(total_credit);
        return ret;
    }
}
