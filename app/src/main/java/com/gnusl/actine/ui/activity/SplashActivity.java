package com.gnusl.actine.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.gnusl.actine.R;
import com.gnusl.actine.model.User;
import com.gnusl.actine.ui.animate.ResizeAnimation;
import com.gnusl.actine.util.SharedPreferencesUtils;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.tv_app_title);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = SharedPreferencesUtils.getUser();
                if (user != null) {
                    if (user.getStatus() == null || user.getStatus().equalsIgnoreCase("paymentless")) {
                        startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                }
                finish();
            }
        }, 10000);


        AnimationSet animationSet = new AnimationSet(false);

//        Animation scale = new ScaleAnimation(100f,10f,1000f,10f);
//        scale.setDuration(1000);
//        animationSet.addAnimation(scale);

        ResizeAnimation resizeAnimation = new ResizeAnimation(imageView);
        resizeAnimation.setDuration(2500);

        resizeAnimation.setParams(10000, 200);

        animationSet.addAnimation(resizeAnimation);

//        Animation fadeOut = new AlphaAnimation(1, 0);
//        fadeOut.setStartOffset(1000);
//        fadeOut.setDuration(1000);
//
//        animationSet.addAnimation(fadeOut);

        animationSet.start();

        imageView.startAnimation(animationSet);

//        Animation a = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
//        a.setDuration(1300);
//        findViewById(R.id.tv_app_title).startAnimation(a);

//        YoYo.with(Techniques.Landing)
//                .duration(1000)
//                .repeat(1)
//                .playOn(findViewById(R.id.tv_app_title));
    }
}
