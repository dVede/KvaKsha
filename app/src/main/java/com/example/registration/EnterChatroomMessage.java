package com.example.registration;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EnterChatroomMessage extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_chatroom_message);

        final Button button = findViewById(R.id.enter_button_cm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText createChatroomName = findViewById(R.id.chatroom_name);
                EditText createPasswordChatroom = findViewById(R.id.password_chatroom);

                final String chatroomNameFind = createChatroomName.getText().toString();
                final String passwordChatroomFind = createPasswordChatroom.getText().toString();

                final List<String> chatroomList = new ArrayList<>();
                reference.child("/chatrooms/").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            chatroomList.add(child.toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if (chatroomNameFind.contains("@")){
                    Toast.makeText(EnterChatroomMessage.this , "Don't use the @ symbol", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (chatroomNameFind.isEmpty() || passwordChatroomFind.isEmpty()) {
                    Toast.makeText(EnterChatroomMessage.this , "Please enter text in chatroomName/pw", Toast.LENGTH_SHORT).show();
                    return;
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        });
    }
}
