package mao.com.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 毛麒添 on 2017/10/25 0025.
 * 测试 画布操作
 */

public class TestView extends View {

    private Paint mPaint=new Paint();

    // 宽高
    private int mWidth, mHeight;

    public TestView(Context context) {
        super(context,null);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public TestView(Context context, AttributeSet attrs) {//布局调用该构造方法才会调用
        super(context, attrs);

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
        //所有的画布操作都只影响后续的绘制，对之前已经绘制过的内容没有影响。
        //demo1(canvas);//画圆 移动操作 translate（x,y）
        //demo2(canvas);//缩放操作
        //demo3(canvas);//循环缩放
        //旋转
        //demo4(canvas);
        //demo5(canvas);//圆形线段旋转

        canvas.translate(mWidth/2,mHeight/2);
        Rect rect=new Rect(0,0,100,100);
        canvas.drawRect(rect,mPaint);
        //错切(skew) public void skew (float sx, float sy)
        //float sx:将画布在x方向上倾斜相应的角度，sx倾斜角度的tan值，
        //float sy:将画布在y轴方向上倾斜相应的角度，sy为倾斜角度的tan值.
        canvas.skew(1,0);//水平错切 <- 45度
        mPaint.setColor(Color.RED);
        canvas.drawRect(rect,mPaint);
    }

    private void demo5(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        //绘制两个圆
        canvas.drawCircle(0,0,200,mPaint);
        canvas.drawCircle(0,0,180,mPaint);
        for (int i = 0; i < 360; i+=5) {
            canvas.drawLine(0,180,0,200,mPaint);
            canvas.rotate(10);
        }
    }

    private void demo4(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        Rect rect=new Rect(0,-400,400,0);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rect,mPaint);
        canvas.rotate(180);//旋转180
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(rect,mPaint);
        canvas.rotate(180,200,0);//旋转180,水平平移200
        mPaint.setColor(Color.RED);
        canvas.drawRect(rect,mPaint);
    }

    private void demo3(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        Rect rect=new Rect(-400,-400,400,400);
        for (int i = 0; i < 20; i++) {
            mPaint.setColor(Color.YELLOW);
            canvas.scale(0.9f,0.9f);
            canvas.drawRect(rect,mPaint);
        }
    }

    private void demo2(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        Rect rect=new Rect(0,-400,400,0);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(rect,mPaint);

        //缩放
        canvas.scale(0.5f,0.5f);
        //缩放并水平平移（平移的是矩形的缩放中心，缩放的中心默认为坐标原点,而缩放中心轴就是坐标轴）
        canvas.scale(0.5F,0.5f,200,0);

        //当缩放比例为负数的时候会根据缩放中心轴进行翻转
        canvas.scale(-0.5F,-0.5f);
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(rect,mPaint);
    }

    private void demo1(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,50,mPaint);

        mPaint.setColor(Color.YELLOW);
        canvas.translate(200,200);
        canvas.drawCircle(0,0,50,mPaint);
    }

}
