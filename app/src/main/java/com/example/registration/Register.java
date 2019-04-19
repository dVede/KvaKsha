package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Register extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView already = findViewById(R.id.already_registartion);
        Button button = findViewById(R.id.register_button_register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.username_edittext_register);
                EditText email = findViewById(R.id.email_edittext_register);
                EditText password = findViewById(R.id.password_eddittext_register);

                String usernameFind = username.getText().toString();
                String emailFind = email.getText().toString();
                String passwordFind = password.getText().toString();

                Log.d("Register", "Email is: " + emailFind);
                Log.d("Register", "Password is: " + passwordFind);
                Log.d("Register", "Username is: " + usernameFind);

                final FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(emailFind, passwordFind)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("emailCreateSuccess", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("emailCreateFail", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }
        });
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Register", "Try to show login activity");
                Intent intent = new Intent(Register.this, LoginIn.class);
                startActivity(intent);
            }
        });
    }
}
