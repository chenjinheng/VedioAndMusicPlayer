package com.example.vediotest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.vediotest.R;

public class MainActivity1 extends AppCompatActivity {
    private FrameLayout frameLayout;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        frameLayout = (FrameLayout) findViewById(R.id.fl_main_layout);
        radioGroup = (RadioGroup) findViewById(R.id.rg_bottom_tag);
        radioGroup.check(R.id.rb_video);
    }
}
