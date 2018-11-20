package com.example.vediotest.base;

import android.content.Context;
import android.view.View;

/**
 * Created by 陈金桁 on 2018/11/18.
 */

public abstract class BasePager {
    public  Context context;
    public View rootView;
    public boolean isInitData = false;
    public BasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    public abstract View initView();
    public void initData(){

    }
}
