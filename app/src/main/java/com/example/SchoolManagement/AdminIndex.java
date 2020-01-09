package com.example.SchoolManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class AdminIndex extends AppCompatActivity {

    Button Logout;
    SharedPreferences pref;
    Editor editor;
    GridView admin_grid_view;
    String users_text[] = {"Teacher Management", "Student Management", "Bus Management", "Emergency Leave"};
    int users_images[] = {R.drawable.teacher, R.drawable.student, R.drawable.bus_tracking, R.drawable.emergency_leave};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_index);

        //Disabling the Back Button on the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        pref = getSharedPreferences("AdminPreferences", 0);
        editor = pref.edit();


        // Checking Whether the admin is login or not.
        String ADMINNAME = pref.getString("AdminName", null);
        editor = pref.edit();

        if (ADMINNAME == null) {
            finish();
            Intent intent = new Intent(AdminIndex.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //Logout Button
        Logout = findViewById(R.id.logout);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Intent intent = new Intent(AdminIndex.this, AdminIndex.class);
                startActivity(intent);
                finish();
            }
        });

        //Grid View For Admin Index
        admin_grid_view = (GridView) findViewById(R.id.AdminIndexGrid);

        AdminIndexAdapter adapter = new AdminIndexAdapter(AdminIndex.this, users_text, users_images);
        admin_grid_view.setAdapter(adapter);

        admin_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i;
                switch (users_text[position]) {
                    case "Teacher Management":
                        i = new Intent(AdminIndex.this, TeacherManagement.class);
                        startActivity(i);
                        break;
                    case "Student Management":
                        i = new Intent(AdminIndex.this, StudentManagement.class);
                        startActivity(i);
                        break;
                    case "Bus Management":
                        i = new Intent(AdminIndex.this, BusManagementActivity.class);
                        startActivity(i);
//                    case "Emenrgency Leave":
//                        i = new Intent(AdminIndex.this, TeacherManagement.class);
//                        startActivity(i);

                }
            }
        });
    }
}
