package com.example.vediotest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.vediotest.domain.Lyric;

import java.util.ArrayList;

/**
 * Created by 陈金桁 on 2018/11/25.
 */

public class ShowLyricView extends TextView {
    private ArrayList<Lyric> lyrics;
    private Paint paint;
    private Paint whitePaint;
    private int width;
    private int height;
    private int index;
    private int textHeight = 100;
    private int currentPostion;
    private long sleepTime;
    private long timePoint;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    public void setLyric(ArrayList<Lyric> lyrics){
        this.lyrics = lyrics;
    }

    public ShowLyricView(Context context) {
        this(context,null);
    }

    public ShowLyricView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShowLyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        lyrics = new ArrayList<>();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(100);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(100);
        whitePaint.setAntiAlias(true);
        whitePaint.setTextAlign(Paint.Align.CENTER);


//        for(int i = 0;i < 1000;i++){
//            Lyric lyric = new Lyric();
//
//            lyric.setTimePoint(1000 * i);
//            lyric.setSleepTime(1500 + i);
//            lyric.setContent("aaaaaaaaaaaa" + i);
//
//            lyrics.add(lyric);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(lyrics != null && lyrics.size() > 0){
            String currentText = lyrics.get(index).getContent();
            canvas.drawText(currentText,width / 2,height / 2,paint);

            int tempY = height / 2;//Y粥中间坐标

            for(int i = index - 1;i >= 0;i--){
                String preText = lyrics.get(i).getContent();

                tempY = tempY - textHeight;

                if(tempY < 0){
                    break;
                }

                canvas.drawText(preText,width / 2,tempY,whitePaint);
            }

            tempY = height / 2;//Y粥中间坐标

            for(int i = index + 1;i < lyrics.size();i++){
                String nextText = lyrics.get(i).getContent();

                tempY = tempY + textHeight;

                if(tempY > height){
                    break;
                }

                canvas.drawText(nextText,width / 2,tempY,whitePaint);
            }

        }else{
            canvas.drawText("没有歌词",width / 2,height / 2,whitePaint);
        }
    }

    public void setShowNextLyric(int currentPosition) {
        this.currentPostion = currentPosition;

        if(lyrics == null || lyrics.size() == 0){
            return;
        }

        for(int i = 1;i < lyrics.size();i++){
            if(currentPosition < lyrics.get(i).getTimePoint()){
                int tempIndex = i - 1;
                if(currentPosition >= lyrics.get(tempIndex).getTimePoint()){
                    index = tempIndex;
                    sleepTime = lyrics.get(index).getSleepTime();
                    timePoint = lyrics.get(index).getTimePoint();


                }
            }
            invalidate();
        }
    }
}
