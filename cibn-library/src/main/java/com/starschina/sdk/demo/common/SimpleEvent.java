package com.starschina.sdk.demo.common;

/**
 * Created by Administrator on 2016/10/18.
 */
public class SimpleEvent {
    public int mType;
    public Object mObj;

    public SimpleEvent(int type) {
        mType = type;
    }

    public SimpleEvent(int type, Object obj) {
        mType = type;
        mObj = obj;
    }
}
