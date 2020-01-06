package com.example.SchoolManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShowHomeWorkAdapter extends RecyclerView.Adapter<ShowHomeWorkAdapter.ViewHolder> {

    private List<ShowHomeWorkModel> list;
    private Context context;

    public ShowHomeWorkAdapter(Context context, List<ShowHomeWorkModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.show_home_work_single_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShowHomeWorkModel showHomeWorkModel = list.get(position);
        holder.subjectname.append(showHomeWorkModel.getTeacherSubjectName());
        holder.subjectteacher.append(showHomeWorkModel.getTeacherName());
        holder.remarks.append(showHomeWorkModel.getRemarks());
        holder.deadline.append(showHomeWorkModel.getDeadline());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView subjectname, subjectteacher, remarks, deadline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectname = itemView.findViewById(R.id.SubjectName);
            subjectteacher = itemView.findViewById(R.id.SubjectTeacherName);
            remarks = itemView.findViewById(R.id.HomeWorkRemarks);
            deadline = itemView.findViewById(R.id.HomeWorkSubjectDeadline);
        }
    }
}
