package com.example.SchoolManagement;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TeacherLoginFragment extends Fragment {


    public TeacherLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_login, container, false);
        final EditText phone = view.findViewById(R.id.LTeacherPhone);
        final EditText password = view.findViewById(R.id.LTeacherPassword);


        Button login = view.findViewById(R.id.LTeacherLogin);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String Phone = phone.getText().toString();
                final String Password = password.getText().toString();
                if (!Phone.isEmpty() && !Password.isEmpty()) {
                    Log.e("FragmentError", Phone + " , " + Password);
                    TeacherActivity teacherActivity = (TeacherActivity) getActivity();
                    teacherActivity.GetDataFromFragment(Phone, Password);
                }
            }
        });

        return view;
    }

}
