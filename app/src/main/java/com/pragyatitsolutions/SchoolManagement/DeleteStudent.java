package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteStudent extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Student> list;
    DatabaseReference ref;
    Spinner classes, section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_student);
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.StudentsListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        classes = findViewById(R.id.ListStudentClasses);
        section = findViewById(R.id.ListStudentSection);

        ref = FirebaseDatabase.getInstance().getReference("StudentsData");
        DisplayDataInRecyclerView();

        classes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DisplayDataInRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DisplayDataInRecyclerView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void DisplayDataInRecyclerView() {

        // Show Dialog Box while Data is Adding to the Database.
        // Build an AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(DeleteStudent.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog_using_alert_dialog, null);

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


        String classesString, sectionString;
        classesString = classes.getSelectedItem().toString();
        sectionString = section.getSelectedItem().toString();

        ref.child(classesString).child(sectionString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                        list.add(d1.getValue(Student.class));
                    }

                    StudentsListAdapter adapter = new StudentsListAdapter(list, DeleteStudent.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                } else {
                    alertDialog.dismiss();
                    list.clear();
                    StudentsListAdapter adapter = new StudentsListAdapter(list, DeleteStudent.this);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(DeleteStudent.this, "No Data Present in the Database to show", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
