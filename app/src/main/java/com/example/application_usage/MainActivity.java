package com.example.application_usage;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView text;
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        findView();
        if(getPermission()){
            setEvent();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setEvent() {
        Calendar now = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.HOUR_OF_DAY,-1);

        UsageStatsManager manager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,yesterday.getTimeInMillis(),now.getTimeInMillis());

        RecyclerAdapter adapter = new RecyclerAdapter(stats,this);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager1);
        recyclerView.setAdapter(adapter);

    }

    public String getAppName(String packageName){
        String name = null;
        ApplicationInfo info = null;
        PackageManager manager = getPackageManager();
        try {
            info = manager.getApplicationInfo(packageName,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) info.loadLabel(manager);
    }

    private void findView() {
        text = findViewById(R.id.text);
        recyclerView = findViewById(R.id.recyclerView);
    }

    public boolean getPermission() {
        boolean res = false;
        if(!checkUsagePermission(this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this, "你的android版本太低", Toast.LENGTH_SHORT).show();
            }
        }else{
            res = true;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean checkUsagePermission(Context context) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(),context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
