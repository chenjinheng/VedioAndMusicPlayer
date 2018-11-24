package com.example.vediotest.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.vediotest.IMusicPlayerService;
import com.example.vediotest.R;
import com.example.vediotest.service.MusicPlayerService;

public class AudioPlayActivity extends AppCompatActivity implements View.OnClickListener {
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

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-11-24 09:50:11 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
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
        } else if ( v == audioBtnPre ) {
            // Handle clicks for audioBtnPre
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
            // Handle clicks for audioBtnNext
        } else if ( v == audioBtnLyrcScreen ) {
            // Handle clicks for audioBtnLyrcScreen
        }
    }


    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMusicPlayerService = IMusicPlayerService.Stub.asInterface(service);
            if(iMusicPlayerService != null){
                try {
                    iMusicPlayerService.openAudio(position);
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
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.S);
        registerReceiver(myReceiver,intentFilter);
    }
   class MyReceiver extends BroadcastReceiver{
       @Override
       public void onReceive(Context context, Intent intent) {
            showViewData();
       }
   }

    private void showViewData() {
        try {
            audioTvArtist.setText(iMusicPlayerService.getAritist().toString());
            audioTvName.setText(iMusicPlayerService.getName().toString());
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
        position = getIntent().getIntExtra("position",0);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(con );
        if (myReceiver != null) {
            myReceiver = null;
            unregisterReceiver(myReceiver);
        }
    }
}
