package com.example.SchoolManagement;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddStudentAttendance extends Fragment {


    AttendanceStudentListHolder addStudentAttendanceAdapter;
    ArrayList<Student> studentList;
    ListView list;
    Spinner classes;
    Spinner section;

    public AddStudentAttendance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_student_attendance, container, false);

        studentList = new ArrayList<Student>();
        list = view.findViewById(R.id.StudentsAttendanceListView);
        classes = (Spinner) view.findViewById(R.id.AttendanceStudentClass);
        section = (Spinner) view.findViewById(R.id.AttendanceStudentSection);
        section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        classes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    public void GetData() {
        // Listing All the Students on the Basis of the Classes and Section Selected.
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentsData");
        ref.child(classes.getSelectedItem().toString()).child(section.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Log.e("Error", d + "");
                    Student s = d.getValue(Student.class);
                    studentList.add(s);
                }

                addStudentAttendanceAdapter = new AttendanceStudentListHolder(getContext(), studentList);
                list.setAdapter(addStudentAttendanceAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
