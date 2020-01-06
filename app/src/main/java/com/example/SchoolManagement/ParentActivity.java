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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentActivity extends AppCompatActivity {

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        final ProgressDialog progress = new ProgressDialog(ParentActivity.this);


        final EditText phone = findViewById(R.id.edtParentPhoneNumber);
        final EditText password = findViewById(R.id.edtParentPassword);
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
                    }
                });
            }
        });
    }
}
