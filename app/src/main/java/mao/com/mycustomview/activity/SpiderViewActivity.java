package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mao.com.mycustomview.R;

/**
 * Created by lenovo on 2017/11/2 0002.
 * 蜘蛛网格
 */

public class SpiderViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spider_web_layout);
    }
}
