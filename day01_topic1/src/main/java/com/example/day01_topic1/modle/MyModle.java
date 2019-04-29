package com.example.day01_topic1.modle;

import com.example.day01_topic1.api.HomeDatas;
import com.example.day01_topic1.api.MyServer;
import com.example.day01_topic1.callback.MyCallBack;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 苏克阳 on 2019/4/27.
 */

public class MyModle implements MyModles{
    @Override
    public void getData(int page, int count, String type, final MyCallBack myCallBack) {

        Retrofit rf = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MyServer.path)
                .build();

        rf.create(MyServer.class).getData(page,count,type)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeDatas>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HomeDatas homeDatas) {
                        myCallBack.onSuccess(homeDatas.getResult());

                            if (homeDatas!=null &&homeDatas.getResult().size()>0){
                                if (homeDatas.getCode()==200){

                                }else{
                                    myCallBack.onFail(homeDatas.getMessage());
                                }
                            }else{
                                myCallBack.onFail("错误");
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        myCallBack.onFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
