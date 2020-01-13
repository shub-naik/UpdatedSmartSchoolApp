package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;


public class EmergencyLeaveSignupActivity extends AppCompatActivity {

    EditText Phone, Remarks, Name, ParentsPhone;
    ImageView image;
    String ImageUrl;
    Uri imageuri;
    Spinner classes, section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_leave_signup);


        Phone = findViewById(R.id.EmergencyLeaveSignupPhone);
        Name = findViewById(R.id.EmergencyLeaveSignupName);
        Remarks = findViewById(R.id.EmergencyLeaveSignupRemarks);
        ParentsPhone = findViewById(R.id.EmergencyLeaveParentPhoneNumber);
        classes = findViewById(R.id.EmergencyStudentsClasses);
        section = findViewById(R.id.EmergencyStudentsSection);

        image = findViewById(R.id.EmergencyLeaveImage);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWriteStoragePermissionGranted()) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 10);
                } else {
                    requestWriteStoragePermission();
                }
            }
        });

        findViewById(R.id.EmergencyLeaveSignupButton).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                InitializeEmergencyLeave();
            }
        });
    }

    //If otp is entered correctly the only execute this part
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void InitializeEmergencyLeave() {
        final ProgressDialog progressDialog = new ProgressDialog(EmergencyLeaveSignupActivity.this);
        progressDialog.setTitle("Uploading Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Data is sending to the server as well Notification to the Parent");
        progressDialog.show();

        if (imageuri != null && !Phone.getText().toString().isEmpty() && !Name.getText().toString().isEmpty() && !Remarks.getText().toString().isEmpty() && !ParentsPhone.getText().toString().isEmpty() && ParentsPhone.getText().toString().length() == 10 && Phone.getText().toString().length() == 10) {
            //Upload Image First
            if (imageuri != null && !Phone.getText().toString().isEmpty() && !Name.getText().toString().isEmpty() && !Remarks.getText().toString().isEmpty() && !ParentsPhone.getText().toString().isEmpty() && ParentsPhone.getText().toString().length() == 10 && Phone.getText().toString().length() == 10) {
                //Upload Image First
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("EmergencyLeaveSignupData");
                final StorageReference riversRef = mStorageRef.child(Phone.getText().toString());
                riversRef.putFile(imageuri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri>[] downloadUrl = new Task[]{riversRef.getDownloadUrl()};
                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ImageUrl = uri.toString();
// Todays Date
                                        LocalDate Today = LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                                                Calendar.getInstance().get(Calendar.MONTH) + 1,
                                                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                                        final String TodaysDate = Today.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));


                                        EmergencyLeave e = new EmergencyLeave("No", ImageUrl, Phone.getText().toString(), Name.getText().toString(), Remarks.getText().toString(), ParentsPhone.getText().toString());
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EmergencyLeaveData");
                                        ref.child(TodaysDate).child(Phone.getText().toString()).setValue(e);
                                        Intent i = new Intent(EmergencyLeaveSignupActivity.this, NotificationSendingActivityUsingVolley.class);
                                        i.putExtra("ParentPhoneNumber", ParentsPhone.getText().toString());
                                        i.putExtra("Classes", classes.getSelectedItem().toString());
                                        i.putExtra("Section", section.getSelectedItem().toString());
                                        i.putExtra("Remarks", Remarks.getText().toString());
                                        i.putExtra("EmergencyTitlePhone", ParentsPhone.getText().toString());
                                        i.putExtra("EmergencyTitleName", Name.getText().toString());
                                        startActivity(i);
                                        finish();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                            }
                        });
                progressDialog.dismiss();
            } else {
                CoordinatorLayout coordinatorLayout = findViewById(R.id.EmergencyLeaveCoordinatorLayout);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#F44336\">Please Enter All Details</font>"), Snackbar.LENGTH_LONG);
                snackbar.show();
                progressDialog.dismiss();
            }
            progressDialog.dismiss();


        } else {
            CoordinatorLayout coordinatorLayout = findViewById(R.id.EmergencyLeaveCoordinatorLayout);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#F44336\">Please Enter All Details</font>"), Snackbar.LENGTH_LONG);
            snackbar.show();
            progressDialog.dismiss();
        }
        progressDialog.dismiss();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
            if (photo != null) {
                imageuri = getImageUri(getApplicationContext(), photo);
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    // Checking for Permissions Such as Read And Write for getting Access to the Image Part in Gallery or on Camera
    public boolean isWriteStoragePermissionGranted() {
        return (ContextCompat.checkSelfPermission(EmergencyLeaveSignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestWriteStoragePermission() {
        ActivityCompat.requestPermissions(EmergencyLeaveSignupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1010);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1010 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 10);
        } else {
            Toast.makeText(this, "Permission Must be Granted For the App Functionality to work Properly", Toast.LENGTH_SHORT).show();
        }
    }
}
