package com.example.mypainting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        final Intent it = new Intent(this, login.class); //你要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
            }
        };
        timer.schedule(task, 1000 * 3); //10秒后*
        // 第二种方法     /*
        // new Handler().postDelayed(new Runnable() {
        // @Override
        // public void run() {
        // Intent intent=new Intent(Main2Activity.this,MainActivity.class);
        // startActivity(intent);
        // finish();            }        }, 3000);*/    }
    }
}