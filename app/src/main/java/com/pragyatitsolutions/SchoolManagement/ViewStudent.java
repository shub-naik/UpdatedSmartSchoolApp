package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewStudent extends AppCompatActivity implements RecyclerViewClickInterface {

    RecyclerView recyclerView;
    List<Student> list;
    DatabaseReference ref;
    Spinner classes, section;
    StudentsListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.StudentsListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentsListAdapter(list, ViewStudent.this, ViewStudent.this);
        recyclerView.setAdapter(adapter);

        classes = findViewById(R.id.ViewListStudentClasses);
        section = findViewById(R.id.ViewListStudentSection);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewStudent.this);

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


        String classesString, sectionString;
        classesString = classes.getSelectedItem().toString();
        sectionString = section.getSelectedItem().toString();

        ref.child(classesString).child(sectionString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TextView status = findViewById(R.id.ViewStudentStatus);
                    status.setVisibility(View.INVISIBLE);
                    list.clear();
                    for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                        list.add(d1.getValue(Student.class));
                    }
                    adapter = new StudentsListAdapter(list, ViewStudent.this, ViewStudent.this);
                    recyclerView.setAdapter(adapter);
                    alertDialog.dismiss();
                } else {
                    TextView status = findViewById(R.id.ViewStudentStatus);
                    status.setVisibility(View.VISIBLE);
                    alertDialog.dismiss();
                    list.clear();
                    StudentsListAdapter adapter = new StudentsListAdapter(list, ViewStudent.this, ViewStudent.this);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(ViewStudent.this, "No Data Present in the Database to show", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void OnItemClick(final int position) {

        final Student s = list.get(position);

        // Show Dialog Box while Data is Adding to the Database.
        // Build an AlertDialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewStudent.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.viewdeletestudentslertdalogbox, null);

        ImageView imageView = dialogView.findViewById(R.id.ViewDeleteAlertDialogStudentImage);
        Picasso.with(ViewStudent.this).load(s.getImageuri()).into(imageView);
        TextView phone = dialogView.findViewById(R.id.ViewDeleteAlertDialogStudentParentPhone);
        phone.append(s.getSphone());
        TextView deviceid = dialogView.findViewById(R.id.ViewDeleteAlertDialogStudentDeviceId);
        deviceid.append(s.getDeviceId());
        TextView FatherName = dialogView.findViewById(R.id.ViewDeleteAlertDialogStudentFathersName);
        FatherName.append(s.getSname());
        TextView Password = dialogView.findViewById(R.id.ViewDeleteAlertDialogPassword);
        Password.append(s.getPassword());
        TextView Email = dialogView.findViewById(R.id.ViewDeleteAlertDialogEmail);
        Email.append(s.getEmail());
        TextView Address = dialogView.findViewById(R.id.ViewDeleteAlertDialogAddress);
        Address.append(s.getSaddress());

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(true);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // Dialog Box Code Ends Here
    }

}
