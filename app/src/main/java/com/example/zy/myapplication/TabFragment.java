package com.example.zy.myapplication;



import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zy.myapplication.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TabFragment extends Fragment {
    ArrayList<UserInfo> userlist;
    private ListView lv_contact;
    @Override//通讯录
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        INITContacts();
        lv_contact= (ListView) view.findViewById(R.id.contact_lv);
        return view;
    }

    private void INITContacts() {
        Utils.GetContacts(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Contacts读取信息失败", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("获取读取Contacts信息中：", "handleMessage: " + data);
                        addUser(data);
                        lv_contact.setAdapter(new UserAdapter(getContext(),userlist));
                    }
                });
            }
        });
    }
//add联系人
    private void addUser(String result) {
        try {
            JSONArray UserArray =  new JSONArray(result);
            userlist = new ArrayList<UserInfo>();
            for (int i = 0; i < UserArray.length(); i++) {
                JSONObject userInfoJson = UserArray.getJSONObject(i);
                String  name= userInfoJson.getString("name");
                String user = userInfoJson.getString("user");
                UserInfo userInfo = new UserInfo(name, user);
                userlist.add(userInfo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
