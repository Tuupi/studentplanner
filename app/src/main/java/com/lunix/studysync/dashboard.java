package com.lunix.studysync;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private EditText selectedDate;
    private Button pickDateBtn;
    private DatabaseReference mDatabase;
    private String userid;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        OnBackPressedDispatcher back = getOnBackPressedDispatcher();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        userid = mAuth.getCurrentUser().getUid();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        replaceFragment(new HomeFragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.exam) {
                replaceFragment(new ExamFragment());
            } else if (item.getItemId() == R.id.subject) {
                replaceFragment(new SubjectFragment());
            } else if (item.getItemId() == R.id.task) {
                replaceFragment(new TaskFragment());
            }

            return true;
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button press event here
                // You can perform any necessary actions or navigation
                AlertDialog.Builder builder = new AlertDialog.Builder(dashboard.this);
                builder.setCancelable(false);
                builder.setMessage("Do you want to Exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user pressed "yes", then he is allowed to exit from application
                        finish();
                    }
                });
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if user select "No", just cancel this dialog and continue with app
                        dialog.cancel();
                    }
                });
                AlertDialog alert=builder.create();
                alert.show();
            }
        };
        back.addCallback(this,callback);
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout layoutTask = dialog.findViewById(R.id.layoutTask);
        LinearLayout layoutExam = dialog.findViewById(R.id.layoutExam);
        LinearLayout layoutSubject = dialog.findViewById(R.id.layoutSubject);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        layoutTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(dashboard.this,"Task",Toast.LENGTH_SHORT).show();
                showNewTask();

            }
        });

        layoutExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(dashboard.this,"Exam",Toast.LENGTH_SHORT).show();
                showNewExam();
            }
        });

        layoutSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(dashboard.this,"Subject",Toast.LENGTH_SHORT).show();
                showNewCourse();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);



    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.nav_logout){
            AlertDialog.Builder builder = new AlertDialog.Builder(dashboard.this);
            builder.setCancelable(false);
            builder.setMessage("Do you want to Log out?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent switchActivityIntent = new Intent(dashboard.this, MainActivity.class);
                    startActivity(switchActivityIntent);
                    finish();
                }
            });
            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user select "No", just cancel this dialog and continue with app
                    dialog.cancel();
                }
            });
            AlertDialog alert=builder.create();
            alert.show();

        } else if(menuItem.getItemId() == R.id.nav_home){
            Toast.makeText(dashboard.this,"Home",Toast.LENGTH_SHORT).show();
        }
//        else if(menuItem.getItemId() == R.id.nav_settings){
//            Toast.makeText(dashboard.this,"Create a settings is Clicked",Toast.LENGTH_SHORT).show();
//        }
        else if(menuItem.getItemId() == R.id.nav_about){
            Toast.makeText(dashboard.this,"About Us",Toast.LENGTH_SHORT).show();
            Intent switchActivityIntent = new Intent(dashboard.this, aboutUs.class);
            startActivity(switchActivityIntent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;


    }
    private void showNewExam() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.createexam);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        pickDateBtn = dialog.findViewById(R.id.idBtnPickDate);
        selectedDate = dialog.findViewById(R.id.Date);
        EditText name = dialog.findViewById(R.id.examName);
        EditText course = dialog.findViewById(R.id.CourseName);
        EditText date = dialog.findViewById(R.id.Date);
        Button submit = dialog.findViewById(R.id.createExam);
        dialog.show();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exam exam = new Exam(name.getText().toString(), course.getText().toString(), date.getText().toString());
                mDatabase.child("users").child(userid).child("exams").child(name.getText().toString()).setValue(exam);
                Toast.makeText(dashboard.this,"Created " + name.getText(),Toast.LENGTH_SHORT).show();
                dialog.dismiss();
////                ExamModel exam = new ExamModel(name.getText().toString(), course.getText().toString(), date.getText().toString());
//                mDatabase.child("users").child(userid).child("exams").child(name.getText().toString()).child("course").setValue(course.getText().toString());
//                mDatabase.child("users").child(userid).child("exams").child(name.getText().toString()).child("date").setValue(date.getText().toString());
            }
        });
        datebutton();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
    private void showNewTask() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.createtask);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        pickDateBtn = dialog.findViewById(R.id.idBtnPickDate);
        selectedDate = dialog.findViewById(R.id.Date);
        EditText name = dialog.findViewById(R.id.TaskName);
        EditText course = dialog.findViewById(R.id.CourseName);
        EditText date = dialog.findViewById(R.id.Date);
        Button submit = dialog.findViewById(R.id.createTask);
//        Button view = dialog.findViewById(R.id.view);
        dialog.show();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task(name.getText().toString(), course.getText().toString(), date.getText().toString());
                mDatabase.child("users").child(userid).child("task").child(name.getText().toString()).setValue(task);
                Toast.makeText(dashboard.this,"Created " + name.getText(),Toast.LENGTH_SHORT).show();
                dialog.dismiss();
////                TaskModel task = new TaskModel(name.getText().toString(), course.getText().toString(), date.getText().toString());
//                DatabaseReference agentRef = mDatabase.child("Courses");
//                agentRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            Toast.makeText(dashboard.this, "hehe", Toast.LENGTH_SHORT).show();
//                        } else {
//                            TaskModel task = new TaskModel(name.getText().toString(), course.getText().toString(), date.getText().toString());
//                            agentRef.setValue(task);
//                            Toast.makeText(dashboard.this, "Agent added successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.e("Firebase", "Error checking agent existence", error.toException());
//                    }
//                });
            }
        });

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(dashboard.this, TaskFragment.class));
//                finish();
//            }
//        });
//
        datebutton();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);


    }
    private void showNewCourse() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.createsubject);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        pickDateBtn = dialog.findViewById(R.id.idBtnPickDate);
        selectedDate = dialog.findViewById(R.id.Date);
        EditText course = dialog.findViewById(R.id.CourseName);
        EditText date = dialog.findViewById(R.id.Date);
        Button submit = dialog.findViewById(R.id.createSubject);
        dialog.show();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject subject = new Subject(course.getText().toString(), date.getText().toString());
                mDatabase.child("users").child(userid).child("courses").child(course.getText().toString()).setValue(subject);
                Toast.makeText(dashboard.this,"Created " + course.getText(),Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        datebutton();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
    public String getiduser(){
        return userid;
    }
    private void datebutton(){

        // on below line we are adding click listener for our pick date button
        pickDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                c.set(Calendar.DAY_OF_MONTH, day);
                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        dashboard.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                cal.set(Calendar.YEAR, year);
                                if(cal.before(c)) {
                                    Toast.makeText(dashboard.this,"please enter a valid date",Toast.LENGTH_SHORT).show();
                                    // notify user about wrong date.
                                    return;
                                }
                                StringBuilder date = new StringBuilder();
                                date.append((dayOfMonth<10?"0":"")).append(dayOfMonth)
                                        .append("-").append((monthOfYear + 1) < 10 ? "0" : "")
                                        .append((monthOfYear+1)).append("-").append(year);
                                selectedDate.setText(date);
                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);

                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
    }


}
