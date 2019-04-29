package com.example.day01_topic1.fram;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.day01_topic1.R;
import com.example.day01_topic1.adapter.HomeAdapter;
import com.example.day01_topic1.api.HomeDatas;
import com.example.day01_topic1.modle.MyModle;
import com.example.day01_topic1.presenter.MyPresenterlmpl;
import com.example.day01_topic1.view.MyView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
public class HomeFram extends Fragment implements MyView {
    private XRecyclerView xrv;
    private ArrayList<HomeDatas.ResultBean> list;
    private HomeAdapter adapter;
    private int page =1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_home_fram, container, false);
        initView(inflate);
        getData();
        return inflate;
    }
    private void getData() {
        MyPresenterlmpl myPresenterlmpl = new MyPresenterlmpl(new MyModle(), this);
        myPresenterlmpl.getData(page,20,"video");
    }
    private void initView(View inflate) {
        xrv = (XRecyclerView) inflate.findViewById(R.id.xrv);
        xrv.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        adapter = new HomeAdapter(getActivity(), list);
        xrv.setAdapter(adapter);

        xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                list.clear();
                getData();
            }
            @Override
            public void onLoadMore() {
                ++page;
                getData();
            }
        });

        //点击事件
        adapter.setAa(new HomeAdapter.Aa() {
            @Override
            public void cc(int i, HomeDatas.ResultBean r) {
                //传值
                EventBus.getDefault().postSticky(r);
            }
        });
    }
    @Override
    public void onSuccess(List<HomeDatas.ResultBean> list1) {
        list.addAll(list1);

        Log.i("tag", "数据: "+list1.toString());
        adapter.notifyDataSetChanged();
        xrv.loadMoreComplete();
        xrv.refreshComplete();
    }
    @Override
    public void onFail(String err) {
        Toast.makeText(getActivity(), ""+ err, Toast.LENGTH_SHORT).show();
    }
}
