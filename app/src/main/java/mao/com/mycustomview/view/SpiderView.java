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
 * Created by 毛麒添 on 2017/11/2 0002.
 * 蜘蛛网格 View
 * 主要为path的应用
 */

public class SpiderView extends View {

    private int count = 6;                //数据个数
    private float angle = (float) (Math.PI*2/count);//每个维度的弧度
    private float radius;                   //网格最大半径
    private int centerX;                  //中心X
    private int centerY;                  //中心Y
    private String[] titles = {"a","b","c","d","e","f"};//每个维度的描述
    private String[] des = {"A","B","C","D","E","F"};//每个维度的描述
    private double[] data = {80,60,60,60,80,50,10,20}; //各维度分值
    private float maxValue = 100;             //数据最大值
    private Paint mainPaint;                //雷达区画笔
    private Paint valuePaint;               //数据区画笔
    private Paint textPaint;                //文本画笔

    public SpiderView(Context context) {
        super(context,null);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mainPaint=new Paint();
        valuePaint=new Paint();
        valuePaint.setColor(Color.YELLOW);
        textPaint=new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius=Math.min(w,h)/2*0.9f;
        centerX=w/2;
        centerY=h/2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制正多边形
        drawPolygon(canvas);
        //绘制网格直线
        drawLines(canvas);
        //绘制每个维度文字描述
        drawText(canvas);
        //绘制覆盖区域
        drawRegion(canvas);
        //绘制覆盖区域的文字
        drawRegionText(canvas);
    }
    //绘制覆盖区域的文字
    private void drawRegionText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;//绘制文字的高度
        for (int i = 0; i <count; i++) {
            double percent = data[i] / maxValue;//对应的维度值百分比
            //根据半径和文字高度，计算出绘制文本每个点的坐标
            float x = (float) (centerX + (radius+fontHeight/2) * Math.cos(angle * i)*percent);
            float y = (float) (centerY + (radius+fontHeight/2) * Math.sin(angle * i)*percent);
            //坐标系四个象限来绘制文本
            if(angle*i>=0 && angle*i<Math.PI/2){//第四象限 0--90度
                float dis = textPaint.measureText(des[i]);//文本长度
                canvas.drawText(des[i],x+dis,y,textPaint);
            }else if(angle*i>=3*Math.PI/2&& angle*i<Math.PI*2){//第三象限 270 ---360
                float dis = textPaint.measureText(des[i]);//文本长度
                canvas.drawText(des[i],x+dis,y,textPaint);
            }else if(angle*i>Math.PI/2&&angle*i<=Math.PI){ //第二象限 90 - 180
                float dis = textPaint.measureText(des[i]);//文本长度
                canvas.drawText(des[i],x-dis-10F,y,textPaint);
            }else if(angle*i>=Math.PI&&angle*i<3*Math.PI/2){//第一象限 180----270
                float dis = textPaint.measureText(des[i]);//文本长度
                canvas.drawText(des[i],x-dis-10F,y,textPaint);
            }
        }
    }

    //绘制区域
    private void drawRegion(Canvas canvas) {
        Path path=new Path();
        valuePaint.setAlpha(255);
        for (int i = 0; i <count; i++) {
            double percent = data[i] / maxValue;//对应的维度值百分比
            //根据半径，计算出蜘蛛丝上每个点的坐标
            float x = (float) (centerX + (radius) * Math.cos(angle *i)* percent);
            float y = (float) (centerY + (radius) * Math.sin(angle *i)* percent);
            //坐标系四个象限来绘制文本
            if(i==0){//如果是中心点
                path.moveTo(x,centerY);
            }else {
                //连接各个坐标点
                path.lineTo(x,y);
            }
            //绘制小圆点
            canvas.drawCircle(x,y,7,valuePaint);
        }
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path,valuePaint);
        valuePaint.setAlpha(80);
        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }

    //绘制每个维度的文字
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;//绘制文字的高度
        for (int i = 0; i <count; i++) {
            //根据半径和文字高度，计算出绘制文本每个点的坐标
            float x = (float) (centerX + (radius+fontHeight/2) * Math.cos(angle * i));
            float y = (float) (centerY + (radius+fontHeight/2) * Math.sin(angle * i));
            //坐标系四个象限来绘制文本
            if(angle*i>=0 && angle*i<Math.PI/2){//第四象限 0--90度
               canvas.drawText(titles[i],x,y,textPaint);
            }else if(angle*i>=3*Math.PI/2&& angle*i<Math.PI*2){//第三象限 270 ---360
                canvas.drawText(titles[i],x,y,textPaint);
            }else if(angle*i>Math.PI/2&&angle*i<=Math.PI){ //第二象限 90 - 180
                float dis = textPaint.measureText(titles[i]);//文本长度
                canvas.drawText(titles[i],x-dis,y,textPaint);
            }else if(angle*i>=Math.PI&&angle*i<3*Math.PI/2){//第一象限 180----270
                float dis = textPaint.measureText(titles[i]);//文本长度
                canvas.drawText(titles[i],x-dis,y,textPaint);
            }
        }
    }

    /**
     * 绘制直线
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        //根据半径，计算出每个末端坐标
        Path path=new Path();
        for (int i = 0; i <count; i++) {
            path.reset();//重置路径
            path.moveTo(centerX,centerY);
            float x = (float) (centerX + radius * Math.cos(angle * i));
            float y = (float) (centerY + radius * Math.sin(angle * i));
            path.lineTo(x,y);
            canvas.drawPath(path,mainPaint);
        }
    }

    /**
     * 绘制正多边形
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path=new Path();
        float r=radius/(count-1); //r是蜘蛛丝之间的间距
        for (int i = 1; i <count; i++) {//中心点不用绘制
            float curR=r*i;//当前半径
            path.reset();//重置路径
            for (int j = 0; j <count; j++) {
                if(j==0){
                    //如果是中心点，移动下一次操作的起点位置
                    path.moveTo(centerX+curR,centerY);
                }else{
                    //根据半径，计算出蜘蛛丝上每个点的坐标
                    float x = (float) (centerX + curR * Math.cos(angle * j));
                    float y = (float) (centerY + curR * Math.sin(angle * j));
                    path.lineTo(x,y);
                }
            }
            path.close();//闭合路径
            mainPaint.setAlpha(50);
            //绘制填充网格区域颜色
            mainPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawPath(path, mainPaint);
            //画出网格
            mainPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path,mainPaint);
        }

    }

    //设置标题
    public void setTitles(String[] titles) {
        this.titles = titles;
    }
    //设置绘制区域标题
    public void setDes(String[] des) {
        this.des = des;
    }

    //设置数值
    public void setData(double[] data) {
        this.data = data;
    }

    //设置最大数值
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    //设置蜘蛛网颜色
    public void setMainPaintColor(int color){
        mainPaint.setColor(color);
    }

    //设置标题颜色
    public void setTextPaintColor(int color){
        textPaint.setColor(color);
    }

    //设置覆盖局域颜色
    public void setValuePaintColor(int color){
        valuePaint.setColor(color);
    }
}
