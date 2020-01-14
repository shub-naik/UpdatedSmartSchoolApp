package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

        // Build an AlertDialog
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateTeacher.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog_using_alert_dialog, null);

        // Specify alert dialog is not cancelable/not ignorable
        builder.setCancelable(false);

        // Set the custom layout as alert dialog view
        builder.setView(dialogView);
        TextView title = dialogView.findViewById(R.id.AlertTitle);
        TextView message = dialogView.findViewById(R.id.AlertMessage);
        title.setText("Loading..");
        message.setText("Data is Loading...");

        final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //AlertDialog Ends Here

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 0) {
                    TextView textView = findViewById(R.id.ListOfTeachersData);
                    textView.setText("No Data Present in the Database to show for the teachers section");
                    textView.setTextColor(Color.RED);
                    alertDialog.dismiss();
                    return;
                }

                AllTeachers.clear();

                for (DataSnapshot IndividualTeacher : dataSnapshot.getChildren()) {
                    Teacher teacher = IndividualTeacher.getValue(Teacher.class);
                    AllTeachers.add(teacher);
                }

                TeacherDataAdapter adapter = new TeacherDataAdapter(UpdateTeacher.this, R.layout.allteachersdata, AllTeachers);
                AllTeachersData.setAdapter(adapter);
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateTeacher.this, "Database Error ....", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
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

        builder.setView(view);
        builder.setTitle("Updating Teacher.. " + phone);

        final AlertDialog dialog = builder.create();
        dialog.show();

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
                dialog.dismiss();
                Toast.makeText(UpdateTeacher.this, "Teacher Data With Phone " + phone + " is Updated.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
