package com.example.registration.Models;

import com.example.registration.Models.Message;

public class Chatroom {

    String chatroomName;
    String pw;
    Message message;

    String chatroomImageUrl;

    Chatroom(){}
    Chatroom(String chatroomName){
        this.chatroomName = chatroomName;
    }
    Chatroom(String chatroomName, String pw){
        this.chatroomName = chatroomName;
        this.pw = pw;
    }
    public Chatroom(String chatroomName, String pw, String chatroomImageUrl){
        this.chatroomName = chatroomName;
        this.pw = pw;
        this.chatroomImageUrl = chatroomImageUrl;
    }
    Chatroom(String chatroomName, Message message){
        this.chatroomName = chatroomName;
        this.message = message;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public Message getMessage() {
        return message;
    }

    public String getChatroomImageUrl() {
        return chatroomImageUrl;
    }

    public void setChatroomImageUrl(String chatroomImageUrl) {
        this.chatroomImageUrl = chatroomImageUrl;
    }
}
