package mao.com.mycustomview.view.Stereo;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by maoqitian on 2018/4/28 0028.
 * 3D 旋转容器
 */

public class Stereo3DView extends ViewGroup{

    //可对外进行设置的参数
    private int mStartScreen = 1;//开始时的item位置
    private Scroller mScroller;
    private float resistance = 1.8f;//滑动阻力

    private Camera mCamera;
    private Matrix mMatrix;

    private int mWidth;//容器的宽度
    private int mHeight;//容器的高度
    private float mDownX, mDownY, mTempY;
    private boolean isSliding = false;//是否滑动
    private int mCurScreen = 1;//记录当前item
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;//速度跟踪

    private static final int standerSpeed = 2000;//正常速度
    private static final int flingSpeed = 800;//猛滑得速度
    private int alreadyAdd = 0;//对滑动多页时的已经新增页面次数的记录
    private int addCount;//记录手离开屏幕后，需要新增的页面次数
    private State mState = State.Normal;//状态

    public enum State {
        Normal, //普通
        ToPre,  //上一页
        ToNext  //下一页
    }

    public Stereo3DView(Context context) {
        super(context,null);
    }

    public Stereo3DView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public Stereo3DView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //初始化
    private void init(Context context) {
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
        mMatrix=new Matrix();
        mCamera=new Camera();
        if(mScroller == null){
            mScroller=new Scroller(context);
        }
    }


    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        mWidth=getMeasuredWidth();
        mHeight=getMeasuredHeight();
        //滑动到设置的StartScreen位置
        scrollTo(0,mStartScreen*mHeight);
    }

    //摆放
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //设置子View 的位置
        int childTop=0;
        for (int i = 0; i <getChildCount(); i++) {
            View viewChild = getChildAt(i);
            if(viewChild.getVisibility()!= GONE){ //子view 显示 摆放
               viewChild.layout(0,childTop,viewChild.getMeasuredWidth(),childTop+viewChild.getMeasuredHeight());
               childTop=childTop+viewChild.getMeasuredHeight();
            }
        }
    }

    //画
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //TODO 分发之后画出效果 还未实现 3D的效果
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN :
                isSliding=false;
                mDownX =x;
                mTempY=mDownY=y;
                if(!mScroller.isFinished()){
                    //当上一次滑动没有结束时，再次点击，强制滑动在点击位置结束
                    mScroller.setFinalY(mScroller.getCurrY());
                    mScroller.abortAnimation();
                    scrollTo(0,getScrollY());
                    isSliding = true;
                }
                break;
            case MotionEvent.ACTION_MOVE :
                if(!isSliding){
                   isSliding = isCanSliding(ev);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);


    }

    private boolean isCanSliding(MotionEvent ev) {
        float moveX;
        float moveY;
        moveX=ev.getX();
        mTempY=moveY=ev.getY();
        if(Math.abs(moveY - mDownX)>mTouchSlop && (Math.abs(moveY - mDownY)>(Math.abs(moveX - mDownY)))){
           return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSliding;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mVelocityTracker==null){//如果速度跟踪为空则将其回收
            mVelocityTracker=VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        float y=event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN ://手指按下屏幕则消费该事件
                return true;
            case MotionEvent.ACTION_MOVE:
                if(isSliding){
                  int realDelta= (int) (mDownY-y);
                  if(mScroller.isFinished()){
                      //循环滑动
                      recycleMove(realDelta);
                  }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(isSliding){
                   isSliding=false;
                   mVelocityTracker.computeCurrentVelocity(1000);
                    float yVelocity = mVelocityTracker.getYVelocity();
                    //滑动的速度大于规定的速度，或者向上滑动时，上一页页面展现出的高度超过1/2。则设定状态为State.ToPre
                    if(yVelocity>standerSpeed || ((getScrollY()+mHeight/2)/mHeight < mStartScreen)){
                       mState=State.ToPre;
                    }else if(yVelocity<-standerSpeed || ((getScrollY()+mHeight/2)/mHeight > mStartScreen)){
                        //滑动的速度大于规定的速度，或者向下滑动时，下一页页面展现出的高度超过1/2。则设定状态为State.ToNext
                        mState=State.ToNext;
                    } else {
                       mState=State.Normal;
                    }
                    //更具状态变化进行相应改变
                    changeByState(yVelocity);
                }
                if(mVelocityTracker!=null){
                    mVelocityTracker.recycle();
                    mVelocityTracker=null;
                }
                break;
        }

        return super.onTouchEvent(event);

    }

    private void changeByState(float yVelocity) {
        alreadyAdd=0;//重置滑动多页时的计数
        switch (mState){
            case Normal:
                toNormalAction();
                break;
            case ToPre:
                toPreAction(yVelocity);
                break;
            case ToNext:
                toNextAction(yVelocity);
                break;
        }
    }

    /**
     * mState=State.ToNext 操作
     * @param yVelocity 竖直方向的速度
     */
    private void toNextAction(float yVelocity) {
        int startY;
        int delta;
        int duration;
        mState=State.ToNext;
        addNext();
        int flingSpeedCount= (Math.abs(yVelocity) - standerSpeed)>0 ? (int) (Math.abs(yVelocity) - standerSpeed) : 0;
        addCount = flingSpeedCount / flingSpeed +1;
        //mScroller开始的坐标
        startY= getScrollY()- mHeight;
        setScrollY(startY);
        //mScroller移动的距离
        delta= mHeight* mStartScreen - startY +(addCount - 1) *mHeight;
        duration=(Math.abs(delta))*3;
        mScroller.startScroll(0,startY,0,delta,duration);
        addCount-- ;
    }

    /**
     * mState=State.ToPre 操作
     * @param yVelocity 竖直方向的速度
     */
    private void toPreAction(float yVelocity) {
        int startY;
        int delta;
        int duration;
        mState=State.ToPre;
        addPre();//增加新的页面
        //计算松手后滑动的item个数
        int flingSpeedCount= (yVelocity - standerSpeed)>0 ? (int) (yVelocity - standerSpeed) : 0;
        addCount = flingSpeedCount / flingSpeed +1;
        //mScroller开始的坐标
        startY=getScrollY()+mHeight;
        setScrollY(startY);
        //mScroller移动的距离
        delta= -( startY - mStartScreen*mHeight ) - (addCount - 1)*mHeight;
        duration=(Math.abs(delta))*3;
        mScroller.startScroll(0,startY,0,delta,duration);
        addCount-- ;
    }

    private void toNormalAction() {
        int startY;
        int delta;
        int duration;
        mState=State.Normal;
        addCount=0;
        startY=getScrollY();
        delta=mHeight*mStartScreen-getScrollY();
        duration=(Math.abs(delta))*4;
        mScroller.startScroll(0,startY,0,delta,duration);
    }

    //根据状态量继续滑动
    private void recycleMove(int realDelta) {
         realDelta=realDelta%mHeight;
         realDelta= (int) (realDelta/resistance);
         if(Math.abs(realDelta)>mHeight/4){
              return;
         }
         scrollBy(0,realDelta);
         if(getScrollY()<5 && mStartScreen!=0){
            addPre();
            scrollBy(0,mHeight);
         }else if(getScrollY()>(getChildCount()-1)*mHeight-5){
            addNext();
            scrollBy(0,-mHeight);
         }
    }
    /**
     * 把第一个item移动到最后一个item位置
     */
    private void addNext() {
        mCurScreen=(mCurScreen+1)%getChildCount();
        int childCount = getChildCount();
        View view = getChildAt(0);
        removeViewAt(0);
        addView(view,childCount-1);
        if(mIStereoListener!=null){
            mIStereoListener.toNext(mCurScreen);
        }
    }

    /**
     * 把最后一个item移动到第一个item位置
     */
    private void addPre() {
        mCurScreen=((mCurScreen-1)+getChildCount())%getChildCount();
        int childCount = getChildCount();
        View view = getChildAt(childCount-1);
        removeViewAt(childCount-1);
        addView(view,0);
        if(mIStereoListener!=null){
            mIStereoListener.toPre(mCurScreen);
        }
    }

    //滑动回调接口
    public interface IStereoListener{
        //滑动上一页的回调
        void toPre(int curScreen );// curScreen 当前item
        //下滑一页的回调
        void toNext(int curScreen );
    }

    public IStereoListener mIStereoListener;

    public void setIStereoListener(IStereoListener mIStereoListener){
        this.mIStereoListener=mIStereoListener;
    }
}
