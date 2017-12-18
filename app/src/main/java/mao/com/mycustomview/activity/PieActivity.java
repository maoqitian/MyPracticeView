package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import mao.com.mycustomview.bean.Piedata;
import mao.com.mycustomview.view.PieView;

/**
 * Created by 毛麒添 on 2017/10/25 0025.
 * 饼状图
 */

public class PieActivity extends AppCompatActivity {

    private ArrayList<Piedata> mData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PieView pieView=new PieView(this);
        setContentView(pieView);

        mData=new ArrayList<>();
        Piedata Piedata = new Piedata("牛肉", 60);
        Piedata Piedata2 = new Piedata("鸡肉", 30);
        Piedata Piedata3 = new Piedata("青菜", 40);
        Piedata Piedata4 = new Piedata("猪肉", 20);
        Piedata Piedata5 = new Piedata("鱼", 20);
        mData.add(Piedata);
        mData.add(Piedata2);
        mData.add(Piedata3);
        mData.add(Piedata4);
        mData.add(Piedata5);
        Log.w("毛麒添", "onCreate: "+mData.toString());
        pieView.setStartAngle(0);
        pieView.setText("日常肉类蔬菜占比");
        pieView.setData(mData);
    }
}
