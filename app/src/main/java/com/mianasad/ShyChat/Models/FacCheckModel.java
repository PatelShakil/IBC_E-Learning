package com.mianasad.ShyChat.Models;

public class FacCheckModel {
    String ukey,storageuri;

    public FacCheckModel(){}
    public FacCheckModel(String ukey) {
        this.ukey = ukey;
    }


    public String getStorageuri() {
        return storageuri;
    }

    public void setStorageuri(String storageuri) {
        this.storageuri = storageuri;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }
}
