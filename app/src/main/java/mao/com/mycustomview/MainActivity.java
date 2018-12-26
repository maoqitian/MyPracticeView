package mao.com.mycustomview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mao.com.mycustomview.action.Action;
import mao.com.mycustomview.retrofit.GitHub;
import mao.com.mycustomview.retrofit.SimpleService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button bt_pie,bt_test,bt_check,bt_loading_view,bt_path_test,bt_path_test2,bt_spider_view,bt_bezier_view,bt_cibn,bt_elastic_ball,bt_path_measure,
    bt_path_search,bt_matrix_test,bt_matrix_login,bt_dispatch_touch_event,bt_lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        /**
         * 异步请求
         */
        /*PostExample postexample = new PostExample();
        String json = postexample.bowlingJson("Jesse", "Jake");
        try {
            postexample.post("http://www.roundsapp.com/post", json);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // Create a very simple REST adapter which points the GitHub API.
        /*Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(GitHub.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create an instance of our GitHub API interface.
        GitHub gitHub = retrofit.create(GitHub.class);
        // Create a call instance for looking up Retrofit contributors.
        final retrofit2.Call<List<SimpleService.Contributor>> call = gitHub.contributors("square", "retrofit");*/
        // Fetch and print a list of the contributors to the library.
        /*call.enqueue(new retrofit2.Callback<List<SimpleService.Contributor>>() {
            @Override
            public void onResponse(retrofit2.Call<List<SimpleService.Contributor>> call, retrofit2.Response<List<SimpleService.Contributor>> response) {
                List<SimpleService.Contributor> body = response.body();
                for (SimpleService.Contributor contributor : body) {
                    Log.e("maoqitian","Retrofit异步请求返回参数"+contributor.login + " (" + contributor.contributions + ")");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<SimpleService.Contributor>> call, Throwable t) {
                Log.e("maoqitian","Retrofit异步请求失败"+t.getMessage());
            }
        });*/

        new Thread(){
            @Override
            public void run() {
                super.run();
                /**
                 * 同步请求
                 */
               /* GetExample getexample = new GetExample();
                String syncresponse = null;
                try {
                    syncresponse = getexample.run("https://raw.github.com/square/okhttp/master/README.md");
                    Log.e("maoqitian","异步请求返回参数"+syncresponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                /*try {
                    List<SimpleService.Contributor> contributors = call.execute().body();
                    for (SimpleService.Contributor contributor : contributors) {
                        Log.e("maoqitian","Retrofit同步请求返回参数"+contributor.login + " (" + contributor.contributions + ")");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }.start();

        Glide.with(this).load("");
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
        bt_lottie=findViewById(R.id.bt_lottie);
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
        bt_lottie.setOnClickListener(this);
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
            case R.id.bt_lottie:
                MyStartActivity(Action.ACTION_LOTTIE);
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

    /**
     * 异步请求
     */
    class PostExample {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        //获取 OkHttpClient 对象
        OkHttpClient client = new OkHttpClient();

        void post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("maoqitian","请求错误"+e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String asynresponse= response.body().string();
                    Log.e("maoqitian","异步请求返回参数"+asynresponse);
                }
            });

        }

        String bowlingJson(String player1, String player2) {
            return "{'winCondition':'HIGH_SCORE',"
                    + "'name':'Bowling',"
                    + "'round':4,"
                    + "'lastSaved':1367702411696,"
                    + "'dateStarted':1367702378785,"
                    + "'players':["
                    + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                    + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                    + "]}";
        }
    }
    /**
     * 同步请求
     */
    class GetExample {
        OkHttpClient client = new OkHttpClient();
        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
    }

}



