package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import mao.com.mycustomview.R;
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

    }
}
