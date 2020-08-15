package com.gnusl.actine.ui.custom;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Spinner;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.model.CategoryItem;
import com.gnusl.actine.ui.adapter.CategorySpinnerAdapter;

import java.util.ArrayList;
import java.util.List;


public class CustomAppBar extends ConstraintLayout {

    Spinner spCategory;
    TextView spGenres;
    private Context mContext;

    public CustomAppBar(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public CustomAppBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public CustomAppBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        LayoutInflater.from(getContext()).inflate(R.layout.layout_app_bar, CustomAppBar.this);

        spCategory = findViewById(R.id.sp_category);
        spGenres = findViewById(R.id.sp_genres);

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

    public TextView getSpGenres() {
        return spGenres;
    }

    public Spinner getSpCategory() {
        return spCategory;
    }
}
