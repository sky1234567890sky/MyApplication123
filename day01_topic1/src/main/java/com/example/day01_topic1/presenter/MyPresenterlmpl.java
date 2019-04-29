package com.example.day01_topic1.presenter;

import com.example.day01_topic1.api.HomeDatas;
import com.example.day01_topic1.callback.MyCallBack;
import com.example.day01_topic1.modle.MyModle;
import com.example.day01_topic1.view.MyView;

import java.util.List;

/**
 * Created by 苏克阳 on 2019/4/27.
 */

public class MyPresenterlmpl implements MyPresenter, MyCallBack {
    private MyModle myModle;
    private MyView myView;

    public MyPresenterlmpl(MyModle myModle, MyView myView) {
        this.myModle = myModle;
        this.myView = myView;
    }

    @Override
    public void getData(int page, int count, String type) {
        myModle.getData(page,count,type,this);
    }

    @Override
    public void onSuccess(List<HomeDatas.ResultBean> list) {
        myView.onSuccess(list);
    }

    @Override
    public void onFail(String err) {
        myView.onFail(err);
    }
}
