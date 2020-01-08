package com.example.SchoolManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class AdminActivity extends AppCompatActivity {

    Button admin_login;
    EditText username, password;
    public SharedPreferences pref;
    CoordinatorLayout coordinatorLayout;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        coordinatorLayout = findViewById(R.id.AdminCoordinatorLayout);

        checkBox = findViewById(R.id.ShowHideAdminPassword);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        admin_login = findViewById(R.id.AdminLogin);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);


        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progress = new ProgressDialog(AdminActivity.this);
                progress.setTitle("Validating Please Wait...");
                progress.setMessage("Your Credentials are Validating");
                progress.setCancelable(false);
                progress.show();
                String username_to_match = username.getText().toString().trim().toLowerCase();
                String password_to_match = password.getText().toString().trim().toLowerCase();
                if (username_to_match.equals("admin") && password_to_match.equals("admin")) {
                    pref = getApplicationContext().getSharedPreferences("AdminPreferences", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();

                    //Initaializing Values to the Preferences.
                    editor.putString("AdminName", username_to_match);

                    editor.commit(); // commit changes
                    progress.dismiss();

                    Intent intent = new Intent(AdminActivity.this, AdminIndex.class);
                    startActivity(intent);
                } else {
                    progress.dismiss();
                    CoordinatorLayout coordinatorLayout = findViewById(R.id.AdminCoordinatorLayout);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#F44336\">Invalid Login , Please Enter Correct Credentails</font>"), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}
