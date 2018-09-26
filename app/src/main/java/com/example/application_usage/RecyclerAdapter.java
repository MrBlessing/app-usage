package com.example.application_usage;

import android.app.usage.UsageStats;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends  RecyclerView.Adapter<RecyclerAdapter.myHolder>{
    List<UsageStats> stats ;
    private Context context;
    RecyclerAdapter(List<UsageStats> stats,Context context){
        this.stats = stats;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list,viewGroup,false);
        RecyclerAdapter.myHolder holder = new RecyclerAdapter.myHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull myHolder myholder, final int i) {
        //设置textView
        String packageName = stats.get(i).getPackageName();
        String appName  = AppInfo.getAppName(context,packageName);
        myholder.appName.setText(appName);

        long usedTime = stats.get(i).getTotalTimeInForeground()-8*60*60*1000;
        SimpleDateFormat format = new SimpleDateFormat("HH小时mm分钟ss秒");
        myholder.usedTime.setText(format.format(new Date(usedTime)));
        myholder.appIcon.setImageDrawable(AppInfo.getAppIcon(context,packageName));

        //设置点击事件
       myholder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AppDetail.class);
                intent.putExtra("appInfo",stats.get(i));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    static class myHolder extends RecyclerView.ViewHolder{

        private TextView appName;
        private TextView usedTime;
        private ImageView appIcon;
        private View view;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            view =itemView;
            appName = itemView.findViewById(R.id.AppName);
            usedTime = itemView.findViewById(R.id.usedTime);
            appIcon = itemView.findViewById(R.id.AppIcon);
        }
    }



}
