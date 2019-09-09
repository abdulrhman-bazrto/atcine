package com.gnusl.actine.application;

import android.app.Application;
import android.content.Context;

import com.gnusl.actine.model.DBShow;
import com.gnusl.actine.util.ObjectBox;

import io.objectbox.Box;

public class Atcine extends Application {

    private static Atcine applicationInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationInstance = this;

        ObjectBox.init(this);

    }

    public static Atcine getApplicationInstance() {
        return applicationInstance;
    }

    public static Context getAppContext() {
        return getApplicationInstance().getApplicationContext();
    }
}
