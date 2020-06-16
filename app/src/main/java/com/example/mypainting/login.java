package com.example.mypainting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private EditText editEmail;
    private EditText editPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView= findViewById(R.id.textView4);
        button=findViewById(R.id.button);
        editPassword=findViewById(R.id.editPassword);
        editEmail=findViewById(R.id.editEmail);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,Register.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=editEmail.getText().toString();
                String password=editPassword.getText().toString();
                if(TextUtils.isEmpty(editEmail.getText())){
                    Toast.makeText(login.this, "请输入邮箱账号", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(editPassword.getText())){
                    Toast.makeText(login.this, "输入密码", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(login.this, ChooseMode.class);
                    startActivity(intent);
                }
            }
        });

        Intent intent=getIntent();
        String email;
        email = intent.getStringExtra("email");
        String password;
        password = intent.getStringExtra("password");
        editEmail.setText(email);
        editPassword.setText(password);

    }
}