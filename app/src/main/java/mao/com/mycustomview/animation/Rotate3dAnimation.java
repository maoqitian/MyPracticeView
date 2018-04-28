package mao.com.mycustomview.animation;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by maoqitian on 2018/4/27 0027.
 * 3D旋转 使用 graphic包下的Camera
 */

public class Rotate3dAnimation extends Animation {

    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    private final float mDepthZ;
    private final boolean mReverse;
    private Camera mCamera;

    float scale = 1;    // 像素密度

    /**
     * 创建一个绕y轴旋转的3D动画效果，旋转过程中具有深度调节，可以指定旋转中心。
     * @param context       添加上下文,为获取像素密度准备
     * @param fromDegrees	起始时角度
     * @param toDegrees 	结束时角度
     * @param centerX 		旋转中心x坐标
     * @param centerY 		旋转中心y坐标
     * @param depthZ		最远到达的z轴坐标
     * @param reverse 		true 表示由从0到depthZ，false相反
     */

    public Rotate3dAnimation(Context context,float fromDegrees, float toDegrees,
                             float centerX, float centerY, float depthZ, boolean reverse){
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
        //获取手机像素密度
        scale=context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera=new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        //super.applyTransformation(interpolatedTime, t);
        final float fromDegrees=mFromDegrees;
        float degress=fromDegrees+((mToDegrees-fromDegrees)*interpolatedTime);
        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;
        final Matrix matrix=t.getMatrix();

        camera.save();
        //调节深度
        if(mReverse){
            camera.translate(0.0f,0.0f,mDepthZ*interpolatedTime);
        }else {
            camera.translate(0.0f,0.0f,mDepthZ*(1.0f-interpolatedTime));
        }
        //绕Y旋转
        camera.rotateY(degress);
        camera.getMatrix(matrix);
        camera.restore();

        // 修正失真，主要修改 MPERSP_0 和 MPERSP_1
        float[] mValues=new float[9];
        matrix.getValues(mValues);
        mValues[6]=mValues[6]/scale;
        mValues[7]=mValues[7]/scale;
        matrix.setValues(mValues);
        //调节中心点
        matrix.preTranslate(-centerX,-centerY);
        matrix.preTranslate(centerX,centerY);

    }
}
