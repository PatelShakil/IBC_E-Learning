package com.mianasad.ShyChat.Models;

public class NoticeModel {
    String title,body,author,noticeid,pdfuri;
    boolean pdf;
    long timestamp;
    int viewCount;
    public NoticeModel(){}
    public NoticeModel(String title, String body, String author,int viewCount,long timestamp,String pdfuri,boolean pdf) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.viewCount = viewCount;
        this.timestamp = timestamp;
        this.pdfuri = pdfuri;
        this.pdf = pdf;
    }

    public String getPdfuri() {
        return pdfuri;
    }

    public void setPdfuri(String pdfuri) {
        this.pdfuri = pdfuri;
    }

    public boolean isPdf() {
        return pdf;
    }

    public void setPdf(boolean pdf) {
        this.pdf = pdf;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
