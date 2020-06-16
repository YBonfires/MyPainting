package com.example.mypainting;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseMode extends AppCompatActivity {
        private TextView info;
        private Button album;
        //闯关按钮
        private Button game;
        //练习按钮
        private Button practice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
        info=findViewById(R.id.info);
        album=findViewById(R.id.button2);
        game=findViewById(R.id.button3);
        practice=findViewById(R.id.button4);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog mydialog=new mydialog(ChooseMode.this,R.style.mydialog);
                   //实例化mydialog设置参数( 参数1:环境上下文 (这里设置this), 参数2：导入样式R.style/样式名)
                mydialog.show();//展示效果
            }
        });
       album.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(ChooseMode.this,Album.class);
               startActivity(intent);
           }
       });

       game.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(ChooseMode.this,MainActivity.class);
               startActivity(intent);
           }
       });
       practice.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(ChooseMode.this,PracticeActivity.class);
               startActivity(intent);
           }
       });
    }
}