package mao.com.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2017/12/18 0018.
 * Path 应用完结
 */

public class PathView2 extends View {

    private Paint mPaint;
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
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10); // 边框宽度  10
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
        //canvas.translate(mWidth/2,mHeight/2);
        Path path=new Path();
        path.moveTo(100,100);
        //rXxx方法的坐标使用的是相对位置(基于当前点的位移)，而之前方法的坐标是绝对位置(基于当前坐标系的坐标)
        //在使用rLineTo之前，当前点的位置在 (100,100) ， 使用了 rLineTo(100,200) 之后，下一个点的位置是在当前点的基础上加上偏移量得到的，即 (100+100, 100+200) 这个位置
        path.rLineTo(100,200);
        canvas.drawPath(path,mPaint);
    }

}
