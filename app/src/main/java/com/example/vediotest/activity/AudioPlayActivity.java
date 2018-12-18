package com.example.vediotest.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vediotest.IMusicPlayerService;
import com.example.vediotest.R;
import com.example.vediotest.domain.MediaItem;
import com.example.vediotest.service.MusicPlayerService;
import com.example.vediotest.utils.LyricUtils;
import com.example.vediotest.utils.Utils;
import com.example.vediotest.view.ShowLyricView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * 视频播放界面
 */

public class AudioPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SHOW_LYRIC = 2;
    private ImageView audio_iv_icon;
    private int position;
    private IMusicPlayerService iMusicPlayerService;

    private MyReceiver myReceiver;

    private ImageView audioIvIcon;
    private TextView audioTvArtist;
    private TextView audioTvName;
    private TextView audioTvTime;
    private SeekBar audioSeekbarAudio;
    private LinearLayout llSelect;
    private Button audioBtnAudiomodle;
    private Button audioBtnPre;
    private Button audioBtnPause;
    private Button audioBtnNext;
    private Button audioBtnLyrcScreen;
    private boolean notification;

    private ShowLyricView slv_lyric;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-11-24 09:50:11 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        slv_lyric = (ShowLyricView) findViewById(R.id.slv_lyric);
        audio_iv_icon = (ImageView) findViewById(R.id.audio_iv_icon);
        audio_iv_icon.setBackgroundResource(R.drawable.animation_list);

        AnimationDrawable rocketAnimation = (AnimationDrawable) audio_iv_icon.getBackground();
        rocketAnimation.start();

        audioTvArtist = (TextView)findViewById( R.id.audio_tv_artist );
        audioTvName = (TextView)findViewById( R.id.audio_tv_name );
        audioTvTime = (TextView)findViewById( R.id.audio_tv_time );
        audioSeekbarAudio = (SeekBar)findViewById( R.id.audio_seekbar_audio );
        llSelect = (LinearLayout)findViewById( R.id.ll_select );
        audioBtnAudiomodle = (Button)findViewById( R.id.audio_btn_audiomodle );
        audioBtnPre = (Button)findViewById( R.id.audio_btn_pre );
        audioBtnPause = (Button)findViewById( R.id.audio_btn_pause );
        audioBtnNext = (Button)findViewById( R.id.audio_btn_next );
        audioBtnLyrcScreen = (Button)findViewById( R.id.audio_btn_lyrc_screen );

        audioBtnAudiomodle.setOnClickListener( this );
        audioBtnPre.setOnClickListener( this );
        audioBtnPause.setOnClickListener( this );
        audioBtnNext.setOnClickListener( this );
        audioBtnLyrcScreen.setOnClickListener( this );

        audioSeekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                try {
                    iMusicPlayerService.seekTo(progress);
                    audioSeekbarAudio.setProgress(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-11-24 09:50:11 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == audioBtnAudiomodle ) {
            // Handle clicks for audioBtnAudiomodle

            setPlaymode();
        } else if ( v == audioBtnPre ) {
            // Handle clicks for audioBtnPre
            if(iMusicPlayerService != null){
                try {
                    iMusicPlayerService.pre();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if ( v == audioBtnPause ) {
            // Handle clicks for audioBtnPause
            if(iMusicPlayerService != null){
                try {
                    if(iMusicPlayerService.isPlaying()){
                        iMusicPlayerService.pause();
                        audioBtnPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    }else{
                        iMusicPlayerService.start();
                        audioBtnPause.setBackgroundResource(R.drawable.btn_pause_audio_select);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if ( v == audioBtnNext ) {

            if(iMusicPlayerService != null){
                try {
                    iMusicPlayerService.next();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if ( v == audioBtnLyrcScreen ) {

        }
    }

    private void setPlaymode() {

        try {
               int playmode = iMusicPlayerService.getPlayMode();
            if(playmode == MusicPlayerService.REPEAT_NORMAL){
                playmode = MusicPlayerService.REPEAT_SINGLE;
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                playmode = MusicPlayerService.REPEAT_ALL;
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }
            else {
                playmode = MusicPlayerService.REPEAT_NORMAL;
            }

            iMusicPlayerService.setPlayMode(playmode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        showPlaymode();
    }

    private void showPlaymode() {
        try {
            int playmode = iMusicPlayerService.getPlayMode();
            if(playmode == MusicPlayerService.REPEAT_NORMAL){
               audioBtnAudiomodle.setBackgroundResource(R.drawable.btn_audiomodle_select);
                Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                audioBtnAudiomodle.setBackgroundResource(R.drawable.btn_audiomodle_signle_select);
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                audioBtnAudiomodle.setBackgroundResource(R.drawable.btn_audiomodle_all_select);
                Toast.makeText(this, "全部循环", Toast.LENGTH_SHORT).show();
            }
            else {
                audioBtnAudiomodle.setBackgroundResource(R.drawable.btn_audiomodle_select);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void checkPlaymode(){
        try {
            int playmode = iMusicPlayerService.getPlayMode();
            if(playmode == MusicPlayerService.REPEAT_NORMAL){
                audioBtnAudiomodle.setBackgroundResource(R.drawable.btn_audiomodle_select);
            }else if(playmode == MusicPlayerService.REPEAT_SINGLE){
                audioBtnAudiomodle.setBackgroundResource(R.drawable.btn_audiomodle_signle_select);
            }else if(playmode == MusicPlayerService.REPEAT_ALL){
                audioBtnAudiomodle.setBackgroundResource(R.drawable.btn_audiomodle_all_select);
            }
            else {
                audioBtnAudiomodle.setBackgroundResource(R.drawable.btn_audiomodle_select);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMusicPlayerService = IMusicPlayerService.Stub.asInterface(service);
            if(iMusicPlayerService != null){
                try {
                    if (!notification) {
                        iMusicPlayerService.openAudio(position);
                    }else{
                        showViewData();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if (iMusicPlayerService != null) {
                    iMusicPlayerService.stop();
                    iMusicPlayerService = null;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);
        initData();
        findViews();
        getData();
        bindAndStartService();
    }

    private void initData() {
//        myReceiver = new MyReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(MusicPlayerService.S);
//        registerReceiver(myReceiver,intentFilter);
        EventBus.getDefault().register(this);
    }
   class MyReceiver extends BroadcastReceiver{
       @Override
       public void onReceive(Context context, Intent intent) {
            showData(null);
       }
   }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = false,priority = 0)
    public void showData(MediaItem mediaItem) {
        showViewData();
        checkPlaymode();

        showLyric();
    }

    private void showLyric() {
        LyricUtils lyricUtils = new LyricUtils();
        try {
            String path = iMusicPlayerService.getAudiaData();
            path = path.substring(0,path.lastIndexOf("."));
            File file = new File(path + ".lrc");
            if(!file.exists()){
                file = new File(path + ".txt");
            }
            lyricUtils.readLyricFile(file);
            slv_lyric.setLyric(lyricUtils.getLyrics());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(lyricUtils.isExistsLyric()) {
            handler.sendEmptyMessage(SHOW_LYRIC);
        }
    }

    private Utils utils;
    private static final int PROGRESS = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    try {
                        utils = new Utils();
                        int currentPostion = iMusicPlayerService.getCurrentPosition();
                        audioSeekbarAudio.setProgress(currentPostion);
                        audioTvTime.setText(utils.stringForTime(currentPostion) + "/" + utils.stringForTime(iMusicPlayerService.getDuration()));
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS,1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case SHOW_LYRIC://显示歌词
                    try {
                        int currentPosition = iMusicPlayerService.getCurrentPosition();

                        handler.removeMessages(SHOW_LYRIC);
                        handler.sendEmptyMessage(SHOW_LYRIC);

                        slv_lyric.setShowNextLyric(currentPosition);

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    private void showViewData() {
        try {
            audioTvArtist.setText(iMusicPlayerService.getAritist().toString());
            audioTvName.setText(iMusicPlayerService.getName().toString());
            audioSeekbarAudio.setMax(iMusicPlayerService.getDuration());
            handler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.example.openaudio");
        bindService(intent,con, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void getData() {
//        position = getIntent().getIntExtra("position",0);

        notification = getIntent().getBooleanExtra("Notification",false);
        if (!notification) {
            position = getIntent().getIntExtra("position",0);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (con != null) {
            unbindService(con );
            con = null;
        }
        EventBus.getDefault().unregister(this);
//        if (myReceiver != null) {
//            unregisterReceiver(myReceiver);
//            myReceiver = null;
//        }
        handler.removeCallbacksAndMessages(null);
    }
}
