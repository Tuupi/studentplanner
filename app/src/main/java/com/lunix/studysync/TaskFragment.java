package com.lunix.studysync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TaskFragment extends Fragment {

    private ArrayList<task> list = new ArrayList<>();
    private DatabaseReference database;
    private TaskAdapter taskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.listTask); // Replace 'listTask' with the actual ID of your RecyclerView
        database = FirebaseDatabase.getInstance().getReference("task");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TaskAdapter taskAdapter = new TaskAdapter(getActivity(), list);
        recyclerView.setAdapter(taskAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear(); // Clear the list before adding new items
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    task menuItem = dataSnapshot.getValue(task.class);
                    list.add(menuItem);
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });

        return view;
    }

    private void setContentView(int fragmentTask) {
    }

    private void fetchDataFromFirebase() {
        list.clear();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    task menuItem = dataSnapshot.getValue(task.class);
                    list.add(menuItem);
                }
                taskAdapter.notifyDataSetChanged(); // Updated to menuAdapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_task, container, false);
//
//        RecyclerView recyclerView = view.findViewById(R.id.listTask); // Replace 'listTask' with the actual ID of your RecyclerView
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference("task");
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        ArrayList<task> list = new ArrayList<>(); // Declare the 'list' variable
//        TaskAdapter = new TaskAdapter(getActivity(), list);
//        recyclerView.setAdapter(TaskAdapter);
//
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear(); // Clear the list before adding new items
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    task menuItem = dataSnapshot.getValue(task.class);
//                    list.add(menuItem);
//                }
//                TaskAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle errors if needed
//            }
//        });
//
//        return view;
//    }
//
////        btnBack = findViewById(R.id.btnBack);
////
////        btnBack.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent back = new Intent(MenuPage.this, HomePage.class);
////                startActivity(back);
////            }
////        });
//
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                performSearch(query);
////                return true;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                if (newText.isEmpty()) {
////                    list.clear();
////                    fetchDataFromFirebase();
////                } else {
////                    performSearch(newText);
////                }
////                return false;
////            }
////        });
////
////        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
////            @Override
////            public boolean onClose() {
////                list.clear();
////                fetchDataFromFirebase();
////                return false;
////            }
////        });
////    }
//
//    private void setContentView(int fragmentTask) {
//    }
//
//    private void fetchDataFromFirebase() {
//        list.clear();
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    task menuItem = dataSnapshot.getValue(task.class);
//                    list.add(menuItem);
//                }
//                TaskAdapter.notifyDataSetChanged(); // Updated to menuAdapter
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle errors if needed
//            }
//        });
//    }

//    private void performSearch(String query) {
//        list.clear();
//        if (!query.isEmpty()) {
//            database.orderByChild("nameProduct").equalTo(query).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        task menuItem = dataSnapshot.getValue(task.class);
//                        list.add(menuItem);
//                    }
//                    TaskAdapter.notifyDataSetChanged(); // Updated to menuAdapter
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle errors if needed
//                }
//            });
//        } else {
//            fetchDataFromFirebase();
//        }
    }