package com.example.day01_topic1.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by 苏克阳 on 2019/4/27.
 */

public class MainPageAdapter  extends FragmentStatePagerAdapter{
    private final ArrayList<Fragment> list;
    public MainPageAdapter(FragmentManager supportFragmentManager, ArrayList<Fragment> list) {
        super(supportFragmentManager);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
