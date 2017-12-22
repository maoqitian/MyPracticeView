package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mao.com.mycustomview.view.PathMeasure.PathMeasureView;

/**
 * Created by 毛麒添 on 2017/12/22 0022.
 *
 */

public class PathMeasureViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PathMeasureView(this));
    }
}
