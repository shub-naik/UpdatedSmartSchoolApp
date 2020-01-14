package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeleteTeacher extends AppCompatActivity {

    List<Teacher> AllTeachers = new ArrayList<Teacher>();
    ListView alteachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_teacher);

        alteachers = findViewById(R.id.AllTeachersData);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TeachersPrimaryData");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Teacher object = d.getValue(Teacher.class);
                    AllTeachers.add(object);
                }

                TeacherDataAdapter adapter = new TeacherDataAdapter(DeleteTeacher.this, R.layout.allteachersdata, AllTeachers);
                alteachers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DeleteTeacher.this, "Error Occured....", Toast.LENGTH_SHORT).show();
            }
        });

        alteachers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = builder = new AlertDialog.Builder(DeleteTeacher.this);

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to close this application ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                final String phone = AllTeachers.get(position).getPhone();
                                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("TeachersPrimaryData").child(phone);
                                ref1.removeValue();

                                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("TeacherSubjectsAndSections");
                                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                                            DataSnapshot ref = d.child(phone);
                                            Boolean b = d.child(phone).getKey() == phone;
                                            if (b) {
                                                ref2.child(d.getKey()).child(phone).removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                Toast.makeText(DeleteTeacher.this, phone + " Teacher Deleted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(DeleteTeacher.this, TeacherManagement.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Delete Teacher ");
                alert.show();
                return false;
            }
        });


    }
}
