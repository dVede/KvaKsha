package com.example.registration;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.net.URI;
import java.util.Date;

public class Message extends AppCompatActivity {
    private String user;
    private String text;
    private long time;
    private Uri image;

    public Message (String text, String user, Uri image) {
        this.text = text;
        this.user = user;
        this.time = new Date().getTime();
        this.image = image;
    }

    public Message () {

    }

    //getters and setters for Firebase compatibility
    public String getText() {return text;}
    public void setText (String text) {this.text = text;}

    public String getUser() {return user;}
    public void setUser (String user) {this.user = user;}

    public long getTime() {return time;}
    public void setTime (long time) {this.time = time;}

    public Uri getImage() {return image;}
    public void setImage (Uri image) {this.image = image;}

    FloatingActionButton sendMessageButton =
            findViewById(R.id.fab);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        EditText userMessageInput = findViewById(R.id.input); //TODO: adapt getReference() to certain chatrooms
        FirebaseDatabase.getInstance().getReference().push()
                .setValue(new Message(
                        userMessageInput.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                );
    }
}
