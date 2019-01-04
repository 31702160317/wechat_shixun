package com.example.zy.myapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.URL;

import javax.security.auth.callback.Callback;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2019/1/3.
 */

public class Utils {
    private static final String URL = "http://123.207.85.214/chat/member.php";
    private static final String LoginURL = "http://123.207.85.214/chat/login.php";
    private static final String Register = "http://123.207.85.214/chat/register.php";
    private static final String Chat = "http://123.207.85.214/chat/chat1.php";
    private static  final String Contacts="http://123.207.85.214/chat/member.php";

    //联系人
    public static void GetContacts(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();//第一步：okhttp对象创建
        Request request = new Request.Builder()
                .url(Contacts)                       //第二步：Requset对象创建
                .build();
        client.newCall(request).enqueue(callback);  // client.newCall(request)call对象
    }

    //登录
    public static void PostLogin(String user, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("user", user)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(LoginURL)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //    注册
    public static void PostRegister(String name, String user, String password, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .add("user", user)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(Register)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //读取聊天信息
    public static void readChat(okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        String login = "1";//因为其他会报错
        FormBody requestBody = new FormBody.Builder().add("user", login).build();
        Request request = new Request.Builder()
                .url(Chat)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .post(requestBody).build();

        client.newCall(request).enqueue(callback);
    }

    //发送聊天信息
    public static void SendChat(Context context, String send_content, okhttp3.Callback callback) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String user = sp.getString("user", "");
        String password = sp.getString("password", "");

        OkHttpClient client = new OkHttpClient();
        FormBody requestBody = new FormBody.Builder().add("user", user).add("password", password).add("chat", send_content).build();
        Request request = new Request.Builder().url(Chat).addHeader("Content-Type", "application/json; charset=UTF-8").post(requestBody).build();

        client.newCall(request).enqueue(callback);
    }
}
