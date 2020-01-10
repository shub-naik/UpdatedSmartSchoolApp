package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ParentMainIndexActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView txtAttendanceStatus;
    Intent NotificationIntent;
    String body = "";
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main_index);

        NotificationIntent = getIntent();
        title = NotificationIntent.getStringExtra("Title");
        body = NotificationIntent.getStringExtra("Body");

        pref = getSharedPreferences("ParentsPreferences", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();

        // Toolbar
        Toolbar t = findViewById(R.id.ParentMainIndexToolbar);
        t.setTitle("Parent MainIndex");
        setSupportActionBar(t);

        // For Navigation Back Button Press and moving to MainActivity
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("ParentsPhoneNumber").commit();
                Intent i = new Intent(ParentMainIndexActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        CardView homework = findViewById(R.id.Homework);
        txtAttendanceStatus = findViewById(R.id.TodayAttendanceStatus);


        String ParentsPhone = pref.getString("ParentsPhoneNumber", null);

        String Classes = pref.getString("Classes", null);
        String Section = pref.getString("Section", null);
        final String Roll_no = pref.getString("Roll_No", null);
        String TodaysDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR);

        // For Todays Attendance
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentAttendance");


        ref.child(Classes).child(Section).child(TodaysDate).addValueEventListener(new ValueEventListener() {
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

        // Opening Fragment on Click of Homework CardView
        homework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ParentMainIndexActivity.this, ShowHomeWorkActivity.class);
                startActivity(i);
            }
        });

    }

    // For Menu in the ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parentmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                pref.edit().remove("ParentsPhoneNumber").commit();
                startActivity(new Intent(ParentMainIndexActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.GetDriversCurrentLocation:
                Intent intent = new Intent(ParentMainIndexActivity.this, ParentGoogleMapActivity.class);
                startActivity(intent);
                break;
            case R.id.ParentNotifications:
                Log.e("Title", title);
                Log.e("Title", body);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
