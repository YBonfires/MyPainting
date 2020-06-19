package com.example.mypainting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypainting.gson.Ret;
import com.example.mypainting.gson.User;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPassword2,editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textView = findViewById(R.id.textView4);
        button = findViewById(R.id.button);
        editName=findViewById(R.id.setName);
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
                final String username=editName.getText().toString();
                //获取输入在相应控件中的字符串
                //判断输入框内容
                if (TextUtils.isEmpty(editEmail.getText()) ) {
                    Toast.makeText(Register.this, "请输入邮箱账号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editName.getText())) {
                    Toast.makeText(Register.this, "请设置用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(editPassword.getText())) {
                    Toast.makeText(Register.this, "请设置密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(editPassword2.getText())) {
                    Toast.makeText(Register.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!(password).equals(password2)) {
                    Toast.makeText(Register.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                    User user = new User(username, email, password, null, 0);
                    Gson gson = new Gson();
                    final String toJson = gson.toJson(user);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://10.0.2.2:8080/guessServer/RegisterServlet";
                            OkHttpClient client = new OkHttpClient.Builder()
                                    //.callTimeout(5, TimeUnit.SECONDS)
                                    .connectTimeout(5, TimeUnit.SECONDS)
                                    .readTimeout(5, TimeUnit.SECONDS)
                                    .writeTimeout(5, TimeUnit.SECONDS)
                                    .build();
                            Request request = new Request.Builder()
                                    .url(url)
                                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson))
                                    .build();
                            try{
                                Response response = client.newCall(request).execute();
                                String res = response.body().string();
                                Gson gson = new Gson();
                                final Ret FromJson = gson.fromJson(res,Ret.class);
                                int result= FromJson.getCode();
                                if(result==0){
                                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    Intent intent=getIntent();
                                    intent.putExtra("email",email);
                                    intent.putExtra("password",password);
                                    intent.putExtra("username",username);
                                    //startActivity(intent);
                                    setResult(0x200,intent);
                                    finish();
                                }else if(result==1){
                                    Toast.makeText(Register.this, "注册失败，该用户已存在，请重新输入", Toast.LENGTH_SHORT).show();
                                    editEmail.setText("");
                                    editName.setText("");
                                    editPassword.setText("");
                                    editPassword2.setText("");
                                    return;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                  //  Intent intent=new Intent(Register.this,login.class);
                }
            }
        });

    }

}