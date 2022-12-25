package com.mianasad.ShyChat.Models;

public class ProfileModel {
    private String phone,bio,birth;

    public ProfileModel(){}
    public ProfileModel(String phone, String bio, String birth) {
        this.phone = phone;
        this.bio = bio;
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
