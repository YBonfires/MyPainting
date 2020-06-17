package com.example.mypainting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private EditText editEmail;
    private EditText editPassword;
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView= findViewById(R.id.textView4);
        button=findViewById(R.id.button);
        editPassword=findViewById(R.id.editPassword1);
        editEmail=findViewById(R.id.editEmail1);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        checkBox=(CheckBox) findViewById(R.id.checkBox);
        boolean isRemember=pref.getBoolean("remember",false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,Register.class);
                startActivityForResult(intent,0x101);
            }
        });
        if(isRemember){
            String email=pref.getString("email","");
            String password=pref.getString("password","");
            editEmail.setSaveEnabled(false);
            editEmail.setText(email);
            editPassword.setText(password);
            checkBox.setChecked(true);
        }

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
                    editor=pref.edit();
                    if(checkBox.isChecked()){
                        editor.putBoolean("remember",true);
                        editor.putString("email",email);
                        editor.putString("password",password);
                        Toast.makeText(login.this, "checked", Toast.LENGTH_SHORT).show();
                    }else{
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(login.this, ChooseMode.class);
                    startActivity(intent);
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
            editEmail.setText(email);
            editPassword.setText(password);
        }
    }

}