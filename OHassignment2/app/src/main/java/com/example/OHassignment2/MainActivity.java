package com.example.OHassignment2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String fullName = "Omar Hussein";
    private final String studentId = "555222333";

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 100;

    private TextView tvGrade;

    private MyService myService;
    private boolean isBound = false;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MyBinder binder = (MyService.MyBinder) service;
            myService = binder.getService();
            isBound = true;
            tvGrade.setText(myService.getMyGrade());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            myService = null;
        }
    };

    private final MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
    private boolean isReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvFullName = findViewById(R.id.tvFullName);
        TextView tvStudentId = findViewById(R.id.tvStudentId);
        tvFullName.setText("Full Name: " + fullName);
        tvStudentId.setText("Student ID: " + studentId);

        tvGrade = findViewById(R.id.tvGrade);

        Button btnStartService = findViewById(R.id.btnStartService);
        Button btnBindService = findViewById(R.id.btnBindService);
        Button btnSendBroadcast = findViewById(R.id.btnSendBroadcast);

        btnStartService.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            }
            Intent serviceIntent = new Intent(this, MyService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        });

        btnBindService.setOnClickListener(v -> {
            Intent bindIntent = new Intent(this, MyService.class);
            bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        });

        btnSendBroadcast.setOnClickListener(v -> {
            Intent broadcastIntent = new Intent(MyBroadcastReceiver.ACTION_MY_BROADCAST);
            broadcastIntent.setPackage(getPackageName());
            sendBroadcast(broadcastIntent);
        });

        Button btnStartExplicit = findViewById(R.id.btnStartExplicit);
        Button btnStartImplicit = findViewById(R.id.btnStartImplicit);

        btnStartExplicit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        btnStartImplicit.setOnClickListener(v -> {
            Intent intent = new Intent("com.example.cs412androidapp.SHOW_CHALLENGES");
            intent.setPackage(getPackageName()); // keep it within this app for this exercise
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(MyBroadcastReceiver.ACTION_MY_BROADCAST);
        ContextCompat.registerReceiver(this, broadcastReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
        isReceiverRegistered = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isReceiverRegistered) {
            unregisterReceiver(broadcastReceiver);
            isReceiverRegistered = false;
        }
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}