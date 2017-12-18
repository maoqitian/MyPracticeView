package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mao.com.mycustomview.R;
import mao.com.mycustomview.view.elastic.ElasticballView;


/**
 * Created by 毛麒添 on 2017/11/29 0029.
 * 自定义控件 滑动弹性小球
 */

public class ElasticBallActivity extends AppCompatActivity {

    Button bt_start_anim;
    ElasticballView myElasticballView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elasticball_layout);
        bt_start_anim=findViewById(R.id.bt_start_anim);
        myElasticballView=findViewById(R.id.my_elastic_ball);
        bt_start_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myElasticballView.startAnimation();
            }
        });
    }
}
