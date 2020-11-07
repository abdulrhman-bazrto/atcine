/*
 * Copyright (c) 2018. Ultratechs.co
 */

package com.gnusl.actine.util;

import android.net.Uri;
import android.view.View;

public abstract class Player {

    protected StatusListener statusListener;
    protected View loading_layout;

    abstract public void start(Uri uri);

    abstract protected void setupControls(View controller);

    abstract public void release();

    public void setStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    public interface StatusListener {
        void onStart();

        void onError();
    }
}
