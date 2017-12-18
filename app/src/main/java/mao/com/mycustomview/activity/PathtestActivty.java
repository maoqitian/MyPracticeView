package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mao.com.mycustomview.view.PathView;

/**
 * Created by 毛麒添 on 2017/11/1 0001.
 * 路径测试View
 */

public class PathtestActivty extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PathView pathView=new PathView(this);
        setContentView(pathView);
    }

}
