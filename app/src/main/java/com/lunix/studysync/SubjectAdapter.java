package com.lunix.studysync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder>{

    Context context;
    ArrayList<Subject> list;

    public SubjectAdapter(Context context, ArrayList<Subject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.subjectentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.MyViewHolder holder, int position) {
        Subject subject = list.get(position);
        holder.course.setText(subject.getCourse());
        holder.date.setText(subject.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView course, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.textcoursesubject);
            date = itemView.findViewById(R.id.textdatesubject);
        }
    }

}
