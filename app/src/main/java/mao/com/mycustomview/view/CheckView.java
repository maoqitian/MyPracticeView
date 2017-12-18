package mao.com.mycustomview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import mao.com.mycustomview.R;

/**
 * Created by 毛麒添 on 2017/10/26 0026.
 * 选择勾选View
 */

public class CheckView extends View {

    private static final int ANIM_NULL = 0;         //动画状态-没有
    private static final int ANIM_CHECK = 1;        //动画状态-开启
    private static final int ANIM_UNCHECK = 2;      //动画状态-结束

    private Context mContext;           // 上下文
    private int mWidth, mHeight;        // 宽高
    private Handler mHandler;           // handler

    private Paint mPaint;        //画笔
    private Bitmap okBitmap;

    private int animCurrentPage = -1;       // 当前页码
    private int animMaxPage = 13;           // 总页数
    private int animDuration = 500;         // 动画时长
    private int animState = ANIM_NULL;      // 动画状态

    private boolean isCheck = false;        // 是否只选中状态

    public CheckView(Context context) {
        super(context,null);
    }

    public CheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //初始化
    private void init(Context context) {
        mContext=context;
        mPaint=new Paint();
        mPaint.setColor(0xffFF5317);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);//防止边缘的锯齿

        okBitmap= BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_check_black_48dp);
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(animCurrentPage<animMaxPage &&animCurrentPage>=0){
                    invalidate();
                    if(animState == ANIM_NULL){
                        return;
                    }
                    if(animState == ANIM_CHECK){
                       animCurrentPage++;
                    }
                    else if(animState==ANIM_UNCHECK){
                        animCurrentPage--;
                    }
                    this.sendEmptyMessageDelayed(0,animDuration/animMaxPage);
                    Log.w("毛麒添", "Count=" + animCurrentPage);
                }else {
                    if(isCheck){
                        animCurrentPage= animMaxPage - 1;
                    }else {
                        animCurrentPage =-1;
                    }
                    invalidate();
                    animState= ANIM_NULL;
                }
            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动坐标系到画布中央
        canvas.translate(mWidth/2,mHeight/2);
        //绘制圆形背景
        canvas.drawCircle(0,0,240,mPaint);
        //得出图像的边长
        int sideLength = okBitmap.getHeight();

        //得到图像选区,也就是选着绘制图片的哪个部分
        Rect src=new Rect(sideLength*animCurrentPage,0,sideLength*(animCurrentPage+1),sideLength);
        //实际绘制图片区域
        Rect des=new Rect(-200,-200,200,200);
        //绘制图片
        canvas.drawBitmap(okBitmap,src,des,null);
    }

    /**
     * 选择
     */
    public void check(){
        if(animState != ANIM_NULL || isCheck)return;
        animState=ANIM_CHECK;
        animCurrentPage=0;
        invalidate();
        mHandler.sendEmptyMessageDelayed(0,animDuration/animMaxPage);
        isCheck=true;
    }

    /**
     * 不选择
     */
    public void unCheck(){
        if(animState != ANIM_NULL || (!isCheck))return;
        animState=ANIM_UNCHECK;
        animCurrentPage=animMaxPage - 1;
        mHandler.sendEmptyMessageDelayed(0,animDuration/animMaxPage);
        isCheck=false;
    }

    /**
     * 设置动画时长
     */
    public void setAnimDuration(int animDuration) {
        if (animDuration <= 0)
            return;
        this.animDuration = animDuration;
    }

    /**
     * 设置背景圆形颜色
     * @param color 颜色值
     */
    public void setBackgroundColor(int color){
        mPaint.setColor(color);
    }
}
