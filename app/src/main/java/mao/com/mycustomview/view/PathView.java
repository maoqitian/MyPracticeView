package mao.com.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 毛麒添 on 2017/11/1 0001.
 * path 路径的用法测试  path的基本用法
 */

public class PathView extends View {

    private Paint mPaint;
    private int mWidth, mHeight;
    public PathView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10); // 边框宽度  10
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
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
        //moveTo：移动下一次操作的起点位置
        // setLastPoint、
        // lineTo 和 close
        //demo1(canvas);

        //addXxx与arcTo
        //demo2(canvas);

        //addPath 就是将两个Path合并成为一个
        //demo3(canvas);

        //addArc 添加一个圆弧到path	直接添加一个圆弧到path中
        //dem4(canvas);

        //arcTo  添加一个圆弧到path	添加一个圆弧到path，如果圆弧的起点和上次最后一个坐标点不相同，就连接两个点
        //demo5(canvas);

        //isEmpty:判断path中是否包含内容

        // isRect:判断path是否是一个矩形，如果是一个矩形的话，会将矩形的信息存放进参数rect中
        //demo6(canvas);

        // isConvex: 凹凸
        // set :将新的path赋值到现有path
        //demo7(canvas);

        // offset:这个的作用也很简单，就是对path进行一段平移，它和Canvas中的translate作用很像，但Canvas作用于整个画布，而path的offset只作用于当前path。
        canvas.translate(mWidth/2,mHeight/2);
        canvas.scale(1,-1); //翻转y坐标轴

        Path path=new Path();
        path.addCircle(0,0,100, Path.Direction.CW);

        Path src=new Path();
        src.addRect(-200,-200,200,200, Path.Direction.CW);

        path.offset(0,100,src);//Y轴平移100

        canvas.drawPath(path,mPaint);

        mPaint.setColor(Color.RED);

        canvas.drawPath(src,mPaint);

    }

    private void demo7(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        canvas.scale(1,-1); //翻转y坐标轴

        Path path=new Path();
        path.addRect(-200,-200,200,200, Path.Direction.CW);

        Path src=new Path();
        src.addCircle(0,100,100, Path.Direction.CW);
        path.set(src);
        canvas.drawPath(path,mPaint);
    }

    private void demo6(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        canvas.scale(1,-1); //翻转y坐标轴

        Path path=new Path();

        path.lineTo(0,100);
        path.lineTo(100,100);
        path.lineTo(100,0);
        path.lineTo(0,0);

        RectF rect = new RectF();
        boolean b = path.isRect(rect);
        Log.w("Rect","isRect:"+b+"| left:"+rect.left+"| top:"+rect.top+"| right:"+rect.right+"| bottom:"+rect.bottom);

        canvas.drawPath(path,mPaint);
    }

    private void demo5(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        canvas.scale(1,-1); //翻转y坐标轴

        Path path=new Path();
        path.lineTo(50,50);
        RectF rect=new RectF(0,0,150,150);
        path.arcTo(rect,0,270);
        //path.arcTo(rect,0,270,false);//和上面一句作用等价

        canvas.drawPath(path,mPaint);
    }

    private void dem4(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        canvas.scale(1,-1); //翻转y坐标轴

        Path path=new Path();
        path.lineTo(50,50);
        RectF rect=new RectF(0,0,150,150);
        path.addArc(rect,0,270);
        //path.arcTo(rect,0,270,true);//和上面一句作用等价

        canvas.drawPath(path,mPaint);
    }

    private void demo3(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        canvas.scale(1,-1); //翻转y坐标轴
        Path path=new Path();
        Path src=new Path();

        path.addRect(-200,-200,200,200, Path.Direction.CW);
        src.addCircle(0,0,100,Path.Direction.CW);

        path.addPath(src,0,200);

        canvas.drawPath(path,mPaint);
    }

    private void demo2(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);
        Path path=new Path();
        /**
         * CW 顺时针
         * CCW 逆时针
         */
        path.addRect(-200,-200,200,200, Path.Direction.CCW);
        path.setLastPoint(-100,100);
        canvas.drawPath(path,mPaint);
    }

    private void demo1(Canvas canvas) {
        canvas.translate(mWidth/2,mHeight/2);

        Path path=new Path();
        path.lineTo(200,200);
        /*
        //在执行完第一次LineTo的时候，本来的默认点位置是A(200,200),但是moveTo将其改变成为了C(200,100),
        所以在第二次调用lineTo的时候就是连接C(200,100) 到 B(200,0) 之间的直线
         */
        //path.moveTo(200,100);
        /*
        setLastPoint是重置上一次操作的最后一个点，在执行完第一次的lineTo的时候，最后一个点是A(200,200),而setLastPoint更改最后一个点为C(200,100),
        所以在实际执行的时候，第一次的lineTo就不是从原点O到A(200,200)的连线了，而变成了从原点O到C(200,100)之间的连线了。
        在执行完第一次lineTo和setLastPoint后，最后一个点的位置是C(200,100),所以在第二次调用lineTo的时候就是C(200,100) 到 B(200,0) 之间的连线
         */
        path.setLastPoint(200,100);
        path.lineTo(200,0);
        //close方法用于连接当前最后一个点和最初的一个点(如果两个点不重合的话)，最终形成一个封闭的图形。如果两个点链接不能成为封闭图形，则close什么也不做
        path.close();
        canvas.drawPath(path,mPaint);
    }
}
