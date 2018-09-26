package com.example.application_usage;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView text;
    private RecyclerView recyclerView;
    private FloatingActionButton button;

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

        List<UsageStats> stats =AppInfo.getStats(this,"day");

        //设置列表
        RecyclerAdapter adapter = new RecyclerAdapter(stats,this);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager1);
        recyclerView.setAdapter(adapter);

        //设置按钮属性
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog().show();
            }
        });
    }
    private void findView() {
        text = findViewById(R.id.text);
        recyclerView = findViewById(R.id.recyclerView);
        button = findViewById(R.id.main_floatActionButton);
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
        return res;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean checkUsagePermission(Context context) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(),context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Dialog setDialog(){
        AlertDialog.Builder bulider = new AlertDialog.Builder(MainActivity.this);
        bulider.setTitle("设置日期");
        bulider.setView(R.layout.dialog_layout);
        //设置内部控件
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_layout,null);
        final EditText text = view.findViewById(R.id.dialog_editText);

        bulider.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return   bulider.create();
    }
}
