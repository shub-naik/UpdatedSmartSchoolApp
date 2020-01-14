package com.pragyatitsolutions.SchoolManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentsListAdapter extends RecyclerView.Adapter<StudentsListAdapter.StudentItemHolder> {

    List<Student> list;
    Context context;

    public StudentsListAdapter(List<Student> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentsListAdapter.StudentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_students, parent, false);
        return new StudentItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsListAdapter.StudentItemHolder holder, int position) {
        Student s = list.get(position);
        String RollString = s.getSroll();
        String NameString = s.getSname();
        holder.name.setText(NameString);
        holder.roll.setText(RollString);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentItemHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView name, roll;

        public StudentItemHolder(@NonNull View itemView) {
            super(itemView);
            roll = itemView.findViewById(R.id.StudentListRoll);
            name = itemView.findViewById(R.id.StudentListName);
            linearLayout = itemView.findViewById(R.id.ListStudentLinearLayout);
        }
    }

}
