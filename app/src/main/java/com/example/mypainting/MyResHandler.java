package com.example.mypainting;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MyResHandler extends Handler{
    public TextView textView;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int arg1 = msg.arg1;
        String name =msg.obj.toString();
        if(arg1==0)
            textView.setText(name);
    }
}
