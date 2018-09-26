package com.example.application_usage;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.content.Context.USAGE_STATS_SERVICE;

public class AppInfo {
    private static final String TAG = "AppInfo";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<UsageStats> getStats(Context context,String type) {
        Calendar now = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        int calenderType = 0;
        int USMType = 0;
        switch (type){
            case "day" :
                calenderType = Calendar.HOUR_OF_DAY;
                USMType = UsageStatsManager.INTERVAL_DAILY;
                break;
            case "week" :
                calenderType = Calendar.DAY_OF_WEEK;
                USMType = UsageStatsManager.INTERVAL_WEEKLY;
                break;
        }
        yesterday.add(calenderType,-1);
        UsageStatsManager manager = (UsageStatsManager)context.getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(USMType,yesterday.getTimeInMillis(),now.getTimeInMillis());

        //筛选UsageStats
        //条件时间不为0，名字中不能有.
        for(int i=0;i<stats.size();i++){
            String appName = AppInfo.getAppName(context,stats.get(i).getPackageName());
            if(appName.contains(".")){
                stats.remove(i);
                i--;
            }
            long apptime = stats.get(i).getTotalTimeInForeground();
            if(apptime<1000){
                stats.remove(i);
                i--;
            }

        }

        //排序UsageStats
        for(int i=0;i<stats.size();i++){
            for(int j=0;j<stats.size();j++){
                long time_i = stats.get(i).getTotalTimeInForeground();
                long time_j = stats.get(j).getTotalTimeInForeground();
                if(time_i>time_j){
                    Collections.swap(stats,i,j);
                }
            }
        }


        return stats;
    }

    public static String getAppName(Context context,String packageName){
        ApplicationInfo info = null;
        PackageManager manager = context.getPackageManager();
        try {
            info = manager.getApplicationInfo(packageName,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) info.loadLabel(manager);
    }

    public static Drawable getAppIcon(Context context, String packageName){
        ApplicationInfo info = null;
        PackageManager manager = context.getPackageManager();
        try {
            info = manager.getApplicationInfo(packageName,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.loadIcon(manager);
    }
}
