package com.example.registration.Models;

public class Chatroom {

    String chatroomName;
    String pw;
    Message message;
    String uid1;
    String uid2;

    Chatroom(){}
    public Chatroom(String chatroomName){
        this.chatroomName = chatroomName;
    }
    public Chatroom(String chatroomName, String uid1, String uid2){
        this.chatroomName = chatroomName;
        this.uid1 = uid1;
        this.uid2 = uid2;
    }
    public Chatroom(String chatroomName, String pw){
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

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
