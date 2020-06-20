package com.example.mypainting;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class MyScoreHandler extends Handler {
        public TextView textView;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int arg1 = msg.arg1;
            String name = (String)msg.obj;
            if(arg1==0)
                textView.setText(name);
            if(arg1==1)
                textView.setText("null");
        }
}

