package com.example.liberty.jiaoxiaotong;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by liberty on 2017/7/7.
 */

public class MyLeanCloudApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "Cqh0u9i6v5t27OG2E9R3munu-gzGzoHsz", "3rtm1BuUUoEDBy7ct99vm25J");

        // 开启调试日志
        //AVOSCloud.setDebugLogEnabled(true);
    }
}
