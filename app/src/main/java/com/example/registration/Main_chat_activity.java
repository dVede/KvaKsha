package com.example.registration;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Main_chat_activity extends AppCompatActivity {
    String chatroomPath;
    String currentUser;
    Uri photoURL;
    String currentUID;

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
                        currentUID = uid;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        photoURL = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();

        Log.d("ChatActivity", "getting the chatroom path, current user and URL");


        RecyclerView listOfMessages = findViewById(R.id.list_of_messages);

        FirebaseRecyclerAdapter<Message, MessageViewHolder> adapter =
                new FirebaseRecyclerAdapter<Message, MessageViewHolder>
                        (Message.class, R.layout.message, MessageViewHolder.class,
                            FirebaseDatabase.getInstance().getReference().child(chatroomPath)) {
                    @Override
                    protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                        //setting the view of a single message
                        viewHolder.setMessage(model);
                    }

                    @Override
                    public int getItemViewType(int position){
                        //if it is our message - return 1
                        //if it was sent by the other person - return 0
                        if(currentUser.equals(getItem(position).getSender())) return 1;
                        else return 0;
                    }

                    @Override
                    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        if(viewType == 0) {
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.message, parent, false);
                            return new MessageViewHolder(view);
                        }
                        else if (viewType == 1){
                            View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.this_user_message, parent, false);
                            return new MessageViewHolder(view);
                        }
                        else{
                            throw new RuntimeException("incorrect type while trying to get viewType");
                        }
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
