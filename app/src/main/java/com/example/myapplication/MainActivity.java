package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.myapplication.adapter.MainPageAdapter;
import com.example.myapplication.fram.DoenFragment;
import com.example.myapplication.fram.HomeFragment;
import com.example.myapplication.fram.UploadFragment;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private Toolbar tol;
    private TabLayout tab;
    private ViewPager vp;
    private RelativeLayout ra;
    private NavigationView na;
    private DrawerLayout dl;
    private MainPageAdapter adapter;
/*
* 苏克阳
* 1809A  周末作业
* */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRg();
    }

    private void initRg() {
        tab.addTab(tab.newTab().setText("首页").setIcon(R.mipmap.ic_login_3party_qq));
        tab.addTab(tab.newTab().setText("上传").setIcon(R.mipmap.ic_launcher_round));
        tab.addTab(tab.newTab().setText("下载").setIcon(R.mipmap.ic_login_3party_weibo));

        Vector<Fragment> list = new Vector<>();
        list.add(new HomeFragment());
        list.add(new UploadFragment());
        list.add(new DoenFragment());

        adapter = new MainPageAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                vp.setCurrentItem(position);
                switch (position) {
                    case 0:
                        tol.setTitle("首页");
                        break;
                    case 1:
                        tol.setTitle("上传");
                        break;
                    case 2:
                        tol.setTitle("下载");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
    }
    private void initView() {
        tol = (Toolbar) findViewById(R.id.tol);
        tab = (TabLayout) findViewById(R.id.tab);
        vp = (ViewPager) findViewById(R.id.vp);
        ra = (RelativeLayout) findViewById(R.id.ra);
        na = (NavigationView) findViewById(R.id.na);
        dl = (DrawerLayout) findViewById(R.id.dl);
        //设置Toolbar
        tol.setTitle("首页");
        //解决图片菜单不现实问题
        na.setItemIconTintList(null);
        //支持Toolbar
        setSupportActionBar(tol);

        //三个杠
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, tol, R.string.app_name, R.string.app_name);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        //侧滑监听事件
        dl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                ra.setX(slideOffset*na.getWidth());
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //侧滑菜单点击事件
        na.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu1:

                        break;
                    case R.id.menu2://pop

                        break;
                    case R.id.menu3://广播

                        break;
                    case R.id.menu4://打电话

                        break;
                }
                return false;
            }
        });
    }
}
