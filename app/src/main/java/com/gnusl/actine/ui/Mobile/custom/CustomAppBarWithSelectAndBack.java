package com.gnusl.actine.ui.Mobile.custom;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.model.CategoryItem;
import com.gnusl.actine.ui.Mobile.adapter.CategorySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;


public class CustomAppBarWithSelectAndBack extends ConstraintLayout {

    ImageView ivBack;
    TextView tvTitle;
    Spinner spCategory;
    private Context mContext;


    public CustomAppBarWithSelectAndBack(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public CustomAppBarWithSelectAndBack(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public CustomAppBarWithSelectAndBack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        LayoutInflater.from(getContext()).inflate(R.layout.layout_app_bar_with_select_and_back, CustomAppBarWithSelectAndBack.this);

        ivBack = findViewById(R.id.iv_back);

        tvTitle = findViewById(R.id.tv_title);

        spCategory = findViewById(R.id.sp_category);

        initCategorySpinner();

    }

    private void initCategorySpinner() {
        final List<CategoryItem> list = new ArrayList<>();
        CategoryItem movies = new CategoryItem(mContext.getString(R.string.movies));
        CategoryItem tvShows = new CategoryItem(mContext.getString(R.string.tv_series));
        list.add(movies);
        list.add(tvShows);

        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(mContext,
                R.layout.item_spinner_category, list,R.layout.item_spinner_category);
        adapter.setDropDownViewResource(R.layout.item_spinner_category);


        spCategory.setAdapter(adapter);
    }

    public ImageView getIvBack() {
        return ivBack;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public Spinner getSpCategory() {
        return spCategory;
    }
}
