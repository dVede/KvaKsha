package com.example.registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.registration.Models.Chatroom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EnterChatroomMessage extends AppCompatActivity {

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_chatroom_message);
        getSupportActionBar().setTitle("Entrance to the chatroom");

        final Button button = findViewById(R.id.enter_button_cm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText createChatroomName = findViewById(R.id.chatroom_name);
                        EditText createPasswordChatroom = findViewById(R.id.password_chatroom);

                        final String chatroomName = createChatroomName.getText().toString();
                        final String chatroomPassword = createPasswordChatroom.getText().toString();

                        if (chatroomName.contains("@")){
                            Toast.makeText(EnterChatroomMessage.this , "Don't use the @ symbol", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (chatroomName.isEmpty() || chatroomPassword.isEmpty()) {
                            Toast.makeText(EnterChatroomMessage.this , "Please enter text in chatroomName/pw", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Query chatroomNameQuery = FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("chatroomName").equalTo(chatroomName);
                        chatroomNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 0){
                                    Toast.makeText(EnterChatroomMessage.this, "Such chatroom don't exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    ref.child("/chatrooms/" + chatroomName).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Chatroom chatroom = dataSnapshot.getValue(Chatroom.class);

                                            String password = chatroom.getPw();
                                            if (password.equals(chatroomPassword)){
                                                Intent intent = new Intent(EnterChatroomMessage.this, Main_chat_activity.class);
                                                intent.putExtra("chatroomName", chatroomName);

                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(EnterChatroomMessage.this, "incorrect password", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                });
            }
        });
    }
}
