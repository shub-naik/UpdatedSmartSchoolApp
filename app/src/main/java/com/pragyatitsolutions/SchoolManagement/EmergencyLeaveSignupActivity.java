package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class EmergencyLeaveSignupActivity extends AppCompatActivity {

    EditText Phone, Remarks, Name;
    ImageView image;
    String ImageUrl;
    Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_leave_signup);


        Phone = findViewById(R.id.EmergencyLeaveSignupPhone);
        Name = findViewById(R.id.EmergencyLeaveSignupName);
        Remarks = findViewById(R.id.EmergencyLeaveSignupRemarks);

        image = findViewById(R.id.EmergencyLeaveImage);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent();
//                i.setType("image/*");
//                i.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(i, 10);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 10);

            }
        });

        findViewById(R.id.EmergencyLeaveSignupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InitializeEmergencyLeave();
            }
        });
    }

    //If otp is entered correctly the only execute this part
    private void InitializeEmergencyLeave() {
        if (!Phone.getText().toString().isEmpty() && !Name.getText().toString().isEmpty() && !Remarks.getText().toString().isEmpty() && Phone.length() == 10) {
            SharedPreferences pref = getSharedPreferences("TokenGenerated", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            final String token = pref.getString("Token", "");


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
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });


            EmergencyLeave e = new EmergencyLeave(ImageUrl, Phone.getText().toString(), Name.getText().toString(), Remarks.getText().toString(), token);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EmergencyLeaveSignupData");
            ref.child(Phone.getText().toString()).

                    setValue(e);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
//            imageuri = data.getData();
//            Picasso.with(EmergencyLeaveSignupActivity.this).load(imageuri).into(image);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
        }
    }
}
