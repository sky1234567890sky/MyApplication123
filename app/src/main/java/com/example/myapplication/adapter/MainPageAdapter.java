package com.example.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Vector;

/**
 * Created by 苏克阳 on 2019/4/26.
 */

public class MainPageAdapter extends FragmentStatePagerAdapter{
    private final Vector<Fragment> list;

    public MainPageAdapter(FragmentManager supportFragmentManager, Vector<Fragment> list) {
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
