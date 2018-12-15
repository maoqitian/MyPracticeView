package mao.com.mycustomview.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import mao.com.mycustomview.R;

/**
 * Created by maoqitian on 2018/12/15 0015.
 * Lottie 收藏动画效果 测试
 */

public class LottieActivity extends AppCompatActivity {

    public static final int COLLECTED = 1;
    public static final int NO_COLLECTED = 0;

    private int collectstata=NO_COLLECTED;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);

        final LottieAnimationView lottieAnimationView=findViewById(R.id.lottie_view);
        lottieAnimationView.setAnimation("heart.json");
        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable = lottieAnimationView.getDrawable();
                if(collectstata == NO_COLLECTED){
                    lottieAnimationView.playAnimation();
                    collectstata = COLLECTED;
                    Toast.makeText(LottieActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                }else if(collectstata == COLLECTED){
                    collectstata=NO_COLLECTED;
                    lottieAnimationView.setAnimation("heart.json");
                    Toast.makeText(LottieActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
