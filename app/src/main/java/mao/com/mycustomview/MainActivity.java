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

    private Button bt_pie,bt_test,bt_check,bt_loading_view,bt_path_test,bt_path_test2,bt_spider_view,bt_bezier_view,bt_cibn,bt_elastic_ball,bt_path_measure,
    bt_path_search,bt_matrix_test,bt_matrix_login,bt_dispatch_touch_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        bt_pie=  findViewById(R.id.bt_pie);
        bt_test=  findViewById(R.id.bt_test);
        bt_check=  findViewById(R.id.bt_check);
        bt_loading_view=  findViewById(R.id.bt_loading);
        bt_path_test=  findViewById(R.id.bt_path_test);
        bt_path_test2=  findViewById(R.id.bt_path_test2);
        bt_spider_view=  findViewById(R.id.bt_spider_view);
        bt_bezier_view=  findViewById(R.id.bt_bezier_view);
        bt_cibn=  findViewById(R.id.bt_cibn);
        bt_elastic_ball=  findViewById(R.id.bt_elastic_ball);
        bt_path_measure=  findViewById(R.id.bt_path_measure);
        bt_path_search=  findViewById(R.id.bt_path_search);
        bt_matrix_test=  findViewById(R.id.bt_matrix_test);
        bt_matrix_login = findViewById(R.id.bt_matrix_login);
        bt_dispatch_touch_event=findViewById(R.id.bt_dispatch_touch_event);
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
        bt_path_measure.setOnClickListener(this);
        bt_path_search.setOnClickListener(this);
        bt_matrix_test.setOnClickListener(this);
        bt_matrix_login.setOnClickListener(this);
        bt_dispatch_touch_event.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bt_pie://饼状图
                MyStartActivity(Action.ACTION_PIE_VIEW);
                break;
            case R.id.bt_test:
                MyStartActivity(Action.ACTION_TEST_VIEW);
                break;
            case R.id.bt_check:
                MyStartActivity(Action.ACTION_CHECK_VIEW);
                break;
            case R.id.bt_loading:
                MyStartActivity(Action.ACTION_LEAF_LOADING_VIEW);
                break;
            case R.id.bt_path_test:
                MyStartActivity(Action.ACTION_PATH_TEST);
                break;
            case R.id.bt_spider_view:
                MyStartActivity(Action.ACTION_SPIDER_VIEW);
                break;
            case R.id.bt_bezier_view:
                MyStartActivity(Action.ACTION_BEZIER_VIEW);
                break;
            case R.id.bt_cibn:
                MyStartActivity(Action.ACTION_CIBN);
                break;
            case R.id.bt_elastic_ball:
                MyStartActivity(Action.ACTION_ELASTIC_BALL);
                break;
            case R.id.bt_path_test2:
                MyStartActivity(Action.ACTION_PATH_TEST2);
                break;
            case R.id.bt_path_measure:
                MyStartActivity(Action.ACTION_PATH_MEASURE);
                break;
            case R.id.bt_path_search:
                MyStartActivity(Action.ACTION_PATH_SEARCH);
                break;
            case R.id.bt_matrix_test:
                MyStartActivity(Action.ACTION_MATRIX_TEST);
                break;
            case R.id.bt_matrix_login:
                MyStartActivity(Action.ACTION_MATRIX_LOGIN);
                break;
            case R.id.bt_dispatch_touch_event:
                MyStartActivity(Action.ACTION_DISPATCH_TOUCH_EVENT);
                break;
        }
    }

    public void MyStartActivity(String action){
        Intent intent=new Intent(action);
        startActivity(intent);
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
