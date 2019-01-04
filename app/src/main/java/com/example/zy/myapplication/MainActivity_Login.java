package com.example.zy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


public class MainActivity_Login extends AppCompatActivity implements View.OnClickListener {
    private Button btn1_register, btn_login;
    private EditText et_usertel, et_password;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        et_usertel = (EditText) findViewById(R.id.et_usertel);
        et_password = (EditText) findViewById(R.id.et_password);
        btn1_register = (Button) findViewById(R.id.btn1_register);
        btn_login = (Button) findViewById(R.id.btn1_login);
        btn1_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1_register:
                Intent intent = new Intent(MainActivity_Login.this, MainActivity_Register.class);
                startActivity(intent);
                break;
            case R.id.btn1_login:
                login();
                break;

        }
    }

    //登录
    private void login() {
        final String username = et_usertel.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        //        final String url="http://123.207.85.214/chat/login.php";
        //验证是否为空
        if (TextUtils.isEmpty(username)) {

            Toast.makeText(MainActivity_Login.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity_Login.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        //登录请求 工具类
        Utils.PostLogin(username, password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("网络请求的返回的结果：", result);
                        gson = new Gson();
                        UserInfo userInfo = gson.fromJson(result, UserInfo.class);
                        Log.i("登录成功过的java对象：", userInfo.toString());
//                        登录状态成功后
                        if (userInfo.getStatus().equals("登陆成功")) {

                            Toast.makeText(MainActivity_Login.this, "欢迎回来!" + userInfo.getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity_Login.this, MainActivity.class);
                            SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("user", userInfo.getUser());
                            edit.putString("user1", et_usertel.getText().toString());
                            edit.putString("name", userInfo.getName());
                            edit.putString("password", et_password.getText().toString());
                            edit.commit();

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity_Login.this, "登陆失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        });


    }

}
