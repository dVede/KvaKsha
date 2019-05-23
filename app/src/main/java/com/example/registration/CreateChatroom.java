package com.example.registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.registration.Models.Chatroom;
import com.example.registration.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CreateChatroom extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);

        final Button button = findViewById(R.id.create_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText createChatroomName = findViewById(R.id.create_chatroom_name);
                        EditText createPasswordChatroom = findViewById(R.id.create_password_chatroom);

                        final String chatroomName = createChatroomName.getText().toString();
                        final String chatroomPassword = createPasswordChatroom.getText().toString();

                        if (chatroomName.contains("@")){
                            Toast.makeText(CreateChatroom.this , "Don't use the @ symbol", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (chatroomName.isEmpty() || chatroomPassword.isEmpty()) {
                            Toast.makeText(CreateChatroom.this , "Please enter text in chatroomName/pw", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ref.child("/users/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final User currentUser = dataSnapshot.getValue(User.class);
                                final String currentUid = currentUser.getUid();
                                final String currentUsername = currentUser.getUsername();
                        Query chatroomNameQuery = FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("chatroomName").equalTo(chatroomName);
                        chatroomNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 0){
                                    ref.child("/chatrooms/" + chatroomName).setValue(new Chatroom(chatroomName, chatroomPassword));
                                    ref.child("/chatrooms/" + chatroomName + "/users/" + currentUsername).setValue(currentUid);
                                    Intent intent = new Intent(CreateChatroom.this, Main_chat_activity.class);
                                    intent.putExtra("chatroomName", chatroomName);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(CreateChatroom.this, "Such chatroom exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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