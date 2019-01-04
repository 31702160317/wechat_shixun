package com.example.zy.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zy.myapplication.Utils.Utils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity_Register extends AppCompatActivity implements View.OnClickListener{
private EditText et_name,et_username,et_password;
    private Button btn_register;
    final OkHttpClient client = new OkHttpClient();
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private  void  initView(){
        et_name= (EditText) findViewById(R.id.et_name);
        et_username= (EditText) findViewById(R.id.et_username);
        et_password= (EditText) findViewById(R.id.et_password);
        btn_register= (Button) findViewById(R.id.btn2_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn2_register:
                Register();
            break;
        }
    }
    private void Register() {
        final String name = et_name.getText().toString().trim();
        final String username = et_username.getText().toString().trim();
        final String password = et_password.getText().toString().trim();

        //验证是否为空
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(MainActivity_Register.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity_Register.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(name)){
            Toast.makeText(MainActivity_Register.this, "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }

//        注册请求
        Utils.PostRegister(name, username, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("失败注册请求", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    final String data=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("注册请求来的数据：", data);
                        gson = new Gson();
                        UserInfo.UserRegister userRegister = gson.fromJson(data, UserInfo.UserRegister.class);
                        Log.i("解析过后注册成java对象:", userRegister.toString());
                        if (userRegister.getStatus().equals("注册成功")) {
                            Toast.makeText(MainActivity_Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity_Register.this, MainActivity_Login.class);
                            startActivity(intent);
                        }else if(userRegister.getStatus().equals(userRegister.getStatus())){
                            Toast.makeText(MainActivity_Register.this, "注册失败"+userRegister.getStatus(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });
    }



}
