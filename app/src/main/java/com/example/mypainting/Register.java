package com.example.mypainting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textView = findViewById(R.id.textView4);
        button = findViewById(R.id.button);
        editEmail = findViewById(R.id.setEmail);
        editPassword = findViewById(R.id.setPassword);
        editPassword2 = findViewById(R.id.setPassword2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent intent = new Intent(Register.this, login.class);
              //  startActivity(intent);
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editEmail.getText().toString();
                final String password = editPassword.getText().toString();
                final String password2 = editPassword2.getText().toString();
                //获取输入在相应控件中的字符串
                //判断输入框内容
                if (TextUtils.isEmpty(editEmail.getText()) ) {
                    Toast.makeText(Register.this, "请输入邮箱账号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editPassword.getText())) {
                    Toast.makeText(Register.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editPassword2.getText())) {
                    Toast.makeText(Register.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!(password).equals(password2)) {
                    Toast.makeText(Register.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();

                  //  Intent intent=new Intent(Register.this,login.class);
                    Intent intent=getIntent();
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                    //startActivity(intent);
                    setResult(0x200,intent);
                    finish();
                }
            }
        });

    }

}