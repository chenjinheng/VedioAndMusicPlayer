package com.example.vediotest.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vediotest.R;

/**
 * Created by 陈金桁 on 2018/11/20.
 */

public class SystemVideoPlayer extends AppCompatActivity{
    private VideoView videoView;
    private Uri uri;
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
