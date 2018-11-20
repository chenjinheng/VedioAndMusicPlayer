package com.example.vediotest.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

/**
 * Created by 陈金桁 on 2018/11/20.
 */

public class SystemVideoPlayer extends AppCompatActivity implements View.OnClickListener {
    private VideoView videoView;
    private Uri uri;

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

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-11-20 17:18:03 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
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
            // Handle clicks for btnExit
        } else if ( v == btnPre ) {
            // Handle clicks for btnPre
        } else if ( v == btnPause ) {
            Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show();
            // Handle clicks for btnPause
        } else if ( v == btnNext ) {
            // Handle clicks for btnNext
        } else if ( v == btnFullScreen ) {
            // Handle clicks for btnFullScreen
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemvideoplayer);
        videoView = (VideoView) findViewById(R.id.videoview);

        videoView.setOnPreparedListener(new MyOnPreparedListener());

        uri = getIntent().getData();
        if(uri != null){
            videoView.setVideoURI(uri);
        }
        findViews();



        videoView.setOnErrorListener(new MyOnErrorListener());

        videoView.setOnCompletionListener(new MyOnCompletion());

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
        }
    }
}
