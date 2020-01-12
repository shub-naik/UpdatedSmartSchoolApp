package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ShowHomeWorkActivity extends AppCompatActivity {
    SharedPreferences pref;
    RecyclerView recyclerView;
    ShowHomeWorkAdapter showHomeWorkAdapter;
    private List<ShowHomeWorkModel> list = new ArrayList<>();
    int flag = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_home_work);
        getSupportActionBar().setTitle("Pragyat IT Solutions");

        final TextView latestHomeWork = findViewById(R.id.txtLatestHomeWork);

        recyclerView = findViewById(R.id.RecyclerViewShowHomeWork);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        pref = getSharedPreferences("ParentsPreferences", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        String Classes = pref.getString("Classes", null);
        String Section = pref.getString("Section", null);
        String Roll_no = pref.getString("Roll_No", null);

//Todays Date
        LocalDate Today = LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        final String TodaysDate = Today.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading Please Wait...");
        progress.setMessage("Your HomeWork Section is Loading");
        progress.setCancelable(false);
        progress.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("HomeWork");
        ref.child(Classes).child(Section).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                        String TeacherName = d1.getKey().split("==>")[0];
                        for (DataSnapshot d2 : d1.getChildren()) {
                            String split_key[] = d2.getKey().split(" ");
                            if (TodaysDate.compareTo(split_key[split_key.length - 1]) <= 0) {
                                if (flag == 0) {
                                    flag = 1;
                                    latestHomeWork.setVisibility(View.VISIBLE);
                                }
                                // Show Your HomeWork Part Here
                                Map<String, Object> message = (Map<String, Object>) d2.getValue();

                                String remarks = (String) message.get("Remarks");
                                String deadline = (String) message.get("Deadline");
                                String subject = (String) message.get("Subject");
                                String teachername = TeacherName;

                                ShowHomeWorkModel showHomeWorkModel = new ShowHomeWorkModel(teachername, subject, deadline, remarks);
                                list.add(showHomeWorkModel);

                            }
                        }
                    }
                    if (flag == 0) {
                        TextView noMoreHomeWork = findViewById(R.id.NoMoreHomeWork);
                        noMoreHomeWork.setVisibility(View.VISIBLE);
                    }
                    showHomeWorkAdapter = new ShowHomeWorkAdapter(ShowHomeWorkActivity.this, list);
                    recyclerView.setAdapter(showHomeWorkAdapter);
                    progress.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
