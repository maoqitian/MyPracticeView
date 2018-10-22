/*
 * Copyright 2016 GcsSloop
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Last modified 16-9-28 上午2:23
 *
 */

package mao.com.mycustomview.view.dispatchtouchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import mao.com.mycustomview.action.Action;

/**
 * 组长
 */
public class ViewGroupA extends RelativeLayout {
    private static final String TAG = Action.TAG3;

    public ViewGroupA(Context context) {
        super(context);
    }

    public ViewGroupA(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupA(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //Log.i(TAG, Action.dispatchTouchEvent + "老板要造宇宙飞船？？");
            Log.i(TAG, Action.dispatchTouchEvent + "组员表现情况？");
            //Log.i(TAG, Action.dispatchTouchEvent + "项目进度?");

        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i(TAG, Action.onInterceptTouchEvent + "我看看组员绩效情况");
            //Log.i(TAG, Action.onInterceptTouchEvent + "我问问程序员");
            //Log.i(TAG, Action.onInterceptTouchEvent);
        }
         //return super.onInterceptTouchEvent(ev);
        return true;//拦截事件 onTouchEvent 中进行处理
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //Log.i(TAG, Action.onTouchEvent + "程序员说做不了");
            //Log.i(TAG, Action.onTouchEvent);
            Log.i(TAG, Action.onTouchEvent+"技术部组员最近表现都很好,项目按时完成，没有迟到早退");
        }
        return true;
    }
}
