package com.example.vediotest.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.vediotest.IMusicPlayerService;
import com.example.vediotest.R;
import com.example.vediotest.activity.AudioPlayActivity;
import com.example.vediotest.domain.MediaItem;
import com.example.vediotest.utils.CacheUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 后台播放服务
 */

public class MusicPlayerService extends Service {
    public static final String S = "com.example.videotext.OPEN";
    private ArrayList<MediaItem> mediaItems = new ArrayList<>();
    private int position;
    private MediaItem mediaItem;
    private MediaPlayer mediaPlayer;

    public static final int REPEAT_NORMAL = 1;
    public static final int REPEAT_SINGLE = 2;
    public static final int REPEAT_ALL = 3;
    private int palymode = REPEAT_NORMAL;


    private NotificationManager notificationManager;


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
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);
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

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }
    };

    @Override
    public void onCreate() {
        this.palymode = CacheUtils.getPlaymode(this,"playmode");
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

    private void seekTo(int position){
        mediaPlayer.seekTo(position);
    }
    //根据位置打开音频文件
    private void openAudio(int position){
        this.position = position;
        if(mediaItems != null && mediaItems.size() > 0){
            mediaItem = mediaItems.get(this.position);
            if(mediaPlayer != null){
//                mediaPlayer.release();
                mediaPlayer.reset();
            }
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                mediaPlayer.prepareAsync();

                if(palymode == REPEAT_SINGLE){
                    mediaPlayer.setLooping(true);
                }else{
                    mediaPlayer.setLooping(false);
                }

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
//            notifyChange(S);
            EventBus.getDefault().post(mediaItem);
            start();
        }
    }

    private void notifyChange(String s) {
        Intent intent = new Intent(s);
        sendBroadcast(intent);
    }

    //播放音乐
    private void start(){
        mediaPlayer.start();

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayActivity.class);
        intent.putExtra("Notification",true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("简易音乐")
                .setContentText("正在播放" + getName())
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(1,notification);
    }

    //暂停音乐
    private void pause(){
        mediaPlayer.pause();
    }

    //停止音乐
    private void stop(){

    }

    //得到当前播放进度
    private int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    //得到当前音频的总时长
    private int getDuration(){
        return mediaPlayer.getDuration();
    }

    private String getAritist(){
        return mediaItem.getArtist();
    }

    private boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }
    //得到歌曲名字
    private String getName(){
        return mediaItem.getName();
    }


    //得到歌曲路径
    private String getAudiaData(){
        return mediaItem.getData();
    }
    //播放下一个
    private void next(){
        setNextPosition();

        openNextAudio();
    }

    private void openNextAudio() {
        Log.e("TAG","openNextAudio" );
            int playmode = getPlayMode();
            if(playmode == MusicPlayerService.REPEAT_NORMAL){
                Log.e("TAG","REPEAT_NORMAL" );
                if(position < mediaItems.size()){
                    openAudio(position);
                    Log.e("TAG",position + "00" );
                }else{
                    position = mediaItems.size() - 1;
                }
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                openAudio(position);

            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                openAudio(position);
            }
            else {
                if(position < mediaItems.size()){
                    openAudio(position);
                }else{
                    position = mediaItems.size() - 1;
                }
            }
    }

    private void setNextPosition() {
        int playmode = getPlayMode();
        Log.e("TAG",position + "");
        if(playmode == MusicPlayerService.REPEAT_NORMAL){
            ++position;
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            Log.e("TAG","REPEAT_SINGLE");
            position++;
            if(position >= mediaItems.size()){
                position = 0;
            }
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            position++;
            if(position >= mediaItems.size()){
                position = 0;
            }
        }
        else {
            position++;
        }
    }

    //播放上一个
    private void pre(){
        setPrePosition();

        openPreAudio();
    }

    private void setPrePosition() {
        int playmode = getPlayMode();
        if(playmode == MusicPlayerService.REPEAT_NORMAL){
            position--;
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            position--;
            if(position < 0){
                position = mediaItems.size() - 1;
            }
        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            position++;
            if(position < 0){
                position = mediaItems.size() - 1;
            }
        }
        else {
            position--;
        }
    }

    private void openPreAudio() {
        int playmode = getPlayMode();
        if(playmode == MusicPlayerService.REPEAT_NORMAL){
            if(position >= 0){
                openAudio(position);
            }else{
                position = 0;
            }
        }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
            openAudio(position);

        }else if(playmode == MusicPlayerService.REPEAT_ALL){
            openAudio(position);
        }
        else {
            if(position >= 0){
                openAudio(position);
            }else{
                position = 0;
            }
        }
    }

    //设置播放模式
    private void setPlayMode(int playmode){
        this.palymode = playmode;
        CacheUtils.putPlaymode(this,"playmode",playmode);
    }

    private int getPlayMode(){
        return this.palymode;
    }

}
