package com.example.zy.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class test extends AppCompatActivity {
private Button btn;
    private EditText ed;
    final OkHttpClient client = new OkHttpClient();

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                Log.d("获取的返回信息1", ReturnMessage);

               /*  gson = new Gson();
                listData = new ArrayList<>();
                listData = gson.fromJson(ReturnMessage, new TypeToken<ArrayList<Contact_bean>>() {}.getType());
                Log.i("tag", "handleMessage: "+listData.toString() );*/


            }



        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn= (Button) findViewById(R.id.test);
        ed= (EditText) findViewById(R.id.test_ed);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getRequest();
            }
        });
    }

    private void getUser() {

        //建立请求表单，添加上传服务器的参数
      /*  RequestBody formBody = new FormBody.Builder()
                .add("user", username)
                .add("password", password)
                .build();*/
        //发起请求
        final Request request = new Request.Builder()
                .url("http://123.207.85.214/chat/member.php")
                .get()
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    //回调
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        handler.obtainMessage(1, response.body().string()).sendToTarget();

                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    private void getRequest() {
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url("http://123.207.85.214/chat/member.php").method("GET",null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            //请求失败执行的方法

            public void onFailure(Call call, IOException e) {
            }
            //请求成功执行的方法

            public void onResponse(Call call, Response response) throws IOException {
                final String data = response.body().string();
                Log.d("response",data);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新UI
                        ed.setText(data);
                    }
                });
            }
        });
    }


}
