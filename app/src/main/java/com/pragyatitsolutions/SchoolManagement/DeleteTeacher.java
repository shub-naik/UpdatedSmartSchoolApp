package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

        // Display Loading Message if Internet Speed is Slow.
        // Build an AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(DeleteTeacher.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog_using_alert_dialog, null);
        TextView title = dialogView.findViewById(R.id.AlertTitle);
        TextView message = dialogView.findViewById(R.id.AlertMessage);
        title.setText("Loading..");
        message.setText("Data is Loading...");

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final int[] flag = {0};
        //AlertDialog Ends Here


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                TextView textView = findViewById(R.id.ListOfDeleteTeachersData);
                if (!dataSnapshot.exists()) {
                    textView.setText("No Data Present in the Database to show");
                    textView.setTextColor(Color.RED);
                    alertDialog.dismiss();
                    return;
                }

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Teacher object = d.getValue(Teacher.class);
                    AllTeachers.add(object);
                }


                TeacherDataAdapter adapter = new TeacherDataAdapter(DeleteTeacher.this, R.layout.allteachersdata, AllTeachers);
                alteachers.setAdapter(adapter);
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DeleteTeacher.this, "Error Occured....", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
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
