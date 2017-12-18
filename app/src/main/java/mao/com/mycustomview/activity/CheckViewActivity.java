package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mao.com.mycustomview.R;
import mao.com.mycustomview.view.CheckView;

/**
 * Created by 毛麒添 on 2017/10/26 0026.
 *
 */

public class CheckViewActivity extends AppCompatActivity implements View.OnClickListener{

    private CheckView mCheckView;

    private Button button_check,button_uncheck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_view_activity);
        mCheckView= (CheckView) findViewById(R.id.check_view);
        button_check= (Button) findViewById(R.id.button_check);
        button_uncheck= (Button) findViewById(R.id.button_uncheck);
        button_check.setOnClickListener(this);
        button_uncheck.setOnClickListener(this);
        mCheckView.setAnimDuration(2000);
        //mCheckView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//关闭硬件加速
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_check:
                mCheckView.check();
                break;
            case R.id.button_uncheck:
                mCheckView.unCheck();
                break;
        }
    }
}
