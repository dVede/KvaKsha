package com.example.registration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    FirebaseUser fUser;
    DatabaseReference reference;

    private List<String> usersList;

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

        reference = FirebaseDatabase.getInstance().getReference().child("/chatrooms/");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                        }
                        if (usersList.isEmpty()) {
                            usersList.add(fUser.getUid());
                        }

                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
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
                            if (mUser.size() != 0){
                                for (User user1: mUser){
                                    if (!user.getUid().equals(user1.getUid())){
                                        mUser.add(user);
                                    }
                                }
                            } else {
                                mUser.add(user);
                            }
                        }
                    }
                }
                userAdap = new UserAdap(getContext(), mUser);
                recyclerView.setAdapter(userAdap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment;
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
                default:
        }
        return true;
    }
}
