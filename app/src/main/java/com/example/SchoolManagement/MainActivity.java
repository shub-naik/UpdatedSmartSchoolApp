package com.example.SchoolManagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    GridView home_grid_view;
    SharedPreferences pref;
    Editor editor;
    String users_text[] = {"Admin Login", "Teacher Login", "Parent Login", "Emergency Leave Login"};
    int users_images[] = {R.drawable.admin, R.drawable.teacher, R.drawable.parent, R.drawable.emergency_leave};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Shared Preferences for Parent's Login if Parents is Already Login.
        SharedPreferences pref = getSharedPreferences("ParentsPreferences", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        String ParentsPhone = pref.getString("ParentsPhoneNumber", null);

        if (ParentsPhone != null) {
            Intent ParentIntent = new Intent(MainActivity.this, ParentMainIndexActivity.class);
            startActivity(ParentIntent);
        }


        //Shared Preferences and Editor For Admin If Login Already .
        pref = getSharedPreferences("AdminPreferences", 0);
        editor = pref.edit();

        if (pref.getString("AdminName", null) != null) {
            Intent AdminIntent = new Intent(MainActivity.this, AdminIndex.class);
            startActivity(AdminIntent);
        }

        //Shared Preferences and Editor For Emergency Leave Login If Login Already .
        pref = getSharedPreferences("EmergencyLeavePreferences", 0);
        editor = pref.edit();

        if (pref.getString("EmergencyPhoneNumber", null) != null) {
            Intent AdminIntent = new Intent(MainActivity.this, EmergencyLeaveMainIndexActivity.class);
            startActivity(AdminIntent);
        }


        home_grid_view = (GridView) findViewById(R.id.HomeGridView1);

        MainAdapter adapter = new MainAdapter(MainActivity.this, users_text, users_images);
        home_grid_view.setAdapter(adapter);

        home_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String action_to_do = users_text[position].toLowerCase();
                switch (action_to_do) {
                    case "admin login":
                        Intent admin_intent = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(admin_intent);
                        break;
                    case "teacher login":
                        Intent teacher_intent = new Intent(MainActivity.this, TeacherActivity.class);
                        startActivity(teacher_intent);
                        break;
                    case "parent login":
                        Intent parent_intent = new Intent(MainActivity.this, ParentActivity.class);
                        startActivity(parent_intent);
                        break;
                    case "emergency leave login":
                        Intent bus_tracking_intent = new Intent(MainActivity.this, EmergencyLeaveLoginActivity.class);
                        startActivity(bus_tracking_intent);
                        break;
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you Sure want to exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();  // It means Closing the app completely and opens the fresh App on open again
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
