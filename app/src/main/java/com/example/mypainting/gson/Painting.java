package com.example.mypainting.gson;

public class Painting {

    private int paintingid;

    private int userid;

    private String url;

    private String create_time;

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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

}
