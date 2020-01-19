package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class UpdateStudent extends AppCompatActivity implements RecyclerViewClickInterface {
    RecyclerView recyclerView;
    List<Student> list;
    DatabaseReference ref;
    Spinner classes, section;
    StudentsListAdapter adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        // For refreshing The Recycler View
        refreshLayout = findViewById(R.id.UpdateRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DisplayDataInRecyclerView();
                refreshLayout.setRefreshing(false);
            }
        });

        // Refreshing the Recycler View Ends here .

        list = new ArrayList<>();

        recyclerView = findViewById(R.id.UpdateStudentsListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentsListAdapter(list, UpdateStudent.this, UpdateStudent.this);
        recyclerView.setAdapter(adapter);

        classes = findViewById(R.id.UpdateListStudentClasses);
        section = findViewById(R.id.UpdateListStudentSection);

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
        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateStudent.this);

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
                    TextView status = findViewById(R.id.UpdateStudentStatus);
                    status.setVisibility(View.INVISIBLE);
                    list.clear();
                    for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                        list.add(d1.getValue(Student.class));
                    }
                    adapter = new StudentsListAdapter(list, UpdateStudent.this, UpdateStudent.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                } else {
                    TextView status = findViewById(R.id.UpdateStudentStatus);
                    status.setVisibility(View.VISIBLE);
                    alertDialog.dismiss();
                    list.clear();
                    StudentsListAdapter adapter = new StudentsListAdapter(list, UpdateStudent.this, UpdateStudent.this);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(UpdateStudent.this, "No Data Present in the Database to show", Toast.LENGTH_SHORT).show();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateStudent.this);

        builder.setTitle("Updating " + s.getSname());

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.update_student_dialog, null);
        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(true);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText name = dialogView.findViewById(R.id.UpdateAlertDialogName);
        name.setText(s.getSname());
        final EditText address = dialogView.findViewById(R.id.UpdateAlertDialogAddress);
        address.setText(s.getSaddress());
        final EditText password = dialogView.findViewById(R.id.UpdateAlertDialogPassword);
        password.setText(s.getPassword());

        Button update = dialogView.findViewById(R.id.UpdateStudentButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.setSname(name.getText().toString());
                s.setSaddress(address.getText().toString());
                s.setPassword(password.getText().toString());
                ref.child(s.getSclass()).child(s.getSsection()).child(s.getSphone()).setValue(s);


                alertDialog.dismiss();
                Toast.makeText(UpdateStudent.this, "Value Updated", Toast.LENGTH_SHORT).show();
            }
        });
        // Dialog Box Code Ends Here
    }
}
