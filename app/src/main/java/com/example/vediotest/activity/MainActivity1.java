package com.example.vediotest.activity;

import android.media.AudioManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.vediotest.R;
import com.example.vediotest.base.BasePager;
import com.example.vediotest.fragment.ReplaceFragment;
import com.example.vediotest.pager.MusicPager;
import com.example.vediotest.pager.NetMusicPager;
import com.example.vediotest.pager.NetVideoPager;
import com.example.vediotest.pager.VideoPager;

import java.util.ArrayList;

public class MainActivity1 extends AppCompatActivity {
    private FrameLayout frameLayout;
    private RadioGroup radioGroup;
    private int position;
    private ArrayList<BasePager> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        frameLayout = (FrameLayout) findViewById(R.id.fl_main_layout);
        radioGroup = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        initPager();
        radioGroup.setOnCheckedChangeListener(new MyOnCheckChangeListener());
        radioGroup.check(R.id.rb_video);
    }

    private void initPager() {
        arrayList.add(new VideoPager(this,true));
        arrayList.add(new NetVideoPager(this));
        arrayList.add(new MusicPager(this,false));
        arrayList.add(new NetMusicPager(this));
    }
    class MyOnCheckChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                default:

                    position = 0;
                    break;
                case R.id.rb_netvideo:
                    position = 1;
                    break;
                case R.id.rb_music:
                    position = 2;
                    break;
                case R.id.rb_netmusic:
                    position = 3;
                    break;
            }
            setFragment();
        }
    }

    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_main_layout,new ReplaceFragment(getBasePager()));
        transaction.commit();
    }

    private BasePager getBasePager() {
        BasePager basePager = arrayList.get(position);
        if(basePager != null && !basePager.isInitData){
            basePager.isInitData = true;
            basePager.initData();
        }
        return basePager;
    }
}
