package com.bimurtoit.com.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bimurtoit.com.adapter.CourseAdapterForCourseListActivity;
import com.bimurtoit.com.calculatecgpa.R;
import com.bimurtoit.com.calculation.CgpaCalculator;
import com.bimurtoit.com.database.CourseOperation;
import com.bimurtoit.com.database.SemesterOperation;
import com.bimurtoit.com.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class CourseListActivity extends AppCompatActivity implements DialogActivity.ClickListenerForCourseListActivity{

    TextView cgpa_display;
    List<Course> courseList;
    SemesterOperation semesterOperation;
    CourseOperation courseOperation;
    RecyclerView recyclerView_course_list;
    LinearLayoutManager linearLayoutManager;
    Long mId;
    CourseAdapterForCourseListActivity courseAdapter;
    CgpaCalculator cgpaCalculator;
    DialogActivity dialogActivity;
    Button btn_addCourse;
    CardView input_module;
    String cgpa_total;
    Vector<Double> values;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Intent intent = getIntent();
        mId = intent.getLongExtra("mId", -1);
        initialize();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void initialize(){
        cgpa_display = findViewById(R.id.show_cgpa_course_list);
        btn_addCourse = findViewById(R.id.dialog_btnAdd);
        input_module = findViewById(R.id.cardView_inputModule);
        courseList = new ArrayList<>();
        courseOperation = new CourseOperation(this);
        semesterOperation = new SemesterOperation(this);
        recyclerView_course_list = findViewById(R.id.recyclerView_courseList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_course_list.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_course_list.setHasFixedSize(true);
        courseList = courseOperation.getCoursesBySemesterId(mId);
        courseAdapter = new CourseAdapterForCourseListActivity(this, courseList);
        recyclerView_course_list.setAdapter(courseAdapter);
        cgpaCalculator = new CgpaCalculator();
        dialogActivity = new DialogActivity(CourseListActivity.this);
        DialogActivity.setListenerForCourseListActivity(CourseListActivity.this);
        setTotalCGPA();
    }

    @Override
    public void addNewCourse(Course course) {
        courseList.add(course);
        Toast.makeText(this, "Course added at the bottom", Toast.LENGTH_SHORT).show();
        courseAdapter.notifyDataSetChanged();
        Vector<Double> values = cgpaCalculator.calculation(courseList);
        double calculated_cgpa = values.elementAt(0);
        double total_credit = values.elementAt(1);
        setTotalCGPA();
        semesterOperation.updateSemester(mId, calculated_cgpa,total_credit,courseList.size());
    }

    @Override
    public void updateList(int position, String courseName, double credit, double gpa) {
        Log.d("Anik", ""+courseName);
        courseList.set(position, new Course(courseName, credit, gpa));
        courseAdapter.notifyItemChanged(position);
        Vector<Double> values = cgpaCalculator.calculation(courseList);
        double calculated_cgpa = values.elementAt(0);
        double total_credit = values.elementAt(1);
        setTotalCGPA();
        semesterOperation.updateSemester(mId, calculated_cgpa,total_credit,courseList.size());

    }

    @Override
    public void removeItem(int position) {
        try {
            courseList.remove(position);
            courseAdapter.notifyItemRemoved(position);
            setTotalCGPA();
            Vector<Double> values = cgpaCalculator.calculation(courseList);
            double calculated_cgpa = values.elementAt(0);
            double total_credit = values.elementAt(1);
            setTotalCGPA();
            if(!courseList.isEmpty())
                semesterOperation.updateSemester(mId, calculated_cgpa,total_credit,courseList.size());
            else
                semesterOperation.updateSemester(mId, 0,0,0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Anik", ""+e);
        }
    }
    public void setTotalCGPA(){
        cgpa_total = cgpaCalculator.calculatedCGPA(courseList);
        cgpa_display.setText(cgpa_total);
    }

    public void addNewCourseFromActivity(View view) {
        dialogActivity.showDialogForAddNewSemesterCourseListActivity(mId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }

}
