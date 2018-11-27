package com.bimurtoit.com.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bimurtoit.com.calculatecgpa.R;
import com.bimurtoit.com.calculation.CgpaCalculator;
import com.bimurtoit.com.database.CourseOperation;
import com.bimurtoit.com.database.SemesterOperation;
import com.bimurtoit.com.model.Course;
import com.bimurtoit.com.model.Semester;
import java.util.List;
import java.util.Vector;

public class DialogActivity {

    private Context context;
    private EditText courseNameDialog, creditDialog, gpaDialog, saveSemester;
    private String mCourseName;
    private double mCredit, mGpa;
    private Button btn_add_dialog;
    private int itemPosition;
    private CgpaCalculator cgpaCalculator;
    private CourseOperation courseOperation;
    private SemesterOperation semesterOperation;
    private static ClickListenerForCalculateCgapActivity clickListenerForCalculateCgapActivity;
    private static ClickListenerForCourseListActivity clickListenerForCourseListActivity;
    private static ClickListenerForResultBookActivity clickListenerForResultBookActivity;

    public DialogActivity(Context context) {
        this.context = context;
        semesterOperation = new SemesterOperation(context);
        courseOperation = new CourseOperation(context);
    }

    //------------------------------DialogForCalculateCGPAActivity-----------------------------------//
    public void showDialogUpdateDeleteForCalculateCGPAActivity(List<Course> courseList, int position){
        itemPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout_update_delete,null);
        builder.setView(view);

        courseNameDialog = view.findViewById(R.id.autoCompleteTextViewDialog);
        creditDialog = view.findViewById(R.id.creditDialog);
        gpaDialog = view.findViewById(R.id.gpaDialog);
        courseNameDialog.setText(courseList.get(position).getCourse_name());
        creditDialog.setText(String.valueOf(courseList.get(position).getCourse_credit()));
        gpaDialog.setText(String.valueOf(courseList.get(position).getObtain_gpa()));

        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("Update or Delete Information");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mCourseName = courseNameDialog.getText().toString();
                    mCredit = Double.parseDouble(creditDialog.getText().toString());
                    mGpa = Double.parseDouble(gpaDialog.getText().toString());
                    clickListenerForCalculateCgapActivity.updateList(itemPosition, mCourseName,mCredit, mGpa);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Log.d("Anik", ""+e);
                }
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickListenerForCalculateCgapActivity.removeItem(itemPosition);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showDialogSaveSemesterForCalculateCgpaActivity(final List<Course> courseList){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_layout_save_semester,null);
        saveSemester = view.findViewById(R.id.semesterName);
        builder.setView(view);
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("New Semester Name");
        cgpaCalculator = new CgpaCalculator();
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Vector<Double> values = cgpaCalculator.calculation(courseList);
                String semester_name = saveSemester.getText().toString();
                double calculated_cgpa = values.elementAt(0);
                double total_credit = values.elementAt(1);
                if(!semester_name.isEmpty()){
                    Semester created_semester =
                            semesterOperation.createSemester(semester_name,calculated_cgpa, courseList.size(), total_credit);
                    //calculated_cgpa, courseList.size(),total_credit
                    Log.d("Anik", ""+calculated_cgpa+" "+courseList.size()+" "+total_credit);
                    Long semesterId = created_semester.getmId();
                    for(int i = 0; i< courseList.size(); i++){
                        String course_name = courseList.get(i).getCourse_name();
                        double course_credit = courseList.get(i).getCourse_credit();
                        double gpa = courseList.get(i).getObtain_gpa();
                        courseOperation.createCourse(course_name,course_credit,gpa,semesterId);
                    }
                    clickListenerForCalculateCgapActivity.clearDisplay();
                    Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context,"Empty Semester name is not allowed",Toast.LENGTH_LONG).show();
                }

            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //------------------------------DialogForResultBookActivity-----------------------------------//
    public void showDialogSveSemesterForResultBookActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout_save_semester,null);
        builder.setView(view);
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("New Semester Name");
        saveSemester = view.findViewById(R.id.semesterName);
        cgpaCalculator = new CgpaCalculator();
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String semester_name = saveSemester.getText().toString();
                if(!semester_name.isEmpty()){
                    Semester newSemester = semesterOperation.createSemester(semester_name,0.00,0,0.00);
                    clickListenerForResultBookActivity.saveNewSemester(newSemester);
                    }
                }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showDialogDeleteSemesterForResultBookActivity(final Semester semester, final int itemPosition){
        String semester_name = semester.getSemester_name();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setTitle("Delete");
        builder.setMessage("Do you wnat to delete "+semester_name+"?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                semesterOperation.deleteSemester(semester);
                clickListenerForResultBookActivity.refreshListAfterDeleteSemester(semester, itemPosition);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //------------------------------DialogForCourseListActivity-----------------------------------//
    public void showDialogUpdateDeleteForCourseListActivity(List<Course> courseList, int position){
        itemPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout_update_delete,null);
        builder.setView(view);

        courseNameDialog = view.findViewById(R.id.autoCompleteTextViewDialog);
        creditDialog = view.findViewById(R.id.creditDialog);
        gpaDialog = view.findViewById(R.id.gpaDialog);
        courseNameDialog.setText(courseList.get(position).getCourse_name());
        creditDialog.setText(String.valueOf(courseList.get(position).getCourse_credit()));
        gpaDialog.setText(String.valueOf(courseList.get(position).getObtain_gpa()));
        final Course course = courseList.get(position);

        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("Update or Delete");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCourseName = courseNameDialog.getText().toString();
                mCredit = Double.parseDouble(creditDialog.getText().toString());
                mGpa = Double.parseDouble(gpaDialog.getText().toString());
                courseOperation.updateCourse(course, mCourseName,mCredit, mGpa);
                clickListenerForCourseListActivity.updateList(itemPosition, mCourseName,mCredit, mGpa);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                courseOperation.deleteCourse(course);
                clickListenerForCourseListActivity.removeItem(itemPosition);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showDialogForAddNewSemesterCourseListActivity(final Long mId){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_layout_input_module,null);
        builder.setView(view);
        courseNameDialog = view.findViewById(R.id.dialog_autoCompleteTextView_courseName);
        creditDialog = view.findViewById(R.id.dialog_credit);
        gpaDialog = view.findViewById(R.id.dialog_gpa);
        btn_add_dialog = view.findViewById(R.id.dialog_btnAdd);

        builder.setIcon(R.drawable.ic_edit_black_24dp);
        builder.setTitle("Add New Course");
        final AlertDialog dialog = builder.create();
        dialog.show();
        btn_add_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String course_name = courseNameDialog.getText().toString();
                if(course_name.isEmpty()){
                    course_name = "Course Name?";
                }
                try {
                    double credit = Double.parseDouble(creditDialog.getText().toString());
                    double gpa = Double.parseDouble(gpaDialog.getText().toString());
                    Course course = courseOperation.createCourse(course_name, credit, gpa, mId);
                    Log.d("Anik", ""+gpa);
                    clickListenerForCourseListActivity.addNewCourse(course);
                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    Toast.makeText(context,"Please fill all the fields carefully",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //---------------------------------------interface----------------------------------------------
    public interface ClickListenerForCalculateCgapActivity {
        void updateList(int position, String courseName, double credit, double gpa);
        void removeItem(int position);
        void clearDisplay();
    }
    public interface ClickListenerForResultBookActivity {
        void saveNewSemester(Semester semester);
        void refreshListAfterDeleteSemester(Semester semester, int position);
    }
    public interface ClickListenerForCourseListActivity {
        void addNewCourse(Course course);
        void updateList(int position, String courseName, double credit, double gpa);
        void removeItem(int position);
    }

    //setListener
    public static  void setListenerForCalculateCgpaActivity(ClickListenerForCalculateCgapActivity clickListener){
        clickListenerForCalculateCgapActivity = clickListener;
    }
    public static void setListenerForResultBookActivity(ClickListenerForResultBookActivity clickListener){
        clickListenerForResultBookActivity = clickListener;
    }
    public static  void setListenerForCourseListActivity(ClickListenerForCourseListActivity clickListener){
        clickListenerForCourseListActivity = clickListener;
    }
}
