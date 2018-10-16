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
 *    Last modified 16-9-27 上午1:28
 *
 */

package mao.com.mycustomview.view.dispatchtouchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import mao.com.mycustomview.action.Action;

/**
 * 扫地阿姨
 */
public class View2 extends View {
    private static final String TAG = Action.TAG5;
    public View2(Context context) {
        super(context);
    }

    public View2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public View2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Log.i(TAG, Action.dispatchTouchEvent+"我只是个扫地阿姨，我不懂你说什么");

        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.i(TAG, Action.onTouchEvent+"经理你问错人了，去问老板吧");
        }
        return super.onTouchEvent(event);
    }
}
