package com.example.vediotest.pager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vediotest.R;
import com.example.vediotest.activity.AudioPlayActivity;
import com.example.vediotest.activity.SystemVideoPlayer;
import com.example.vediotest.adapter.VideoPagerAdapter;
import com.example.vediotest.base.BasePager;
import com.example.vediotest.domain.MediaItem;
import com.example.vediotest.utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 陈金桁 on 2018/11/18.
 */

public class MusicPager extends BasePager {
    private final boolean isVideo;
    private Utils utils = new Utils();
    private VideoPagerAdapter videoPagerAdapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size() > 0){
                videoPagerAdapter = new VideoPagerAdapter(context,mediaItems,false);
                listView.setAdapter(videoPagerAdapter);
                tv_video.setVisibility(View.GONE);
            }else{
                tv_video.setVisibility(View.VISIBLE);
            }
            pb_video.setVisibility(View.GONE);
        }
    };
    private TextView tv_video;
    private ListView listView;
    private ProgressBar pb_video;
    private ArrayList<MediaItem> mediaItems;
    public MusicPager(Context context,boolean isVideo) {
        super(context);
        this.isVideo = isVideo;
        this.context = context;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.videp_pager,null);
        listView = (ListView) view.findViewById(R.id.listview);
        tv_video = (TextView) view.findViewById(R.id.tv_video);
        pb_video = (ProgressBar) view.findViewById(R.id.pb_video);
        listView.setOnItemClickListener(new MyOnItemClickListtener());
        return view;
    }
    class MyOnItemClickListtener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(context,AudioPlayActivity.class);
            intent.putExtra("position",position);
            context.startActivity(intent);
        }
    }
    @Override
    public void initData() {
        super.initData();
        getDataFromLocal();
    }

    private void getDataFromLocal() {
        mediaItems = new ArrayList<>();
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(5);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,5,10l, TimeUnit.MINUTES,blockingQueue);
        Runnable target = new Runnable() {
            @Override
            public void run() {
                ContentResolver contentProvider = context.getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST,
                };
                Cursor cursor = contentProvider.query(uri,objs,null,null,null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        MediaItem mediaItem = new MediaItem();
                        String name = cursor.getString(0);
                        mediaItem.setName(name);
                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);
                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);
                        String data = cursor.getString(3);
                        mediaItem.setData(data);
                        String artist = cursor.getString(4);
                        mediaItem.setArtist(artist);

                        mediaItems.add(mediaItem);
                    }
                    cursor.close();
                }
                handler.sendEmptyMessage(0);
            }
        };
        executor.execute(target);
    }
}
