package com.lunix.studysync;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    private ArrayList<Task> list = new ArrayList<>();
    private DatabaseReference database;
    private TaskAdapter taskAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.listTask);
        database = FirebaseDatabase.getInstance().getReference("Task");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskAdapter = new TaskAdapter(getActivity(), list);
        recyclerView.setAdapter(taskAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Task taskItem = dataSnapshot.getValue(Task.class);
                    list.add(taskItem);
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });

        Button createButton = view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click event
                // Perform your desired action here, such as opening a new activity or displaying a dialog
            }
        });

        return view;
    }
}
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_task, container, false);
//
//        RecyclerView recyclerView = view.findViewById(R.id.listTask); // Replace 'listTask' with the actual ID of your RecyclerView
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Task");
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        ArrayList<Task> list = new ArrayList<>(); // Declare the 'list' variable
//        TaskAdapter = new TaskAdapter(getActivity(), list);
//        recyclerView.setAdapter(TaskAdapter);
//
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear(); // Clear the list before adding new items
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Task menuItem = dataSnapshot.getValue(Task.class);
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
//                    Task menuItem = dataSnapshot.getValue(Task.class);
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
//                        Task menuItem = dataSnapshot.getValue(Task.class);
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
