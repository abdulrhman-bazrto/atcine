package com.gnusl.actine.ui.Mobile.activity;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.gnusl.actine.R;
import com.gnusl.actine.model.User;
import com.gnusl.actine.ui.TV.activity.TVAuthActivity;
import com.gnusl.actine.ui.TV.activity.TVMainActivity;
import com.gnusl.actine.util.SharedPreferencesUtils;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        setContentView(R.layout.activity_splash);


        ImageView imageView = findViewById(R.id.tv_app_title);
        imageView1 = findViewById(R.id.bg);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    User user = SharedPreferencesUtils.getUser();
                    if (user != null) {
                        if (user.getStatus() == null || user.getStatus().equalsIgnoreCase("paymentless")) {
                            startActivity(new Intent(getApplicationContext(), TVAuthActivity.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), TVMainActivity.class));
                        }
                    } else {
//                    ActivityOptionsCompat activityOptionsCompat
//                            = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageView1, "imageMain");
//
//                  startActivity(new Intent(getApplicationContext(), AuthActivity.class), activityOptionsCompat.toBundle());
                        Intent intent = new Intent(getApplicationContext(), TVAuthActivity.class);
                        startActivity(intent
//                            ,
//                            ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle()
                        );
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

//                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    }
//                finish();
                }
            }, 3000);

        }else{
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
//                    ActivityOptionsCompat activityOptionsCompat
//                            = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, imageView1, "imageMain");
//
//                  startActivity(new Intent(getApplicationContext(), AuthActivity.class), activityOptionsCompat.toBundle());
                        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                        startActivity(intent
//                            ,
//                            ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle()
                        );
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

//                    startActivity(new Intent(getApplicationContext(), AuthActivity.class));
                    }
//                finish();
                }
            }, 3000);

        }

//        LottieAnimationView animationView = findViewById(R.id.animation_view);
////        LottieDrawable drawable = new LottieDrawable();
////        LottieComposition.Factory.fromAssetFileName(this, "intro.json",(composition ->
////        {
////            drawable.setComposition(composition);
////            drawable.playAnimation();
////            drawable.setScale(4);
////            animationView.setImageDrawable(drawable);
////
////        }));
//
//        animationView.setAnimation(R.raw.intro);
//        animationView.playAnimation();
//        animationView.loop(true);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_out);
        anim.setDuration(2000);
        imageView1.startAnimation(anim);

//
//        AnimationSet animationSet = new AnimationSet(false);
//
////        Animation scale = new ScaleAnimation(100f,10f,1000f,10f);
////        scale.setDuration(1000);
////        animationSet.addAnimation(scale);
//
//        ResizeAnimation resizeAnimation = new ResizeAnimation(imageView1);
//        resizeAnimation.setDuration(2500);
//
//        resizeAnimation.setParams(10000, 200);
//
//        animationSet.addAnimation(resizeAnimation);
//
////        Animation fadeOut = new AlphaAnimation(1, 0);
////        fadeOut.setStartOffset(1000);
////        fadeOut.setDuration(1000);
////
////        animationSet.addAnimation(fadeOut);
//
//        animationSet.start();
//
//        imageView.startAnimation(animationSet);

//        Animation a = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
//        a.setDuration(1300);
//        findViewById(R.id.tv_app_title).startAnimation(a);

//        YoYo.with(Techniques.Landing)
//                .duration(1000)
//                .repeat(1)
//                .playOn(findViewById(R.id.tv_app_title));
    }

    private boolean shouldFinish = false;

    @Override
    public void onStop() {
        super.onStop();
        finish();

    }
}
