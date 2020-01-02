package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmergencyLeaveMainIndexActivity extends AppCompatActivity {


    // Notification Added
    String parent_token;
    public static final String CHANNEL_ID = "shubham_naik";
    private static final String CHANNEL_NAME = "shubham naik";
    private static final String CHANNEL_DESC = "HELLO WORLD , WELCOME WORLD";

    SharedPreferences pref = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_leave_main_index);

        Toolbar t = findViewById(R.id.EmergencyLeaveMainIndexToolbar);
        t.setTitle("Main Index Page");
        setSupportActionBar(t);

        // Notification Channel Build
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        pref = getSharedPreferences("EmergencyLeavePreferences", 0);

        // Checking Whether the admin is login or not.
        final String EMERGENCY_PHONE = pref.getString("EmergencyPhoneNumber", null);
        SharedPreferences.Editor editor = pref.edit();

        if (EMERGENCY_PHONE == null) {
            Intent intent = new Intent(EmergencyLeaveMainIndexActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        // For Navigation Back Button Press and moving to MainActivity
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().remove("EmergencyPhoneNumber").commit();
                Intent i = new Intent(EmergencyLeaveMainIndexActivity.this, MainActivity.class);
                startActivity(i);
            }
        });


        findViewById(R.id.Submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(EmergencyLeaveMainIndexActivity.this);
                progress.setCancelable(false);
                progress.setMessage("Searching Through Data Please wait....");
                progress.show();

                EditText Phone = findViewById(R.id.EmergencyPhoneNumber);
                final Spinner sp1 = findViewById(R.id.StudentsClasses);
                final Spinner sp2 = findViewById(R.id.StudentsSection);

                String phone = Phone.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentsData");
                ref.child(sp1.getSelectedItem().toString()).child(sp2.getSelectedItem().toString()).orderByKey().equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            CardView card = findViewById(R.id.CardView);
                            card.setVisibility(View.VISIBLE);
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                for (DataSnapshot d1 : d.getChildren()) {
                                    if (d1.getKey().equals("token")) {
                                        parent_token = d1.getValue(String.class);
                                    } else if (d1.getKey().equals("sphone")) {
                                        String parent_phone = d1.getValue(String.class);
                                        TextView phone = findViewById(R.id.EParentPhone);
                                        phone.setVisibility(View.VISIBLE);
                                        phone.setText(parent_phone);
                                    } else if (d1.getKey().equals("sname")) {
                                        String student_name = d1.getValue(String.class);
                                        TextView name = findViewById(R.id.EStudentName);
                                        name.setVisibility(View.VISIBLE);
                                        name.setText(student_name);
                                    } else if (d1.getKey().equals("imageuri")) {
                                        String student_image = d1.getValue(String.class);
                                        ImageView imageview = findViewById(R.id.EStudentImage);
                                        imageview.setVisibility(View.VISIBLE);
                                        Picasso.with(EmergencyLeaveMainIndexActivity.this).load(student_image).into(imageview);
                                    }
                                }
                            }
                            progress.dismiss();
                        } else {
                            progress.dismiss();
                            Toast.makeText(EmergencyLeaveMainIndexActivity.this, "Invalid Phone Number Entered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        findViewById(R.id.SendNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(EmergencyLeaveMainIndexActivity.this);
                String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";
                final String serverKey = "key=" + "AIzaSyAjmxNSjxAPTKd67NbN7rkfO0VlydmwuZI";

                // For Sending Request via Volley Library and using Json
                JSONObject mainJsonObject = new JSONObject();
                try {
                    mainJsonObject.put("to", parent_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//        mainJsonObject.put("to","/topics/updates");

                JSONObject notificationObject = new JSONObject();
                try {
                    notificationObject.put("title", "Hello World 1234567890");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    notificationObject.put("body", "Message From : " + EMERGENCY_PHONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    mainJsonObject.put("notification", notificationObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                        FCM_API_URL,
                        mainJsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(EmergencyLeaveMainIndexActivity.this, "response : " + response, Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EmergencyLeaveMainIndexActivity.this, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> header = new HashMap<String, String>();
                        header.put("content-type", "application/json");
                        header.put("authorization", serverKey);
                        return header;
                    }
                };
                requestQueue.add(objectRequest);
            }
        });
    }

    // For Menu in the ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Logout:
                pref.edit().remove("EmergencyPhoneNumber").commit();
                Intent intent = new Intent(EmergencyLeaveMainIndexActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
