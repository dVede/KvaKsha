package com.example.registration;

public class Chatroom {

    String chatroomName;
    String pw;
    Message message;

    Chatroom(){}
    Chatroom(String chatroomName){
        this.chatroomName = chatroomName;
    }
    Chatroom(String chatroomName, String pw){
        this.chatroomName = chatroomName;
        this.pw = pw;
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

    public void setMessage(Message message) {
        this.message = message;
    }
}
