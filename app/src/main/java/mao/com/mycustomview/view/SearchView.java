package mao.com.mycustomview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 毛麒添 on 2018/2/23 0023.
 * 搜索动画效果 View
 * StartSearch 方法动画开始，表示进入搜索状态
 */

public class SearchView extends View {
    //画笔
    private Paint mPaint;
    //宽高
    private int width;
    private int height;

    // 当前的状态(非常重要)
    private State mCurrentState = State.NONE;

    // 放大镜与外部圆环
    private Path path_search;
    private Path path_circle;

    // 测量Path 并截取部分的工具
    private PathMeasure mMeasure;

    // 默认的动效周期 2s
    private int defaultDuration = 2000;

    // 控制各个过程的动画
    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;

    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue = 0;

    // 动效过程监听器
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    // 用于控制动画状态转换
    private Handler mAnimatorHandler;

    // 判断是否已经搜索结束
    private boolean isOver = false;

    private int count = 0;

    //搜索视图拥有的状态
    public static enum State{
        NONE,//初始状态
        STARTING,//开始搜索
        SEARCHING,//正在搜索
        ENDING//搜索结束
    }

    public SearchView(Context context) {
        super(context,null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        initAll();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAll();
    }

    private void initAll() {
        initPaint();

        initPath();

        initListener();

        initHandler();

        initAnimator();
        //进入开始动画
        //mCurrentState=State.STARTING;
        //mStartingAnimator.start();
    }

    private void initPaint() {
        mPaint=new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    private void initPath() {
       path_search=new Path();
       path_circle=new Path();
       mMeasure=new PathMeasure();
       //放大镜圆环
       RectF oval1=new RectF(-50,-50,50,50);
        path_search.addArc(oval1,45,359.9f);
       //外部圆环
        RectF oval2=new RectF(-100,-100,100,100);
        path_circle.addArc(oval2,45,-359.9f);

        float[] pos = new float[2];
        mMeasure.setPath(path_circle,false);//放大镜把手位置
        mMeasure.getPosTan(0,pos,null);

        path_search.lineTo(pos[0],pos[1]);//放大镜把手
        Log.e("毛麒添", "pos=" + pos[0] + ":" + pos[1]);
    }

    private void initListener() {
       mUpdateListener=new ValueAnimator.AnimatorUpdateListener() {
           @Override
           public void onAnimationUpdate(ValueAnimator animation) {
               mAnimatorValue= (float) animation.getAnimatedValue();
               invalidate();
           }
       };
       mAnimatorListener=new Animator.AnimatorListener() {
           @Override
           public void onAnimationStart(Animator animation) {

           }

           @Override
           public void onAnimationEnd(Animator animation) {
               //getHandle发消息通知动画状态更新
               mAnimatorHandler.sendEmptyMessage(0);
           }

           @Override
           public void onAnimationCancel(Animator animation) {

           }

           @Override
           public void onAnimationRepeat(Animator animation) {

           }
       };
    }


    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mAnimatorHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (mCurrentState){
                    case STARTING:
                        mListener.onStart();
                        //由开始动画转换搜索动画
                        isOver=false;
                        mCurrentState=State.SEARCHING;
                        mStartingAnimator.removeAllListeners();//结束开始动画
                        mSearchingAnimator.start();//搜索中的动画开始
                        break;
                    case SEARCHING:
                        mListener.onSearching();
                        if(!isOver){//如果搜索未结束
                            mSearchingAnimator.start();
                            Log.e("毛麒添", "RESTART");
                            count++;
                            if(count>2){// count大于2则进入结束状态
                                isOver=true;
                            }
                        }else {//搜索结束，结束动画
                            mCurrentState=State.ENDING;
                            mEndingAnimator.start();
                        }
                        break;
                    case ENDING:
                        mListener.onEnding();
                        // 从结束动画转变为无状态
                        mCurrentState=State.NONE;
                        break;
                }
            }
        };
    }

    //初始化动画
    private void initAnimator() {
        mStartingAnimator=ValueAnimator.ofFloat(0,1).setDuration(defaultDuration);
        mSearchingAnimator=ValueAnimator.ofFloat(0,1).setDuration(defaultDuration);
        mEndingAnimator=ValueAnimator.ofFloat(1,0).setDuration(defaultDuration);

        mStartingAnimator.addUpdateListener(mUpdateListener);
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addUpdateListener(mUpdateListener);

        mStartingAnimator.addListener(mAnimatorListener);
        mSearchingAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width=w;
        height=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearch(canvas);
    }

    //画搜索图标
    private void drawSearch(Canvas canvas) {

        mPaint.setColor(Color.WHITE);
        canvas.translate(width/2,height/2);
        canvas.drawColor(Color.parseColor("#0082D7"));
        switch (mCurrentState){
            case NONE:
                canvas.drawPath(path_search,mPaint);
                break;
            case STARTING:
                mMeasure.setPath(path_search,false);
                Path dst=new Path();
                mMeasure.getSegment(mMeasure.getLength()*mAnimatorValue,mMeasure.getLength(),dst,true);//获取一个片段开始搜索的片段
                canvas.drawPath(dst,mPaint);

                break;
            case SEARCHING:
                mMeasure.setPath(path_circle,false);
                Path dst2=new Path();
                float stop=mMeasure.getLength()*mAnimatorValue;
                float start= (float) (stop-((0.5-Math.abs(mAnimatorValue-0.5))*200f));
                mMeasure.getSegment(start,stop,dst2,true);//获取一个片段开始搜索的片段
                canvas.drawPath(dst2,mPaint);
                break;
            case ENDING:
                mMeasure.setPath(path_search,false);
                Path dst3=new Path();
                mMeasure.getSegment(mMeasure.getLength()*mAnimatorValue,mMeasure.getLength(),dst3,true);//获取一个片段开始搜索的片段
                canvas.drawPath(dst3,mPaint);
                mListener.onEnding();
                break;
        }
    }

    //回调
    private onSearchViewStatus mListener;
    public void setOnSearchViewStatus(onSearchViewStatus listener){//搜索状态监听
        this.mListener=listener;
    }
    public interface onSearchViewStatus{
       void onStart();
       void onSearching();
       void onEnding();
    }

   /* private OnClickListener mClickListener;
    private onViewClick mViewClick;
    private int startRawX;
    private int startRawY;

    //添加点击事件
    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mClickListener=l;
    }

    public void setOnViewClick(onViewClick  listener){//点击事件的监听方法
        this.mViewClick=listener;
    }

    public interface onViewClick{
        void onClick(float scrollX, float scrollY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                startRawX=rawX;
                startRawY=rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (x + getLeft() < getRight() && y + getTop() < getBottom()) {
                    mClickListener.onClick(this);
                    mViewClick.onClick(rawX - startRawX, rawY - startRawY);
                }
                break;
        }
        return true;
    }*/

    //开始搜索
    public void StartSearch(){
        //进入开始动画
        mCurrentState=State.STARTING;
        mStartingAnimator.start();
    }
}
