package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mao.com.mycustomview.view.TestView;

/**
 * Created by 毛麒添 on 2017/10/25 0025.
 * 测试 View
 */

public class TestViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestView testView=new TestView(this);
        setContentView(testView);
    }
}
