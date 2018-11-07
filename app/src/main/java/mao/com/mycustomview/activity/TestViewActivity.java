package mao.com.mycustomview.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mao.com.mycustomview.R;
import mao.com.mycustomview.view.TestView;
import mao.com.mycustomview.view.scroll.TestScroll;

/**
 * Created by 毛麒添 on 2017/10/25 0025.
 * 测试 View
 */

public class TestViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TestView testView=new TestView(this);
        setContentView(R.layout.activity_test);

        TestScroll testScroll = findViewById(R.id.test_scroll);
        //滑动方法4 属性动画滑动
        // ObjectAnimator.ofFloat(testScroll,"translationX",0,300).setDuration(2000).start();
        //滑动方式6 scroller 配合 scrollTo 滑动view
        testScroll.smoothScrollTo(-300,-300);
        Log.e("毛麒添","滑动后的 getLeft:"+testScroll.getLeft()+"getTop:"+testScroll.getTop()+"getRight:"+testScroll.getRight()+"getBottom:"+testScroll.getBottom());
        testScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestViewActivity.this,"被点击了",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
