package com.starschina.sdk.demo.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.starschina.sdk.player.ThinkoEnvironment;
import com.starschina.types.DChannel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class LiveActivity extends Activity {

    private ListAdapter mAdapter;

    @BindView(R2.id.list) ListView mChannelList;
    @BindView(R2.id.progress) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live);
        ButterKnife.bind(this);

        mAdapter = new ListAdapter(this);
        mChannelList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("demo", "LiveActivity.onResume");
        if (mAdapter.getCount() == 0) {
            getChannenllist();
        }
    }

    public void getChannenllist(){
        ThinkoEnvironment.getChannelList(mChannelsOnChangeListener);
    }

    @OnItemClick(R2.id.list)
    public void selectToPlay(int position) {
        Intent i = new Intent(LiveActivity.this ,VideoActivity.class);
        i.putExtra("channel", mAdapter.getItem(position));
        startActivity(i);
    }

    ThinkoEnvironment.OnGetChannelsListener mChannelsOnChangeListener = new ThinkoEnvironment.OnGetChannelsListener() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void getChannelList(ArrayList<DChannel> channellist) {
            // TODO Auto-generated method stub
            Log.e("demo", "channellist.size:"+channellist.size());
            if (channellist.get(0).currentEpg != null){
                Log.e("demo", "DChannel epg:"+channellist.get(0).currentEpg.name);
            }
            if (!LiveActivity.this.isDestroyed() && !LiveActivity.this.isFinishing()) {
                progressBar.setVisibility(View.GONE);

                mAdapter.setData(channellist);
                mAdapter.notifyDataSetChanged();
            }

        }
    };
}
