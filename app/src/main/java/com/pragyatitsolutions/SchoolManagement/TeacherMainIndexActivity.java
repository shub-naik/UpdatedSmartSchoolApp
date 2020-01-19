package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class TeacherMainIndexActivity extends AppCompatActivity {


    SharedPreferences.Editor editor;
    Teacher object;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main_index);



        Intent i = getIntent();
        object = (Teacher) i.getSerializableExtra("TeacherObject");


        //Getting Teacher Preferences
        SharedPreferences pref = getSharedPreferences("TeacherPreferences", 0);

        // Checking Whether the admin is login or not.
        final String EMERGENCY_PHONE = pref.getString("TeacherPhoneNumber", null);
        editor = pref.edit();



        // From Here We will create Horizontal Scroll and Show the Teacher User 2 menu items
        LinearLayout studentAttendance = findViewById(R.id.StudentAttendance);
        LinearLayout studentHomework = findViewById(R.id.StudentHomework);

        studentHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHomeWorkFragment fragment = new AddHomeWorkFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.TeacherFrameContainer, fragment).commit();
            }
        });

        studentAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStudentAttendance addStudentAttendance = new AddStudentAttendance();
                getSupportFragmentManager().beginTransaction().replace(R.id.TeacherFrameContainer, addStudentAttendance).commit();
            }
        });


        // For Tool Bar
        // for ToolBar
        Toolbar t = findViewById(R.id.TeacherMainIndexToolbar);
        t.setTitle("Teacher Name");

        setSupportActionBar(t);

        // For Navigation Back Button Press and moving to MainActivity
        t.setNavigationOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                editor.remove("TeacherPhoneNumber").commit();
                Intent i = new Intent(TeacherMainIndexActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


    }





    // For Menu in the ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                editor.remove("TeacherPhoneNumber").commit();
                Intent intent = new Intent(TeacherMainIndexActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
