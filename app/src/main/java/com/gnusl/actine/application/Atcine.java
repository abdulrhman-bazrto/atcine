package com.gnusl.actine.application;

import android.app.Application;
import android.content.Context;

public class Atcine extends Application {

    private static Atcine applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationInstance = this;

    }

    public static Atcine getApplicationInstance() {
        return applicationInstance;
    }

    public static Context getAppContext() {
        return getApplicationInstance().getApplicationContext();
    }
}
