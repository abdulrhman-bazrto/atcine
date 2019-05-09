package com.gnusl.actine.ui.custom;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.gnusl.actine.R;


public class CustomAppBar extends ConstraintLayout {


    public CustomAppBar(Context context) {
        super(context);
        init(null);
    }

    public CustomAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomAppBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {

        LayoutInflater.from(getContext()).inflate(R.layout.layout_app_bar, CustomAppBar.this);

    }

}
