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
 * Use the {@link SubjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SubjectFragment";

    private String mParam1;
    private String mParam2;
    private DatabaseReference databaseUsers;

    RecyclerView recyclerView;
    ArrayList<Subject> list;
    DatabaseReference databaseReference;
    SubjectAdapter subjectAdapter;

    FirebaseAuth mAuth;
    String user;

    public SubjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectFragment newInstance(String param1, String param2) {
        SubjectFragment fragment = new SubjectFragment();
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
        databaseUsers = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_subject, container, false);
        recyclerView = rootView.findViewById(R.id.listSubject);
        Log.d(TAG, "Debug log message" + user);
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user).child("courses");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        subjectAdapter = new SubjectAdapter(requireContext(), list);
        recyclerView.setAdapter(subjectAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Subject.class).getCourse());
                    Log.d(TAG, "Test nama : " + dataSnapshot.getValue(Subject.class).getDate());
                    Subject subject = dataSnapshot.getValue(Subject.class);
                    list.add(subject);
                }
                subjectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                int position = viewHolder.getAdapterPosition(); // this is how you can get the position
                Subject subject = subjectAdapter.list.get(position);
                builder.setMessage("Are you sure you want to delete " + subject.getCourse());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        // You will have your own class ofcourse.

                        // then you can delete the object
                        databaseReference.child(subject.getCourse()).setValue(null);
                    }
                });
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        subjectAdapter.notifyItemChanged(position);
                        dialog.cancel();
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;

    }
}