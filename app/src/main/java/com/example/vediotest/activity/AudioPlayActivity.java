package com.example.vediotest.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.vediotest.IMusicPlayerService;
import com.example.vediotest.R;
import com.example.vediotest.service.MusicPlayerService;

public class AudioPlayActivity extends AppCompatActivity {
    private ImageView audio_iv_icon;
    private int position;
    private IMusicPlayerService iMusicPlayerService;
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

        findViews();
        getData();
        bindAndStartService();
    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.example.openaudio");
        bindService(intent,con, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void getData() {
        position = getIntent().getIntExtra("position",0);

    }

    private void findViews() {
        audio_iv_icon = (ImageView) findViewById(R.id.audio_iv_icon);
        audio_iv_icon.setBackgroundResource(R.drawable.animation_list);

        AnimationDrawable rocketAnimation = (AnimationDrawable) audio_iv_icon.getBackground();
        rocketAnimation.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(con );
    }
}
