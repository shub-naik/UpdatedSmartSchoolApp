package com.example.SchoolManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AttendanceStudentListHolder extends BaseAdapter {

    private final List<Student> student;
    private final Context context;


    public AttendanceStudentListHolder(Context context, List<Student> student) {
        this.context = context;
        this.student = student;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.students_list_item, parent, false);

        TextView roll = view.findViewById(R.id.AttendanceStudentRoll);
        TextView name = view.findViewById(R.id.AttendanceStudentName);
        CheckBox checkBox = view.findViewById(R.id.AttendanceStatus);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, isChecked + "Checked " + position, Toast.LENGTH_SHORT).show();
            }
        });

        Student s = student.get(position);

        roll.setText(s.getSroll());
        name.setText(s.getSname());


        return view;
    }

    public void addData(Student stud) {
        student.add(stud);
    }

    @Override
    public int getCount() {
        return student.size();
    }

    @Override
    public Student getItem(int position) {
        return student.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
