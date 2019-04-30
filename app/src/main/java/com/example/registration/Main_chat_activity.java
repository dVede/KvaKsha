package com.example.registration;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Main_chat_activity extends AppCompatActivity {
    private FirebaseListAdapter<Message> adapter;

    public void displayMessages() {
        setContentView(R.layout.activity_chatroom);
        //TODO: set slider here
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.message,
                FirebaseDatabase.getInstance().getReference().child("/conversations/main")
        ) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getText());
                messageUser.setText(model.getUser());
                SimpleDateFormat sdp = new SimpleDateFormat("dd-MM (HH:mm:ss)");
                messageTime.setText(sdp.format(model.getTime()));
            }

        };
        listOfMessages.setAdapter(adapter);
    }

}
