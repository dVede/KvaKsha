package com.example.registration.Models;

public class User{
    private String username, uid, profileImageUrl, email, status;

    public User(String email, String uid, String profileImageUrl, String username, String status) {
        this.username = username;
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
        this.status = status;
    }
    public User() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
