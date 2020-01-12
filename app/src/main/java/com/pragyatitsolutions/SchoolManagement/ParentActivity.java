package com.pragyatitsolutions.SchoolManagement;

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

public class ParentActivity extends AppCompatActivity {

    int flag = 0;
    EditText phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        CheckBox show_hide_password = findViewById(R.id.ShowHidePassword);
        phone = findViewById(R.id.edtParentPhoneNumber);
        password = findViewById(R.id.edtParentPassword);

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

        final ProgressDialog progress = new ProgressDialog(ParentActivity.this);

        Button login = findViewById(R.id.ButtonParentLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setTitle("Validating Please Wait...");
                progress.setMessage("Your Credentials are Validating");
                progress.setCancelable(false);
                progress.show();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentsData");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot d2 : d1.getChildren()) {
                                for (DataSnapshot d3 : d2.getChildren()) {
                                    Student s = d3.getValue(Student.class);
                                    if (s.getSphone().equals(phone.getText().toString()) && s.getPassword().equals(password.getText().toString())) {
                                        Intent i = new Intent(ParentActivity.this, ParentMainIndexActivity.class);

                                        flag = 1;

                                        // Storing Parent Login Credentials
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ParentsPreferences", 0); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();

                                        editor.putString("Classes", s.getSclass());
                                        editor.putString("Section", s.getSsection());
                                        editor.putString("Roll_No", s.getSroll());

                                        editor.putString("ParentsPhoneNumber", s.getSphone());

                                        editor.commit(); // commit changes

                                        progress.dismiss();

                                        startActivity(i);
                                        finish();
                                    }
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
