package com.starschina.sdk.demo.common;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.starschina.types.DChannel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ListAdapter extends BaseAdapter{
    private Context mContext;

    public ListAdapter(Context context) {
        mContext = context;
    }

    private ArrayList<DChannel> mDatas;

    public void setData(ArrayList<DChannel> chs){
        mDatas = chs;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public DChannel getItem(int position) {
        return mDatas != null ? mDatas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.item_video_view_demo, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        DChannel channel = getItem(position);
        if(channel != null){
            if (!TextUtils.isEmpty(channel.icon)) {
                holder.icon.setImageURI(Uri.parse(channel.icon));
            }
            holder.chName.setText(channel.name);
            if (channel.type == 1) {
                holder.curEpg.setText("正在播放:"+(channel.currentEpg != null ? channel.currentEpg.name : ""));
                holder.nextEpg.setText("即将播放:"+(channel.nextEpg != null ? channel.nextEpg.name : ""));
            }
        }
        return convertView;
    }


    class ViewHolder{
        @BindView(R2.id.icon) SimpleDraweeView icon;
        @BindView(R2.id.tv_cnlname) TextView chName;
        @BindView(R2.id.cur_epg) TextView curEpg;
        @BindView(R2.id.next_epg) TextView nextEpg;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
