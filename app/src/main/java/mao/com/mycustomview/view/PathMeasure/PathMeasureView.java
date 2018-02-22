package mao.com.mycustomview.view.PathMeasure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import mao.com.mycustomview.R;

/**
 * Created by 毛麒添 on 2017/12/22 0022.
 * PathMeasure是一个用来测量Path的类
 */

public class PathMeasureView extends View {

    private int mWidth, mHeight;

    private Paint mPaint;


    //箭头图片变量
    private float currentValue;// 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度
    private Bitmap mBitmap;// 箭头图片
    private float[] pos;// 当前点的实际位置
    private float[] tan;     // 当前点的tangent值,用于计算图片所需旋转的角度

    private Matrix mMatrix;// 矩阵,用于对图片进行一些操作


    public PathMeasureView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10); // 边框宽度  10

        pos=new float[2];
        tan=new float[2];
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 2;       // 缩放图片
        mBitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.jiantou,options);
        mMatrix=new Matrix();
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
        demo4(canvas);

    }

    /**
     * getPosTan 用于得到路径上某一长度的位置以及该位置的正切值 箭头图片绕圆轨道环绕
     * @param canvas 画布对象
     */
    private void demo4(Canvas canvas) {

        Path path=new Path();
        path.addCircle(0,0,200, Path.Direction.CW);//添加一个圆形
        PathMeasure pathMeasure=new PathMeasure(path,false);//创建一个PathMeasure
        currentValue += 0.005;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }

        pathMeasure.getPosTan(pathMeasure.getLength()*currentValue,pos,tan);// 获取当前位置的坐标以及趋势

        mMatrix.reset(); // 重置Matrix
        //当前点在曲线上的方向，使用 Math.atan2(tan[1], tan[0]) 获取到正切角的弧度值。
        float degrees= (float) (Math.atan2(tan[1],tan[0])*180.0/Math.PI);// 计算图片旋转角度

        mMatrix.postRotate(degrees,mBitmap.getWidth()/2,mBitmap.getHeight()/2);//旋转图片
        // 将图片绘制中心调整到与当前点重合
        mMatrix.postTranslate(pos[0]-mBitmap.getWidth()/2,pos[1]-mBitmap.getHeight()/2);

        canvas.drawPath(path, mPaint);                                   // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);                     // 绘制箭头

        invalidate();                                                           // 重绘页面

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

        pathMeasure.nextContour();//跳转到下一个圆,获取圆的周长

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
