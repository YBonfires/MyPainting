package com.example.mypainting.gson;

public class Painting {
    private  String topic;
    private int paintingId;
    public  Painting (String topic,int paintingId){
        this.topic=topic;
        this.paintingId=paintingId;
    }
    public String getTopic(){
        return topic;
    }
    public int getPaintingId(){
        return paintingId;
    }
}
