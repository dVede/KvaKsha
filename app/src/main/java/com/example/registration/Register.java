package com.example.registration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;


public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText password = findViewById(R.id.password_eddittext_register);
    EditText username = findViewById(R.id.username_edittext_register);
    EditText email = findViewById(R.id.email_edittext_register);

    Uri selectedPhoto = null;

    public void photo(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    public void already(){
        Log.d("Register", "Try to show login activity");
        Intent intent = new Intent(Register.this, LoginIn.class);
        startActivity(intent);
    }

    public void registerAccount(String email, String password){
        Log.d("Register", "Email is: " + email);
        Log.d("Register", "Password is: " + password);
        Log.d("Register", "Username is: " + username);

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("emailCreateSuccess", "createUserWithEmail:success");
                            uploadUserImageToFirebase();
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

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register_button_register) {
            registerAccount(email.getText().toString(), password.getText().toString());
        } else if (i == R.id.already_registartion) {
            already();
        }
        else if (i == R.id.selectphoto_button_register) {
            photo();
        }}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Register.super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
           selectedPhoto = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
            Button photo = findViewById(R.id.selectphoto_button_register);
            photo.setBackgroundDrawable(bitmapDrawable);


        }
    }

    public void uploadUserImageToFirebase(){
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        StorageTask<UploadTask.TaskSnapshot> registerActivity = ref.putFile(selectedPhoto)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(UploadTask.TaskSnapshot authResult) {
                Log.d("RegisterActivity", "Successfully upload image:" +
                        Objects.requireNonNull(authResult.getMetadata()).getPath());
                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>(){
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Log.d("RegisterActivity", "File location " + task);
                        saveUserInDatabase(task.toString());
                    }
                });
            }
                });
    }
    public void saveUserInDatabase(String profileImageUrl){
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users/" + uid);
        EditText username = findViewById(R.id.username_edittext_register);

        Object user = new User(uid, username.getText().toString(), profileImageUrl);
        ref.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("RegisterActivity", "ADDED");
            }
        });
    }
}
class User {
    private String uid, username, profileImageUrl;
    User(String uid, String toString, String profileImageUrl) {
    }
}