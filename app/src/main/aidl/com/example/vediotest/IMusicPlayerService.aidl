// IMusicPlayerService.aidl
package com.example.vediotest;

// Declare any non-default types here with import statements

interface IMusicPlayerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     //根据位置打开音频文件
        void openAudio(int position);

        //播放音乐
       void start();

        //暂停音乐
        void pause();

        //停止音乐
        void stop();

        //得到当前播放进度
        int getCurrentPosition();

        void seekTo(int position);

        //得到当前音频的总时长
        int getDuration();

        String getAritist();


        //得到歌曲名字
        String getName();


        //得到歌曲路径
        String getAudiaData();
        //播放下一个
        void next();

        //播放上一个
        void pre();

        //设置播放模式
        void setPlayMode(int playmode);

        int getPlayMode();

        boolean isPlaying();
}
