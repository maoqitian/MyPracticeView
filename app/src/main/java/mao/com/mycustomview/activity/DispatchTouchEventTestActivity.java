package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import mao.com.mycustomview.R;
import mao.com.mycustomview.action.Action;

/**
 * Created by maoqitian on 2018/5/10 0010.
 * 事件分发机制测试 老板
 */

public class DispatchTouchEventTestActivity extends AppCompatActivity {
    private static final String TAG = Action.TAG1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_touch_event_test);
    }



    //Actiivty 只有 dispatchTouchEvent 和 onTouchEvent 方法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            //Log.i(TAG, Action.dispatchTouchEvent+"经理,我准备发展一下电商业务,下周之前做一个淘宝出来.");
            //Log.i(TAG, Action.dispatchTouchEvent+"把按钮做的好看一点,要有光泽,给人一种点击的欲望.");
            Log.i(TAG, Action.dispatchTouchEvent+"现在项目做到什么程度了?");
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            //Log.i(TAG, Action.onTouchEvent+"这么简单都做不了,你们都是干啥的(愤怒).");
            Log.i(TAG, Action.onTouchEvent+"....");
            Log.i(TAG, Action.onTouchEvent);
        }
        return super.onTouchEvent(event);
    }
}
