package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Build;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class ParentMainIndexActivity extends AppCompatActivity {

    SharedPreferences pref;
    TextView txtAttendanceStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main_index);

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


        final String ParentsPhone = pref.getString("ParentsPhoneNumber", null);

        final String Classes = pref.getString("Classes", null);
        final String Section = pref.getString("Section", null);
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


        // Getting The Device Id From Shared Preferences
        final SharedPreferences pref1 = getSharedPreferences("TokenGenerated", 0);
        final String DeviceID = pref1.getString("DeviceId", null);

        if (DeviceID != null && ParentsPhone != null) {
            final DatabaseReference ref_token_update = FirebaseDatabase.getInstance().getReference("StudentsData");
            ref_token_update.child(Classes).child(Section).orderByKey().equalTo(ParentsPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() == 1) {
                        for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                            Student s = d1.getValue(Student.class);
                            if (DeviceID.equals(s.getDeviceId())) {
                                ref_token_update.child(Classes).child(Section).child(ParentsPhone).child("token").setValue(pref1.getString("Token", ""));
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ParentMainIndexActivity.this, "DataBase Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                Intent NotificationIntent = getIntent();
                String title = NotificationIntent.getStringExtra("Title");
                String body = NotificationIntent.getStringExtra("Body");
                final String EmergencyPhoneForEmergencyStatusUpdate = NotificationIntent.getStringExtra("EmergencyPhoneNumber");
                AlertDialog.Builder builder = new AlertDialog.Builder(ParentMainIndexActivity.this);
                if (title != null && body != null) {
                    builder.setTitle(title);
                    builder.setMessage(body);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Todays Date
                            LocalDate Today = LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                                    Calendar.getInstance().get(Calendar.MONTH) + 1,
                                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                            final String TodaysDate = Today.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EmergencyLeaveData");
                            ref = ref.child(TodaysDate).child(EmergencyPhoneForEmergencyStatusUpdate);
                            final DatabaseReference finalRef = ref;
                            ref.orderByKey().equalTo("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    finalRef.child("status").setValue("Yes");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(ParentMainIndexActivity.this, "Yes Clicked!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(ParentMainIndexActivity.this, "Permission Denied!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }

        }
        return super.onOptionsItemSelected(item);
    }


}
