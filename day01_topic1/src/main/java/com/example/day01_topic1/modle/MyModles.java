package com.example.day01_topic1.modle;

import com.example.day01_topic1.callback.MyCallBack;

/**
 * Created by 苏克阳 on 2019/4/27.
 */

interface MyModles {
    void getData(int page,int count,String type,MyCallBack myCallBack);
}
