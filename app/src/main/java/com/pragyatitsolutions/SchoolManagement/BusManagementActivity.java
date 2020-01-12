package com.pragyatitsolutions.SchoolManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class BusManagementActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    CardView addDriverCardView, deleteDriverCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_management);
        frameLayout = findViewById(R.id.BusManagementFrameContainer);
        addDriverCardView = findViewById(R.id.AddDriverCardView);
        deleteDriverCardView = findViewById(R.id.DeleteDriverCardView);

        addDriverCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBusDriverFragment fragment = new AddBusDriverFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.BusManagementFrameContainer, fragment).commit();
            }
        });

        deleteDriverCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteBusDriverFragment fragment = new DeleteBusDriverFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.BusManagementFrameContainer, fragment).commit();
            }
        });
    }
}
