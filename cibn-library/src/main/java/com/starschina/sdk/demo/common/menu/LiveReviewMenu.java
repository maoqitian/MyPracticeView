package com.starschina.sdk.demo.common.menu;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.starschina.sdk.demo.common.Epg.EpgDetailFragment;
import com.starschina.sdk.demo.common.R;
import com.starschina.sdk.demo.common.utils.DateUtils;
import com.starschina.types.DChannel;

/**
 * Created by shigaoyang on 2016/9/2 0002.
 */
public class LiveReviewMenu extends BaseMenu implements View.OnClickListener{
    private ReviewFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TextView mTitle;
    private ImageView mLast;
    private ImageView mNext;
    private String tabTitles[] = new String[4];
    private DChannel mChannel;
    private long mMils;
    private long mId;
    private boolean mIsReview;
    private FragmentActivity mActivity;
    private int curPositon = 3;

    public LiveReviewMenu(Context context) {
        super(context);
    }

    public LiveReviewMenu(FragmentActivity activity, DChannel ch, long currentMil, long id, boolean isReview) {
        super(activity);
        this.mActivity = activity;
        this.mChannel = ch;
        this.mMils = currentMil;
        this.mId = id;
        this.mIsReview = isReview;
        initView();
    }

    @Override
    public void initView() {
        mRoot = View.inflate(mContext, R.layout.menu_live_review, null);
        mTitle = (TextView) mRoot.findViewById(R.id.title);
        mLast = (ImageView) mRoot.findViewById(R.id.last);
        mNext = (ImageView) mRoot.findViewById(R.id.next);
        mLast.setOnClickListener(this);
        mNext.setOnClickListener(this);
        initData();
        pagerAdapter = new ReviewFragmentPagerAdapter(mActivity.getSupportFragmentManager(),mContext,tabTitles);
        viewPager = (ViewPager) mRoot.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                curPositon = position;
                mTitle.setText(tabTitles[position]);
                if(position==0){
                    mLast.setVisibility(View.GONE);
                }else if(position==3){
                    mNext.setVisibility(View.GONE);
                }else {
                    mNext.setVisibility(View.VISIBLE);
                    mLast.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(curPositon);
    }

    public void setData(DChannel ch, long currentMil, long id, boolean isReview){
        this.mChannel = ch;
        this.mMils = currentMil;
        this.mIsReview = isReview;
        this.mId = id;
        pagerAdapter.notifyDataSetChanged();
        curPositon = 3;
        viewPager.setCurrentItem(curPositon);
    }

    private void initData() {
        tabTitles[0] = DateUtils.getFormerlyTimeString(3);
        tabTitles[1] = DateUtils.getFormerlyTimeString(2);
        tabTitles[2] = "昨天";
        tabTitles[3] = "今天";
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.last) {
            if (curPositon > 0) {
                viewPager.setCurrentItem(--curPositon);
            }

        } else if (i == R.id.next) {
            if (curPositon < 3) {
                viewPager.setCurrentItem(++curPositon);
            }

        }
    }


    private class ReviewFragmentPagerAdapter extends FragmentStatePagerAdapter {
        final int PAGE_COUNT = 4;
        private String tabTitles[];
        private int[] index = new int[]{3,2,1,0};
        private Context context;

        public ReviewFragmentPagerAdapter(FragmentManager fm, Context context, String[] tabTitles) {
            super(fm);
            this.context = context;
            this.tabTitles = tabTitles;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("demo", "-------getItem-------- ");
            return EpgDetailFragment.newInstance(mChannel, index[position], position, mMils, mId, mIsReview, true);
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
