package com.example.day01_topic1.api;

import java.io.File;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 苏克阳 on 2019/4/27.
 */
public interface MyServer {
    //https://api.apiopen.top/getJoke?page=1&count=2&type=video
    String path = "https://api.apiopen.top/";

    @FormUrlEncoded
    @POST("getJoke?")
    Observable<HomeDatas>getData(@Field("page")int page,@Field("count")int count,@Field("page")String type);
   }
