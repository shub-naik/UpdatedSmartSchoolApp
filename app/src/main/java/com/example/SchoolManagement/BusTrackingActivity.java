package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BusTrackingActivity extends AppCompatActivity {
    int flag = 0;
    EditText phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_tracking);

        CheckBox show_hide_password = findViewById(R.id.ShowHideBusDriverPassword);
        phone = findViewById(R.id.edtBusDriverPhoneNumber);
        password = findViewById(R.id.edtBusDriverPassword);

        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        final ProgressDialog progress = new ProgressDialog(BusTrackingActivity.this);

        Button login = findViewById(R.id.ButtonBusDriverLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setTitle("Validating Please Wait...");
                progress.setMessage("Your Credentials are Validating");
                progress.setCancelable(false);
                progress.show();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DriversData");
                ref.orderByKey().equalTo(phone.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                                HashMap<String, Object> map = (HashMap<String, Object>) d1.getValue();
                                if (map.get("Password").equals(password.getText().toString())) {
                                    flag = 1;

                                    // Storing Parent Login Credentials
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("BusDriversPreferences", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();


                                    editor.putString("BusDriverPhoneNumber", map.get("Phone").toString());

                                    editor.commit(); // commit changes

                                    progress.dismiss();
                                    Intent intent = new Intent(BusTrackingActivity.this, BusDriverMainIndexActivity.class);
                                    startActivity(intent);
                                } else {
                                }
                            }
                        }
                        if (flag == 0) {
                            progress.dismiss();
                            CoordinatorLayout coordinatorLayout = findViewById(R.id.ParentLoginCoordinatorLayout);
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#F44336\">Invalid Login , Please Enter Correct Credentails</font>"), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progress.dismiss();
                        CoordinatorLayout coordinatorLayout = findViewById(R.id.ParentLoginCoordinatorLayout);
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#F44336\">DataBase Error Occured</font>"), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        });
    }

}
