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
            //Log.i(TAG, Action.dispatchTouchEvent+"经理,我有个大胆的想法,今年造个宇宙飞船出来.");
            //Log.i(TAG, Action.dispatchTouchEvent+"经理,现在项目做到什么程度了?");
            Log.i(TAG, Action.dispatchTouchEvent+"经理,问问技术部这个月的表现情况");
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            Log.i(TAG, Action.onTouchEvent+"套你猴子，我想多了.");
            //Log.i(TAG, Action.onTouchEvent+"....");
            //Log.i(TAG, Action.onTouchEvent);
        }
        return super.onTouchEvent(event);
    }
}
