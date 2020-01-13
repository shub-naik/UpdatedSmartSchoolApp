package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationSendingActivityUsingVolley extends AppCompatActivity {


    String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";
    final String serverKey = "key=AIzaSyAjmxNSjxAPTKd67NbN7rkfO0VlydmwuZI";

    // Notification Added
    public static final String CHANNEL_ID = "shubham_naik";
    private static final String CHANNEL_NAME = "shubham naik";
    private static final String CHANNEL_DESC = "HELLO WORLD , WELCOME WORLD";
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_leave_main_index);

        //getting parents phone number.
        i = getIntent();
        String ParentPhone = i.getStringExtra("ParentPhoneNumber");
        String classes = i.getStringExtra("Classes");
        String section = i.getStringExtra("Section");
        final String titlename = i.getStringExtra("EmergencyTitleName");
        final String titlephone = i.getStringExtra("EmergencyTitlePhone");
        String remarks = i.getStringExtra("Remarks");

        // Notification Channel Build
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("StudentsData");
        ref.child(classes).child(section).orderByKey().equalTo(ParentPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() == 1) {
                    for (DataSnapshot d1 : dataSnapshot.getChildren()) {

                        Student s = d1.getValue(Student.class);
                        String parent_token = s.getToken();


                        // Sending Notifications Part Begins Here
                        RequestQueue requestQueue = Volley.newRequestQueue(NotificationSendingActivityUsingVolley.this);

                        // For Sending Request via Volley Library and using Json
                        JSONObject mainJsonObject = new JSONObject();
                        try {
                            mainJsonObject.put("to", parent_token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONObject notificationObject = new JSONObject();
                        try {
                            notificationObject.put("title", "Emergency Leave From " + titlephone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            notificationObject.put("body", "Message From Admin :\n " + titlename + " wants to carry your child to home ,\n Phone Number is :" + titlephone + "\n , Do You Recognize this person?");
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
                                        Toast.makeText(NotificationSendingActivityUsingVolley.this, "Notification Send To The Registered Parent's Phone Number", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(NotificationSendingActivityUsingVolley.this, EmergencyLeaveSignupActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(NotificationSendingActivityUsingVolley.this, "DataBase Error ", Toast.LENGTH_SHORT).show();
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
