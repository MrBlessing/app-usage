package com.example.application_usage;

import android.app.usage.UsageStats;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppDetail extends AppCompatActivity {

    private TextView firstRunTime;
    private TextView lastRunTime;
    private TextView finalRunTime;
    private CircleImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        findView();
        setEvent();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setEvent() {
        //获得上一个页面的数据
        Intent intent = getIntent();
        UsageStats state = intent.getParcelableExtra("appInfo");
        //格式化数据
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        //显示数据

        imageView.setImageDrawable(AppInfo.getAppIcon(this,state.getPackageName()));

        firstRunTime.setText(format.format(state.getFirstTimeStamp()));
        lastRunTime.setText(format.format(state.getLastTimeUsed()));
        finalRunTime.setText(format.format(state.getLastTimeStamp()));
    }

    private void findView() {
        firstRunTime = findViewById(R.id.app_detail_firstRun);
        lastRunTime = findViewById(R.id.app_detail_lastRun);
        finalRunTime = findViewById(R.id.app_detail_finalRun);
        imageView = findViewById(R.id.app_detail_appImage);
    }
}
