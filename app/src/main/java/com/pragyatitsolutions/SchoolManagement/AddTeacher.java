package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddTeacher extends AppCompatActivity {


    EditText Email, Username, Password, PhoneNumber;
    ImageView teacherimage;
    Button add;
    private DatabaseReference reference;
    private StorageReference mStorageRef;
    Uri imageuri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        reference = FirebaseDatabase.getInstance().getReference("TeachersPrimaryData");
        mStorageRef = FirebaseStorage.getInstance().getReference("TeacherImages/");

        Email = findViewById(R.id.email);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        PhoneNumber = findViewById(R.id.phonenumber);
        teacherimage = findViewById(R.id.TeacherImage);


        //Click Listener for loading image into the ImageView.
        teacherimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent variable for loading teacher image into the imageview.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 10);
            }
        });

        add = findViewById(R.id.AddTeacher);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTeacher();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            Picasso.with(AddTeacher.this).load(imageuri).into(teacherimage);
        }
    }

    public void AddTeacher() {
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        final String username = Username.getText().toString().trim();
        final String phone = PhoneNumber.getText().toString().trim();
        if (email == null || password == null || username == null || phone == null || imageuri == null) {
            Toast.makeText(this, "All Fields Are Mandatory", Toast.LENGTH_SHORT).show();
        } else {

            final StorageReference riversRef = mStorageRef.child(phone);

            riversRef.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
//                            Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                            Task<Uri> downloadUrl = riversRef.getDownloadUrl();

                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    //Do what you want with the url
                                    Toast.makeText(AddTeacher.this, "" + downloadUrl, Toast.LENGTH_SHORT).show();
                                    DatabaseReference id = reference.child(phone);
                                    Teacher teacher = new Teacher(username, email, password, phone, downloadUrl.toString());
                                    id.setValue(teacher);
                                }

                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(AddTeacher.this, "Failure Occured while Uploading the Data......", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }
}
