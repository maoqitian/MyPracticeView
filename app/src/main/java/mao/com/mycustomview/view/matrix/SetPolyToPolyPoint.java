package mao.com.mycustomview.view.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gcssloop.view.utils.CanvasAidUtils;

import mao.com.mycustomview.R;

/**
 * Created by maoqitian on 2018/4/13 0013.
 * 方法参数 pointCount 实测
 * boolean setPolyToPoly (
 float[] src,    // 原始数组 src [x,y]，存储内容为一组点
 int srcIndex,   // 原始数组开始位置
 float[] dst,    // 目标数组 dst [x,y]，存储内容为一组点
 int dstIndex,   // 目标数组开始位置
 int pointCount) // 测控点的数量 取值范围是: 0到4
 */

public class SetPolyToPolyPoint extends View {

    private static final String TAG = "SetPolyToPoly";

    private int testPoint = 0;
    private int triggerRadius = 180;    // 触发半径为180px

    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mPolyMatrix;         // 测试setPolyToPoly用的Matrix

    private float[] src = new float[8];
    private float[] dst = new float[8];

    private Paint pointPaint;



    public SetPolyToPolyPoint(Context context) {
        super(context,null);
        initBitmapAndMatrix();
    }

    public SetPolyToPolyPoint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
        initBitmapAndMatrix();
    }

    public SetPolyToPolyPoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmapAndMatrix();
    }

    private void initBitmapAndMatrix() {
        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_black_48dp);
        mPolyMatrix=new Matrix();

        float[] temp = {0,0,         //左上
                mBitmap.getWidth(),0,//右上
                mBitmap.getWidth(),mBitmap.getHeight(),//右下
                0,mBitmap.getHeight()};//左下
        src=temp.clone();
        dst=temp.clone();

        pointPaint=new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(50);
        pointPaint.setColor(0xffd19165);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);

        mPolyMatrix.setPolyToPoly(src,0,src,0,4);
        
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float tempX=event.getX();
                float tempY=event.getY();
                //更具触控的位置改变dst
                for (int i = 0; i < testPoint * 2; i+=2) {
                    if(Math.abs(tempX - dst[i]) <= triggerRadius && Math.abs(tempY - dst[i+1]) <= triggerRadius){
                       dst[i] = tempX-100;
                       dst[i+1] = tempY-100;
                       break;
                    }
                }
                restPolyMatrix(testPoint);
                invalidate();
                break;
        }
        return true;
    }

    private void restPolyMatrix(int pointCount) {
        mPolyMatrix.reset();
        //设置 测控点个数 核心
        mPolyMatrix.setPolyToPoly(src,0,dst,0,pointCount);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(100,100);
        //绘制坐标系
        CanvasAidUtils.set2DAxisLength(900,0,1200,0);
        CanvasAidUtils.draw2DCoordinateSpace(canvas);

        //根据Matrix 绘制变换后的图片
        canvas.drawBitmap(mBitmap,mPolyMatrix,null);

        float[] dst =new float[8];
        mPolyMatrix.mapPoints(dst,src);
        //绘制触控点
        for (int i = 0; i < testPoint * 2; i+=2) {
            canvas.drawPoint(dst[i],dst[i+1],pointPaint);
        }
    }

    public void setTestPoint(int testPoint){
        this.testPoint=testPoint >4 || testPoint <0 ?4:testPoint;
        dst=src.clone();
        restPolyMatrix(this.testPoint);
        invalidate();
    }
}
