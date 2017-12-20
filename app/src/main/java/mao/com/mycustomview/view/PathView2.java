package mao.com.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2017/12/18 0018.
 * Path 应用完结
 */

public class PathView2 extends View {

    private Paint mPaint,mWhitePaint,mBlackPaint;
    private int mWidth, mHeight;

    public PathView2(Context context) {
        super(context);
        init();
    }

    public PathView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mWhitePaint=new Paint();
        mBlackPaint=new Paint();
        mWhitePaint.setColor(Color.WHITE);
        mBlackPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10); // 边框宽度  10
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        //demo1(canvas);
        //demo2(canvas);
        //demo3(canvas);

        //Path的布尔运算 画出太极的图示
        Path path1=new Path();
        Path path2=new Path();
        Path path3=new Path();
        Path path4=new Path();

        Path path5=new Path();//小白圆
        Path path6=new Path();//小黑圆

        Path path7=new Path();

        path1.addCircle(0,0,200, Path.Direction.CW);//显示整个圆
        path2.addRect(0,-200,200,200, Path.Direction.CW);
        path3.addCircle(0,-100,100, Path.Direction.CW);
        path4.addCircle(0,100,100, Path.Direction.CW);

        path7.addCircle(0,0,201, Path.Direction.CW);

        path1.op(path2,Path.Op.DIFFERENCE);//圆与正方形差集
        path1.op(path3,Path.Op.UNION);//大圆与小圆的并集
        path1.op(path4,Path.Op.DIFFERENCE);//大圆与小圆的差集
        canvas.drawPath(path1, mPaint);

        path5.addCircle(0,-100,30, Path.Direction.CW);
        path6.addCircle(0,100,30, Path.Direction.CW);

        //三个色块的交集
        path5.op(path3,Path.Op.INTERSECT);
        canvas.drawPath(path5, mWhitePaint);
        canvas.drawPath(path6, mBlackPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path7, mPaint);
    }

    private void demo3(Canvas canvas) {
        Path path=new Path();
        // 添加小正方形 (通过这两行代码来控制小正方形边的方向,从而演示不同的效果)
        //path.addRect(-200,-200,200,200, Path.Direction.CW);
        path.addRect(-200,-200,200,200, Path.Direction.CCW);
        path.addRect(-400,-400,400,400, Path.Direction.CCW);

        path.setFillType(Path.FillType.WINDING);//规则非零环绕
        canvas.drawPath(path,mPaint);
    }

    private void demo2(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        Path path=new Path();
        path.addRect(-200,-200,200,200, Path.Direction.CW);
        //path.setFillType(Path.FillType.EVEN_ODD);// 设置Path填充模式为 奇偶规则
        path.setFillType(Path.FillType.INVERSE_EVEN_ODD);//反奇偶规则
        canvas.drawPath(path,mPaint);
    }

    private void demo1(Canvas canvas) {
        Path path=new Path();
        path.moveTo(100,100);
        //rXxx方法的坐标使用的是相对位置(基于当前点的位移)，而之前方法的坐标是绝对位置(基于当前坐标系的坐标)
        //在使用rLineTo之前，当前点的位置在 (100,100) ， 使用了 rLineTo(100,200) 之后，下一个点的位置是在当前点的基础上加上偏移量得到的，即 (100+100, 100+200) 这个位置
        path.rLineTo(100,200);
        canvas.drawPath(path,mPaint);
    }

}
