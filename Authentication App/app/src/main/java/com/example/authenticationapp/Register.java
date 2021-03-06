package com.example.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText firstName, lastName, emailAddress, phoneNumber, password;
    Button register;
    Button login;
    ProgressBar pb;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailAddress = findViewById(R.id.emailAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        password = findViewById(R.id.pass);
        register = findViewById(R.id.registerBtn);
        login = findViewById(R.id.login);
        pb = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailAddress.getText().toString();
                String pass = password.getText().toString();
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String phone = phoneNumber.getText().toString();

                /*Checks for null fields*/
                if (TextUtils.isEmpty(fName)) {
                    firstName.setError("Please enter a first name");
                    return;
                }

                if (TextUtils.isEmpty(lName)) {
                    lastName.setError("Please enter a last name");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    emailAddress.setError("Please enter a valid email address");
                    return;
                }

                if (TextUtils.isEmpty(pass)){
                    password.setError("Please enter a valid password");
                    return;
                }

                if (pass.length() < 6) {
                    password.setError("Password can only be >= 6 characters");
                    return;
                }

                if (phone.length() != 10) {
                    phoneNumber.setError("Phone numbers have 10 characters");
                    return;
                }

                pb.setVisibility(View.VISIBLE);

                /*Create a new user with provided email and password*/
                fAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this,"User Created", Toast.LENGTH_SHORT).show();

                            uID = fAuth.getCurrentUser().getUid();
                            DocumentReference dRef = fStore.collection("Authorized Users").document(uID);

                            Map<String, Object> authorizedUsers = new HashMap<>();

                            authorizedUsers.put("First Name", fName);
                            authorizedUsers.put("Last Name", lName);
                            authorizedUsers.put("Phone Number", phone);
                            authorizedUsers.put("Email Address", email);

                            dRef.set(authorizedUsers);

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Register.this,"Error!!! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        /*Moves the user to login activity*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}