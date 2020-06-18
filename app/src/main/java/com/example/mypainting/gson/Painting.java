package com.example.mypainting.gson;

import java.sql.Timestamp;

public class Painting {

    private int paintingid;

    private int userid;

    private String url;

    private Timestamp create_time;

    public Painting() {
    }

    public Painting(int userid, String url) {
        super();
        this.userid = userid;
        this.url = url;
    }

    public int getPaintingid() {
        return paintingid;
    }

    public void setPaintingid(int paintingid) {
        this.paintingid = paintingid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

}
