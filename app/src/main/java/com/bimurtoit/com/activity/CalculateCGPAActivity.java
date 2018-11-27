package com.bimurtoit.com.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bimurtoit.com.adapter.CourseAdapterForCalculateCgpaActivity;
import com.bimurtoit.com.calculatecgpa.R;
import com.bimurtoit.com.calculation.CgpaCalculator;
import com.bimurtoit.com.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CalculateCGPAActivity extends AppCompatActivity implements DialogActivity.ClickListenerForCalculateCgapActivity {

    TextView cgpa_display;
    EditText courseName, courseCredit, courseGPA;
    Button add_course_btn;
    RecyclerView recyclerView_course_list;
    FloatingActionButton fab_save;
    CourseAdapterForCalculateCgpaActivity courseAdapter;
    List<Course> courseList;
    LinearLayoutManager linearLayoutManager;
    CgpaCalculator cgpaCalculator;
    DialogActivity dialogActivity;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculatecgpa);
        initialize_view();

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course = courseName.getText().toString();
                if(course.isEmpty()){
                    course = "Course Name?";
                }
                try {
                    double mCredit = Double.parseDouble(courseCredit.getText().toString());
                    double mCgpa = Double.parseDouble(courseGPA.getText().toString());
                    courseList.add(new Course(course,mCredit,mCgpa));
                    courseAdapter.notifyDataSetChanged();
                    setTotalCGPA();
                    courseGPA.setText("");
                    courseName.setText("");
                } catch (NumberFormatException e) {
                    Toast.makeText(CalculateCGPAActivity.this,"Please fill all the fields carefully",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogActivity = new DialogActivity(CalculateCGPAActivity.this);
                dialogActivity.showDialogSaveSemesterForCalculateCgpaActivity(courseList);
            }
        });
    }
    private void initialize_view(){
        cgpa_display = findViewById(R.id.show_cgpa_calculateCGPA);
        courseName = findViewById(R.id.autoCompleteTextView_courseName);
        courseCredit = findViewById(R.id.credit_home);
        courseGPA = findViewById(R.id.gpa_home);
        add_course_btn = findViewById(R.id.btnAdd_home);
        recyclerView_course_list = findViewById(R.id.recyclerView_courseList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_course_list.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_course_list.setHasFixedSize(true);
        fab_save = findViewById(R.id.floatingActionButton_save);
        courseList = new ArrayList<>();
        cgpaCalculator = new CgpaCalculator();
        courseAdapter = new CourseAdapterForCalculateCgpaActivity(this, courseList);
        DialogActivity.setListenerForCalculateCgpaActivity(CalculateCGPAActivity.this);
        recyclerView_course_list.setAdapter(courseAdapter);
    }

    @Override
    public void updateList(int position, String courseName, double credit, double gpa) {
        Log.d("Anik", ""+courseName);
        courseList.set(position, new Course(courseName, credit, gpa));
        courseAdapter.notifyItemChanged(position);
        setTotalCGPA();
    }
    @Override
    public void removeItem(int position) {
        courseList.remove(position);
        courseAdapter.notifyItemRemoved(position);
        setTotalCGPA();
    }

    @Override
    public void clearDisplay() {
        cgpa_display.setText("0.00");
        courseList.clear();
        courseGPA.setText("");
        courseName.setText("");
        courseCredit.setText("");
        courseAdapter.notifyDataSetChanged();
        finish();
    }

    public void setTotalCGPA(){
        String cgpa = cgpaCalculator.calculatedCGPA(courseList);
        cgpa_display.setText(cgpa);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_calculatecgpa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        else if(id == R.id.clear_display){
            clear_display();
            Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void clear_display(){
        cgpa_display.setText("0.00");
        courseList.clear();
        courseGPA.setText("");
        courseName.setText("");
        courseCredit.setText("");
        courseAdapter.notifyDataSetChanged();
    }
}
