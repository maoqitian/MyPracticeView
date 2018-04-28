package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import mao.com.mycustomview.R;
import mao.com.mycustomview.animation.Rotate3dAnimation;
import mao.com.mycustomview.view.matrix.MatrixSetPolyToPolyTest;
import mao.com.mycustomview.view.matrix.SetPolyToPolyPoint;

/**
 * Created by lenovo on 2018/4/13 0013.
 * Matrix 测试
 */

public class MatrixSetPolyToPolyTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setpolytopoly_layout);

        final SetPolyToPolyPoint poly = findViewById(R.id.poly);

        LinearLayout ll=findViewById(R.id.ll_view);

        RadioGroup group =  findViewById(R.id.group);
        assert group != null;
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.point0:
                        poly.setTestPoint(0);
                        break;
                    case R.id.point1:
                        poly.setTestPoint(1);
                        break;
                    case R.id.point2:
                        poly.setTestPoint(2);
                        break;
                    case R.id.point3:
                        poly.setTestPoint(3);
                        break;
                    case R.id.point4:
                        poly.setTestPoint(4);
                        break;
                }
            }
        });

        poly.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);
        ImageView imageView=findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 计算中心点（这里是使用view的中心作为旋转的中心点）
                final float centerX = v.getWidth() / 2.0f;
                final float centerY = v.getHeight() / 2.0f;
                //自定义动画
                Rotate3dAnimation rotate3dAnimation=new Rotate3dAnimation(
                        MatrixSetPolyToPolyTestActivity.this,0,
                        180, centerX, centerY, 0f, true);
                rotate3dAnimation.setDuration(3000);
                rotate3dAnimation.setFillAfter(true);
                rotate3dAnimation.setInterpolator(new LinearInterpolator());
                v.startAnimation(rotate3dAnimation);
            }
        });
    }
}
