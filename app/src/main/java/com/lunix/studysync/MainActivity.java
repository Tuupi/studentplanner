package com.lunix.studysync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emaillogin, passwordlogin;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginBtn = (Button)findViewById(R.id.login);
        emaillogin = findViewById(R.id.emailreg);
        passwordlogin = findViewById(R.id.passwordreg);
        Button registBtn = findViewById(R.id.registerBtn);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr, passwordStr;
                emailStr = emaillogin.getText().toString();
                passwordStr = passwordlogin.getText().toString();

                if(TextUtils.isEmpty(emailStr)){
                    Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                } else
                if(TextUtils.isEmpty(passwordStr)){
                    Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                signIn(emailStr, passwordStr);

            }
        }
        );

        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, register.class);
                startActivity(in);
                emaillogin.setText("");
                passwordlogin.setText("");
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent switchActivityIntent = new Intent(MainActivity.this, dashboard.class);
            startActivity(switchActivityIntent);
            finish();
            return;
        }
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Authentication Success",
                                    Toast.LENGTH_SHORT).show();
                            emaillogin.setText("");
                            passwordlogin.setText("");
                            Intent switchActivityIntent = new Intent(MainActivity.this, dashboard.class);
                            startActivity(switchActivityIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}