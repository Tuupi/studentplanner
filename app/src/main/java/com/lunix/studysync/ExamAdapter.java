package com.lunix.studysync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.MyViewHolder>{

    Context context;
    ArrayList<Exam> list;

    public ExamAdapter(Context context, ArrayList<Exam> list) {
        this.context = context;
        this.list = list;
    }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.examentry, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ExamAdapter.MyViewHolder holder, int position) {
            Exam exam = list.get(position);
            holder.name.setText(exam.getName());
            holder.course.setText(exam.getCourse());
            holder.date.setText(exam.getDate());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView name, course, date;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.textnameExam);
                course = itemView.findViewById(R.id.textcourseExam);
                date = itemView.findViewById(R.id.textdateExam);
            }
    }
}
