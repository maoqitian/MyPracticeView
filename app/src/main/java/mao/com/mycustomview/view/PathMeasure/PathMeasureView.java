package mao.com.mycustomview.view.PathMeasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 毛麒添 on 2017/12/22 0022.
 * PathMeasure是一个用来测量Path的类
 */

public class PathMeasureView extends View {

    private int mWidth, mHeight;

    private Paint mPaint;

    public PathMeasureView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10); // 边框宽度  10
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs) {
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

        canvas.translate(mWidth/2,mHeight/2);
        //demo1(canvas);
        //demo2(canvas);
        //demo3(canvas);

    }

    //PathMeasure.nextContour() path路径跳转 曲线的顺序与 Path 中添加的顺序有关，
    // getLength 获取到到是当前一条曲线分长度，而不是整个 Path 的长度。
    private void demo3(Canvas canvas) {
        Path path=new Path();
        path.addCircle(0,0,100, Path.Direction.CW);
        path.addCircle(0,0,200, Path.Direction.CW);

        canvas.drawPath(path,mPaint);

        PathMeasure pathMeasure=new PathMeasure(path,false);
        float length1 = pathMeasure.getLength();//获取第一个圆的周长

        pathMeasure.nextContour();//跳转到下一个圆

        float length2=pathMeasure.getLength();//获取第二个圆的周长

        Log.e("毛麒添", "onDraw: pathMeasure1.getLength()"+length1);//600
        Log.e("毛麒添", "onDraw: pathMeasure2.getLength()"+length2);//800
    }

    //getSegment 用于获取Path的一个片段
    private void demo2(Canvas canvas) {
        Path path=new Path();
        //添加一个正方形
        path.addRect(-200,-200,200,200, Path.Direction.CW);

        PathMeasure pathMeasure=new PathMeasure(path,false);// 将 Path 与 PathMeasure 关联
        Path dst=new Path();//存储截取后内容的Path

        dst.lineTo(300,-300);//被截取的 Path 片段会添加到 dst 中，而不是替换 dst 中到内容

        // 截取一部分存入dst中，并使用 moveTo 保持截取得到的 Path 第一个点的位置不变
        pathMeasure.getSegment(200,600,dst,true);

        // 如果 startWithMoveTo 为 true, 则被截取出来到Path片段保持原状，如果 startWithMoveTo 为 false，
        // 则会将截取出来的 Path 片段的起始点移动到 dst 的最后一个点，以保证 dst 的连续性。
        //pathMeasure.getSegment(200, 600, dst, false);
        canvas.drawPath(dst,mPaint);
    }

    //PathMeasure初探
    private void demo1(Canvas canvas) {
        Path path=new Path();
        path.lineTo(0,200);
        path.lineTo(200,200);
        path.lineTo(200,0);
        /**
         * 1.将 Path 与两个的 PathMeasure 进行关联，并给 forceClosed 设置了不同的状态，
         * 之后绘制再绘制出来的 Path 没有任何变化，所以与 Path 与 PathMeasure进行关联并不会影响 Path 状态。
         2.我们可以看到，设置 forceClosed 为 true 的方法比设置为 false 的方法测量出来的长度要长一点，
         这是由于 Path 没有闭合的缘故，多出来的距离正是 Path 最后一个点与最开始一个点之间点距离。
         forceClosed 为 false 测量的是当前 Path 状态的长度， forceClosed 为 true，则不论Path是否闭合测量的都是 Path 的闭合长度。
         */
        PathMeasure pathMeasure1=new PathMeasure(path,false);
        PathMeasure pathMeasure2=new PathMeasure(path,true);

        Log.e("毛麒添", "onDraw: pathMeasure1.getLength()"+ pathMeasure1.getLength());//600
        Log.e("毛麒添", "onDraw: pathMeasure2.getLength()"+ pathMeasure2.getLength());//800

        canvas.drawPath(path,mPaint);
    }
}
