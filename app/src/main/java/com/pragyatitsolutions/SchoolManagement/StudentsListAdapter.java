package com.pragyatitsolutions.SchoolManagement;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StudentsListAdapter extends RecyclerView.Adapter<StudentsListAdapter.StudentItemHolder> {

    private List<Student> list;
    private Context context;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }


    public StudentsListAdapter(List<Student> list, Context context, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.list = list;
        this.context = context;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public StudentsListAdapter.StudentItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_students, parent, false);
        return new StudentItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsListAdapter.StudentItemHolder holder, int position) {
        final Student s = list.get(position);
        String RollString = s.getSroll();
        String NameString = s.getSname();
        holder.name.setText(NameString);
        holder.roll.setText(RollString);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentItemHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView name, roll;

        public StudentItemHolder(@NonNull View itemView) {
            super(itemView);
            roll = itemView.findViewById(R.id.StudentListRoll);
            name = itemView.findViewById(R.id.StudentListName);
            linearLayout = itemView.findViewById(R.id.ListStudentLinearLayout);


            // Set your listener here.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickInterface.OnItemClick(getAdapterPosition());
                }
            });


        }
    }

}
