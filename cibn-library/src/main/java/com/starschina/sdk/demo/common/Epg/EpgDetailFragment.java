package com.starschina.sdk.demo.common.Epg;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.starschina.sdk.demo.common.EventConst;
import com.starschina.sdk.demo.common.R;
import com.starschina.sdk.demo.common.SimpleEvent;
import com.starschina.sdk.demo.common.utils.DateUtils;
import com.starschina.types.DChannel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class EpgDetailFragment extends Fragment implements IEpgView{

    public static final String NUM_PAGE = "NUM_PAGE";

    private EpgPresenter mEpgPresenter;

    private DChannel mChannel;
    private boolean mIsMove;
    private int mPos = -1, mOldPos = -1, mCurrPos = -1;
    private long mMils;
    private long mId;//可以改为从presenter返回
    private boolean mIsReview, mLastReview, mIsMenu;
    private int mPosition, mIndex, mPreviousPosition = -1;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private DetailAdapter mDetailAdapter;

    private ProgressBar mProgressBar;

    private Handler mHandler = new Handler();
    private Context mContext;

    public static EpgDetailFragment newInstance(DChannel ch, int requestIndex, int page, long currtMil, long id, boolean isReview, boolean isMenu) {
        Bundle args = new Bundle();
        args.putSerializable("channel", ch);
        args.putInt("index", requestIndex);
        args.putInt(NUM_PAGE, page);
        args.putLong("currentMil", currtMil);
        args.putLong("id", id);
        args.putBoolean("ir", isReview);
        args.putBoolean("menu", isMenu);
        EpgDetailFragment pageFragment = new EpgDetailFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("demo", "EpgDetailFragment.onCreateView");
        mContext = getContext();
        View rootview = inflater.inflate(R.layout.play_fg_epg_detail, container, false);

        mChannel = (DChannel) getArguments().getSerializable("channel");
        mMils = getArguments().getLong("currentMil", 0);
        mPosition = getArguments().getInt(NUM_PAGE);
        mId = getArguments().getLong("id");
        mLastReview = getArguments().getBoolean("ir");
        mIndex = getArguments().getInt("index");
        mIsMenu = getArguments().getBoolean("menu");

        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.epglist);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mProgressBar = (ProgressBar) rootview.findViewById(R.id.progress);

        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("demo", "EpgDetailFragment.onActivityCreated");
    }

    @Override
    public void onResume() {
        Log.d("demo", "EpgDetailFragment.onResume:pos"+mIndex);
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("demo", "EpgDetailFragment.setUserVisibleHint:pos:"+mIndex);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mEpgPresenter == null) {
                        mEpgPresenter = new EpgPresenter(mContext, EpgDetailFragment.this, mChannel);
                        mEpgPresenter.getEpgList(mIndex);
                    }
                }
            }, 1000);
        }
    }

    @Override
    public void showEpg(List<EpgEntity> epgs) {
        Log.d("demo", "EpgDetailFragment.showEpg");
        Log.d("demo", "EpgDetailFragment isvisible:");

        mProgressBar.setVisibility(View.GONE);

        mDetailAdapter = new DetailAdapter(mIsMenu ? R.layout.fg_ry_epg_item_menu : R.layout.fg_ry_epg_item, epgs);
        mRecyclerView.setAdapter(mDetailAdapter);

        mDetailAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position, int selected_pos) {
                if (selected_pos >= 0) {
                     mDetailAdapter.getItemData(selected_pos).setSelected(false);
                     mDetailAdapter.notifyItemChanged(selected_pos);
                }

                EpgEntity epg = mDetailAdapter.getItemData(position);
                Log.d("demo", "onItemClick.epg:"+epg.getEpgName());
                Log.d("demo", "onItemClick.pos:"+position);
                epg.setSelected(true);
                mDetailAdapter.notifyItemChanged(position);

                EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_LIVE_SEEK_BY_EPG, epg));
                if (position < mDetailAdapter.getItemCount() - 1) {
                    EpgEntity next = mDetailAdapter.getItemData(position + 1);
                    EventBus.getDefault().post(new SimpleEvent(EventConst.EVENT_UPDATE_NEXT_EPG, next));
                }
            }
        });
    }

    @Override
    public void showErrorHint(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        mProgressBar.setVisibility(View.GONE);
    }

    class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

        private int mLayoutResId;
        private List<EpgEntity> mData;

        private MyViewHolder mViewHolder;
        private OnRecyclerViewItemClickListener mOnItemClickListener;

        private int selectPos = -1;

        public DetailAdapter(int layoutResId, List<EpgEntity> data) {
            mLayoutResId = layoutResId;
            mData = data != null ? data : new ArrayList<EpgEntity>();
        }

        public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(
                    getContext()).inflate(mLayoutResId, parent,
                    false);
            //view.setOnClickListener(this);
            mViewHolder = new MyViewHolder(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            EpgEntity epg = mData.get(position);
            holder.mEpgName.setText(epg.getEpgName());

            if (holder.mEpgTime != null) {
                holder.mEpgTime.setText(DateUtils.getHourAndMin((long) epg.getStartTime()*1000)+
                        "-"+DateUtils.getHourAndMin((long) epg.getEndTime()*1000));
            }


            holder.mPlay.setVisibility(View.VISIBLE);
            if (epg.getStatus() == 1) {
                holder.mPlay.setText("直播中");
            }else if (epg.getStatus() == 0) {
                holder.mPlay.setText("回看");
            }else {
                holder.mPlay.setText("即将播放");
            }

            if (mData.get(position).isSelected()) {
                selectPos = position;
            }

            holder.itemView.setTag(mData.get(position));
            holder.itemView.setSelected(mData.get(position).isSelected());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(position, selectPos);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public EpgEntity getItemData(int pos) {
            return mData.get(pos);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mEpgName;
            TextView mPlay;
            TextView mEpgTime;

            public MyViewHolder(View itemView) {
                super(itemView);
                mEpgName = (TextView) itemView.findViewById(R.id.epg_name);
                mPlay = (TextView) itemView.findViewById(R.id.epg_play);
                mEpgTime = (TextView) itemView.findViewById(R.id.epg_time);
            }
        }
    }

    interface OnRecyclerViewItemClickListener {
        void onItemClick(int pos, int selected_pos);
    }
}
