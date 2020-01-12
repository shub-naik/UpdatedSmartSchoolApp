package com.pragyatitsolutions.SchoolManagement;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddBusDriverFragment extends Fragment {

    private TextInputLayout phone, license, password;
    Button register;

    public AddBusDriverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_bus_driver, container, false);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        phone = view.findViewById(R.id.DriverMobileNumber);
        password = view.findViewById(R.id.DriverPassword);
        license = view.findViewById(R.id.DriverLicenseNumber);
        register = view.findViewById(R.id.RegisterDriver);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Driver Management");
                progressDialog.setMessage("Adding Driver");
                progressDialog.setCancelable(false);
                final String Phone = phone.getEditText().getText().toString();
                final String Password = password.getEditText().getText().toString();
                final String License = license.getEditText().getText().toString();


                if (!validatePhoneNumber(Phone) | !validatePassword(Password) | !validateLicenseNumber(License)) {
                    return;
                } else {
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DriversData");

                    final HashMap<String, Object> hashMap = new HashMap<>();

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            progressDialog.show();
                            long childrens = dataSnapshot.getChildrenCount() + 1;
                            hashMap.put("ID", "D-" + Phone + "-" + childrens);
                            hashMap.put("Phone", Phone);
                            hashMap.put("Password", Password);
                            hashMap.put("License_Number", License);
                            ref.child(Phone).setValue(hashMap);
                            progressDialog.dismiss();
                            Intent intent = new Intent(getActivity(), BusManagementActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                progressDialog.dismiss();
            }
        });

        return view;
    }


    private boolean validatePhoneNumber(String Phone) {
        if (Phone.isEmpty() || Phone.length() < 10) {
            phone.setError("Invalid Phone Number");
            return false;
        }
        return true;
    }

    private boolean validateLicenseNumber(String License) {
        if (License.isEmpty()) {
            license.setError("License Number is Mandatory");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String Password) {
        if (Password.isEmpty() && Password.length() < 6) {
            password.setError("Password must be atleast 6 characters long");
            return false;
        }
        return true;
    }
}
