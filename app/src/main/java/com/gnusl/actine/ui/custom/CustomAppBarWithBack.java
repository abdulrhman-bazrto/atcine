package com.gnusl.actine.ui.custom;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnusl.actine.R;


public class CustomAppBarWithBack extends ConstraintLayout {

    ImageView ivBack;
    TextView tvTitle;

    public CustomAppBarWithBack(Context context) {
        super(context);
        init(null);
    }

    public CustomAppBarWithBack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomAppBarWithBack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {

        LayoutInflater.from(getContext()).inflate(R.layout.layout_app_bar_with_back, CustomAppBarWithBack.this);

        ivBack = findViewById(R.id.iv_back);

        tvTitle = findViewById(R.id.tv_title);

    }

    public ImageView getIvBack() {
        return ivBack;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }
}
