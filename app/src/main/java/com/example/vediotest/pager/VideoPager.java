package com.example.vediotest.pager;

import android.content.ContentProvider;
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
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vediotest.R;
import com.example.vediotest.activity.SystemVideoPlayer;
import com.example.vediotest.adapter.VideoPagerAdapter;
import com.example.vediotest.base.BasePager;
import com.example.vediotest.domain.MediaItem;
import com.example.vediotest.utils.Utils;

import java.util.ArrayList;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * Created by 陈金桁 on 2018/11/18.
 */

public class VideoPager extends BasePager {
    private final boolean isVideo;
    private Utils utils = new Utils();
    private VideoPagerAdapter videoPagerAdapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size() > 0){
                videoPagerAdapter = new VideoPagerAdapter(context,mediaItems,true);
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
    public VideoPager(Context context,boolean isVideo) {
        super(context);
        this.context = context;
        this.isVideo = isVideo;
    }

    @Override
    public View initView() {
        View view = View.inflate(context,R.layout.videp_pager,null);
        listView = (ListView) view.findViewById(R.id.listview);
        tv_video = (TextView) view.findViewById(R.id.tv_video);
        pb_video = (ProgressBar) view.findViewById(R.id.pb_video);
        listView.setOnItemClickListener(new MyOnItemClickListtener());
        return view;
    }
    class MyOnItemClickListtener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);

            Intent intent = new Intent(context,SystemVideoPlayer.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtras(bundle);
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
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.ARTIST,
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
//    class VideoPagerAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return mediaItems.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder viewHolder;
//            if(convertView == null){
//                convertView = View.inflate(context,R.layout.item_video_view,null);
//                viewHolder = new ViewHolder();
//                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//                viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
//                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
//
//                convertView.setTag(viewHolder);
//            }
//            else{
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
//            MediaItem mediaItem = mediaItems.get(position);
//            viewHolder.tv_name.setText(mediaItem.getName());
//            viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
//            viewHolder.tv_time.setText(utils.stringForTime((int)mediaItem.getDuration()));
//            return convertView;
//        }
//
//    }
//    static class ViewHolder{
//        TextView tv_name;
//        TextView tv_size;
//        TextView tv_time;
//
//    }
}
