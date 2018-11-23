package com.example.vediotest.service;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.Toast;

import com.example.vediotest.IMusicPlayerService;
import com.example.vediotest.domain.MediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MusicPlayerService extends Service {
    private ArrayList<MediaItem> mediaItems = new ArrayList<>();
    private int position;
    private MediaItem mediaItem;
    private MediaPlayer mediaPlayer;

    public MusicPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return iMusicPlayerService;
    }
    private IMusicPlayerService.Stub iMusicPlayerService = new IMusicPlayerService.Stub() {
        MusicPlayerService service = MusicPlayerService.this;
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getAritist() throws RemoteException {
            return service.getAritist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudiaData() throws RemoteException {
            return service.getAudiaData();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {
            service.setPlayMode(playmode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }
    };

    @Override
    public void onCreate() {
        getDataFromList();
        super.onCreate();
    }

    private void getDataFromList() {
        mediaItems = new ArrayList<>();
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(5);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,5,10l, TimeUnit.MINUTES,blockingQueue);
        Runnable target = new Runnable() {
            @Override
            public void run() {
                ContentResolver contentProvider = getContentResolver();
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

            }
        };
        executor.execute(target);
    }

    //根据位置打开音频文件
    private void openAudio(int position){
        this.position = position;
        if(mediaItems != null && mediaItems.size() > 0){
            mediaItem = mediaItems.get(position);
            if(mediaPlayer != null){
                mediaPlayer.release();
                mediaPlayer.reset();
            }
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "数据正在初始化", Toast.LENGTH_SHORT).show();
        }
    }
    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return false;
        }
    }
    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            start();
        }
    }
    //播放音乐
    private void start(){
        mediaPlayer.start();
    }

    //暂停音乐
    private void pause(){

    }

    //停止音乐
    private void stop(){

    }

    //得到当前播放进度
    private int getCurrentPosition(){
        return 0;
    }

    //得到当前音频的总时长
    private int getDuration(){
        return 0;
    }

    private String getAritist(){
        return null;
    }


    //得到歌曲名字
    private String getName(){
        return "";
    }


    //得到歌曲路径
    private String getAudiaData(){
        return "";
    }
    //播放下一个
    private void next(){

    }

    //播放上一个
    private void pre(){}

    //设置播放模式
    private void setPlayMode(int playmode){

    }

    private int getPlayMode(){
        return 0;
    }
}
