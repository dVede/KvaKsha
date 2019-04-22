package com.example.registration;

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

public class LoginIn extends AppCompatActivity implements View.OnClickListener {

    EditText email = findViewById(R.id.email_edittext_login);
    EditText password = findViewById(R.id.password_edittext_login);

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_button_login) {
            createAccount(email.getText().toString(), password.getText().toString());
        } else if (i == R.id.login_registration) {
            finish();
        }}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void createAccount(String email, String password){
            final FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("signInWithEmailSuccess", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("signInWithEmailFail", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }

}

