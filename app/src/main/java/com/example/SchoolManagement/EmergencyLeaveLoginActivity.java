package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmergencyLeaveLoginActivity extends AppCompatActivity {


    TextView SignUp;
    EditText phone, Password;
    Button Signin;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_leave_login);


        SignUp = findViewById(R.id.EmergencyLeavePersonSignup);
        Signin = findViewById(R.id.EmergencyLeavePersonLogin);
        phone = findViewById(R.id.Phone);
        Password = findViewById(R.id.Password);

        final ProgressDialog progress = new ProgressDialog(this);


        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                progress.setCancelable(false);
                progress.setTitle("Validating Please Wait....");
                progress.setMessage("Validating........");
                final String Phone = phone.getText().toString();
                final String password = Password.getText().toString();

                ref = FirebaseDatabase.getInstance().getReference("EmergencyLeaveSignupData");

                ref.orderByKey().equalTo(Phone).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                EmergencyLeave e = d.getValue(EmergencyLeave.class);
                                if (e.getPassword().equals(password)) {
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("EmergencyLeavePreferences", 0); // 0 - for private mode
                                    SharedPreferences.Editor editor = pref.edit();

                                    //Initaializing Values to the Preferences.
                                    editor.putString("EmergencyPhoneNumber", Phone);

                                    editor.commit(); // commit changes
                                    Intent i = new Intent(EmergencyLeaveLoginActivity.this, EmergencyLeaveMainIndexActivity.class);
                                    progress.dismiss();
                                    startActivity(i);
                                    finish();
                                }
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(EmergencyLeaveLoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(EmergencyLeaveLoginActivity.this, "Error Occured....", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmergencyLeaveLoginActivity.this, EmergencyLeaveSignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
