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

import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EnterPrivateMessage extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_private_message);

        final Button button = findViewById(R.id.enter_pm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pmUser = findViewById(R.id.pm_username);

                final String userFind = pmUser.getText().toString();


                if (userFind.isEmpty()) {
                    Toast.makeText(EnterPrivateMessage.this , "Please enter text in Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                ref = FirebaseDatabase.getInstance().getReference();


                final Map<String ,String> uidAndUsernameMap = new TreeMap<>();
                final String[] currenUsername = new String[1];
                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                reference.child("/users/").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                                uidAndUsernameMap.put(child.getKey(), child.child("/username/").getValue().toString());
                                if(child.getKey().equals(uid)){
                                    currenUsername[0] = child.child("/username/").getValue().toString();
                                }
                            }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                final List<String> chatroomList = new ArrayList<>();
                reference.child("/chatrooms/").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            chatroomList.add(child.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ref.child("/users/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                final String username = user.getUsername();
                                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(userFind);
                                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount() > 0){
                                            String chatroomName = "@" + username + "@" + userFind;
                                            ref.child("/chatrooms/" +chatroomName).setValue(new Chatroom("@" + username + "@" + userFind));
                                            Intent intent = new Intent(EnterPrivateMessage.this, Message.class);
                                            intent.putExtra("chatroomName", chatroomName);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(EnterPrivateMessage.this, "Such user does not exist", Toast.LENGTH_SHORT).show();
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
                        if(chatroomList.contains("@" + currenUsername[0] + "@" + userFind) ||
                                chatroomList.contains("@" + userFind + "@" + currenUsername[0])){
                            Toast.makeText(EnterPrivateMessage.this, "++++++", Toast.LENGTH_SHORT).show();
                        } else {
                            if (uidAndUsernameMap.values().contains(userFind)) {
                                reference.child("/chatrooms/" + "@" + currenUsername[0] + "@" + userFind + "/chatroomPW/").setValue("0000");

                            } else {
                                Toast.makeText(EnterPrivateMessage.this, "Such user does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
            }
        });
    }
}