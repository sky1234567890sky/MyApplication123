package com.example.myapplication.api;

import com.example.myapplication.beans.UploadDatas;
import com.example.myapplication.tab_beans.TabDatas;
import com.example.myapplication.tab_beans.TabPagerDatas;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by 苏克阳 on 2019/4/26.
 */

public interface MyServer {
    String path = "https://www.wanandroid.com/";
    //https://www.wanandroid.com/
    @GET("project/tree/json")
    Observable<TabDatas>getDatas();
    @FormUrlEncoded
    @POST("project/list/{page}/json?cid=294")
    Observable<TabPagerDatas>getArtDatas(@Path("page")int page, @Field("cid") int id);


    //http://yun918.cn/study/public/file_upload.php

    String uploadpath = "http://yun918.cn/study/public/";
    @Multipart
    @POST("file_upload.php")
    Observable<UploadDatas>getuploaddatas(@Part("key")RequestBody key, @Part MultipartBody.Part file);
}
