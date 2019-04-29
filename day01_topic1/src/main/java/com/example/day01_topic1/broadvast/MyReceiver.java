package com.example.day01_topic1.broadvast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String cid = intent.getStringExtra("cid");
        Toast.makeText(context, ""+cid, Toast.LENGTH_SHORT).show();

    }
}
