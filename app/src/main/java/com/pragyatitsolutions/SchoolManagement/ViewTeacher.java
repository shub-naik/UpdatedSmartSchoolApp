package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewTeacher extends AppCompatActivity {

    ImageView teacherimage;
    TextView teachername, teacheremail, teacherpassword, teacherphone, subjectssections, namelabel, emaillabel, passwordlabel, phonelabel;
    EditText phone;
    Button viewTeacher;

    DatabaseReference reference, reference_subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teacher);

        reference = FirebaseDatabase.getInstance().getReference("TeachersPrimaryData");
        reference_subjects = FirebaseDatabase.getInstance().getReference("TeacherSubjectsAndSections");

        namelabel = findViewById(R.id.NameLabel);
        emaillabel = findViewById(R.id.EmailLabel);
        passwordlabel = findViewById(R.id.PasswordLabel);
        phonelabel = findViewById(R.id.PhoneLabel);

        teacherimage = findViewById(R.id.TeacherImage);
        teachername = findViewById(R.id.Name);
        teacheremail = findViewById(R.id.Email);
        teacherpassword = findViewById(R.id.Password);
        teacherphone = findViewById(R.id.Phone);
        phone = findViewById(R.id.TeacherPhone);
        subjectssections = findViewById(R.id.SubjectsSections);
        viewTeacher = findViewById(R.id.ViewTeacher);

        viewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String p = phone.getText().toString();
                if (!p.isEmpty()) {


                    final ProgressDialog progress = new ProgressDialog(ViewTeacher.this);
                    progress.setCancelable(false);
                    progress.setMessage("Loading Please Wait....");
                    progress.show();
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                //Retrieving Subjects of the Teacher.
                                reference_subjects.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot childrens : dataSnapshot.getChildren()) {
                                            for (DataSnapshot child : childrens.getChildren()) {
                                                if (child.getKey().equals(p)) {
                                                    for (DataSnapshot d : child.getChildren()) {
                                                        if (d.getKey().equals("subject")) {
                                                            subjectssections.append("Subject: " + d.getValue());
                                                            Log.e("ViewTeacherError.class", d.getValue() + "");
                                                        } else if (d.getKey().equals("classes")) {
                                                            subjectssections.append("Class: " + d.getValue());
                                                            Log.e("ViewTeacherError.class", d.getValue() + "");
                                                        } else if (d.getKey().equals("section")) {
                                                            subjectssections.append("Section: " + d.getValue());
                                                            Log.e("ViewTeacherError.class", d.getValue() + "");
                                                        }
                                                        subjectssections.append("\n");
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                for (DataSnapshot d : dataSnapshot.getChildren()) {

                                    namelabel.setVisibility(View.VISIBLE);
                                    emaillabel.setVisibility(View.VISIBLE);
                                    passwordlabel.setVisibility(View.VISIBLE);
                                    phonelabel.setVisibility(View.VISIBLE);

                                    teacherimage.setVisibility(View.VISIBLE);
                                    teachername.setVisibility(View.VISIBLE);
                                    teacheremail.setVisibility(View.VISIBLE);
                                    teacherpassword.setVisibility(View.VISIBLE);
                                    teacherphone.setVisibility(View.VISIBLE);
                                    subjectssections.setVisibility(View.VISIBLE);
                                    Teacher t = d.getValue(Teacher.class);
                                    teachername.setText(t.getName());
                                    teacheremail.setText(t.getEmail());
                                    teacherpassword.setText(t.getPassword());
                                    teacherphone.setText(t.getPhone());
                                    Picasso.with(ViewTeacher.this).load(t.getImageuri()).into(teacherimage);
                                }
                                progress.dismiss();
                            } else {
                                progress.dismiss();

                                namelabel.setVisibility(View.INVISIBLE);
                                emaillabel.setVisibility(View.INVISIBLE);
                                passwordlabel.setVisibility(View.INVISIBLE);
                                phonelabel.setVisibility(View.INVISIBLE);

                                teacherimage.setVisibility(View.INVISIBLE);
                                teachername.setVisibility(View.INVISIBLE);
                                teacherphone.setVisibility(View.INVISIBLE);
                                teacherpassword.setVisibility(View.INVISIBLE);
                                teacheremail.setVisibility(View.INVISIBLE);
                                subjectssections.setVisibility(View.INVISIBLE);
                                Toast.makeText(ViewTeacher.this, "Invalid Phone Number Entered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progress.dismiss();
                        }
                    };
                    Query query = reference.orderByChild("phone").equalTo(p);
                    query.addListenerForSingleValueEvent(valueEventListener);
                } else {
                    Toast.makeText(ViewTeacher.this, "Please Enter The Phone Number of the Teacher To View It", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
