package com.example.vediotest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vediotest.base.BasePager;

/**
 *
 * Created by 陈金桁 on 2018/11/18.
 */

public class ReplaceFragment extends Fragment {
    private BasePager basePager;
    public ReplaceFragment(BasePager basePager){
        this.basePager = basePager;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return basePager.rootView;
    }
}
