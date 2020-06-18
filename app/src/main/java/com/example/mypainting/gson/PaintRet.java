package com.example.mypainting.gson;


public class PaintRet {
    private int code;
    private String msg;
    private String data;
    public PaintRet(int code, String msg, String data){
        super();
        this.code=code;
        this.msg=msg;
        this.data=data;
    }
    public int getCode() {
        return code;
    }
    public String getData() {
        return data;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
