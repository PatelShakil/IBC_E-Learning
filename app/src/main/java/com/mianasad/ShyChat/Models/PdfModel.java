package com.mianasad.ShyChat.Models;

public class PdfModel {
    public String uid,filename,filepath,notesId,semname,subname;
    long timestamp;
    public int viewCount;

    public PdfModel() {
    }

    public PdfModel(String uid, String filename, String filepath,long timestamp) {
        this.uid = uid;
        this.filename = filename;
        this.filepath = filepath;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSemname() {
        return semname;
    }

    public void setSemname(String semname) {
        this.semname = semname;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getNotesId() {
        return notesId;
    }

    public void setNotesId(String notesId) {
        this.notesId = notesId;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
