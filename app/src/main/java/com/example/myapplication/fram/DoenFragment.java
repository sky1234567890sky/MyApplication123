package com.example.myapplication.fram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.InstallUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;

public class DoenFragment extends Fragment implements View.OnClickListener {

    private Button btn_down;
    private ProgressBar bar;
    private TextView myprogress;
    private TextView mymax;
    private String mpath;
    private long max;
    private long count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_doen, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        btn_down = (Button) inflate.findViewById(R.id.btn_down);
        bar = (ProgressBar) inflate.findViewById(R.id.bar);
        myprogress = (TextView) inflate.findViewById(R.id.myprogress);
        mymax = (TextView) inflate.findViewById(R.id.mymax);

        btn_down.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_down:
                File sd = Environment.getExternalStorageDirectory();
                okDown(sd+"/"+"xxx.apk");
                break;
        }
    }

    private void okDown(final String s) {

        OkHttpClient ok = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url("http://cdn.banmi.com/banmiapp/apk/banmi_330.apk")
                .get()
                .build();
        ok.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        InputStream inputStream = body.byteStream();//获取流
                        //获取文件的最大长度
                        max = body.contentLength();
                        saveFile(inputStream, max,s);

                    }
                });

    }

    private void saveFile(InputStream inputStream, final long max, final String s) {
        //下载
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(s));

            //当前进度
            count = 0;
            int length = -1 ;

            byte[]by= new byte[1024*20];//读取的数量
            while((length=inputStream.read(by))!=-1){
                fileOutputStream.write(by,0,length);
                count +=length;
                Log.i("tag","当前进度："+ count +"，总进度："+max);
                //将值赋给bar
                bar.setProgress((int) count);
                bar.setMax((int) max);
                //赋给文本控件

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myprogress.setText("当前进度："+count/max*100+"%");
                        mymax.setText("最大值:100%");
                    }
                });

            }
            //通知下载完毕

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "下载完毕", Toast.LENGTH_SHORT).show();

                    //安装处理
                    mpath = s;

                    InstallUtil.installApk(getActivity(),s);

                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//下载处理
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //工具类
        if (requestCode == RESULT_OK && resultCode == InstallUtil.UNKNOWN_CODE){
            InstallUtil.installApk(getActivity(),mpath);
        }
    }

    //下载
}
