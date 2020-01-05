package com.example.SchoolManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class ParentMainIndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main_index);


        SharedPreferences pref = getSharedPreferences("ParentsPreferences", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        String ParentsPhone = pref.getString("ParentsPhoneNumber", null); // getting String


    }
}
