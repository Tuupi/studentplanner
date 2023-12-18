package com.lunix.studysync;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HomeFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth mAuth;
    String user;
    ArrayList<Task> tasklist;
    DatabaseReference taskReference;
    TaskAdapter taskAdapter;
    ArrayList<Exam> examList;
    DatabaseReference examReference;
    ExamAdapter examAdapter;
    ArrayList<Subject> subjectList;
    DatabaseReference subjectReference;
    SubjectAdapter subjectAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        TextView amountCourse = rootView.findViewById(R.id.amountCourse);
        TextView amountTask = rootView.findViewById(R.id.amountTask);
        TextView amountExam = rootView.findViewById(R.id.amountExam);
        String courseamt;
        String taskamt;
        taskReference = FirebaseDatabase.getInstance().getReference("users").child(user).child("task");
        tasklist = new ArrayList<>();
        taskAdapter = new TaskAdapter(requireContext(), tasklist);

        taskReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tasklist.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Task.class).getName());
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Task.class).getCourse());
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Task.class).getDate());
                    Task task = dataSnapshot.getValue(Task.class);
                    tasklist.add(task);

                }
                taskAdapter.notifyDataSetChanged();
                amountTask.setText(Integer.toString(taskAdapter.getItemCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        examReference = FirebaseDatabase.getInstance().getReference("users").child(user).child("exams");
        examList = new ArrayList<>();
        examAdapter = new ExamAdapter(requireContext(), examList);
        examReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                examList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Exam.class).getName());
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Exam.class).getCourse());
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Exam.class).getDate());
                    Exam exam = dataSnapshot.getValue(Exam.class);
                    examList.add(exam);
                }
                examAdapter.notifyDataSetChanged();
                amountExam.setText(Integer.toString(examAdapter.getItemCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        subjectReference = FirebaseDatabase.getInstance().getReference("users").child(user).child("courses");
        subjectList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(requireContext(), subjectList);
        subjectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                subjectList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Subject.class).getCourse());
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Subject.class).getDate());
                    Subject subject = dataSnapshot.getValue(Subject.class);
                    subjectList.add(subject);

                }
                subjectAdapter.notifyDataSetChanged();
                amountCourse.setText(Integer.toString(subjectAdapter.getItemCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        Log.d(TAG, "onCreateView: " + subjectAdapter.getItemCount());
        if (!subjectAdapter.list.isEmpty()) {

        } if(!taskAdapter.list.isEmpty()) {

        } if (!examAdapter.list.isEmpty()) {

        }
        Log.d(TAG, "test");
        return rootView;
    }
}