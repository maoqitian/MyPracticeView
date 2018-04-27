package mao.com.mycustomview.view.matrix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import mao.com.mycustomview.R;

/**
 * Created by maoqitian on 2018/4/17 0017.
 *  setRectToRect方法测试
 */

public class MatrixSetRectToRectTest extends View {

    private int mViewWidth, mViewHeight;

    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mRectMatrix;         // 测试etRectToRect用的Matrix


    public MatrixSetRectToRectTest(Context context) {
        super(context);
        mBitmap= BitmapFactory.decodeResource(getResources(), R.drawable.matrix_test);
        mRectMatrix=new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth=w;
        mViewHeight=h;
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF src=new RectF(0,0,mBitmap.getWidth(),mBitmap.getHeight());
        RectF dst=new RectF(0,0,mViewWidth,mViewHeight);

        mRectMatrix.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER);//放入目标区域的中心

        canvas.drawBitmap(mBitmap,mRectMatrix,new Paint());
    }
}
