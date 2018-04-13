package mao.com.mycustomview.view.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

import mao.com.mycustomview.R;

/**
 * Created by lenovo on 2018/4/13 0013.
 * Matrix 测试
 */

public class MatrixSetPolyToPolyTest extends View {

    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mPolyMatrix;         // 测试setPolyToPoly用的Matrix

    public MatrixSetPolyToPolyTest(Context context) {
        super(context);
        initBitmapAndMatrix();
    }

    private void initBitmapAndMatrix() {
        mBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.matrix_test);
        mPolyMatrix=new Matrix();

        float[] src = {0,0,         //左上
                mBitmap.getWidth(),0,//右上
                mBitmap.getWidth(),mBitmap.getHeight(),//右下
                0,mBitmap.getHeight()};//左下

        float[] dst = {0,0,         //左上
                mBitmap.getWidth(),400,//右上
                mBitmap.getWidth(),mBitmap.getHeight()-200,//右下
                0,mBitmap.getHeight()};//左下
        // src.length >> 1 为位移运算 相当于除以2
        mPolyMatrix.setPolyToPoly(src,0,dst,0,src.length >> 1);

        mPolyMatrix.postScale(0.26f, 0.26f);
        mPolyMatrix.postTranslate(0,200);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //根据Matrix 变换 画出图片
        canvas.drawBitmap(mBitmap,mPolyMatrix,null);
    }
}
