package com.example.myapplication.fram;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.api.MyServer;
import com.example.myapplication.beans.UploadDatas;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment implements View.OnClickListener {

    private Button btn;
    private ImageView img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_upload, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        btn = (Button) inflate.findViewById(R.id.btn);
        img = (ImageView) inflate.findViewById(R.id.img);

        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                initSd();
                break;
        }
    }

    private void initSd() {
        //给权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            readSd();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            readSd();
        }else{
            Toast.makeText(getActivity(), "错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void readSd() {
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File sd = Environment.getExternalStorageDirectory();
            file= new File(sd, "baobao1.jpg");//上传图片名字
        }
        //ok上传
       // okfile(file);

        //rf上传
        rffile(file);
    }
    private void rffile(File file) {
        //普通参数
        RequestBody body = RequestBody.create(MediaType.parse("application/octet_stream"), "baobao");
        //数据类型
        RequestBody body1 = RequestBody.create(MediaType.parse("image/jpg"), file);
        //文件参数
        MultipartBody.Part file1 = MultipartBody.Part.createFormData("file", file.getName(), body1);

        Retrofit rf = new Retrofit.Builder()
                .baseUrl(MyServer.uploadpath)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        rf.create(MyServer.class).getuploaddatas(body,file1)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UploadDatas>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UploadDatas uploadDatas) {
                        final String url = uploadDatas.getData().getUrl();
                        Log.i("tag", "路径: "+url);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(getActivity()).load(url).into(img);
                            }
                        });
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "rf错误: "+e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                    }
                });


    }

    private void okfile(File file) {
        //普通参数
        RequestBody responseBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        //文件类型
        MultipartBody build = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", "baobaoxxxxmarong")
                .addFormDataPart("file", file.getName(), responseBody)
                .build();

        //post请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        Request build1 = new Request.Builder()
                .post(build)
                .url("http://yun918.cn/study/public/file_upload.php")
                .build();

        okHttpClient.newCall(build1)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("tag", "错误日志: "+e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        String string = body.string();

                        final UploadDatas data = new Gson().fromJson(string, UploadDatas.class);
                        Log.i("tag", "路径: "+data.getData().getUrl());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(getActivity()).load(data.getData().getUrl()).into(img);
                            }
                        });

                    }
                });


    }
}
