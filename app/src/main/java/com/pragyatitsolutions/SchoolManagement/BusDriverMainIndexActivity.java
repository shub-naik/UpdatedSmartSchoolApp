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

public class BusDriverMainIndexActivity extends AppCompatActivity {

    SharedPreferences pref;
    String BusDriverPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_driver_main_index);

        pref = getSharedPreferences("BusDriversPreferences", 0); // 0 - for private mode
        final SharedPreferences.Editor editor = pref.edit();
        BusDriverPhoneNumber = pref.getString("BusDriverPhoneNumber", null);

        // Toolbar
        Toolbar t = findViewById(R.id.BusDriverMainIndexToolbar);
        t.setTitle("Bus Driver MainIndex");
        setSupportActionBar(t);

        // For Navigation Back Button Press and moving to MainActivity
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("ParentsPhoneNumber").commit();
                Intent i = new Intent(BusDriverMainIndexActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    // For Menu in the ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.busdrivermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.BusDriverLogout:
                pref.edit().remove("BusDriverPhoneNumber").commit();
                startActivity(new Intent(BusDriverMainIndexActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.BusDriverCurrentLocation:
                Intent intent = new Intent(BusDriverMainIndexActivity.this, BusDriverCurrentLocationActivity.class);
                intent.putExtra("DriverPhoneNumber", BusDriverPhoneNumber);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
