package com.pragyatitsolutions.SchoolManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TeacherDataAdapter extends ArrayAdapter<Teacher> {

    private Context context;
    List<Teacher> AllTeachersData;


    public TeacherDataAdapter(@NonNull Context context, int resource, @NonNull List<Teacher> objects) {
        super(context, resource, objects);
        this.context = context;
        AllTeachersData = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(context);
        convertView = inflator.inflate(R.layout.allteachersdata, parent, false);
        TextView phone, name, email, password;
        phone = convertView.findViewById(R.id.PhoneNumber);
        name = convertView.findViewById(R.id.Name);
        email = convertView.findViewById(R.id.Email);
        password = convertView.findViewById(R.id.Password);

        Teacher object = AllTeachersData.get(position);

        phone.setText(object.getPhone());
        name.setText(object.getName());
        email.setText(object.getEmail());
        password.setText(object.getPassword());


        return convertView;
    }
}
