package com.example.myapplication.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.beans.ArtDatas;
import com.example.myapplication.beans.BeannerDatas;
import com.example.myapplication.tab_beans.TabDatas;
import com.example.myapplication.tab_beans.TabPagerDatas;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
/**
 * Created by 苏克阳 on 2019/4/26.
 */
public class HomeAdapter extends RecyclerView.Adapter {
    private final FragmentActivity activity;
    private final List<TabPagerDatas.DataBean.DatasBean> artDatas;
    private final List<TabDatas.DataBean> beannerDatas;
    public HomeAdapter(FragmentActivity activity, List<TabPagerDatas.DataBean.DatasBean> artDatas, List<TabDatas.DataBean> beannerDatas) {
        this.activity = activity;
        this.artDatas = artDatas;
        this.beannerDatas = beannerDatas;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType==1){
            View inflate = LayoutInflater.from(activity).inflate(R.layout.viewholederartdata_item, null, false);
            viewHolder = new ViewHolederArtData(inflate);
        }else{
            View inflate = LayoutInflater.from(activity).inflate(R.layout.viewholederbeannerdata_item, null, false);
            viewHolder = new ViewHolederBeannerDatas(inflate);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);
        if (type ==1){//文章列表
            int newposition = position;
            if (beannerDatas.size()>0){

                newposition = position - 1;
                TabPagerDatas.DataBean.DatasBean d = artDatas.get(newposition);
                ViewHolederArtData h = (ViewHolederArtData) holder;

                RequestOptions transform = new RequestOptions().transform(new RoundedCorners(8));
                Glide.with(activity).load(d.getEnvelopePic()).apply(transform).placeholder(R.mipmap.angel).into(h.img);
                h.tv1.setText(d.getTitle());
                h.tv2.setText(d.getType());

            }
        }else{//轮播图

            final ViewHolederBeannerDatas b = (ViewHolederBeannerDatas) holder;

            b.mybanner.setImages(beannerDatas).setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    TabPagerDatas.DataBean.DatasBean p = (TabPagerDatas.DataBean.DatasBean) path;
                    String pa = p.getEnvelopePic();
                    Glide.with(activity).load(pa).placeholder(R.mipmap.angel).into(imageView);
                }
            }).start();
        }
    }
    @Override
    public int getItemCount() {
        if (beannerDatas.size()>0){
            return artDatas.size()+1;
        }
        return artDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 &&beannerDatas.size()>0){
           return 2;//返回轮播图
        }
             return 1;//返回文章列表

    }


    class ViewHolederArtData extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView tv1;
        private TextView tv2;
        public ViewHolederArtData(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);


        }
    }

    class ViewHolederBeannerDatas extends RecyclerView.ViewHolder{
        private Banner mybanner;
        public ViewHolederBeannerDatas(View itemView) {
            super(itemView);
            mybanner = (Banner) itemView.findViewById(R.id.mybanner);


        }
    }
}
