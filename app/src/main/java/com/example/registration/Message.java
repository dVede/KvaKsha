package com.example.registration;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.net.URI;
import java.util.Date;
import java.util.Objects;

public class Message extends AppCompatActivity {
    private String user;
    private String text;
    private Date time;
    private Uri image;

    public Message (String text, String user, Uri image) {
        this.text = text;
        this.user = user;
        this.time = new Date();
        this.image = image;
    }

    public Message () {

    }

    //getters and setters for Firebase compatibility
    public String getText() {return text;}
    public void setText (String text) {this.text = text;}

    public String getUser() {return user;}
    public void setUser (String user) {this.user = user;}

    public Date getTime() {return time;}
    public void setTime (Date time) {this.time = time;}

    public Uri getImage() {return image;}
    public void setImage (Uri image) {this.image = image;}

    FloatingActionButton sendMessageButton =
            findViewById(R.id.fab);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                EditText userMessageInput = findViewById(R.id.input); //TODO: adapt getReference() to certain chatrooms
                FirebaseDatabase.getInstance().getReference().child("/conversations/main").push()
                        .setValue(new Message(
                                userMessageInput.getText().toString(),
                                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName(),
                                FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                        );
            }
        });


    }
}
