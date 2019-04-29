package com.example.day01_topic1;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownActivty extends AppCompatActivity implements View.OnClickListener {
    private Button btn;
    private ProgressBar bar;
    private TextView tv;
    private File sd;
    private int finalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_activty);
        initView();

    }

    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        bar = (ProgressBar) findViewById(R.id.bar);
        tv = (TextView) findViewById(R.id.tv);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                sd = Environment.getExternalStorageDirectory();
                initDown(sd+"/"+"sss.apk");
                break;
        }
    }

    private void initDown(final String s) {

        OkHttpClient ok = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .get()
                .url("http://cdn.banmi.com/banmiapp/apk/banmi_330.apk")
                .build();
        ok.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(DownActivty.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();

                InputStream inputStream = body.byteStream();
                long max = body.contentLength();

                saveFile(inputStream,max,s);


            }
        });

    }

    private void saveFile(InputStream inputStream, final long max, String s) {

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(s));

            long count =0;

            int length = -1;

            while ((length = inputStream.read(new byte[1024*20]))!=-1){
                    fileOutputStream.write(new byte[1024*20],0,length);
                    count +=length;
                Log.i("tag", "总进度: "+length+",当前进度："+count);
                finalCount += length;

                bar.setMax((int) max);
                bar.setProgress((int) count);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("进度："+ finalCount /max*100+"%");
                    }
                });
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DownActivty.this, "下载完毕", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
