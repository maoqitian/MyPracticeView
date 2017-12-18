package mao.com.mycustomview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import mao.com.mycustomview.action.Action;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bt_pie,bt_test,bt_check,bt_loading_view,bt_path_test,bt_path_test2,bt_spider_view,bt_bezier_view,bt_cibn,bt_elastic_ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        bt_pie= (Button) findViewById(R.id.bt_pie);
        bt_test= (Button) findViewById(R.id.bt_test);
        bt_check= (Button) findViewById(R.id.bt_check);
        bt_loading_view= (Button) findViewById(R.id.bt_loading);
        bt_path_test= (Button) findViewById(R.id.bt_path_test);
        bt_path_test2= (Button) findViewById(R.id.bt_path_test2);
        bt_spider_view= (Button) findViewById(R.id.bt_spider_view);
        bt_bezier_view= (Button) findViewById(R.id.bt_bezier_view);
        bt_cibn= (Button) findViewById(R.id.bt_cibn);
        bt_elastic_ball= (Button) findViewById(R.id.bt_elastic_ball);
        bt_test.setOnClickListener(this);
        bt_pie.setOnClickListener(this);
        bt_check.setOnClickListener(this);
        bt_loading_view.setOnClickListener(this);
        bt_path_test.setOnClickListener(this);
        bt_path_test2.setOnClickListener(this);
        bt_spider_view.setOnClickListener(this);
        bt_bezier_view.setOnClickListener(this);
        bt_cibn.setOnClickListener(this);
        bt_elastic_ball.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bt_pie://饼状图
                intent=new Intent(Action.ACTION_PIE_VIEW);
                startActivity(intent);
                break;
            case R.id.bt_test:
                intent=new Intent(Action.ACTION_TEST_VIEW);
                startActivity(intent);
                break;
            case R.id.bt_check:
                intent=new Intent(Action.ACTION_CHECK_VIEW);
                startActivity(intent);
                break;
            case R.id.bt_loading:
                intent=new Intent(Action.ACTION_LEAF_LOADING_VIEW);
                startActivity(intent);
                break;
            case R.id.bt_path_test:
                intent=new Intent(Action.ACTION_PATH_TEST);
                startActivity(intent);
                break;
            case R.id.bt_spider_view:
                intent=new Intent(Action.ACTION_SPIDER_VIEW);
                startActivity(intent);
                break;
            case R.id.bt_bezier_view:
                intent=new Intent(Action.ACTION_BEZIER_VIEW);
                startActivity(intent);
                break;
            case R.id.bt_cibn:
                intent=new Intent(Action.ACTION_CIBN);
                startActivity(intent);
                break;
            case R.id.bt_elastic_ball:
                intent=new Intent(Action.ACTION_ELASTIC_BALL);
                startActivity(intent);
                break;
            case R.id.bt_path_test2:
                intent=new Intent(Action.ACTION_PATH_TEST2);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doubleClickExit();
        }
        return false;
    }

    private static Boolean mIsExit = false;

    private void doubleClickExit() {
        Timer exitTimer = null;
        if (mIsExit == false) {
            mIsExit = true;
           Toast.makeText(getApplicationContext(),"再点一次退出应用",Toast.LENGTH_SHORT).show();
            exitTimer = new Timer();
            exitTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
