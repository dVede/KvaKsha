package com.example.registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;

public class EnterChatroomMessage extends AppCompatActivity {

    DatabaseReference ref;

    String currentUserUid;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_chatroom_message);
        getSupportActionBar().setTitle("Entrance to the chatroom");

        final Button button = findViewById(R.id.enter_button_cm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                ref = FirebaseDatabase.getInstance().getReference();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText createChatroomName = findViewById(R.id.chatroom_name);
                        EditText createPasswordChatroom = findViewById(R.id.password_chatroom);

                        final String chatroomName = createChatroomName.getText().toString();
                        final String chatroomPassword = createPasswordChatroom.getText().toString();

                        if(chatroomName.isEmpty() || chatroomName.contains("@") || chatroomPassword.isEmpty()) {
                            if (chatroomName.isEmpty()) {
                                createChatroomName.setError("Empty");
                            } else {
                                if (chatroomName.contains("@")) {
                                    createChatroomName.setError("Invalid name'@'");
                                }
                            }
                            if (chatroomPassword.isEmpty()) {
                                createPasswordChatroom.setError("Empty");
                            }
                            return;
                        }

                        ref.child("/users/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final User currentUser = dataSnapshot.getValue(User.class);
                                final String currentUsername = currentUser.getUsername();
                                currentUserUid = currentUser.getUid();
                                Query chatroomNameQuery = FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("chatroomName").equalTo(chatroomName);
                                chatroomNameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() == 0) {
                                            Toast.makeText(EnterChatroomMessage.this, "Such chatroom don't exist", Toast.LENGTH_SHORT).show();
                                        } else {
                                            ref.child("/chatrooms/" + chatroomName).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    Chatroom chatroom = dataSnapshot.getValue(Chatroom.class);

                                                    String password = chatroom.getPw();
                                                    if (password.equals(chatroomPassword)) {
                                                        ref.child("/chatrooms/" + chatroomName + "/users/" + currentUsername).setValue(currentUserUid);
                                                        IntentWithData(chatroomName);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(EnterChatroomMessage.this, "incorrect combination", Toast.LENGTH_SHORT).show();
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
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }


                });
            }
        });
    }

    private void IntentWithData(String chatroomName) {
        Intent intent = new Intent(EnterChatroomMessage.this, Main_chat_activity.class);
        intent.putExtra("chatroomName", chatroomName);
        startActivity(intent);
        Log.d("createSavedMessages", "throwing intent with name:" + chatroomName);
    }
    private void status(String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("/users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}
