package com.example.day01_topic1.fram;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.day01_topic1.R;
import com.example.day01_topic1.api.HomeDatas;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class WbFragment extends Fragment {
    private WebView myweb;
    private WebSettings settings;
    private HomeDatas.ResultBean r;
    private String TAG =WebView.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EventBus.getDefault().register(this);
        View inflate = inflater.inflate(R.layout.fragment_wb, container, false);
        initView(inflate);
        return inflate;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
        }
    }
//    @Subscribe annotation，很尴尬，之后各种查网上大神解释，依然没注意问题所在，
// 查看以前写的代码，突然醒悟，因为最近在学 Kotlin，方法定义的时候可以直接定义方法，
// 经常省略方法权限 public/private/protected 等，意识到接收 Event 消息必须是 public 才可以，
// 一个很简单等知识点却因为基础不扎实而浪费了很多时间，希望大家不会遇到小菜的问题。
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public  void getData(HomeDatas.ResultBean r) {
        if (r!=null){
            String thumbnail = r.getThumbnail();
            Log.i(TAG, "路径: "+thumbnail);
            myweb.loadUrl(thumbnail);
            //解除粘性广播
            HomeDatas.ResultBean stickyEvent = EventBus.getDefault().getStickyEvent(r.getClass());
            if (stickyEvent!=null){
                EventBus.getDefault().removeStickyEvent(stickyEvent);
            }
        }
    }


    private void initView(View inflate) {

        myweb = (WebView) inflate.findViewById(R.id.myweb);
        settings = myweb.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        myweb.setWebViewClient(new WebViewClient());

    }
    //解除订阅

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
