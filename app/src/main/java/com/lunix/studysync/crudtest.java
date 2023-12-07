package com.lunix.studysync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class crudtest extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudtest);
        Button signout = findViewById(R.id.button69);
         mAuth = FirebaseAuth.getInstance();
        signout.setOnClickListener(new View.OnClickListener() {

       @Override
       public void onClick(View view) {
                mAuth.signOut();
                Intent switchActivityIntent = new Intent(crudtest.this, MainActivity.class);
                startActivity(switchActivityIntent);
               }
           }
        );
    }
}