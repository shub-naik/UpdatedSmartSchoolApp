package com.example.SchoolManagement;


import android.app.DatePickerDialog;

import android.os.Bundle;

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


import java.util.Calendar;
import java.util.HashMap;


public class AddHomeWorkFragment extends Fragment {


    public AddHomeWorkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view1 = inflater.inflate(R.layout.fragment_add_home_work, container, false);
        final Spinner classes = view1.findViewById(R.id.HomeworkClasses);
        final Spinner section = view1.findViewById(R.id.HomeworkSection);


        // For Chooosing the Deadline Date for HomeWork Submission
        final Button chooseDeadline = (Button) view1.findViewById(R.id.ChooseDeadline);
        chooseDeadline.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR));


        chooseDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                chooseDeadline.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
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

                String Node = "Subject : " + Subject.getText().toString() + " On :" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.YEAR) + "-------->" + "Due Date is " + chooseDeadline.getText().toString();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("HomeWork");
                ref.child(c).child(s).child(t.getName() + "==>" + t.getPhone())
                        .child(Node)
                        .setValue(map);

            }
        });


        return view1;
    }

}
