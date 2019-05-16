package com.example.registration;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EnterPrivateMessage extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

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