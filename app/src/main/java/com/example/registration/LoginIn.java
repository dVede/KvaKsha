package com.example.registration;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginIn extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_button_login).setOnClickListener(this);
        findViewById(R.id.login_registration).setOnClickListener(this);
    }

    public void signIn() {
        EditText email = findViewById(R.id.email_edittext_login);
        EditText password = findViewById(R.id.password_edittext_login);

        String emailFound = email.getText().toString();
        String passwordFound = password.getText().toString();

        if (emailFound.isEmpty() || passwordFound.isEmpty()) {
            Toast.makeText(LoginIn.this , "Pleas enter text in email/pw", Toast.LENGTH_SHORT).show();
            //TODO: move toast string to strings.xml
            return;
        }

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(emailFound, passwordFound)
                .addOnCompleteListener(LoginIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signInWithEmailSuccess", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginIn.this, SlideMenu.class);
                            startActivity(intent);
                            finish();
                            //TODO: showChat() for chowing main chat layout
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signInWithEmailFail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.login_registration) {
            Intent intent = new Intent(LoginIn.this, Register.class);
            startActivity(intent);
        } else if (i == R.id.login_button_login) {
            signIn();
        }
    }
}