package com.example.SchoolManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    Button admin_login;
    EditText username, password;
    public SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        admin_login = findViewById(R.id.AdminLogin);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);


        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_to_match = username.getText().toString().trim().toLowerCase();
                String password_to_match = password.getText().toString().trim().toLowerCase();
                if (username_to_match.equals("admin") && password_to_match.equals("admin")) {
                    Toast.makeText(AdminActivity.this, "Successful Admin Login", Toast.LENGTH_SHORT).show();

                    pref= getApplicationContext().getSharedPreferences("AdminPreferences", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    //Initaializing Values to the Preferences.
                    editor.putString("AdminName", username_to_match);

                    editor.commit(); // commit changes

                    Intent intent = new Intent(AdminActivity.this, AdminIndex.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AdminActivity.this, "Username or Password Mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
