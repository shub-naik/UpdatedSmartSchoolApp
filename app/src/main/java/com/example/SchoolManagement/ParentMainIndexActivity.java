package com.example.SchoolManagement;

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

public class ParentMainIndexActivity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main_index);


        pref = getSharedPreferences("ParentsPreferences", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        String ParentsPhone = pref.getString("ParentsPhoneNumber", null);


        Toolbar t = findViewById(R.id.ParentMainIndexToolbar);
        t.setTitle("Main Index Page");
        setSupportActionBar(t);
        // For Navigation Back Button Press and moving to MainActivity
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().remove("EmergencyPhoneNumber").commit();
                Intent i = new Intent(ParentMainIndexActivity.this, MainActivity.class);
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
                pref.edit().remove("EmergencyPhoneNumber").commit();
                Intent intent = new Intent(ParentMainIndexActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
