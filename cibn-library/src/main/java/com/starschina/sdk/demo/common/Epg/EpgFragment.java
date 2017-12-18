package com.starschina.sdk.demo.common.Epg;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starschina.sdk.demo.common.R;
import com.starschina.sdk.demo.common.utils.DateUtils;
import com.starschina.types.DChannel;

/**
 * Created by Administrator on 2016/10/14.
 */
public class EpgFragment extends Fragment {

    private EpgPresenter mPresenter;

    private String tabTitles[] = new String[4];

    private DChannel mChannel;
    private long mMils;
    private long mId;
    private boolean mIsReview;


    public static EpgFragment newInstance(DChannel channel, boolean isReview) {
        Log.d("demo", "EpgFragment.newInstance");
        Bundle args = new Bundle();
        args.putSerializable("channel", channel);
        //args.putLong("currentMil", currentMil);
        //args.putLong("id", epgId);
        args.putBoolean("ir", isReview);

        EpgFragment fragment = new EpgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("demo", "EpgFragment.onCreateView");
        mChannel = (DChannel) getArguments().getSerializable("channel");
        //mMils = getArguments().getLong("currentMil", 0);
        //mId = getArguments().getLong("id");
        mIsReview = getArguments().getBoolean("ir");

        initData();

        View rootview = inflater.inflate(R.layout.play_fg_epg, container, false);

        SimpleFragmentPagerAdapter pagerAdapter = new SimpleFragmentPagerAdapter(getChildFragmentManager(), getActivity(), tabTitles);
        ViewPager viewPager = (ViewPager) rootview.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(3);
        TabLayout tabLayout = (TabLayout) rootview.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initData() {
        tabTitles[0] = DateUtils.getFormerlyTimeString(3);
        tabTitles[1] = DateUtils.getFormerlyTimeString(2);;
        tabTitles[2] = "昨天";
        tabTitles[3] = "今天";
    }

    private class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 4;
        private String tabTitles[];
        private int[] index = new int[]{3, 2, 1, 0};
        private Context context;

        public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, String[] tabTitles) {
            super(fm);
            this.context = context;
            this.tabTitles = tabTitles;
        }

        @Override
        public Fragment getItem(int position) {
            return EpgDetailFragment.newInstance(mChannel, index[position], position, mMils, mId, mIsReview, false);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }
}
