package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TeacherActivity extends AppCompatActivity {


    FrameLayout frameLayout;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        frameLayout = findViewById(R.id.TeacherFrameContainer);
        linearLayout = findViewById(R.id.LTeacherLinearLayout);

        // Fragment for Teacher Login.
        TeacherLoginFragment login = new TeacherLoginFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.TeacherFrameContainer, login).commit();
    }

    public void GetDataFromFragment(final String Phone, final String Password) {
        if (!Phone.isEmpty() && !Password.isEmpty()) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Validating...." + "\n" + "Please Wait....");
            dialog.show();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TeachersPrimaryData");
            Query query = ref.orderByKey().equalTo(Phone);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                        Teacher teacher_object = d1.getValue(Teacher.class);

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("TeacherPreferences", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();

                        //Initaializing Values to the Preferences.
                        editor.putString("TeacherPhoneNumber", Phone);

                        editor.commit(); // commit changes

                        Intent i = new Intent(TeacherActivity.this, TeacherMainIndexActivity.class);
                        i.putExtra("TeacherObject", teacher_object);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            dialog.dismiss();
        } else {
            Snackbar.make(linearLayout, "All Fields Are Mandatory Or Invalid Details", Snackbar.LENGTH_LONG).show();
        }
    }
}
