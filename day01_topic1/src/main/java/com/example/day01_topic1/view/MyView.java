package com.example.day01_topic1.view;

import com.example.day01_topic1.api.HomeDatas;

import java.util.List;

/**
 * Created by 苏克阳 on 2019/4/27.
 */

public interface MyView {

    void onSuccess(List<HomeDatas.ResultBean> list);
    void onFail(String err);

}
