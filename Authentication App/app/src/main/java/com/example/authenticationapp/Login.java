package com.example.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailAddress, password;
    Button register;
    Button loginBtn;
    ProgressBar pb2;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.pass);
        register = findViewById(R.id.register);
        loginBtn = findViewById(R.id.loginBtn);
        pb2 = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();

        /*Logs in the user into the app*/
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailAddress.getText().toString();
                String pass = password.getText().toString();

                pb2.setVisibility(View.VISIBLE);

                /*Matches the user credentials to that stored in database*/
                fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this,"Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Login.this,"Error!!! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pb2.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        /*Move the user to the registration process view*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });


    }
}