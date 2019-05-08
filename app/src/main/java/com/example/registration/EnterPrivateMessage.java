package com.example.registration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.child("/chatrooms/" + "@" + userFind + "/chatroomPW/").setValue("0000");
                    }
                });
            }
        });
    }
}
