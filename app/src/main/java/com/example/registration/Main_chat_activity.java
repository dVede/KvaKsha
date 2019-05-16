package com.example.registration;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Main_chat_activity extends AppCompatActivity {
    String chatroomPath;
    String currentUser;
    Uri photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        //TODO: set slider here


        Bundle arguments = getIntent().getExtras();
        chatroomPath = "/chatrooms/" + arguments.get("chatroomName").toString() + "/messages";

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("/users/")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    if(child.getKey().equals(uid)){
                        currentUser = child.child("/username/").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        photoURL = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        Log.d("ChatActivity", "getting the chatroom path, current user and URL");


        ListView listOfMessages = findViewById(R.id.list_of_messages);
        FirebaseListAdapter<Message> adapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child(chatroomPath)){
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getText());
                messageUser.setText(model.getUser());
                messageTime.setText(model.getTime());
            }

        };
        listOfMessages.setAdapter(adapter);


        FloatingActionButton sendMessageButton =
                findViewById(R.id.fab);

        View.OnClickListener fabClickListener = new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                EditText userMessageInput = findViewById(R.id.input);

                Message userMessage = new Message(
                        userMessageInput.getText().toString(),
                        currentUser,
                        photoURL
                        );

                DatabaseReference messageID = FirebaseDatabase.getInstance().getReference().child(chatroomPath).push();
                messageID.setValue(userMessage);

                userMessageInput.setText("");
            }
        };
        sendMessageButton.setOnClickListener(fabClickListener);
    }
}
