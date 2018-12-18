package com.example.vediotest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * 自定义VideoView
 * Created by 陈金桁 on 2018/11/20.
 */

public class VideoView extends android.widget.VideoView {

    public VideoView(Context context) {
        this(context,null);

    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
    public void setVideoSize(int videoWidth,int videoHeight){
         ViewGroup.LayoutParams params = getLayoutParams();
        params.width = videoWidth;

        params.height = videoHeight;
        setLayoutParams(params);

    }


}
