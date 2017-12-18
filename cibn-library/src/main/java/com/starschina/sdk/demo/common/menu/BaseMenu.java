package com.starschina.sdk.demo.common.menu;

import android.content.Context;
import android.view.View;

/**
 * Created by shigaoayng on 2016/9/1 0001.
 */
public abstract class BaseMenu {
    public Context mContext;
    public View mRoot;

    public BaseMenu(Context context) {
        mContext = context;
    }

    public abstract void initView();

    public View getRoot(){
        return mRoot;
    }

    public void show(){
        mRoot.setVisibility(View.VISIBLE);
    }

    public void hide(){
        mRoot.setVisibility(View.GONE);
    }

    public boolean isShown(){
        return mRoot.isShown();
    }
}
