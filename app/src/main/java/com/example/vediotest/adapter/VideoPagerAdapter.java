package com.example.vediotest.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vediotest.R;
import com.example.vediotest.domain.MediaItem;
import com.example.vediotest.pager.VideoPager;
import com.example.vediotest.utils.Utils;

import java.util.ArrayList;

/**
 * Created by 陈金桁 on 2018/11/20.
 */

public class VideoPagerAdapter  extends BaseAdapter {
    private Utils utils = new Utils();
    private Context context;
    private ArrayList<MediaItem> mediaItems;
    public VideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems){
        this.mediaItems = mediaItems;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_video_view,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
        viewHolder.tv_time.setText(utils.stringForTime((int)mediaItem.getDuration()));
        return convertView;
    }
    static class ViewHolder{
        TextView tv_name;
        TextView tv_size;
        TextView tv_time;

    }
}
