package com.gnusl.actine.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;

public class Utils {

    public static void setOnFocusScale(View view) {
        if (view == null)
            return;
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // run scale animation and make it bigger
                Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_in);
                view.startAnimation(anim);
                anim.setFillAfter(true);
            } else {
                // run scale animation and make it smaller
                Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_out);
                view.startAnimation(anim);
                anim.setFillAfter(true);
            }
        });
    }

    public static void setOnFocusScale1(View view, RecyclerView rv,int pos) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // run scale animation and make it bigger
                Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_in1);
                rv.smoothScrollToPosition(pos);
                view.startAnimation(anim);
                anim.setFillAfter(true);
            } else {
                // run scale animation and make it smaller
                Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_out1);
                view.startAnimation(anim);
                anim.setFillAfter(true);
            }
        });
    }
}
