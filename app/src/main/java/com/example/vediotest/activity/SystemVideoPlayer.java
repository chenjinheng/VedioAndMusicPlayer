package com.example.vediotest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vediotest.R;
import com.example.vediotest.domain.MediaItem;
import com.example.vediotest.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 陈金桁 on 2018/11/20.
 */

public class SystemVideoPlayer extends AppCompatActivity implements View.OnClickListener {
    public final int PROGRESS = 1;

    private VideoView videoView;
    private Uri uri;
    private MyReceiver myReceiver;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView ivSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrenttime;
    private SeekBar seekbarVideo;
    private TextView tvMaxtime;
    private LinearLayout llSelect;
    private Button btnExit;
    private Button btnPre;
    private Button btnPause;
    private Button btnNext;
    private Button btnFullScreen;

    private ArrayList<MediaItem> mediaItems;
    private int position;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-11-20 17:18:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name_video );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        ivSystemTime = (TextView)findViewById( R.id.iv_system_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (Button)findViewById( R.id.btn_switch_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrenttime = (TextView)findViewById( R.id.tv_currenttime );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvMaxtime = (TextView)findViewById( R.id.tv_maxtime );
        llSelect = (LinearLayout)findViewById( R.id.ll_select );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnPre = (Button)findViewById( R.id.btn_pre );
        btnPause = (Button)findViewById( R.id.btn_pause );
        btnNext = (Button)findViewById( R.id.btn_next );
        btnFullScreen = (Button)findViewById( R.id.btn_full_screen );

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnPre.setOnClickListener( this );
        btnPause.setOnClickListener( this );
        btnNext.setOnClickListener( this );
        btnFullScreen.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-11-20 17:18:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
        } else if ( v == btnExit ) {
            finish();
            // Handle clicks for btnExit
        } else if ( v == btnPre ) {
            // Handle clicks for btnPre
            playPreVideo();
        } else if ( v == btnPause ) {
            // Handle clicks for btnPause
            if(videoView.isPlaying()){
                videoView.pause();
                btnPause.setBackgroundResource(R.drawable.btn_video_start_select);
            }else{
                videoView.start();
                btnPause.setBackgroundResource(R.drawable.btn_pause_select);
            }
        } else if ( v == btnNext ) {
            // Handle clicks for btnNext
            playNextVideo();
        } else if ( v == btnFullScreen ) {
            // Handle clicks for btnFullScreen
        }
    }

    private void playPreVideo() {
        if(mediaItems != null && mediaItems.size() > 0){
            position--;
            if(position >= 0){
                MediaItem mediaItem = mediaItems.get(position);
                videoView.setVideoPath(mediaItem.getData());
                setButtonState();
            }
        }
    }

    private void playNextVideo() {
        if(mediaItems != null && mediaItems.size() > 0){
            position++;
            if(position < mediaItems.size()){
                MediaItem mediaItem = mediaItems.get(position);
                videoView.setVideoPath(mediaItem.getData());
                setButtonState();
            }
            else if(uri != null){
                setButtonState();
            }
        }
    }

    private void setButtonState() {
        if(mediaItems != null && mediaItems.size() > 0){
            if(mediaItems.size() == 1){
                btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnPre.setEnabled(false);
                btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnNext.setEnabled(false);
            }
            else{
                if(position == 0){
                    btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnPre.setEnabled(false);
                    if(!btnNext.isEnabled()){
                        btnNext.setBackgroundResource(R.drawable.btn_pre_select);
                        btnNext.setEnabled(true);
                    }
                }
                else if(position == mediaItems.size() - 1){
                    btnNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnNext.setEnabled(false);
                    if(!btnPre.isEnabled()){
                        btnPre.setBackgroundResource(R.drawable.btn_pre_select);
                        btnPre.setEnabled(true);
                    }
                }else{
                    btnPre.setBackgroundResource(R.drawable.btn_pre_select);
                    btnPre.setEnabled(true);

                    btnNext.setBackgroundResource(R.drawable.btn_pre_select);
                    btnNext.setEnabled(true);
                }
            }
        }
        else if(uri != null){
            btnPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnPre.setEnabled(false);
            btnNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnNext.setEnabled(false);
        }
    }

    class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);
            setBattert(level);
        }
    }

    private void setBattert(int level) {
        if(level <= 0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level > 0 && level <= 20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }
        else if(level > 20 && level <= 40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else{
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }
    }

    private void initBerrary(){
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver,intentFilter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemvideoplayer);
        videoView = (VideoView) findViewById(R.id.videoview);
        getData();
        setData();
        initBerrary();
        findViews();
        setListener();
        setButtonState();
    }

    private void setData() {
        if(mediaItems != null && mediaItems.size() > 0){
           MediaItem mediaItem = mediaItems.get(position);
            if (mediaItem.getName() != null) {
//                tvName.setText(mediaItem.getName());
            }
            videoView.setVideoPath(mediaItem.getData());
        }else if(uri != null){
//            tvName.setText(uri.toString());
            videoView.setVideoURI(uri);
        }
    }

    private void getData() {
        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position",0);

    }


    private void setListener(){
        videoView.setOnPreparedListener(new MyOnPreparedListener());

        videoView.setOnErrorListener(new MyOnErrorListener());

        videoView.setOnCompletionListener(new MyOnCompletion());

        seekbarVideo.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
    }

    @Override
    protected void onDestroy() {
        if(myReceiver != null){
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        super.onDestroy();
    }

    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

               @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser == true){
                videoView.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    class MyOnCompletion implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(SystemVideoPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
        }
    }


    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            return false;
        }
    }
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            videoView.start();
            int duration = videoView.getDuration();
            seekbarVideo.setMax(duration);
            handler.sendEmptyMessage(PROGRESS);
            tvMaxtime.setText(utils.stringForTime(videoView.getDuration()));
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    int currentPostion = videoView.getCurrentPosition();
                    seekbarVideo.setProgress(currentPostion);
                    removeMessages(PROGRESS);
                    tvCurrenttime.setText(utils.stringForTime(currentPostion));
                    handler.sendEmptyMessageDelayed(PROGRESS,1000);
                    ivSystemTime.setText(getSystemTime());
                    break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    private Utils utils = new Utils();
}
