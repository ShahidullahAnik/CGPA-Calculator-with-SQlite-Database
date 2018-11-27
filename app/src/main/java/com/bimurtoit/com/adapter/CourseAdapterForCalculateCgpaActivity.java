package com.bimurtoit.com.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bimurtoit.com.activity.DialogActivity;
import com.bimurtoit.com.calculatecgpa.R;
import com.bimurtoit.com.model.Course;

import java.text.DecimalFormat;
import java.util.List;

public class CourseAdapterForCalculateCgpaActivity extends RecyclerView.Adapter<CourseAdapterForCalculateCgpaActivity.MyViewHolder>{

    private DecimalFormat precision = new DecimalFormat("0.00");
    private List<Course> courseList;
    private LayoutInflater inflater;
    private DialogActivity dialogActivity;
    Context context;

    public CourseAdapterForCalculateCgpaActivity(Context context, List<Course> courses_list){
        inflater = LayoutInflater.from(context);
        courseList = courses_list;
        dialogActivity = new DialogActivity(context);
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singel_row_course, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Course course = courseList.get(position);
        holder.mCourse.setText(course.getCourse_name());
        String str = "Credit: "+String.valueOf(course.getCourse_credit());
        holder.mCredit.setText(str);
        holder.mGpa.setText(String.valueOf(precision.format(course.getObtain_gpa())));
    }
    @Override
    public int getItemCount() {
        return !courseList.isEmpty() ? courseList.size() : 0;
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mCourse, mCredit, mGpa;
        CardView cardView;

        MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mCourse = itemView.findViewById(R.id.coureseName);
            mCredit = itemView.findViewById(R.id.courseCredit);
            mGpa = itemView.findViewById(R.id.courseCgpa);
            cardView = itemView.findViewById(R.id.single_row_card_layout);
        }
        @Override
        public void onClick(View view) {
            dialogActivity.showDialogUpdateDeleteForCalculateCGPAActivity(courseList, getAdapterPosition());
            Log.d("Anik", context.getClass().getName());
            }
        }
}
