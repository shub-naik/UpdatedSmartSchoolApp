package com.pragyatitsolutions.SchoolManagement;


import android.app.DatePickerDialog;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;


public class AddHomeWorkFragment extends Fragment {


    public AddHomeWorkFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String[] formattedDate = new String[1];

        // Inflate the layout for this fragment
        final View view1 = inflater.inflate(R.layout.fragment_add_home_work, container, false);
        final Spinner classes = view1.findViewById(R.id.HomeworkClasses);
        final Spinner section = view1.findViewById(R.id.HomeworkSection);


// Todays Date
        LocalDate Today = LocalDate.of(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        final String TodaysDate = Today.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));


        // For Chooosing the Deadline Date for HomeWork Submission
        final Button chooseDeadline = (Button) view1.findViewById(R.id.ChooseDeadline);
        chooseDeadline.setText("Choose DeadLine");


        chooseDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                LocalDate deadline_date = LocalDate.of(year, month + 1, dayOfMonth);
                                formattedDate[0] = deadline_date.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"));
                                chooseDeadline.setText(formattedDate[0]);
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                // For Disabling all the Dates Before The Today's Date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                datePickerDialog.show();
            }
        });

        view1.findViewById(R.id.HomeworkAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherMainIndexActivity activity = (TeacherMainIndexActivity) getActivity();
                Teacher t = activity.object;

                final String c = classes.getSelectedItem().toString();
                final String s = section.getSelectedItem().toString();


                // Object To be Inserted into the Database.
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Class", c);
                map.put("Section", s);

                EditText Subject = view1.findViewById(R.id.HomeworkSubject);
                map.put("Subject", Subject.getText().toString());

                EditText Remarks = view1.findViewById(R.id.HomworkRemarks);
                map.put("Remarks", Remarks.getText().toString());

                map.put("Deadline", chooseDeadline.getText().toString());

                String Node = "Subject : " + Subject.getText().toString() + " On : " + TodaysDate + " and DeadLine : " + formattedDate[0];

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("HomeWork");
                ref.child(c).child(s).child(t.getName() + "==>" + t.getPhone())
                        .child(Node)
                        .setValue(map);

            }
        });


        return view1;
    }

}
