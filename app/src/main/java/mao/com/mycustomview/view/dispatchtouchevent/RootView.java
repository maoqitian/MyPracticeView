

package mao.com.mycustomview.view.dispatchtouchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import mao.com.mycustomview.action.Action;

/**
 * 经理
 */
public class RootView extends RelativeLayout {

    private static final String TAG = Action.TAG2;

    public RootView(Context context) {
        super(context);
    }

    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
             //Log.i(TAG, Action.dispatchTouchEvent + "呼叫技术部,老板要做淘宝,下周上线.");
            //Log.i(TAG, Action.dispatchTouchEvent + "技术部,老板说要加入超级VIP功能.");
            Log.i(TAG, Action.dispatchTouchEvent + "技术部,你们的app快做完了么?");
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //Log.i(TAG, Action.onInterceptTouchEvent + "(老板可能疯了,但又不是我做.)");
            Log.i(TAG, Action.onInterceptTouchEvent+"老板问项目进度" );
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //Log.i(TAG, Action.onTouchEvent+"报告老板, 技术部说做不了");
            //Log.i(TAG, Action.onTouchEvent +"老板，正在测试");
            Log.i(TAG, Action.onTouchEvent +".....");
        }
        return super.onTouchEvent(event);
    }
}
