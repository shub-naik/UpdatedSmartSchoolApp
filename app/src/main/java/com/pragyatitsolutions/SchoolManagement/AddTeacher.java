package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
                if (isReadStorageGranted()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 10);
                } else {
                    requestReadStoragePermission();
                }
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

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Data, Please Wait ....");
        progressDialog.show();

        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();
        final String username = Username.getText().toString().trim();
        final String phone = PhoneNumber.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || phone.isEmpty() || imageuri.toString().isEmpty()) {
            Toast.makeText(this, "All Fields Are Mandatory", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
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
                                    Toast.makeText(AddTeacher.this, "Teacher Data Added Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddTeacher.this, TeacherManagement.class));
                                }

                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(AddTeacher.this, "Failure Occured while Uploading the Data......", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddTeacher.this, TeacherManagement.class));
                        }
                    });
            progressDialog.dismiss();
        }
        progressDialog.dismiss();
    }

    // Checking for Permissions Such as Read And Write for getting Access to the Image Part in Gallery or on Camera
    public boolean isReadStorageGranted() {
        return (ContextCompat.checkSelfPermission(AddTeacher.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestReadStoragePermission() {
        ActivityCompat.requestPermissions(AddTeacher.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1010);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1010 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Permission Must be Granted For the App Functionality to work Properly", Toast.LENGTH_SHORT).show();
        }
    }

}
