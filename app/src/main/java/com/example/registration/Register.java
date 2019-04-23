package com.example.registration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView already = findViewById(R.id.already_registartion);
        Button button = findViewById(R.id.register_button_register);
        Button photo = findViewById(R.id.selectphoto_button_register);

        photo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("RegisterActivity", "Try to show photo selector");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.username_edittext_register);
                EditText email = findViewById(R.id.email_edittext_register);
                EditText password = findViewById(R.id.password_eddittext_register);

                final String usernameFind = username.getText().toString();
                final String emailFind = email.getText().toString();
                final String passwordFind = password.getText().toString();

                if (selectedPhoto == null) {
                    Toast.makeText(Register.this, "Please choose image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (emailFind.isEmpty() || passwordFind.isEmpty()) {
                    Toast.makeText(Register.this , "Pleas enter text in email/pw", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("RegisterActivity", "Try to create user with email: " + emailFind);
                Log.d("RegisterActivity", "Try to create user with password: " + passwordFind);
                Log.d("RegisterActivity", "Try to create user with username:" + usernameFind);

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
                                    uploadImageToStorage();
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
                Log.d("RegisterActivity", "Try to show login activity");
                Intent intent = new Intent(Register.this, LoginIn.class);
                startActivity(intent);
            }
        });
    }

    Uri selectedPhoto = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Register.super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity", "Photo selected");
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

    public  void uploadImageToStorage(){
        if (selectedPhoto == null)
            return;
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/image/" + filename);
        ref.putFile(selectedPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.d("RegisterActivity", "Successfully uploaded image:" + taskSnapshot.getMetadata().getPath());
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Log.d("RegisterActivity", "FileLocation " + uri);
                    saveUserInDatabase(uri.toString());
                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RegisterActivity", "Failed to upload image to storage");
            }
        });
    }

    private void saveUserInDatabase(String profileImageUrl){
        String uid = FirebaseAuth.getInstance().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        EditText username = findViewById(R.id.username_edittext_register);
        User user = new User(uid, username.getText().toString(), profileImageUrl);
        ref.child(user.uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("RegisterActivity", "Saved in Database");
                Intent intent = new Intent(Register.this, Messages.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("RegisterActivity", "Failed to save in Database");
            }
        });
    }
}
class User{
    String uid, username, profileImageUrl;

    User(String uid, String username, String profileImageUrl) {
        this.username = username;
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
    }
}