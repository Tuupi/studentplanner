package com.lunix.studysync;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainTask extends AppCompatActivity {
    Button create, view;
    EditText name, course, date;
    DatabaseReference databaseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createtask);
        create = findViewById(R.id.createTask);
        name = findViewById(R.id.TaskName);
        course = findViewById(R.id.CourseName);
        date = findViewById(R.id.Date);
        databaseUsers = FirebaseDatabase.getInstance().getReference();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainTask.this, Tasklist.class));
                finish();
            }
        });
    }

    private void InsertData(){
        String taskname = name.getText().toString();
        String taskcourse = course.getText().toString();
        String taskdate = date.getText().toString();
        String id = databaseUsers.push().getKey();

        Mytask mytask = new Mytask(taskname, taskcourse, taskdate);
        databaseUsers.child("users").child(id).setValue(mytask)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainTask.this, "Task details inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
