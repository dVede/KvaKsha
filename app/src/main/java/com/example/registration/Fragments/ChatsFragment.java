package com.example.registration.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.registration.ChatroomAdap;
import com.example.registration.CreateChatroom;
import com.example.registration.EnterChatroomMessage;
import com.example.registration.EnterPrivateMessage;
import com.example.registration.Models.Chatroom;
import com.example.registration.R;
import com.example.registration.Models.User;
import com.example.registration.UserAdap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdap userAdap;
    private List<User> mUser;
    private ChatroomAdap chatroomAdap;
    private List<Chatroom> mChatrooms;

    FirebaseUser fUser;
    DatabaseReference reference;

    private List<String> usersList;
    private List<String> chatroomsList;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chatsmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        chatroomsList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("/chatrooms/");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatroomsList.clear();
                usersList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    if (snapshot.toString().contains("@")) {
                        boolean flag = false;
                        for (DataSnapshot d : snapshot.child("/users/").getChildren()) {
                            if (d.getValue().toString().equals(fUser.getUid())) {
                                flag = true;
                            }
                        }
                        if (flag) {
                            for (DataSnapshot d : snapshot.child("/users/").getChildren()) {
                                if (!(d.getValue().toString().equals(fUser.getUid()))) {
                                    usersList.add(d.getValue().toString());
                                }
                            }
                            if (usersList.isEmpty()) {
                                usersList.add(fUser.getUid());
                            }
                        }
                        readChats();
                    } else {
                        for (DataSnapshot d : snapshot.child("/users/").getChildren()) {
                            if (d.getValue().toString().equals(fUser.getUid())) {
                                chatroomsList.add(snapshot.getKey());
                                readChatroom();
                            }
                        }
                        }
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void readChatroom(){
        mChatrooms = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("/chatrooms/");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatrooms.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatroom chatroom = snapshot.getValue(Chatroom.class);

                    for (String id : chatroomsList){
                        if (chatroom.getChatroomName().equals(id)){
                            mChatrooms.add(chatroom);
                        }
                    }
                }
                chatroomAdap = new ChatroomAdap(getContext(), mChatrooms);
                recyclerView.setAdapter(chatroomAdap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChats(){
        mUser = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("/users/");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    for (String id : usersList){
                        if (user.getUid().equals(id)){
                            if (mUser.size() != 0) {
                                for (int i = 0; i < 1 ; i++) {
                                    User user1 = mUser.get(i);
                                    if (!user.getUid().equals(user1.getUid())) {
                                        mUser.add(user);
                                    }
                                }
                            } else {
                                mUser.add(user);
                            }
                        }

                    }
                }
                //userAdap = new UserAdap(getContext(), mUser);
                //recyclerView.setAdapter(userAdap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.create_chatroom:
                Intent intentCC = new Intent(getActivity(), CreateChatroom.class);
                startActivity(intentCC);
                break;
            case R.id.chatroom_messages:
                Intent intentCM = new Intent(getActivity(), EnterChatroomMessage.class);
                startActivity(intentCM);
                break;
            case R.id.private_messages:
                Intent intentPM = new Intent(getActivity(), EnterPrivateMessage.class);
                startActivity(intentPM);
                break;
        }
        return true;
    }
}