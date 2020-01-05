package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddStudent extends AppCompatActivity {


    Button add;
    ImageView image;
    EditText phone;
    Spinner classes, section;
    Uri imageuri;
    FirebaseAuth auth;
    final Uri[] ImageDownloadUrl = new Uri[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);


        SharedPreferences pref = getSharedPreferences("TokenGenerated", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        final String token = pref.getString("Token", "");


        auth = FirebaseAuth.getInstance();


        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("StudentsImages/");

        image = findViewById(R.id.StudentImage);
        phone = findViewById(R.id.StudentPhone);
        classes = findViewById(R.id.StudentClass);
        section = findViewById(R.id.StudentSection);
        add = findViewById(R.id.StudentAdd);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, 10);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageuri != null && !phone.getText().toString().isEmpty() && !classes.getSelectedItem().toString().isEmpty() && !section.getSelectedItem().toString().isEmpty()) {
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentsData");
                    final String p = phone.getText().toString();
                    final String c = classes.getSelectedItem().toString();
                    Log.e("Classes", c);
                    final String s = section.getSelectedItem().toString();
                    Log.e("Section", s);

                    final EditText sname = findViewById(R.id.StudentName);
                    final EditText email = findViewById(R.id.StudentEmail);
                    final EditText mothersname = findViewById(R.id.StudentMotherName);
                    final EditText studentaddress = findViewById(R.id.StudentAddress);

                    ref.child(c).child(s).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                    final StorageReference riversRef = mStorageRef.child(c).child(s).child(p);
                                    riversRef.putFile(imageuri)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    final Task<Uri>[] downloadUrl = new Task[]{riversRef.getDownloadUrl()};

                                                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            ImageDownloadUrl[0] = uri;
                                                            // for Data Upload to the Database
                                                            Long RollNumber = dataSnapshot.getChildrenCount() + 1;
                                                            String RollNo = Long.toString(RollNumber);
                                                            Student student = new Student(token, ImageDownloadUrl[0].toString(), sname.getText().toString(), p, c, s, RollNo, email.getText().toString(), studentaddress.getText().toString(), mothersname.getText().toString());

                                                            ref.child(c).child(s).child(p).setValue(student);
                                                        }
                                                    });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(AddStudent.this, "Failure Occured while Uploading the Data......", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            }
                    );
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            Picasso.with(AddStudent.this).load(imageuri).into(image);
        }
    }
}
