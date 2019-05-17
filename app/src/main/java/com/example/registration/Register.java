package com.example.registration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity implements
        View.OnClickListener {
    DatabaseReference ref;
    FirebaseAuth mAuth;
    StorageReference imageRef;

    String uid;
    String usernameFound;
    String emailFound;
    String passwordFound;
    String filename;

    CircleImageView imageCircle;
    EditText username;
    EditText password;
    EditText email;
    Button photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.already_registartion).setOnClickListener(this);
        findViewById(R.id.register_button_register).setOnClickListener(this);
        findViewById(R.id.selectphoto_button_register).setOnClickListener(this);
    }

    Uri selectedPhoto = Uri.parse("https://firebasestorage.googleapis.com/v0/b/kvaksha-77242.appspot.com/o/image%2Flogo_blfstya.png?alt=media&token=12bcabf6-4553-4ef0-9628-af2c8fa7303c");
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
            imageCircle = findViewById(R.id.selectphoto_imagevies_circle);
            photo = findViewById(R.id.selectphoto_button_register);
            imageCircle.setImageBitmap(bitmap);
            photo.setAlpha(0f);
        }
    }

    public void uploadImageToStorage(){
        filename = UUID.randomUUID().toString();
        imageRef = FirebaseStorage.getInstance().getReference("/image/" + filename);
        imageRef.putFile(selectedPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("RegisterActivity", "Successfully uploaded image:" + Objects.requireNonNull(taskSnapshot.getMetadata()).getPath());
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

    private void saveUserInDatabase (String profileImageUrl) {
        uid = FirebaseAuth.getInstance().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        email = findViewById(R.id.email_edittext_register);
        username = findViewById(R.id.username_edittext_register);
        User user = new User(email.getText().toString(), uid, profileImageUrl, username.getText().toString());
        ref.child("/users/" + user.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("RegisterActivity", "Saved in Database");
                Intent intent = new Intent(Register.this, LoginIn.class);
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

    public void imageSelect() {
        Log.d("RegisterActivity", "Try to show photo selector");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    public void register() {
        username = findViewById(R.id.username_edittext_register);
        email = findViewById(R.id.email_edittext_register);
        password = findViewById(R.id.password_eddittext_register);

        usernameFound = username.getText().toString();
        emailFound = email.getText().toString();
        passwordFound = password.getText().toString();

        if (emailFound.isEmpty() || passwordFound.isEmpty() || usernameFound.isEmpty()) {
            Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
        }

        if (passwordFound.length() < 6){
            Toast.makeText(Register.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        }

        Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(usernameFound);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0){
                    Toast.makeText(Register.this, "Username already taken", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(emailFound, passwordFound)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("emailCreateSuccess", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        uploadImageToStorage();
                                    } else {
                                        Log.w("emailCreateFail", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.selectphoto_button_register) {
            imageSelect();
        } else if (i == R.id.register_button_register) {
            register();
        } else if (i == R.id.already_registartion) {
            finish();
        }
    }
}

class User{
    private String username, uid, profileImageUrl, email;

    User(String email, String uid, String profileImageUrl, String username) {
        this.username = username;
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
    }
    public User() {

    }

    String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}