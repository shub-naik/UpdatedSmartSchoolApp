package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class ParentMainIndexActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView txtAttendanceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main_index);

        txtAttendanceStatus = findViewById(R.id.TodayAttendanceStatus);


        pref = getSharedPreferences("ParentsPreferences", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        String ParentsPhone = pref.getString("ParentsPhoneNumber", null);
        Toolbar t = findViewById(R.id.ParentMainIndexToolbar);
        setSupportActionBar(t);

        String Classes = pref.getString("Classes", null);
        String Section = pref.getString("Section", null);
        final String Roll_no = pref.getString("Roll_No", null);
        String TodaysDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);

        // For Todays Attendance
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentAttendance");


        ref.child(Classes).child(Section).child(TodaysDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int flag = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getValue(String.class).equals(Roll_no)) {
                        flag = 1;
                        txtAttendanceStatus.append("\n : Present Today");
                        txtAttendanceStatus.setTextColor(Color.parseColor("#00574B"));
                        break;
                    }
                }
                if (flag == 0) {
                    txtAttendanceStatus.append("\n : Absent Today");
                    txtAttendanceStatus.setTextColor(Color.parseColor("#F44336"));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                pref.edit().remove("ParentsPhoneNumber");
                startActivity(new Intent(ParentMainIndexActivity.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
