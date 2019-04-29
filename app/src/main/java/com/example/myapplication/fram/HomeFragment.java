package com.example.myapplication.fram;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.HomeAdapter;
import com.example.myapplication.api.MyServer;
import com.example.myapplication.beans.ArtDatas;
import com.example.myapplication.beans.BeannerDatas;
import com.example.myapplication.tab_beans.TabDatas;
import com.example.myapplication.tab_beans.TabPagerDatas;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public class HomeFragment extends Fragment {
    private XRecyclerView xrv;
    private Vector<TabDatas.DataBean> artDatas;
    private Vector<TabPagerDatas.DataBean.DatasBean> beannerDatas;
    private HomeAdapter adapter;
    private int page = 1;
    private List<TabDatas.DataBean> listbanner;
    private List<TabPagerDatas.DataBean.DatasBean> listArt;
    private String TAG = HomeFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        initView(inflate);
        getDatas();//轮播图
        getArtDatas();//文章列表
        return inflate;
    }
    private void getArtDatas() {

        Retrofit rf = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MyServer.path)
                .build();
        rf.create(MyServer.class).getArtDatas(page,294)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TabPagerDatas>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(TabPagerDatas tabPagerDatas) {
                        listArt.addAll(tabPagerDatas.getData().getDatas());
                        Log.i(TAG, "listArt: "+listArt.toString());
                        adapter.notifyDataSetChanged();
                        xrv.refreshComplete();
                        xrv.loadMoreComplete();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete() {
                                }
                });
    }
    private void getDatas() {
        //刷新
        Retrofit rf = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MyServer.path)
                .build();
        rf.create(MyServer.class).getDatas()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TabDatas>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TabDatas tabDatas) {
                        listbanner.addAll(tabDatas.getData());
                        Log.i(TAG, "banner: "+listbanner.toString());
                        adapter.notifyDataSetChanged();
                        xrv.refreshComplete();
                        xrv.loadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

//    4.1 项目分类4.2 项目列表数据
//    https://www.wanandroid.com/project/tree/json
//    某一个分类下项目列表数据，分页展示
//    https://www.wanandroid.com/project/list/1/json?cid=294

    private void initView(View inflate) {
        xrv = (XRecyclerView) inflate.findViewById(R.id.xrv);
        xrv.setLayoutManager(new LinearLayoutManager(getActivity()));
        listbanner = new ArrayList<>();//轮播图
        listArt = new ArrayList<>();//文章列表
        adapter = new HomeAdapter(getActivity(), listArt, listbanner);
        xrv.setAdapter(adapter);
        xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                artDatas.clear();
                beannerDatas.clear();
                getDatas();//轮播图
                getArtDatas();//文章列表
            }

            @Override
            public void onLoadMore() {
                ++page;
                getArtDatas();//文章列表
            }
        });

    }
}
