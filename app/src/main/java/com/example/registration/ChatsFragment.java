package com.example.registration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class ChatsFragment extends Fragment {
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chatsmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_chats, container, false);
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
                default:
        }
        return true;
    }
}
