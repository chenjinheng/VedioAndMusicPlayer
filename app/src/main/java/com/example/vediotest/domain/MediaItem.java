package com.example.vediotest.domain;

/**
 * Created by 陈金桁 on 2018/11/19.
 */

public class MediaItem {
    private String name;
    private long size;
    private long duration;
    private String data;
    private String artist;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }
}
