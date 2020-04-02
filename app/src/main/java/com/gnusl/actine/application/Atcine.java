package com.gnusl.actine.application;

import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.androidnetworking.AndroidNetworking;
import com.gnusl.actine.util.ObjectBox;

public class Atcine extends Application {

    private static Atcine applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationInstance = this;

        ObjectBox.init(this);

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
