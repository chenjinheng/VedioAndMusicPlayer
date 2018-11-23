package com.example.vediotest.activity;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.vediotest.R;

public class AudioPlayActivity extends AppCompatActivity {
    private ImageView audio_iv_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);

        findViews();
    }

    private void findViews() {
        audio_iv_icon = (ImageView) findViewById(R.id.audio_iv_icon);
        audio_iv_icon.setBackgroundResource(R.drawable.animation_list);

        AnimationDrawable rocketAnimation = (AnimationDrawable) audio_iv_icon.getBackground();
        rocketAnimation.start();
    }
}
