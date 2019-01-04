package com.example.zy.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.zy.myapplication.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TabFragment1 extends Fragment {
    private EditText content;
    private Button send;
    private ListView chat_lv;
    ArrayList<ChatInfo> chatlist;
    SharedPreferences sp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//聊天
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        content = (EditText) view.findViewById(R.id.lv_content);
        send = (Button) view.findViewById(R.id.send);
        chat_lv = (ListView) view.findViewById(R.id.lv_chat);
//       读取聊天信息
        readChat();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            点击发送   发送信息
                send_Chat();


            }


        });
        return view;
    }

    //读取聊天信息
    public void readChat() {
        Utils.readChat(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("读取chat信息失败", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("获取读取chat信息中：", "handleMessage: " + data);
                        showChat(data);
                        chat_lv.setAdapter(new ChatAdapter(getContext(), chatlist));

                    }

                });
            }
        });


    }

    //发送聊天信息
    private void send_Chat() {

        Utils.SendChat(getContext(), content.getText().toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("发送chat信息失败", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("发送的信息：", "handleMessage: " + data);
                        showChat(data);
                        chat_lv.setAdapter(new ChatAdapter(getContext(), chatlist));
                    }
                });

            }
        });

    }
    
    //添加chat展示发送聊天信息;
    private void showChat(String returnMessage) {
        try {

            Log.i("ddd", "handleMessage: " + returnMessage);
            JSONArray chatArray = new JSONArray(returnMessage);
            chatlist = new ArrayList<ChatInfo>();
//            倒叙插入
            for (int i = chatArray.length() - 1; i >= 0; i--) {
                JSONObject chatInfoJson = chatArray.getJSONObject(i);
                String name = chatInfoJson.getString("name");
                String time = chatInfoJson.getString("time");
                String chat = chatInfoJson.getString("chat");

                ChatInfo chatinfo = new ChatInfo(name, chat, time);
                chatlist.add(chatinfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
