package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);


        final EditText phone = findViewById(R.id.edtParentPhoneNumber);
        final EditText password = findViewById(R.id.edtParentPassword);
        Button login = findViewById(R.id.ButtonParentLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentsData");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot d2 : d1.getChildren()) {
                                for (DataSnapshot d3 : d2.getChildren()) {
                                    Student s = d3.getValue(Student.class);
                                    if (s.getSphone().equals(phone.getText().toString()) && s.getPassword().equals(password.getText().toString())) {
                                        Intent i = new Intent(ParentActivity.this, ParentMainIndexActivity.class);

                                        // Storing Parent Login Credentials
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("ParentsPreferences", 0); // 0 - for private mode
                                        SharedPreferences.Editor editor = pref.edit();

                                        editor.putString("Classes", s.getSclass());
                                        editor.putString("Section", s.getSsection());
                                        editor.putString("Roll_No", s.getSroll());

                                        editor.putString("ParentsPhoneNumber", s.getSphone());

                                        editor.commit(); // commit changes

                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
