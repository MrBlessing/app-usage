package com.example.application_usage;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.myHolder>{
    List<UsageStats> stats = new ArrayList<>();
    private Context context;
    RecyclerAdapter(List<UsageStats> stats,Context context){
        this.stats = stats;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
        RecyclerAdapter.myHolder holder = new RecyclerAdapter.myHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull myHolder myholder, int i) {
        String appName  = getAppName(stats.get(i).getPackageName());
        myholder.appName.setText(appName);

        long usedTime = stats.get(i).getTotalTimeInForeground();
        SimpleDateFormat format = new SimpleDateFormat("hh小时mm分钟ss秒");
        myholder.usedTime.setText(format.format(new Date(usedTime)));
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    static class myHolder extends RecyclerView.ViewHolder{

        private TextView appName;
        private TextView usedTime;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.AppName);
            usedTime = itemView.findViewById(R.id.usedTime);
        }
    }

    public String getAppName(String packageName){
        String name = null;
        ApplicationInfo info = null;
        PackageManager manager = context.getPackageManager();
        try {
            info = manager.getApplicationInfo(packageName,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) info.loadLabel(manager);
    }

}
