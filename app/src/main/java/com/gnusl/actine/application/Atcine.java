package com.gnusl.actine.application;

import android.app.Application;
import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.gnusl.actine.util.ObjectBox;

public class Atcine extends Application {

    private static Atcine applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationInstance = this;

        ObjectBox.init(this);

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY); // enabling logging with level

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
