package com.example.SchoolManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AttendanceStudentListHolder extends BaseAdapter {

    List<Student> student;
    Context context;

    // ArrayList For Storing the List Of Students Present for the Current Date
    ArrayList<String> PresentList = new ArrayList<>();


    public AttendanceStudentListHolder(Context context, List<Student> student) {
        this.context = context;
        this.student = student;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.students_list_item, parent, false);

        final TextView roll = view.findViewById(R.id.AttendanceStudentRoll);
        TextView name = view.findViewById(R.id.AttendanceStudentName);
        CheckBox checkBox = view.findViewById(R.id.AttendanceStatus);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (PresentList.contains(roll.getText().toString())) {

                    } else {
                        PresentList.add(roll.getText().toString());
                    }
                } else {
                    PresentList.remove(new String(roll.getText().toString()));
                }

                returnPresentList();
//                Toast.makeText(context, isChecked + "Checked " + position, Toast.LENGTH_SHORT).show();
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


    public ArrayList<String> returnPresentList() {
        Toast.makeText(context, "" + PresentList, Toast.LENGTH_SHORT).show();
        return PresentList;
    }

}


