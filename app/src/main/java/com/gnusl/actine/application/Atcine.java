package com.gnusl.actine.application;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.gnusl.actine.R;
import com.gnusl.actine.util.ObjectBox;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class Atcine extends Application {

    private static Atcine applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationInstance = this;

        ObjectBox.init(this);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/font.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
//        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY); // enabling logging with level

    }


//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
//    }

    public static Atcine getApplicationInstance() {
        return applicationInstance;
    }

    public static Context getAppContext() {
        return getApplicationInstance().getApplicationContext();
    }


}
