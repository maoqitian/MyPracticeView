package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mao.com.mycustomview.view.PathView2;

/**
 * Created by 毛麒添 on 2017/12/18 0018.
 */

public class Pathtest2Activiy extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PathView2(this));
    }
}
