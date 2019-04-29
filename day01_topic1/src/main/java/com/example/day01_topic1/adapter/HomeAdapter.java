package com.example.day01_topic1.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.day01_topic1.R;
import com.example.day01_topic1.api.HomeDatas;

import java.util.ArrayList;

/**
 * Created by 苏克阳 on 2019/4/27.
 */

public class HomeAdapter  extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    private final FragmentActivity activity;
    private final ArrayList<HomeDatas.ResultBean> list;

    public HomeAdapter(FragmentActivity activity, ArrayList<HomeDatas.ResultBean> list) {

        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(activity, R.layout.item_home, null);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final HomeDatas.ResultBean r = list.get(position);
        Glide.with(activity).load(r.getHeader()).apply(new RequestOptions()
                .transform(new RoundedCorners(8)))
                .placeholder(R.mipmap.angel).into(holder.img);
        holder.tv1.setText(r.getText()+"//"+r.getName());
        holder.tv2.setText(r.getType()+"//"+r.getThumbnail());

        //条目点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aa!=null){
                    aa.cc(position,r);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv1;
        private TextView tv2;
        public ViewHolder(View itemView) {
            super(itemView);


            img = (ImageView) itemView.findViewById(R.id.img);
            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);

        }
    }
    private Aa aa;
    public interface Aa {
        void cc(int i,HomeDatas.ResultBean r);
    }

    public void setAa(Aa aa) {
        this.aa = aa;
    }
}
