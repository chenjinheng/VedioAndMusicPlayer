package com.example.vediotest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vediotest.R;

/**
 * 标题栏
 * Created by 陈金桁 on 2018/11/19.
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {
    private View tv_search;
    private View rl_game;
    private View iv_record;
    private Context context;

    public TitleBar(Context context) {
        super(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("TitleBar","Success");
        this.context = context;
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_search = getChildAt(0);
        rl_game = getChildAt(1);

        iv_record = getChildAt(2);
        Log.e("Finash","Success");
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Toast.makeText(context, "搜搜", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(context, "ganme", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record:
                Toast.makeText(context, "record", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
