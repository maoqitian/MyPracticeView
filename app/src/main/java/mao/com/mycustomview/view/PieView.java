package mao.com.mycustomview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import mao.com.mycustomview.bean.Piedata;

/**
 * Created by 毛麒添 on 2017/10/24 0024.
 *  自定义View 之饼状图
 */

public class PieView extends View {

    private String mText;//饼状图描述

    // 颜色表 (注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    private Paint mPaint=new Paint();//画笔

    // 宽高
    private int mWidth, mHeight;

    // 饼状图初始绘制角度
    private float mStartAngle = 0;
    // 数据
    private ArrayList<Piedata> mData;

    // 文字色块部分
    private PointF mStartPoint = new PointF(20, 20);
    private PointF mCurrentPoint = new PointF(mStartPoint.x, mStartPoint.y);
    private float mColorRectSideLength = 20;
    private float mTextInterval = 10;
    private float mRowMaxLength;


    public PieView(Context context) {
        super(context,null);
    }

    public PieView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    /**
     * 确定View大小(记录当前View的宽高)
     * @param w 宽度
     * @param h 高度
     * @param oldw 上一次宽度
     * @param oldh 上一次高度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取控件的宽高
        mWidth=w;
        mHeight=h;
    }

    /**
     * 饼状图暂时不需要测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 继承View 没有子View 不用关心摆放问题
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mData == null)return;
        float currentStartAngle=mStartAngle;//当前的起始坐标
        //将画布的起始原点移到中心位置
        canvas.translate(mWidth/2,mHeight/2);
        //饼状图的半径
        float r= (float) (Math.min(mWidth,mHeight)/2*0.8);
        //饼状图的绘制区域
        RectF mRectF=new RectF(-r,-r,r,r);
        //根据数据来绘制扇形
        for (int i = 0; i < mData.size(); i++) {
            Piedata piedata=mData.get(i);
            mPaint.setColor(piedata.getColor());
            //画弧 startAngle  // 开始角度  sweepAngle  // 扫过角度  useCenter   // 是否使用中心(圆弧的中心从圆心开始)
            canvas.drawArc(mRectF,currentStartAngle,piedata.getAngle(),true,mPaint);
            currentStartAngle += piedata.getAngle();

            canvas.save();//保持页面的状态
            canvas.translate(- mWidth/2,- mHeight/2);
            RectF colorRect = new RectF(mCurrentPoint.x, mCurrentPoint.y, mCurrentPoint.x + mColorRectSideLength, mCurrentPoint.y + mColorRectSideLength);

            canvas.restore();

        }
        //设置饼状图的描述
        canvas.drawText(mText,50,200,mPaint);
    }
    //设置饼状图描述
    public void setText(String text){
        this.mText=text;
        invalidate();
    }

    //设置饼状图起始角度
    public void setStartAngle(int mStartAngle){
         this.mStartAngle=mStartAngle;
         invalidate();//刷新
    }

    //设置数据
    public void setData(ArrayList<Piedata> mData){
        this.mData=mData;
        initData(mData);
        invalidate();//刷新
    }

    //初始化数据
    private void initData(ArrayList<Piedata> mData) {
        if(mData == null && mData.size() == 0){
            return;
        }
        float sumValue=0;//数值和
        for (int i = 0; i <mData.size(); i++) {

            Piedata piedata = mData.get(i);
            sumValue +=piedata.getValue(); //计算数值和
            int j = i % mColors.length;
            piedata.setColor(mColors[j]);//设置颜色
        }

        //角度和
        float sumAngle=0;
        for (int i =0;i<mData.size();i++){

            Piedata piedata = mData.get(i);
            float percentage = piedata.getValue() / sumValue;  // 百分比
            float angle = percentage * 360;  // 对应的角度
            //记录百分百
            piedata.setPercentage(percentage);
            //记录角度
            piedata.setAngle(angle);
            sumAngle +=angle;

        }
    }

}
