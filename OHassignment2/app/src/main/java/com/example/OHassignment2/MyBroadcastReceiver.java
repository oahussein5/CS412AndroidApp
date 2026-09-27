package com.example.OHassignment2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_MY_BROADCAST = "com.example.MY_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Broadcast received!", Toast.LENGTH_SHORT).show();
    }
}