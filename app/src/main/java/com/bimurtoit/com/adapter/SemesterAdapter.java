package com.bimurtoit.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bimurtoit.com.activity.CourseListActivity;
import com.bimurtoit.com.activity.DialogActivity;
import com.bimurtoit.com.calculatecgpa.R;
import com.bimurtoit.com.model.Semester;

import java.text.DecimalFormat;
import java.util.List;


public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.MyViewHolder> {

    private DecimalFormat precision = new DecimalFormat("0.00");
    private List<Semester> semesterList;
    private LayoutInflater inflater;
    private Context context;
    private DialogActivity dialogActivity;
    public SemesterAdapter(Context context, List<Semester> semesterList){
        this.semesterList = semesterList;
        inflater = LayoutInflater.from(context);
        this.context = context;
        dialogActivity = new DialogActivity(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_row_semester, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mSemesterName.setText(semesterList.get(holder.getAdapterPosition()).getSemester_name());
        holder.mSemesterCgpa.setText(String.valueOf(precision.format(semesterList.get(holder.getAdapterPosition()).getTotal_gpa())));
        String credit = "Total Credit: "+String.valueOf(semesterList.get(holder.getAdapterPosition()).getTotal_credit());
        holder.mSemesterCredit.setText(credit);
        String gpa = "Total Course: "+String.valueOf(semesterList.get(holder.getAdapterPosition()).getTotal_course());
        holder.mSemesterCourse.setText(gpa);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Semester semester = semesterList.get(holder.getAdapterPosition());
                long mId = semester.getmId();
                //Toast.makeText(context,"Clicked"+position+" "+mId, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CourseListActivity.class);
                intent.putExtra("mId", mId);
                context.startActivity(intent);
            }
        });
        holder.btn_delete_semester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Semester semester = semesterList.get(holder.getAdapterPosition());
                dialogActivity.showDialogDeleteSemesterForResultBookActivity(semester, holder.getAdapterPosition());*/
                PopupMenu popupMenu = new PopupMenu(context, holder.btn_delete_semester);
                popupMenu.inflate(R.menu.option_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getItemId() == R.id.rename){
                            Toast.makeText(context,"Rename",Toast.LENGTH_SHORT).show();
                        }
                        else if(menuItem.getItemId() == R.id.delete){
                            Semester semester = semesterList.get(holder.getAdapterPosition());
                            dialogActivity.showDialogDeleteSemesterForResultBookActivity(semester, holder.getAdapterPosition());
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Semester semester = semesterList.get(holder.getAdapterPosition());
                dialogActivity.showDialogDeleteSemesterForResultBookActivity(semester, holder.getAdapterPosition());
                return true;
            }
        });
    }
    @Override
    public int getItemCount() {
        return !semesterList.isEmpty() ? semesterList.size() : 0;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mSemesterName, mSemesterCgpa, mSemesterCredit, mSemesterCourse, opt_btn;
        CardView cardView;
        ImageButton btn_delete_semester;

        private MyViewHolder(View itemView) {
            super(itemView);
            mSemesterName = itemView.findViewById(R.id.semester_name);
            mSemesterCgpa = itemView.findViewById(R.id.semester_cgpa);
            mSemesterCredit = itemView.findViewById(R.id.semester_credit);
            mSemesterCourse = itemView.findViewById(R.id.semester_course);
            //opt_btn = itemView.findViewById(R.id.option_dot);
            cardView = itemView.findViewById(R.id.single_row_card_layout_semester);
            btn_delete_semester = itemView.findViewById(R.id.btn_delete_semester);
        }
    }
}
