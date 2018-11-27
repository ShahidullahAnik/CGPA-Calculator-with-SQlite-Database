package com.bimurtoit.com.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bimurtoit.com.adapter.SemesterAdapter;
import com.bimurtoit.com.calculatecgpa.R;
import com.bimurtoit.com.calculation.CgpaCalculator;
import com.bimurtoit.com.database.CourseOperation;
import com.bimurtoit.com.database.SemesterOperation;
import com.bimurtoit.com.model.Course;
import com.bimurtoit.com.model.Semester;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResultBookActivity extends AppCompatActivity implements DialogActivity.ClickListenerForResultBookActivity{
    SemesterOperation semesterOperation;
    CourseOperation courseOperation;
    RecyclerView recyclerView_semester_list;
    LinearLayoutManager linearLayoutManager;
    CgpaCalculator cgpaCalculator;
    List<Semester> semesterList;
    List<Course> courseList;
    SemesterAdapter semesterAdapter;
    DialogActivity dialogActivity;
    TextView cgpa_header;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_book);
        initialize_view();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(semesterList.isEmpty())
        {
            Toast.makeText(this, "Empty List", Toast.LENGTH_LONG).show();
        }
    }
    private void initialize_view(){
        semesterList = new ArrayList<>();
        courseList = new ArrayList<>();
        cgpa_header = findViewById(R.id.show_cgpa_result_book);
        semesterOperation = new SemesterOperation(this);
        courseOperation = new CourseOperation(this);
        courseList = courseOperation.getAllCourses();
        cgpaCalculator = new CgpaCalculator();
        String cgpa_total = cgpaCalculator.calculatedCGPA(courseList);
        cgpa_header.setText(cgpa_total);
        semesterList = semesterOperation.getAllSemester();
        recyclerView_semester_list = findViewById(R.id.recyclerview_semester);
        linearLayoutManager = new LinearLayoutManager(ResultBookActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_semester_list.setLayoutManager(linearLayoutManager);
        recyclerView_semester_list.setHasFixedSize(true);
        semesterAdapter = new SemesterAdapter(ResultBookActivity.this, semesterList);
        recyclerView_semester_list.setAdapter(semesterAdapter);
        dialogActivity = new DialogActivity(ResultBookActivity.this);
        DialogActivity.setListenerForResultBookActivity(this);
    }
    public void addNewSemester(View view) {
        dialogActivity.showDialogSveSemesterForResultBookActivity();
    }
    @Override
    public void saveNewSemester(Semester newSemester) {
        semesterList.add(newSemester);
        semesterAdapter.notifyDataSetChanged();
        Log.d("Anik", "Data saved");
    }
    @Override
    public void refreshListAfterDeleteSemester(Semester semester, int position) {
        semesterList.remove(semester);
        semesterAdapter.notifyItemRemoved(position);
        courseList.clear();;
        courseList = courseOperation.getAllCourses();
        String cgpa_total = cgpaCalculator.calculatedCGPA(courseList);
        cgpa_header.setText(cgpa_total);
        Toast.makeText(this,"Delete",Toast.LENGTH_SHORT).show();
        Log.d("Anik", "Data deleted");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        initialize_view();
    }
}

