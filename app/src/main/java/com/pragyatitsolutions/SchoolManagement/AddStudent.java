package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

public class AddStudent extends AppCompatActivity {

    Button add, GenerateOTPButton;
    ImageView image;
    EditText phone, otpentered, Deviceid;
    Spinner classes, section;
    Uri imageuri;
    FirebaseAuth auth;
    final Uri[] ImageDownloadUrl = new Uri[1];
    String verificationId;
    StorageReference mStorageRef;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        getSupportActionBar().setTitle("Add Student");

        // For Otp
        otpentered = findViewById(R.id.AddStudentOTPReceived);
        GenerateOTPButton = findViewById(R.id.GetStudentOTP);
        // End Part ENd Here


        SharedPreferences pref = getSharedPreferences("TokenGenerated", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        token = pref.getString("Token", "");


        auth = FirebaseAuth.getInstance();


        mStorageRef = FirebaseStorage.getInstance().getReference("StudentsImages/");

        image = findViewById(R.id.StudentImage);
        phone = findViewById(R.id.StudentPhone);
        classes = findViewById(R.id.StudentClass);
        section = findViewById(R.id.StudentSection);
        add = findViewById(R.id.AddStudent);
        Deviceid = findViewById(R.id.ParentDeviceId);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < 19) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(i, 10);
                } else {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    startActivityForResult(i, 10);
                }
            }
        });
        GenerateOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageuri != null && !phone.getText().toString().isEmpty() && !classes.getSelectedItem().toString().isEmpty() && !section.getSelectedItem().toString().isEmpty()) {
                    final String p = phone.getText().toString();
                    // Changes Done From Here.
                    SendVerificationCode("+91" + p);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpentered.getText().toString() != null) {
                    VerifyCode(otpentered.getText().toString());
                } else {
                    Log.e("EmptyError", "HELLO WORLD");
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

    private void VerifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        SignInWithCredentials(credential);
    }

    private void SignInWithCredentials(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                AddDataToFirebase(task);
            }
        });
    }


    // Add Data To Firebase
    private void AddDataToFirebase(Task<AuthResult> task) {
        if (task.isSuccessful()) {

            // Show Dialog Box while Data is Adding to the Database.
            // Build an AlertDialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(AddStudent.this);

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.progress_dialog_using_alert_dialog, null);

            // Specify alert dialog is not cancelable/not ignorable
            builder.setCancelable(false);

            // Set the custom layout as alert dialog view
            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            // Dialog Box Code Ends Here

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentsData");
            final String p = phone.getText().toString();
            final String c = classes.getSelectedItem().toString();
            final String s = section.getSelectedItem().toString();

            final EditText sname = findViewById(R.id.StudentName);
            final EditText email = findViewById(R.id.StudentEmail);
            final EditText mothersname = findViewById(R.id.StudentMotherName);
            final EditText studentaddress = findViewById(R.id.StudentAddress);
            final EditText studentpassword = findViewById(R.id.StudentPassword);

            // Add Student if OTP is Successfully Entered.
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
                                                    Student student = new Student(Deviceid.getText().toString(), token, ImageDownloadUrl[0].toString(), sname.getText().toString(), p, c, s, RollNo, email.getText().toString(), studentaddress.getText().toString(), mothersname.getText().toString(), studentpassword.getText().toString());

                                                    ref.child(c).child(s).child(p).setValue(student);
                                                    alertDialog.dismiss();
                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(AddStudent.this, "Failure Occured while Uploading the Data......", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            alertDialog.dismiss();
                        }
                    }
            );

            Intent i = new Intent(AddStudent.this, AddStudent.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(AddStudent.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void SendVerificationCode(String code) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                code,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                VerifyCode(code);
            }
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(AddStudent.this, "Verification Failed....Sorry " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
    };
}
