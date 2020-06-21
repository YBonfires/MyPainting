package com.example.mypainting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypainting.gson.Ret;
import com.example.mypainting.gson.User;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class login extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private int userId;
    private EditText editEmail;
    private EditText editPassword,editName;
    public static int USER_ID;
   // private SharedPreferences sharedPreferences;
   // private SharedPreferences.Editor editor;
    private int result;
    String password,username,email;
    private static final int LOAD_OPERATE=0x101;
    private CheckBox checkBox;
    private static final String TAG = "LoginTest";


    private Handler myHandler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOAD_OPERATE) {
                // editEmail.setText(msg.arg1);
                result = msg.arg1;
                userId=msg.arg2;
                Log.i("rres", String.valueOf(result));
                if (result == 0) {
                    Intent intent = new Intent(login.this, ChooseMode.class);
                    intent.putExtra("userId",userId);
                    startActivity(intent);

                } else if (result == 1) {
                    Toast.makeText(login.this, "该用户不存在，请重新输入", Toast.LENGTH_SHORT).show();
                    editName.setText("");
                    editPassword.setText("");
                    editEmail.setText("");
                    return;
                } else if (result == 2) {
                    Toast.makeText(login.this, "密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    editPassword.setText("");
                    return;
                }


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView= findViewById(R.id.textView4);
        button=findViewById(R.id.button);
        editPassword=findViewById(R.id.editPassword1);
        editEmail=findViewById(R.id.editEmail1);
        editName=findViewById(R.id.editName);
        SharedPreferences sharedPreferences =getSharedPreferences("data", Context.MODE_PRIVATE);
        checkBox=(CheckBox) findViewById(R.id.checkBox);
        boolean isRemember=sharedPreferences.getBoolean("remember",false);
        if(isRemember){
             email=sharedPreferences.getString("email","0");
             password=sharedPreferences.getString("password","0");
              username=sharedPreferences.getString("name","0");
            editEmail.setSaveEnabled(false);
            editEmail.setText(email);
            editPassword.setText(password);
            editName.setText(username);
            checkBox.setChecked(true);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,Register.class);
                startActivityForResult(intent,0x101);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=editEmail.getText().toString();
                final String password=editPassword.getText().toString();
                final String  username=editName.getText().toString();
                if(TextUtils.isEmpty(editEmail.getText())){
                    Toast.makeText(login.this, "请输入邮箱账号", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(editName.getText())){
                    Toast.makeText(login.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(editPassword.getText())) {
                    Toast.makeText(login.this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                   else {
                    User user = new User(username, email, password, null, 0);
                    Gson gson = new Gson();
                    final String toJson = gson.toJson(user);//把字符串转换成json格式

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://49.235.199.207:8080/guessServer/LoginServlet";
                            OkHttpClient client = new OkHttpClient.Builder()//创建一个OkhttpClient实例
                                    //.callTimeout(5, TimeUnit.SECONDS)
                                    .connectTimeout(5, TimeUnit.SECONDS)
                                    .readTimeout(5, TimeUnit.SECONDS)
                                    .writeTimeout(5, TimeUnit.SECONDS)
                                    .build();
                            Request request = new Request.Builder()//要发起http请求，创建一个request对象
                                    .url(url)//设置目标网络地址
                                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toJson))
                                    .build();
                            //把转换成json格式的数据上传
                            try{
                                Response response = client.newCall(request).execute();
                                String res = response.body().string();//服务器返回的数据
                                Gson gson = new Gson();
                                final Ret FromJson = gson.fromJson(res,Ret.class);
                                result= FromJson.getCode();
                                User user =new User();
                                user =gson.fromJson(FromJson.getData(),User.class);
                                USER_ID=user.getUserid();
                                Log.i("userId", String.valueOf(USER_ID));
                                Message message=Message.obtain();
                                message.what=LOAD_OPERATE;
                                message.arg1=result;
                                message.arg2=USER_ID;
                                SharedPreferences sharedPreferences =getSharedPreferences("data", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                if (checkBox.isChecked()) {
                                    editor.clear();
                                    editor.putBoolean("remember", true);
                                    editor.putString("email", email);
                                    editor.putString("password", password);
                                    editor.putString("name", username);
                                    if(sharedPreferences.contains("email")) {
                                        Log.i("ttext", email);
                                    }
                                } else {
                                    editor.clear();
                                }
                                editor.apply();
                                myHandler.sendMessage(message);
                                Log.i("res", String.valueOf(result));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        String email;
        if (resultCode==0x200) {
            email = intent.getStringExtra("email");
            String password;
            password = intent.getStringExtra("password");
            String username= intent.getStringExtra("username");
            editEmail.setText(email);
            editPassword.setText(password);
            editName.setText(username);
        }
    }

}