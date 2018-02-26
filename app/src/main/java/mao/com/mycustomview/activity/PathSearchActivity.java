package mao.com.mycustomview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import mao.com.mycustomview.R;
import mao.com.mycustomview.view.SearchView;

/**
 * Created by 毛麒添 on 2018/2/24 0024.
 * 动效搜索
 */

public class PathSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.path_search);

        SearchView searchView=findViewById(R.id.bt_path_search);
        searchView.StartSearch();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("毛麒添", "onClick");
            }
        });
        searchView.setOnSearchViewStatus(new SearchView.onSearchViewStatus() {
            @Override
            public void onStart() {
                Log.e("毛麒添", "onStart");
            }

            @Override
            public void onSearching() {
                Log.e("毛麒添", "onSearching" );
            }

            @Override
            public void onEnding() {
                Log.e("毛麒添", "onEnding" );
            }
        });
    }
}
