package com.example.SchoolManagement;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteBusDriverFragment extends Fragment {

    private TextInputLayout phone;
    private Button delete;
    CoordinatorLayout coordinatorLayout;

    public DeleteBusDriverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_delete_bus_driver, container, false);
        delete = view.findViewById(R.id.DeleteDriverButton);
        phone = view.findViewById(R.id.DeletingDriverPhoneNumber);

        coordinatorLayout = view.findViewById(R.id.DeleteBusDriverCoordinatorLayout);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateDeletePhoneNumber(phone.getEditText().getText().toString(), view);
            }
        });
        return view;
    }

    private void validateDeletePhoneNumber(String phone, final View view) {
        if (view != null && !phone.isEmpty()) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DriversData");
            ref.orderByKey().equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        ref.removeValue();
                        CoordinatorLayout coordinatorLayout = view.findViewById(R.id.DeleteBusDriverCoordinatorLayout);
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#F44336\">Deleted Successfully</font>"), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Intent intent = new Intent(getActivity(), BusManagementActivity.class);
                        startActivity(intent);
                    } else {
                        CoordinatorLayout coordinatorLayout = view.findViewById(R.id.DeleteBusDriverCoordinatorLayout);
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, Html.fromHtml("<font color=\"#F44336\">Invalid Phone Number , Please Enter Correct Credentails</font>"), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
