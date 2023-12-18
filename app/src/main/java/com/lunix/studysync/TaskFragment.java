package com.lunix.studysync;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "TaskFragment";
    private String mParam1;
    private String mParam2;
    private DatabaseReference databaseUsers;

    RecyclerView recyclerView;
    ArrayList<Task> list;
    DatabaseReference databaseReference;
    TaskAdapter taskAdapter;

    FirebaseAuth mAuth;
   String user;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        recyclerView = findViewById(R.id.recycleview);
//        databaseReference = FirebaseDatabase.getInstance().getReference("users");
//        list = new ArrayList<>();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        taskAdapter = new TaskAdapter(this, list);
//        recyclerView.setAdapter(taskAdapter);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    Mytask mytask = dataSnapshot.getValue(Mytask.class);
//                    list.add(mytask);
//                }
//                taskAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
        recyclerView = rootView.findViewById(R.id.listTask);
        Log.d(TAG, "Debug log message" + user);
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user).child("task");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskAdapter = new TaskAdapter(requireContext(), list);
        recyclerView.setAdapter(taskAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Task.class).getName());
                            Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Task.class).getCourse());
                            Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Task.class).getDate());
                            Task task = dataSnapshot.getValue(Task.class);
                            list.add(task);
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT| ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                int position = viewHolder.getAdapterPosition(); // this is how you can get the position
                Task task = taskAdapter.list.get(position);
                builder.setMessage("Are you sure you want to delete " + task.getName());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        // You will have your own class ofcourse.

                        // then you can delete the object
                        databaseReference.child(task.getName()).setValue(null);
                    }
                });
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        taskAdapter.notifyItemChanged(position);
                        dialog.cancel();
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;

        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
//        Button create = rootView.findViewById(R.id.create);
//        Button view = rootView.findViewById(R.id.view);
//        EditText name = rootView.findViewById(R.id.TaskName);
//        EditText course = rootView.findViewById(R.id.CourseName);
//        EditText date = rootView.findViewById(R.id.Date);
//
//        create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InsertData(name.getText().toString(), course.getText().toString(), date.getText().toString());
//            }
//        });
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Tasklist.class));
//                getActivity().finish();
//            }
//        });
//
//        return rootView;
//    }
//
//    private void InsertData(String taskname, String taskcourse, String taskdate) {
//        String id = databaseUsers.push().getKey();
//        Mytask mytask = new Mytask(taskname, taskcourse, taskdate);
//        databaseUsers.child("users").child(id).setValue(mytask)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getActivity(), "Task details inserted", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

}