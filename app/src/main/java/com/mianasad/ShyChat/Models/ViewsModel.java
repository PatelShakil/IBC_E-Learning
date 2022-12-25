package com.mianasad.ShyChat.Models;

public class ViewsModel {
    String uid,name,userid;
    long timestamp;
public ViewsModel(){}
    public ViewsModel(String uid, long timestamp) {
        this.uid = uid;
        this.timestamp = timestamp;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
