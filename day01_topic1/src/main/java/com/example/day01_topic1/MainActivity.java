package com.example.day01_topic1;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.day01_topic1.adapter.MainPageAdapter;
import com.example.day01_topic1.broadvast.MyReceiver;
import com.example.day01_topic1.fram.CFragment;
import com.example.day01_topic1.fram.HomeFram;
import com.example.day01_topic1.fram.WbFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar tol;
    private ViewPager vp;
    private TabLayout tab;
    private LinearLayout ll;
    private NavigationView na;
    private DrawerLayout dl;
    private MainPageAdapter adapter;
    private NotificationManager systemService;
    private MyReceiver myReceiver;
    private Intent intent_call;

    // 苏克阳   H1809A   4/27
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initRg();


    }

    private void initRg() {

        ArrayList<Fragment> list = new ArrayList<>();
        HomeFram homeFram = new HomeFram();
        WbFragment wbFragment = new WbFragment();
        CFragment cFragment = new CFragment();
        list.add(homeFram);
        list.add(wbFragment);
        list.add(cFragment);

        tab.addTab(tab.newTab().setText("首页").setIcon(R.drawable.selector_tab1));
        tab.addTab(tab.newTab().setText("web页面").setIcon(R.drawable.selector_tab2));
        tab.addTab(tab.newTab().setText("拍照相册").setIcon(R.drawable.selector_tab3));

        adapter = new MainPageAdapter(getSupportFragmentManager(), list);
        vp.setAdapter(adapter);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                vp.setCurrentItem(position);

                if (position==0){
                    tol.setTitle("首页");
                }else if (position==1){
                    tol.setTitle("web页面");
                }else if (position==2){
                    tol.setTitle("拍照相册");
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
        vp = (ViewPager) findViewById(R.id.vp);
        tab = (TabLayout) findViewById(R.id.tab);
        ll = (LinearLayout) findViewById(R.id.ll);
        na = (NavigationView) findViewById(R.id.na);
        dl = (DrawerLayout) findViewById(R.id.dl);

        tol.setTitle("首页");
        na.setItemIconTintList(null);
        setSupportActionBar(tol);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, tol, R.string.app_name, R.string.app_name);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        //侧滑监听
        dl.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                ll.setX(slideOffset*na.getWidth());
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
        //侧滑菜单监听事件
        na.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu1://pop
                        View inflate = View.inflate(getApplicationContext(), R.layout.item_pop, null);

                        final PopupWindow pw = new PopupWindow(inflate,ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
                        pw.setOutsideTouchable(true);
                        pw.setBackgroundDrawable(new ColorDrawable());
                        inflate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pw.dismiss();
                            }
                        });
                        pw.showAtLocation(ll,Gravity.CENTER,0,0);
                        break;
                    case R.id.menu2://广播

                        //动态广播
                        Intent intent = new Intent("sky");
                        intent.putExtra("cid","这是主界面发给broad");
                        sendBroadcast(intent);
                        break;

                    case R.id.menu3://打电话
                        //打电话
                        //给权限
                        CallPhone();
                        break;
                }
                return false;
            }
        });
    }
//打电话
    private void CallPhone() {
        intent_call = new Intent(Intent.ACTION_CALL);
        intent_call.setData(Uri.parse("tel:10086"));
//授权
        if (Build.VERSION.SDK_INT>=23){
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED){
                //授权打电话
//                CallPhone();
                startActivity(intent_call);
            }else{
                //未授权   动态申请授权
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},100);//响应码
            }
        }else{
            startActivity(intent_call);
        }
    }
    
    //动态申请权限回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断结果
        if (requestCode ==100){
            //允许
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //拨打电话
                startActivity(intent_call);
                Toast.makeText(this, "用户拨打电话中......", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "用户不允许拨打电话......", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Re注册广播
    @Override
    protected void onResume() {
        super.onResume();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sky");
        registerReceiver(myReceiver,intentFilter);
    }
    //解除注册

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    //选项菜单

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,1,1,"通知");
        menu.add(1,2,1,"取消通知");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                systemService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //创建Notification对象，创建状态通道

                String id = "1";
                String name ="DEFAULT";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.enableLights(true);
                    notificationChannel.setShowBadge(true);
                    notificationChannel.setLightColor(Color.RED);
                    systemService.createNotificationChannel(notificationChannel);
                }
                Intent intent = new Intent(MainActivity.this,DownActivty.class);//跳转
                startActivity(intent);
                PendingIntent activities = PendingIntent.getActivities(getApplicationContext(), 123, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification build = new NotificationCompat.Builder(this, "1")
                        .setContentTitle("发送内容")
                        .setSmallIcon(R.mipmap.icon_xianlu_s)
                        .setContentText("标题")
                        .setContentIntent(activities)//演示意图
                        .build();
                systemService.notify(100,build);
                break;
                case 2://取消通知
                    systemService.cancel(100);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}