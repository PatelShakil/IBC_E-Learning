package com.mianasad.ShyChat.Models;

public class User {

    private String uid, name, email, imageUrl, token;
    public User() {

    }

    public User(String name, String email, String imageUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl ;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String phoneNumber) {
        this.email = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
