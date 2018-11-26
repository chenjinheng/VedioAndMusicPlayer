package com.example.vediotest.utils;

import com.example.vediotest.domain.Lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by 陈金桁 on 2018/11/25.
 */

public class LyricUtils1 {
    private ArrayList<Lyric> lyrics;

    public void readLyricFile(File file){
        if(file == null || !file.exists()){
            lyrics = null;
        }else{


            try {
                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GBK"));
                String line = "";
                try {
                    while((line = reader.readLine()) != null){
                        line = parsedLyric(line);

                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private String parsedLyric(String line) {
        int pos1 = line.indexOf("[");
        int pos2 = line.indexOf("]");
        if(pos1 == 0 && pos2 != -1){
            long [] times = new long[getCountTag(line)];
            String strTime = line.substring(pos1 + 1,pos2);
            times[0] = strTimeToLongTime(strTime);

            String content = line;
            int i = 1;
            while(pos1 == 0 && pos2 != -1){
                content = content.substring(pos2 + 1);
                pos1 = content.indexOf("[");
                pos2 = content.indexOf("]");
                if (pos2 != -1) {
                    strTime = content.substring(pos1 + 1,pos2);
                    times[i] = strTimeToLongTime(strTime);

                    if(times[i] == -1){
                        return "";
                    }
                    i++;
                }
            }
            Lyric lyric = new Lyric();
            for(int j = 0;j < times.length;j++){
                if(times[j] != 0){
                    lyric.setContent(content);
                    lyric.setTimePoint(times[j]);
                    lyrics.add(lyric);
                    lyric = new Lyric();
                }
            }
            return content;
        }
        return "";
    }

    private long strTimeToLongTime(String strTime) {
        long result = -1;
        try {


            String [] s1 = strTime.split(":");
            String [] s2 = s1[1].split("\\.");

            long min = Long.parseLong(s1[0]);

            long second = Long.parseLong(s2[0]);

            long mil = Long.parseLong(s2[1]);

            result = min *60 * 1000 + second * 1000 + mil * 10;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            result = -1;
        }

        return result;
    }

    private int getCountTag(String line) {
        int result = -1;
        String [] left = line.split("\\[");
        String [] right = line.split("\\]");
        if(left.length == 0 && right.length == 0){
            result = -1;
        }
        else if(left.length > right.length){
            result = left.length;
        }else{
            result = right.length;
        }
        return result;
    }
}
