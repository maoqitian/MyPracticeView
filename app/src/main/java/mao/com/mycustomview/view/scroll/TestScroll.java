package mao.com.mycustomview.view.scroll;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;

import mao.com.mycustomview.R;
import mao.com.mycustomview.view.TestView;

/**
 * Created by maoqitian on 2018/11/5 0005.
 * 测试View 的滑动方式
 */

public class TestScroll extends View {

    //手指触屏坐标
    private float x;
    private float y;

    private float lastX;
    private float lastY;

    private Context mContext;

    private Scroller mScroller;

    public TestScroll(Context context) {
        super(context,null);
        init(context);
    }

    public TestScroll(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        init(context);
    }

    public TestScroll(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext=context;
        mScroller=new Scroller(context);
    }


    public void smoothScrollTo(int desx,int desy){
        int scaleX = (int) getScaleX();
        int scaleY = (int) getScaleY();
        int deltaX = desx-scaleX;
        int deltaY = desy-scaleY;
        //3秒内弹性滑到desx desy 位置
        mScroller.startScroll(scaleX,scaleY,deltaX,deltaY,3000);
        //重新绘制界面 会调用computeScroll方法
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){//还没滑动到指定位置
            ((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         //获取触屏时候的坐标
        Log.e("毛麒添","getLeft:"+getLeft()+"getTop:"+getTop()+"getRight:"+getRight()+"getBottom:"+getBottom());
         x = event.getRawX();
         y = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //滑动方式 4 补间动画滑动
               // startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.testscroll));
                break;
            case MotionEvent.ACTION_MOVE:
                //手指移动偏移量
                int offsetX = (int) (x-lastX);
                int offsetY = (int) (y-lastY);
                //滑动方式1
                //layout(getLeft()+offsetX,getTop()+offsetY,getRight()+offsetX,getBottom()+offsetY);
                //滑动方式2
               /* offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);*/
                //滑动方式3
                ((View)getParent()).scrollBy(-offsetX,-offsetY);
                //滑动方式5
                //moveView(offsetX,offsetY);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("毛麒添","getLeft:"+getLeft()+"getTop:"+getTop()+"getRight:"+getRight()+"getBottom:"+getBottom());
                break;
             default:
                 break;
        }
        lastX=x;
        lastY=y;
        return super.onTouchEvent(event);
    }

    private void moveView(int offsetX, int offsetY) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        layoutParams.leftMargin = getLeft() + offsetX;
        layoutParams.topMargin = getTop() + offsetY;
        setLayoutParams(layoutParams);
    }

}
