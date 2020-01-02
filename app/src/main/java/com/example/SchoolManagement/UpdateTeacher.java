package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateTeacher extends AppCompatActivity {

    DatabaseReference reference;
    ListView AllTeachersData;
    Spinner classes, section;
    EditText subject;
    String phone;
    Button Update;
    TextView TeacherPhone;
    List<Teacher> AllTeachers = new ArrayList<Teacher>();


    @Override
    protected void onStart() {
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                AllTeachers.clear();

                for (DataSnapshot IndividualTeacher : dataSnapshot.getChildren()) {
                    Teacher teacher = IndividualTeacher.getValue(Teacher.class);
                    AllTeachers.add(teacher);
                }

                TeacherDataAdapter adapter = new TeacherDataAdapter(UpdateTeacher.this, R.layout.allteachersdata, AllTeachers);
                AllTeachersData.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);
        AllTeachersData = findViewById(R.id.AllTeachersData);


        reference = FirebaseDatabase.getInstance().getReference("TeachersPrimaryData");

        AllTeachersData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Teacher teacher = AllTeachers.get(position);
                phone = teacher.getPhone();
                showUpdateDialog(teacher.phone);
                return false;
            }
        });
    }


    private void showUpdateDialog(final String phone) {

        //Alert Dialog Box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        LayoutInflater inflator = LayoutInflater.from(this);
        View view = inflator.inflate(R.layout.teacherupdatedialog, null, false);

        // Id Linking
        classes = view.findViewById(R.id.Classes);
        section = view.findViewById(R.id.Section);
        subject = view.findViewById(R.id.Subject);
        Update = view.findViewById(R.id.UpdateTeacher);

        //Update Button Listener
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TeacherClass = classes.getSelectedItem().toString();
                String TeacherSection = section.getSelectedItem().toString();
                String TeacherSubject = subject.getText().toString();

                TeacherSectionClassSubject t = new TeacherSectionClassSubject(TeacherClass, TeacherSection, TeacherSubject);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TeacherSubjectsAndSections");
                String id = ref.push().getKey();
                ref.child(id).child(phone).setValue(t);
            }
        });


        builder.setView(view);
        builder.setTitle("Updating Teacher.. " + phone);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
