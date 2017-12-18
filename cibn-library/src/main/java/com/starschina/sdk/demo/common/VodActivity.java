package com.starschina.sdk.demo.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.starschina.types.DChannel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class VodActivity extends Activity {

    private ListAdapter mAdapter;

    @BindView(R2.id.list)
    ListView mChannelList;
    @BindView(R2.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);

        mAdapter = new ListAdapter(this);
        mChannelList.setAdapter(mAdapter);

        progressBar.setVisibility(View.GONE);

        /**
         * videoId: 269971,
         videoName: "哆啦A梦",
         videoType: 0,
         description: "",
         videoStatus: 1,
         rows: - [
         - {
         title: "哆啦A梦",
         duration: 0,
         isDownload: 1,
         pUrlId: 249696362,
         mediaId: 0,
         isp2p: 0,
         vip: 0,
         sort: 1
         }
         ]
         点播传递的id是 pUrlId
         */
        ArrayList<DChannel> channellist = new ArrayList<DChannel>();
        DChannel ch_1 = new DChannel();
        ch_1.id = 1149427;
        ch_1.name = "测试";
        ch_1.type = 0;
        //ch_1.url = "http://dnionmedia.starschinalive.com/f3f2096a9af7302aad804bf5835d3178/1485331353/video/2017/1/23/20171231485135413853_1_1399.mp4";
        //ch_1.url = "http://bsymedia1.starschinalive.com/video/2016/10/23/201610231477194425528_28_4179.mp4";//"rtmp://182.92.189.92/live/test";
        //ch_1.url = "http://172.17.10.104/test.m3u8";
        //ch_1.url = "http://dnion.haixin.starschinalive.com/4df68b54bf54546c069f83ce890b3956/1493709934/0009/index_1200k.m3u8";
        channellist.add(ch_1);

        mAdapter.setData(channellist);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnItemClick(R2.id.list)
    public void selectToPlay(int position) {
        Intent i = new Intent(VodActivity.this, VideoActivity.class);
        i.putExtra("channel", mAdapter.getItem(position));
        startActivity(i);
    }
}
