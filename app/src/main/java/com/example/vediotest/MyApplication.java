package com.example.vediotest;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 陈金桁 on 2018/11/22.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能
    }
}
