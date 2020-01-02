package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


        Toolbar t = findViewById(R.id.TeacherMainIndexToolbar);
        t.setTitle("Teacher Main Index Page");
        setSupportActionBar(t);

        // For Navigation Back Button Press and moving to MainActivity
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("TeacherPhoneNumber").commit();
                Intent i = new Intent(TeacherMainIndexActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        // From Here We will create Horizontal Scroll and Show the Teacher User 2 menu items
        LinearLayout studentAttendance = findViewById(R.id.StudentAttendance);
        LinearLayout studentHomework = findViewById(R.id.StudentHomework);

        studentHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHomeWorkFragment fragment = new AddHomeWorkFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.TeacherFrameContainer, fragment).commit();
            }
        });

        studentAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
