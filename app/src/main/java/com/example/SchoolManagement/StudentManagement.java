package com.example.SchoolManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentManagement extends AppCompatActivity {


    Button Add, Update, view, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_management);
        Add = findViewById(R.id.StudentAdd);
        Update = findViewById(R.id.UpdateStudent);
        view = findViewById(R.id.ViewStudent);
        delete = findViewById(R.id.DeleteStudent);

        //Adding the Teacher to the FireBase
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentManagement.this, AddStudent.class);
                startActivity(i);
                finish();
            }
        });


//        //Updating Teacher and Adding the Subjects
//        Update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(StudentManagement.this, UpdateStudent.class);
//                startActivity(i);
//                finish();
//            }
//        });
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(StudentManagement.this, ViewStudent.class);
//                startActivity(i);
//                finish();
//            }
//        });
//
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(StudentManagement.this, DeleteStudent.class);
//                startActivity(i);
//                finish();
//            }
//        });
    }
}
